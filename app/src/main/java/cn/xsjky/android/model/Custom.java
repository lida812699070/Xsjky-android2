package cn.xsjky.android.model;

import android.content.ContentValues;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.util.LogU;

/**
 * Created by ${lida} on 2016/7/22.
 */
public class Custom extends DataSupport implements Serializable{
    private int id;
    private String customerId;
    private String customerName;
    private String fax;
    private String telAreaCode;
    private String tel;
    private String contactPerson;
    private String remarks;
    private String isMonthlyBalance;
    private String insuranceRate;
    private String needInsuranced;
    private String address;
    private String isCustomRate;
    private Coordinate coordinate;
    private String OperatorCount;
    private String SendSmsToReceiver;
    private String IsForbidden;

    @Override
    public String toString() {
        return "Custom{" +
                "customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", fax='" + fax + '\'' +
                ", telAreaCode='" + telAreaCode + '\'' +
                ", tel='" + tel + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", remarks='" + remarks + '\'' +
                ", isMonthlyBalance='" + isMonthlyBalance + '\'' +
                ", insuranceRate='" + insuranceRate + '\'' +
                ", needInsuranced='" + needInsuranced + '\'' +
                ", address='" + address + '\'' +
                ", isCustomRate='" + isCustomRate + '\'' +
                ", coordinate=" + coordinate +
                ", OperatorCount='" + OperatorCount + '\'' +
                ", SendSmsToReceiver='" + SendSmsToReceiver + '\'' +
                ", IsForbidden='" + IsForbidden + '\'' +
                '}';
    }

