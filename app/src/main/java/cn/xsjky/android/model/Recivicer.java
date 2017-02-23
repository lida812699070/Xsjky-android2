package cn.xsjky.android.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${lida} on 2016/9/14.
 */
public class Recivicer extends DataSupport {

    /**
     * CustomerId : 65
     * ShortName : null
     * CustomerName : 瑞霖医药
     * ReceiverId : 43
     * ReceiverName : 福州常春药业有限公司
     * City : 福州市
     * ContactPerson : 常春医药
     * AreaCode : 0591
     * TelNumber : 83272826
     * Mobile :
     * Address : 福建省 福州市 台江区  八一七中路835号嘉兴小区三层
     * Coordinate : {"Longitude":0,"Latitude":0}
     * Remarks : null
     */

    private int CustomerId;
    private Object ShortName;
    private String CustomerName;
    private int ReceiverId;
    private String ReceiverName;
    private String City;
    private String ContactPerson;
    private String AreaCode;
    private String TelNumber;
    private String Mobile;
    private String Address;
    /**
     * Longitude : 0
     * Latitude : 0
     */

    private CoordinateBean Coordinate;
    private Object Remarks;

    public static Recivicer objectFromData(String str) {

        return new Gson().fromJson(str, Recivicer.class);
    }

    public static Recivicer objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(key).toString(), Recivicer.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Recivicer> arrayRecivicerFromData(String str) {
        Type listType = new TypeToken<ArrayList<Recivicer>>() {}.getType();

        return new Gson().fromJson(str, listType);

    }

    public static List<Recivicer> arrayRecivicerFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<Recivicer>>() {
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

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public Object getRemarks() {
        return Remarks;
    }

    public void setRemarks(Object remarks) {
        Remarks = remarks;
    }

    public CoordinateBean getCoordinate() {
        return Coordinate;
    }

    public void setCoordinate(CoordinateBean coordinate) {
        Coordinate = coordinate;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getTelNumber() {
        return TelNumber;
    }

    public void setTelNumber(String telNumber) {
        TelNumber = telNumber;
    }

    public String getAreaCode() {
        return AreaCode;
    }

    public void setAreaCode(String areaCode) {
        AreaCode = areaCode;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String contactPerson) {
        ContactPerson = contactPerson;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getReceiverName() {
        return ReceiverName;
    }

    public void setReceiverName(String receiverName) {
        ReceiverName = receiverName;
    }

    public int getReceiverId() {
        return ReceiverId;
    }

    public void setReceiverId(int receiverId) {
        ReceiverId = receiverId;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public Object getShortName() {
        return ShortName;
    }

    public void setShortName(Object shortName) {
        ShortName = shortName;
    }

    @Override
    public String toString() {
        return "Recivicer{" +
                "CustomerId=" + CustomerId +
                ", ShortName=" + ShortName +
                ", CustomerName='" + CustomerName + '\'' +
                ", ReceiverId=" + ReceiverId +
                ", ReceiverName='" + ReceiverName + '\'' +
                ", City='" + City + '\'' +
                ", ContactPerson='" + ContactPerson + '\'' +
                ", AreaCode='" + AreaCode + '\'' +
                ", TelNumber='" + TelNumber + '\'' +
                ", Mobile='" + Mobile + '\'' +
                ", Address='" + Address + '\'' +
                ", Coordinate=" + Coordinate +
                ", Remarks=" + Remarks +
                '}';
    }

    //如果不存在就保存   如果存在可就更新
    public static boolean saveList(List<Recivicer> recivicers) {
        for (Recivicer recivicer : recivicers) {
            List<Recivicer> recivicerList = DataSupport.where("ReceiverId = ?", recivicer.getReceiverId() + "").find(Recivicer.class);
            if (recivicerList == null || recivicerList.size() == 0) {
                save(recivicer);
            } else {
                updataRecivicer(recivicer);
            }
        }
        return false;
    }

    public static boolean updataRecivicer(Recivicer recivicer) {
        /*int id = DataSupport.select("id").where("ReceiverId = ?", recivicer.getReceiverId() + "").findFirst(Recivicer.class).getId();
        recivicer.update(id);
        int coorId = recivicer.getCoordinate().getId();
        recivicer.getCoordinate().update(coorId);*/
        //删除数据库中相对应RecorcId的那条数据
        deleteRecivicer(recivicer);
        //保存数据
        return save(recivicer);
        //return true;
    }

    public static void deleteRecivicer(Recivicer recivicer) {
        DataSupport.deleteAll(Recivicer.class, "ReceiverId = ?", recivicer.getReceiverId() + "");
    }

    public static boolean save(Recivicer recivicer) {
        recivicer.getCoordinate().save();
        return recivicer.save();
    }
}
