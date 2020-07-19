package com.example.myapplication.bean;

import java.io.Serializable;

/**
 * Created by ASXY_home on 2018-10-09.
 */

public class GoodsManageBean implements Serializable {
    private String num;//序号
    private String goodsName;//商品名称
    private String goodsImg;//商品图片
    private double price;//价格
    private String HuoNumber;//货号
    private int index;//存的货道号，便于在更换商品列表找到对应的商品
    private int max;//货道最大库存
    private int stock;//货道现有库存
    private String tunnel_no;//货道编号
    private String tunnel_code;//货道号

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getHuoNumber() {
        return HuoNumber;
    }

    public void setHuoNumber(String huoNumber) {
        HuoNumber = huoNumber;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
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
        return "GoodsManageBean{" +
                "num='" + num + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", goodsImg='" + goodsImg + '\'' +
                ", price=" + price +
                ", HuoNumber='" + HuoNumber + '\'' +
                ", index=" + index +
                ", max=" + max +
                ", stock=" + stock +
                ", tunnel_no='" + tunnel_no + '\'' +
                ", tunnel_code='" + tunnel_code + '\'' +
                '}';
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (obj == null){
            return false;
        }
        if (getClass() != obj.getClass()){
            return false;
        }
        GoodsManageBean bean = (GoodsManageBean) obj;
        if (bean.getIndex() == 0) {
            return false;
        }
        if (index == 0) {
            if (bean.getIndex() != 0){
                return false;
            }
        } else if (index != bean.getIndex()){
            return false;
        }
        return true;
    }
}
