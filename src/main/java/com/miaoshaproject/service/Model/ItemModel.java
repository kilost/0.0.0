package com.miaoshaproject.service.Model;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ItemModel {
    //商品编号
    private Integer id;
    //商品名
    //@NotBlank(message = "商品名不能为空")
    @javax.validation.constraints.NotBlank(message = "商品名不能为空")
    private String title;
    //商品价格
    @NotNull( message = "商品价格不能空缺")
    @Min(value = 0,message = "商品价格不能小于0")
    private BigDecimal price;
    //商品库存
    @NotNull(message = "商品库存不能空缺")
    @Min(value = 0,message = "商品库存不能为0")
    private Integer stock;
    //商品描述
    @NotBlank(message = "商品描述不能为空")
    private String description;
    //商品销量

    private Integer sales;
    //商品图片
    @NotBlank(message = "商品图片不能为空")
    private String imgUrl;

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {

        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
