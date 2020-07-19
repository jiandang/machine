package com.example.myapplication.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;


/**
 * Created by ASXY_home on 2018-11-02.
 */

/**
 * 数据库表的实体类
 */
@Table(name = "order")//确定表名

public class Order {
    //必须要有无参构造,否则创建表不成功
    public Order() {
    }
    @Column(name = "_id",isId = true,autoGen = true)//isId为true,代表为主键,autoGen为true代表自增
    public int _id;
    @Column(name = "shopName")//设置为列名
    public String shopName;//商品名称
    @Column(name = "tunnel_code")
    public String tunnel_code;//货道号
    @Column(name = "shopPrice")
    public String shopPrice;//商品价格
    @Column(name = "order_status")
    public int order_status;//订单状态
    @Column(name = "trade_status")
    public int trade_status;//支付状态
    @Column(name = "refund_status")
    public int refund_status;//退款状态
     @Column(name = "deliver_status")
    public int deliver_status;//出货状态

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getTunnel_code() {
        return tunnel_code;
    }

    public void setTunnel_code(String tunnel_code) {
        this.tunnel_code = tunnel_code;
    }

    public String getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(String shopPrice) {
        this.shopPrice = shopPrice;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public int getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(int trade_status) {
        this.trade_status = trade_status;
    }

    public int getRefund_status() {
        return refund_status;
    }

    public void setRefund_status(int refund_status) {
        this.refund_status = refund_status;
    }

    public int getDeliver_status() {
        return deliver_status;
    }

    public void setDeliver_status(int deliver_status) {
        this.deliver_status = deliver_status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "_id=" + _id +
                ", shopName='" + shopName + '\'' +
                ", tunnel_code='" + tunnel_code + '\'' +
                ", shopPrice=" + shopPrice +
                ", order_status=" + order_status +
                ", trade_status=" + trade_status +
                ", refund_status=" + refund_status +
                ", deliver_status=" + deliver_status +
                '}';
    }
}
