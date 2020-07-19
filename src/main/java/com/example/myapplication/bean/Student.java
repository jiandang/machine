package com.example.myapplication.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;


/**
 * Created by ASXY_home on 2018-11-02.
 */
/**
 * 数据库表的实体类
 */
@Table(name = "student")//确定表名

public class Student {
    //必须要有无参构造,否则创建表不成功
    public Student() {
    }
    @Column(name = "_id",isId = true,autoGen = true)//isId为true,代表为主键,autoGen为true代表自增
    public int _id;
    @Column(name = "name")//设置为列名
    public String name;//姓名
    @Column(name = "age")
    public int age;//年龄
    @Column(name = "sex")
    public String sex;//性别
     @Column(name = "bbbb")
    public String bbbb;
     @Column(name = "classes")
    public String classes;
    @Column(name = "aaaaa")
    public String aaaaa;
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBbbb() {
        return bbbb;
    }

    public void setBbbb(String bbbb) {
        this.bbbb = bbbb;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getAaaaa() {
        return aaaaa;
    }

    public void setAaaaa(String aaaaa) {
        this.aaaaa = aaaaa;
    }

    @Override
    public String toString() {
        return "Student{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", bbbb='" + bbbb + '\'' +
                ", classes='" + classes + '\'' +
                ", aaaaa='" + aaaaa + '\'' +
                '}';
    }
}
