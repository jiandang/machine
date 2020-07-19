package com.example.myapplication.bean;

import java.io.Serializable;

/**
 * Created by ASXY_home on 2018-08-06.
 */

public class DrinkBean implements Serializable {
    private String imgUrl;//商品图
    private String name;//商品名
    private String price;//商品价格
    private String goodsNum;//商品编号
    private String stock;//商品现存
    private String tunnel_no;//货道编号
    private String tunnel_code;//货道号

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(String goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getTunnel_no() {
        return tunnel_no;
    }

    public void setTunnel_no(String tunnel_no) {
        this.tunnel_no = tunnel_no;
    }

    public String getTunnel_code() {
        return tunnel_code;
    }

    public void setTunnel_code(String tunnel_code) {
        this.tunnel_code = tunnel_code;
    }

    @Override
    public String toString() {
        return "DrinkBean{" +
                "imgUrl='" + imgUrl + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", goodsNum='" + goodsNum + '\'' +
                ", stock='" + stock + '\'' +
                ", tunnel_no='" + tunnel_no + '\'' +
                ", tunnel_code='" + tunnel_code + '\'' +
                '}';
    }
}
