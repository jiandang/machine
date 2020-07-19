package com.example.myapplication.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;


/**
 * Created by ASXY_home on 2018-11-02.
 */

/**
 * 数据库表的实体类
 */
@Table(name = "goods")//确定表名

public class Goods {
    //必须要有无参构造,否则创建表不成功
    public Goods() {
    }
    @Column(name = "_id",isId = true,autoGen = true)//isId为true,代表为主键,autoGen为true代表自增
    public int _id;
    @Column(name = "tunnel_code")//设置为列名
    public String tunnel_code;//货道号
    @Column(name = "stock")
    public String stock;//库存

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTunnel_code() {
        return tunnel_code;
    }

    public void setTunnel_code(String tunnel_code) {
        this.tunnel_code = tunnel_code;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "_id=" + _id +
                ", tunnel_code='" + tunnel_code + '\'' +
                ", stock='" + stock + '\'' +
                '}';
    }
}
