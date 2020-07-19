package com.example.myapplication.bean;

import java.io.Serializable;

/**
 * Created by ASXY_home on 2018-10-12.
 */

public class SettingBean implements Serializable {
    private String imgName;//图片名称
    private String imgUrl;//图片url
    private String imgPay;//支付方式

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgPay() {
        return imgPay;
    }

    public void setImgPay(String imgPay) {
        this.imgPay = imgPay;
    }

    @Override
    public String toString() {
        return "SettingBean{" +
                "imgName='" + imgName + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", imgPay='" + imgPay + '\'' +
                '}';
    }

}
