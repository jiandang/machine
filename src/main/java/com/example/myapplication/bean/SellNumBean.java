package com.example.myapplication.bean;

import java.io.Serializable;

/**
 * Created by ASXY_home on 2018-10-09.
 */

public class SellNumBean implements Serializable {
    private String rank;//名次
    private String goodsName;//商品名称（日）
    private String dayNum;//日销量数量
    private String goodsName1;//商品名称（月）
    private String monthNum;//月销量数量

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getDayNum() {
        return dayNum;
    }

    public void setDayNum(String dayNum) {
        this.dayNum = dayNum;
    }

    public String getGoodsName1() {
        return goodsName1;
    }

    public void setGoodsName1(String goodsName1) {
        this.goodsName1 = goodsName1;
    }

    public String getMonthNum() {
        return monthNum;
    }

    public void setMonthNum(String monthNum) {
        this.monthNum = monthNum;
    }

    @Override
    public String toString() {
        return "SellNumBean{" +
                "rank='" + rank + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", dayNum='" + dayNum + '\'' +
                ", goodsName1='" + goodsName1 + '\'' +
                ", monthNum='" + monthNum + '\'' +
                '}';
    }
}
