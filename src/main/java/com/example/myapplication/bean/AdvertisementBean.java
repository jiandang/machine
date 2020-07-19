package com.example.myapplication.bean;

import java.io.Serializable;

/**
 * Created by Think on 2018/12/6.
 */

public class AdvertisementBean implements Serializable {
    private String ad_name;//广告图名称
    private String ad_filename;//广告图
    private String ad_videonam;//广告视频
    private int time_start;//广告生效日期 精确到日
    private int time_end;//广告结束日期 精确到日
    private boolean long_term;//表示是否受time控制
    private boolean frozen;//表示当前是否冻结，如果冻结就不播放
    private int period_start;//一天内生效的时间
    private int period_end;//一天内结束的时间

    public String getAd_name() {
        return ad_name;
    }

    public void setAd_name(String ad_name) {
        this.ad_name = ad_name;
    }

    public String getAd_filename() {
        return ad_filename;
    }

    public void setAd_filename(String ad_filename) {
        this.ad_filename = ad_filename;
    }

    public int getTime_start() {
        return time_start;
    }

    public void setTime_start(int time_start) {
        this.time_start = time_start;
    }

    public int getTime_end() {
        return time_end;
    }

    public void setTime_end(int time_end) {
        this.time_end = time_end;
    }

    public boolean isLong_term() {
        return long_term;
    }

    public void setLong_term(boolean long_term) {
        this.long_term = long_term;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public int getPeriod_start() {
        return period_start;
    }

    public void setPeriod_start(int period_start) {
        this.period_start = period_start;
    }

    public int getPeriod_end() {
        return period_end;
    }

    public void setPeriod_end(int period_end) {
        this.period_end = period_end;
    }

    @Override
    public String toString() {
        return "AdvertisementBean{" +
                "ad_name='" + ad_name + '\'' +
                ", ad_filename='" + ad_filename + '\'' +
                ", time_start=" + time_start +
                ", time_end=" + time_end +
                ", long_term=" + long_term +
                ", frozen=" + frozen +
                ", period_start=" + period_start +
                ", period_end=" + period_end +
                '}';
    }
}
