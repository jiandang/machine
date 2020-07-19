package com.example.myapplication.bean;

import java.io.Serializable;

/**
 * Created by Think on 2018/11/30.
 */

public class SheBeiBean implements Serializable {
    private String terminal_no;//终端编号（设备定义的）
    private String terminal_name;//终端名称
    private String terminal_address;//终端地址
    private String aisle_model;//货道型号
    private String operate_way;//运营方式
    private String terminal_code;//终端编号（平台定义的）

    public String getTerminal_no() {
        return terminal_no;
    }

    public void setTerminal_no(String terminal_no) {
        this.terminal_no = terminal_no;
    }

    public String getTerminal_name() {
        return terminal_name;
    }

    public void setTerminal_name(String terminal_name) {
        this.terminal_name = terminal_name;
    }

    public String getTerminal_address() {
        return terminal_address;
    }

    public void setTerminal_address(String terminal_address) {
        this.terminal_address = terminal_address;
    }

    public String getAisle_model() {
        return aisle_model;
    }

    public void setAisle_model(String aisle_model) {
        this.aisle_model = aisle_model;
    }

    public String getOperate_way() {
        return operate_way;
    }

    public void setOperate_way(String operate_way) {
        this.operate_way = operate_way;
    }

    public String getTerminal_code() {
        return terminal_code;
    }

    public void setTerminal_code(String terminal_code) {
        this.terminal_code = terminal_code;
    }

    @Override
    public String toString() {
        return "SheBeiBean{" +
                "terminal_no='" + terminal_no + '\'' +
                ", terminal_name='" + terminal_name + '\'' +
                ", terminal_address='" + terminal_address + '\'' +
                ", aisle_model='" + aisle_model + '\'' +
                ", operate_way='" + operate_way + '\'' +
                ", terminal_code='" + terminal_code + '\'' +
                '}';
    }
}
