package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.ItemDOMapper;
import com.miaoshaproject.dao.ItemStockDOMapper;
import com.miaoshaproject.dataobject.ItemDO;
import com.miaoshaproject.dataobject.ItemStockDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusienessError;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.Model.ItemModel;
import com.miaoshaproject.validator.ValidationResult;
import com.miaoshaproject.validator.Validatorimpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private Validatorimpl validator;

    @Autowired
    private ItemDOMapper itemDOMapper;

    @Autowired
    ItemStockDOMapper itemStockDOMapper;

    private ItemDO convertItemDoFromItenModel(ItemModel itemModel){
        if(itemModel == null){
            return null;
        }
        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemModel,itemDO);
        itemDO.setPrice(itemModel.getPrice().doubleValue());
        return itemDO;
    }
    private ItemStockDO convertItemStockDoFromItemModel(ItemModel itemModel){
        if(itemModel == null){
            return null;
        }
        ItemStockDO itemStockDO = new ItemStockDO();
        itemStockDO.setStock(itemModel.getStock());
        itemStockDO.setItemId(itemModel.getId());
        return itemStockDO;
    }

    @Override
    @Transactional
    public ItemModel creatItem(ItemModel itemModel) throws BusinessException {
        //校验入参
        ValidationResult result = validator.validate(itemModel);
        if(result.isHasErrors()){
            throw new BusinessException(EmBusienessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }

        //转化为dataobject
        ItemDO itemDO = convertItemDoFromItenModel(itemModel);



        //写入数据库
        itemDOMapper.insertSelective(itemDO);
        itemModel.setId(itemDO.getId());
        ItemStockDO itemStockDO = convertItemStockDoFromItemModel(itemModel);

        itemStockDOMapper.insertSelective(itemStockDO);


        //返回创建完成的对象
        return this.getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {
        List<ItemDO> itemDOList= itemDOMapper.listItem();
        List<ItemModel> itemModelList = itemDOList.stream().map(itemDO -> {
            ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
            ItemModel itemModel = convertModelFromDo(itemDO,itemStockDO);
            return itemModel;
        }).collect(Collectors.toList());

        return itemModelList;
    }

    @Override
    public ItemModel getItemById(Integer id) {

         ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);
         if(itemDO == null){
             return null;
         }
         //操作获得库存数量
         ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
        if(itemStockDO == null){
            return null;
        }

         ItemModel itemModel= convertModelFromDo(itemDO,itemStockDO);


        return itemModel;
    }

    @Override
    public boolean decreaseStockByOrderAmount(Integer itemId, Integer amount) {
        int affectRows = itemStockDOMapper.decreaseStockByOrderAmount(itemId,amount);
        if(affectRows>0){
            return true;
        }else {
            return false;
        }
    }

    private ItemModel convertModelFromDo(ItemDO itemDO,ItemStockDO itemStockDO){

        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO,itemModel);
        itemModel.setPrice(new BigDecimal(itemDO.getPrice()));


        itemModel.setStock(itemStockDO.getStock());
        return itemModel;
    }
}
