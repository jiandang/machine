package com.example.myapplication.bean;

import java.io.Serializable;

/**
 * Created by ASXY_home on 2018-10-12.
 */

public class BuhuoBean implements Serializable {
    private String num;//货道号
    private DrinkBean bean;//商品

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public DrinkBean getBean() {
        return bean;
    }

    public void setBean(DrinkBean bean) {
        this.bean = bean;
    }

    @Override
    public String toString() {
        return "{" +
                "num=" + num +
                ", bean=" + bean +
                '}';
    }
}