    public static List<Custom> querData(int pagenumber) {
        return DataSupport.offset(10 * pagenumber).limit(10).find(Custom.class, true);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOperatorCount() {
        return OperatorCount;
    }

    public void setOperatorCount(String operatorCount) {
        OperatorCount = operatorCount;
    }

    public String getIsForbidden() {
        return IsForbidden;
    }

    public void setIsForbidden(String isForbidden) {
        IsForbidden = isForbidden;
    }

    public String getSendSmsToReceiver() {
        return SendSmsToReceiver;
    }

    public void setSendSmsToReceiver(String sendSmsToReceiver) {
        SendSmsToReceiver = sendSmsToReceiver;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public static int saveListIntoDb(List<Custom> listCustomers) {
        int size = 0;
        for (Custom custom : listCustomers) {
            List<Custom> customs = DataSupport.select("customerId")
                    .where("customerId = ?", custom.getCustomerId()).find(Custom.class);
            if (customs == null || customs.size() == 0) {
                custom.save();
                size++;
            }
        }
        return size;
    }

    public static boolean delete(Custom custom) {
        try{
            if (custom != null) {
                List<Custom> customs = DataSupport.where("customerId = ?", custom.getCustomerId()).find(Custom.class);
                int delete = DataSupport.delete(Custom.class, customs.get(0).getId());
                if (delete >= 1) {
                    return true;
                }
            }
        }catch (Exception e){
            return false;
        }

        return false;
    }

    public static int updataListIntoDb(List<Custom> listCustomers) {
        int size = 0;
        for (Custom custom : listCustomers) {
            List<Custom> customs = DataSupport.where("customerId = ?", custom.getCustomerId()).find(Custom.class);
            if (customs == null || customs.size() == 0) {
                Coordinate coordinate = custom.getCoordinate();
                if (coordinate == null)
                    return 0;
                coordinate.save();
                custom.setCoordinate(coordinate);
                custom.save();
                size++;
            } else if (customs.size() >= 1) {
                DataSupport.deleteAll(Custom.class, "customerId = ?", custom.getCustomerId());
                Coordinate coordinate = custom.getCoordinate();
                if (coordinate == null)
                    return 0;
                coordinate.save();
                custom.setCoordinate(coordinate);
                custom.save();
            }
        }
        return size;
    }


    private static List<Custom> castToCustom(List<CustomJson> modifyDataCustomJsons) {
        ArrayList<Custom> customs = new ArrayList<>();
        for (CustomJson customJson : modifyDataCustomJsons) {
            Custom custom = new Custom();
            custom.setCustomerId(customJson.getCustomerId() + "");
            custom.setCustomerName(customJson.getCustomerName() + "");
            custom.setFax(customJson.getFax() + "");
            custom.setTel(customJson.getTel() + "");
            custom.setTelAreaCode(customJson.getTelAreaCode() + "");
            custom.setContactPerson(customJson.getContactPerson() + "");
            custom.setRemarks(customJson.getRemarks() + "");
            custom.setIsMonthlyBalance(customJson.isIsMonthlyBalance() + "");
            custom.setInsuranceRate(customJson.getInsuranceRate() + "");
            custom.setNeedInsuranced(customJson.isNeedInsuranced() + "");
            custom.setAddress(customJson.getAddress() + "");
            custom.setIsCustomRate(customJson.isIsCustomRate() + "");
            Coordinate coordinate = new Coordinate();
            CustomJson.CoordinateBean coordinatejson = customJson.getCoordinate();
            coordinate.setLatitude(coordinatejson.getLatitude() + "");
            coordinate.setLongitude(coordinatejson.getLongitude() + "");
            custom.setCoordinate(coordinate);
            custom.setOperatorCount(customJson.getOperatorCount() + "");
            custom.setSendSmsToReceiver(customJson.getSendSmsToReceiver() + "");
            custom.setIsForbidden(customJson.isIsForbidden() + "");
            customs.add(custom);
        }
        return customs;
    }


    public static void updata(Custom custom) {
        if (custom == null)
            return;
        ContentValues contentValues = new ContentValues();
        contentValues.put("customerId", custom.getCustomerId());
        contentValues.put("customerName", custom.getCustomerName());
        contentValues.put("fax", custom.getFax());
        contentValues.put("telAreaCode", custom.getTelAreaCode());
        contentValues.put("tel", custom.getTel());
        contentValues.put("contactPerson", custom.getContactPerson());
        contentValues.put("remarks", custom.getRemarks());
        contentValues.put("isMonthlyBalance", custom.getIsMonthlyBalance());
        contentValues.put("insuranceRate", custom.getInsuranceRate());
        contentValues.put("needInsuranced", custom.getNeedInsuranced());
        contentValues.put("address", custom.getAddress());
        contentValues.put("isCustomRate", custom.getIsCustomRate());
        DataSupport.updateAll(Custom.class,contentValues, "customerId = ?", custom.getCustomerId());
    }


    public static boolean saveIntoDb(Custom custom) {
        List<Custom> customs = DataSupport.where("customerId = ?", custom.getCustomerId()).find(Custom.class);
        if (customs == null || customs.size() == 0) {
            Coordinate coordinate = custom.getCoordinate();
            coordinate.save();
            custom.setCoordinate(coordinate);
            return custom.save();
        } else if (customs.size() >= 1) {
            DataSupport.deleteAll(Custom.class, "customerId = ?", custom.getCustomerId());
            Coordinate coordinate = custom.getCoordinate();
            coordinate.save();
            custom.setCoordinate(coordinate);
            return custom.save();
        }
        return false;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getIsCustomRate() {
        return isCustomRate;
    }

    public void setIsCustomRate(String isCustomRate) {
        this.isCustomRate = isCustomRate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNeedInsuranced() {
        return needInsuranced;
    }

    public void setNeedInsuranced(String needInsuranced) {
        this.needInsuranced = needInsuranced;
    }

    public String getInsuranceRate() {
        return insuranceRate;
    }

    public void setInsuranceRate(String insuranceRate) {
        this.insuranceRate = insuranceRate;
    }

    public String getIsMonthlyBalance() {
        return isMonthlyBalance;
    }

    public void setIsMonthlyBalance(String isMonthlyBalance) {
        this.isMonthlyBalance = isMonthlyBalance;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getTelAreaCode() {
        return telAreaCode;
    }

    public void setTelAreaCode(String telAreaCode) {
        this.telAreaCode = telAreaCode;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }


    public static List<Custom> queryByCustomerName(String textComName) {

        return DataSupport.where("CustomerName like ?", "%" + textComName + "%").find(Custom.class);
    }
}
