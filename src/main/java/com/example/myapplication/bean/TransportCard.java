package com.example.myapplication.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;


/**
 * Created by ASXY_home on 2018-11-02.
 */

/**
 * 数据库表的实体类
 */
@Table(name = "transportCard")//确定表名

public class TransportCard {
    //必须要有无参构造,否则创建表不成功
    public TransportCard() {
    }
    @Column(name = "_id",isId = true,autoGen = true)//isId为true,代表为主键,autoGen为true代表自增
    public int _id;
    @Column(name = "card_num")//设置为列名
    //我发送的
    public String card_num;//卡号
    @Column(name = "balance")
    public int balance;//当前余额 单位分 不是传10.00 要传1000
    @Column(name = "order_no")
    public String order_no;//终端订单号 用设备定义的终端编号+时间戳
    //合肥通获取写卡
    @Column(name = "data0005")
    public String data0005;//05文件
    @Column(name = "data0015")
    public String data0015;//15文件
    @Column(name = "init_result")
    public String init_result;//圈存初始化
    //中银通获取写卡
    @Column(name = "first_gac_result")
    public String first_gac_result;//第一次GAC
    @Column(name = "random")
    public String random;//随机数
    //写卡结果
    @Column(name = "result")
    public String result;//写卡成功或失败
    @Column(name = "tac")
    public String tac;//tac
    //返回的数据
    @Column(name = "money")
    public String money;//领款金额
     @Column(name = "order_code")
    public String order_code;//订单code
    @Column(name = "type")
    public String type;//卡种
    @Column(name = "createtime")
    public String createtime;//订单交易时间
    @Column(name = "apdu")
    public String apdu;//写卡指令
    @Column(name = "arcode")
    public String arcode;//认证通过的授权码
    @Column(name = "arpc")
    public String arpc;//外部认证认证:ARPC

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getCard_num() {
        return card_num;
    }

    public void setCard_num(String card_num) {
        this.card_num = card_num;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getData0005() {
        return data0005;
    }

    public void setData0005(String data0005) {
        this.data0005 = data0005;
    }

    public String getData0015() {
        return data0015;
    }

    public void setData0015(String data0015) {
        this.data0015 = data0015;
    }

    public String getInit_result() {
        return init_result;
    }

    public void setInit_result(String init_result) {
        this.init_result = init_result;
    }

    public String getFirst_gac_result() {
        return first_gac_result;
    }

    public void setFirst_gac_result(String first_gac_result) {
        this.first_gac_result = first_gac_result;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTac() {
        return tac;
    }

    public void setTac(String tac) {
        this.tac = tac;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getApdu() {
        return apdu;
    }

    public void setApdu(String apdu) {
        this.apdu = apdu;
    }

    public String getArcode() {
        return arcode;
    }

    public void setArcode(String arcode) {
        this.arcode = arcode;
    }

    public String getArpc() {
        return arpc;
    }

    public void setArpc(String arpc) {
        this.arpc = arpc;
    }

    @Override
    public String toString() {
        return "TransportCard{" +
                "_id=" + _id +
                ", card_num='" + card_num + '\'' +
                ", balance=" + balance +
                ", order_no='" + order_no + '\'' +
                ", data0005='" + data0005 + '\'' +
                ", data0015='" + data0015 + '\'' +
                ", init_result='" + init_result + '\'' +
                ", first_gac_result='" + first_gac_result + '\'' +
                ", random='" + random + '\'' +
                ", result='" + result + '\'' +
                ", tac='" + tac + '\'' +
                ", money='" + money + '\'' +
                ", order_code='" + order_code + '\'' +
                ", type='" + type + '\'' +
                ", createtime='" + createtime + '\'' +
                ", apdu='" + apdu + '\'' +
                ", arcode='" + arcode + '\'' +
                ", arpc='" + arpc + '\'' +
                '}';
    }
}
