package cn.xsjky.android.model;

import java.io.Serializable;

/**
 * Created by ${lida} on 2017/2/15.
 */
public class CountWeightInfo implements Serializable {
    private int count;
    private double countWeight;
    private double vol;
    private String remark;

    @Override
    public String toString() {
        return "CountWeightInfo{" +
                "count=" + count +
                ", countWeight=" + countWeight +
                ", vol=" + vol +
                ", remark='" + remark + '\'' +
                '}';
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getVol() {
        return vol;
    }

    public void setVol(double vol) {
        this.vol = vol;
    }

    public double getCountWeight() {
        return countWeight;
    }

    public void setCountWeight(double countWeight) {
        this.countWeight = countWeight;
    }
}
