package com.example.myapplication.bean;

import java.io.Serializable;

/**
 * Created by Think on 2018/11/21.
 */

public class SerialPort implements Serializable {
    private String devices;//串口号
    private int baudrates;//波特率

    public SerialPort(String devices, int baudrates) {
        this.devices = devices;
        this.baudrates = baudrates;
    }

    public SerialPort() {
    }

    public String getDevices() {
        return devices;
    }

    public void setDevices(String devices) {
        this.devices = devices;
    }

    public int getBaudrates() {
        return baudrates;
    }

    public void setBaudrates(int baudrates) {
        this.baudrates = baudrates;
    }

    @Override
    public String toString() {
        return "SerialPort{" +
                "devices='" + devices + '\'' +
                ", baudrates=" + baudrates +
                '}';
    }


}
