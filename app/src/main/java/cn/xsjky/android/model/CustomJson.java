package cn.xsjky.android.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${lida} on 2016/9/7.
 */
public class CustomJson {

    /**
     * CustomerId : 1
     * CustomerName : 张镇
     * Fax :
     * TelAreaCode :
     * Tel : 15820146511
     * ContactPerson : 小丽
     * Remarks :
     * IsMonthlyBalance : false
     * InsuranceRate : 0.0
     * NeedInsuranced : false
     * Address : 广东省 深圳市 福田区 百合路
     * Coordinate : {"Longitude":0,"Latitude":0}
     * IsCustomRate : false
     * OperatorCount : 0
     * BalancePeriod :
     * SendSmsToReceiver : 0
     * IsForbidden : false
     */

    private int CustomerId;
    private String CustomerName;
    private String Fax;
    private String TelAreaCode;
    private String Tel;
    private String ContactPerson;
    private String Remarks;
    private boolean IsMonthlyBalance;
    private double InsuranceRate;
    private boolean NeedInsuranced;
    private String Address;
    /**
     * Longitude : 0.0
     * Latitude : 0.0
     */

    private CoordinateBean Coordinate;
    private boolean IsCustomRate;
    private int OperatorCount;
    private String BalancePeriod;
    private int SendSmsToReceiver;
    private boolean IsForbidden;

    @Override
    public String toString() {
        return "CustomJson{" +
                "CustomerId=" + CustomerId +
                ", CustomerName='" + CustomerName + '\'' +
                ", Fax='" + Fax + '\'' +
                ", TelAreaCode='" + TelAreaCode + '\'' +
                ", Tel='" + Tel + '\'' +
                ", ContactPerson='" + ContactPerson + '\'' +
                ", Remarks='" + Remarks + '\'' +
                ", IsMonthlyBalance=" + IsMonthlyBalance +
                ", InsuranceRate=" + InsuranceRate +
                ", NeedInsuranced=" + NeedInsuranced +
                ", Address='" + Address + '\'' +
                ", Coordinate=" + Coordinate +
                ", IsCustomRate=" + IsCustomRate +
                ", OperatorCount=" + OperatorCount +
                ", BalancePeriod='" + BalancePeriod + '\'' +
                ", SendSmsToReceiver=" + SendSmsToReceiver +
                ", IsForbidden=" + IsForbidden +
                '}';
    }

    public static CustomJson objectFromData(String str) {

        return new Gson().fromJson(str, CustomJson.class);
    }

    public static CustomJson objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(key).toString(), CustomJson.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<CustomJson> arrayCustomJsonFromData(String str) {

        Type listType = new TypeToken<ArrayList<CustomJson>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<CustomJson> arrayCustomJsonFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<CustomJson>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(key).toString(), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int CustomerId) {
        this.CustomerId = CustomerId;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String CustomerName) {
        this.CustomerName = CustomerName;
    }

    public String getFax() {
        return Fax;
    }

    public void setFax(String Fax) {
        this.Fax = Fax;
    }

    public String getTelAreaCode() {
        return TelAreaCode;
    }

    public void setTelAreaCode(String TelAreaCode) {
        this.TelAreaCode = TelAreaCode;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String Tel) {
        this.Tel = Tel;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String ContactPerson) {
        this.ContactPerson = ContactPerson;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String Remarks) {
        this.Remarks = Remarks;
    }

    public boolean isIsMonthlyBalance() {
        return IsMonthlyBalance;
    }

    public void setIsMonthlyBalance(boolean IsMonthlyBalance) {
        this.IsMonthlyBalance = IsMonthlyBalance;
    }

    public double getInsuranceRate() {
        return InsuranceRate;
    }

    public void setInsuranceRate(double InsuranceRate) {
        this.InsuranceRate = InsuranceRate;
    }

    public boolean isNeedInsuranced() {
        return NeedInsuranced;
    }

    public void setNeedInsuranced(boolean NeedInsuranced) {
        this.NeedInsuranced = NeedInsuranced;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public CoordinateBean getCoordinate() {
        return Coordinate;
    }

    public void setCoordinate(CoordinateBean Coordinate) {
        this.Coordinate = Coordinate;
    }

    public boolean isIsCustomRate() {
        return IsCustomRate;
    }

    public void setIsCustomRate(boolean IsCustomRate) {
        this.IsCustomRate = IsCustomRate;
    }

    public int getOperatorCount() {
        return OperatorCount;
    }

    public void setOperatorCount(int OperatorCount) {
        this.OperatorCount = OperatorCount;
    }

    public String getBalancePeriod() {
        return BalancePeriod;
    }

    public void setBalancePeriod(String BalancePeriod) {
        this.BalancePeriod = BalancePeriod;
    }

    public int getSendSmsToReceiver() {
        return SendSmsToReceiver;
    }

    public void setSendSmsToReceiver(int SendSmsToReceiver) {
        this.SendSmsToReceiver = SendSmsToReceiver;
    }

    public boolean isIsForbidden() {
        return IsForbidden;
    }

    public void setIsForbidden(boolean IsForbidden) {
        this.IsForbidden = IsForbidden;
    }

    public static class CoordinateBean {
        private double Longitude;
        private double Latitude;

        public static CoordinateBean objectFromData(String str) {

            return new Gson().fromJson(str, CoordinateBean.class);
        }

        public static CoordinateBean objectFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);

                return new Gson().fromJson(jsonObject.getString(str), CoordinateBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public static List<CoordinateBean> arrayCoordinateBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<CoordinateBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static List<CoordinateBean> arrayCoordinateBeanFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);
                Type listType = new TypeToken<ArrayList<CoordinateBean>>() {
                }.getType();

                return new Gson().fromJson(jsonObject.getString(str), listType);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return new ArrayList();


        }

        public double getLongitude() {
            return Longitude;
        }

        public void setLongitude(double Longitude) {
            this.Longitude = Longitude;
        }

        public double getLatitude() {
            return Latitude;
        }

        public void setLatitude(double Latitude) {
            this.Latitude = Latitude;
        }
    }
}
