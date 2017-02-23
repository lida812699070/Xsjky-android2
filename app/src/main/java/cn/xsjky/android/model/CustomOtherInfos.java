package cn.xsjky.android.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

import cn.xsjky.android.util.LogU;

/**
 * Created by ${lida} on 2016/11/8.
 */
public class CustomOtherInfos extends DataSupport implements Serializable {

    private String tocity;
    private String customName;
    private String consigneeName;
    private String shipMode;
    private String goodsName;
    private String countWeight;
    private String shipName;
    private boolean insurance;

    public String getCountWeight() {
        return countWeight;
    }

    public void setCountWeight(String countWeight) {
        this.countWeight = countWeight;
    }

    public boolean isInsurance() {
        return insurance;
    }

    public void setInsurance(boolean insurance) {
        this.insurance = insurance;
    }

    @Override
    public String toString() {
        return "CustomOtherInfos{" +
                "tocity='" + tocity + '\'' +
                ", customName='" + customName + '\'' +
                ", consigneeName='" + consigneeName + '\'' +
                ", shipMode='" + shipMode + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", countWeight='" + countWeight + '\'' +
                ", shipName='" + shipName + '\'' +
                ", insurance=" + insurance +
                '}';
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getTocity() {
        return tocity;
    }

    public void setTocity(String tocity) {
        this.tocity = tocity;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getShipMode() {
        return shipMode;
    }

    public void setShipMode(String shipMode) {
        this.shipMode = shipMode;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public static void saveIfNotExit(CustomOtherInfos customOtherInfos) {


        try{
            List<CustomOtherInfos> customOtherInfosList = DataSupport
                    .where("tocity = '" + customOtherInfos.getTocity()
                            + "' and customName = '" + customOtherInfos.getCustomName()
                            + "' and goodsName = '" + customOtherInfos.getGoodsName()
                            + "' and shipMode = '" + customOtherInfos.getShipMode()
                            + "' and shipName = '" + customOtherInfos.getShipName()
                            + "' and consigneeName = '" + customOtherInfos.getConsigneeName() + "'").find(CustomOtherInfos.class);
            if (customOtherInfosList == null || customOtherInfosList.size() == 0) {
                customOtherInfos.save();
            }
        }catch (Exception e){
            LogU.e(e.getMessage());
        }

    }

    public static List<CustomOtherInfos> queryByToString(String sql) {

        return DataSupport.where(sql).find(CustomOtherInfos.class);

    }
}
