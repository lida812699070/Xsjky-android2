package cn.xsjky.android.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.util.LogU;

/**
 * Created by ${lida} on 2016/9/9.
 */
public class SendPeroson extends DataSupport {

    /**
     * RecordId : 0
     * CompanyId : 1
     * CompanyName : 张镇
     * ContactName : 13822115668
     * MobileNumber : 13822115668
     * Address :
     * Tel : null
     * Fax : null
     * EMail : null
     * IsDefault : false
     * Remarks : null
     * Coordinate : {"Longitude":0,"Latitude":0}
     */
    @Column(unique = true)
    private int RecordId;
    private int CompanyId;
    private String CompanyName;
    private String ContactName;
    private String MobileNumber;
    private String Address;
    private Object Tel;
    private Object Fax;
    private Object EMail;
    private boolean IsDefault;
    private Object Remarks;
    /**
     * Longitude : 0.0
     * Latitude : 0.0
     */

    private CoordinateBean Coordinate;

    @Override
    public String toString() {
        return "SendPeroson{" +
                "RecordId=" + RecordId +
                ", CompanyId=" + CompanyId +
                ", CompanyName='" + CompanyName + '\'' +
                ", ContactName='" + ContactName + '\'' +
                ", MobileNumber='" + MobileNumber + '\'' +
                ", Address='" + Address + '\'' +
                ", Tel=" + Tel +
                ", Fax=" + Fax +
                ", EMail=" + EMail +
                ", IsDefault=" + IsDefault +
                ", Remarks=" + Remarks +
                ", Coordinate=" + Coordinate +
                '}';
    }

    public static SendPeroson objectFromData(String str) {

        return new Gson().fromJson(str, SendPeroson.class);
    }

    public static SendPeroson objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            return new Gson().fromJson(jsonObject.getString(key).toString(), SendPeroson.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<SendPeroson> arraySendPerosonFromData(String str) {

        Type listType = new TypeToken<ArrayList<SendPeroson>>() {}.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<SendPeroson> arraySendPerosonFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<SendPeroson>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(key).toString(), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public int getRecordId() {
        return RecordId;
    }

    public void setRecordId(int RecordId) {
        this.RecordId = RecordId;
    }

    public int getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(int CompanyId) {
        this.CompanyId = CompanyId;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String CompanyName) {
        this.CompanyName = CompanyName;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String ContactName) {
        this.ContactName = ContactName;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String MobileNumber) {
        this.MobileNumber = MobileNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public Object getTel() {
        return Tel;
    }

    public void setTel(Object Tel) {
        this.Tel = Tel;
    }

    public Object getFax() {
        return Fax;
    }

    public void setFax(Object Fax) {
        this.Fax = Fax;
    }

    public Object getEMail() {
        return EMail;
    }

    public void setEMail(Object EMail) {
        this.EMail = EMail;
    }

    public boolean isIsDefault() {
        return IsDefault;
    }

    public void setIsDefault(boolean IsDefault) {
        this.IsDefault = IsDefault;
    }

    public Object getRemarks() {
        return Remarks;
    }

    public void setRemarks(Object Remarks) {
        this.Remarks = Remarks;
    }

    public CoordinateBean getCoordinate() {
        return Coordinate;
    }

    public void setCoordinate(CoordinateBean Coordinate) {
        this.Coordinate = Coordinate;
    }

    public static boolean saveList(List<SendPeroson> sendPerosons) {
        for (SendPeroson sendPeroson : sendPerosons) {
            List<SendPeroson> sendPerosonsList = DataSupport.where("RecordId = ?", sendPeroson.getRecordId() + "").find(SendPeroson.class);
            if (sendPerosonsList == null || sendPerosonsList.size() == 0) {
                save(sendPeroson);
            } else {
                updataSendPerson(sendPeroson);
            }
        }
        return false;
    }

    private static boolean updataSendPerson(SendPeroson sendPeroson) {
        //删除数据库中相对应RecorcId的那条数据
        deleteSendPerson(sendPeroson);
        //保存数据
        return save(sendPeroson);
    }

    private static void deleteSendPerson(SendPeroson sendPeroson) {
        DataSupport.deleteAll(SendPeroson.class, "RecordId = ?", sendPeroson.getRecordId()+"");
    }

    private static boolean save(SendPeroson sendPeroson) {
        sendPeroson.getCoordinate().save();
        return sendPeroson.save();
    }
}
