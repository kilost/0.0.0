package com.miaoshaproject.controller;

import com.miaoshaproject.controller.viewobject.ItemVo;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.Model.ItemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller("/item")
@RequestMapping("/item")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class ItemController extends BaseController {

    @Autowired
    ItemService itemService;

    //创建商品的类目
    @RequestMapping(value = "/creatitem",method = RequestMethod.POST)
    @ResponseBody
    private CommonReturnType creatItem(@RequestParam(name = "title")String title,
                                       @RequestParam(name = "description")String description,
                                       @RequestParam(name = "imgUrl")String imgUrl,
                                       @RequestParam(name = "stock")Integer stock,
                                       @RequestParam(name = "price") BigDecimal price
                                       ) throws BusinessException {
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setImgUrl(imgUrl);

        ItemModel itemModelForReturn = itemService.creatItem(itemModel);

        ItemVo itemVo = this.convertFromModel(itemModelForReturn);

        return CommonReturnType.create(itemVo);

    }

    //商品详情浏览
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType getItem(@RequestParam(name = "id")Integer id){
        if(id == null){
            return null;
        }
        ItemModel itemModel = itemService.getItemById(id);
        ItemVo itemVo = this.convertFromModel(itemModel);
        return CommonReturnType.create(itemVo);
    }
    //商品列表浏览
    @RequestMapping(value = "/listItem",method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType listItem(){
        //获取商品列表
        List<ItemModel> itemModelList = itemService.listItem();
        //返回商品前端列表
        List<ItemVo> itemVoList = itemModelList.stream().map(itemModel -> {
            ItemVo itemVo = this.convertFromModel(itemModel);
            return itemVo;
                }).collect(Collectors.toList());

        return CommonReturnType.create(itemVoList);
    }

    private ItemVo convertFromModel(ItemModel itemModel){
        if(itemModel == null){
            return null;
        }
        ItemVo itemVo = new ItemVo();
        BeanUtils.copyProperties(itemModel,itemVo);
        return itemVo;
    }
}
