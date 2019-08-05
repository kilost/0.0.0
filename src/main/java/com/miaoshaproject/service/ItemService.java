package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.Model.ItemModel;

import java.util.List;

public interface ItemService {
    //创建商品
    ItemModel creatItem(ItemModel itemModel) throws BusinessException;

    //商品列表浏览
    List<ItemModel> listItem();

    //商品详情浏览
    ItemModel getItemById(Integer id);

    //下单减库存
    boolean decreaseStockByOrderAmount(Integer itemId,Integer amount);
}
