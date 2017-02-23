package cn.xsjky.android.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

import cn.xsjky.android.util.LogU;

/**
 * Created by ${lida} on 2016/11/8.
 */
public class Consignee extends DataSupport implements Serializable {
    //Consignee{id=1, companyName='新世纪', person='李达', city='安徽省,安庆市,枞阳县', addressDetail='哈哈哈', areacode='0556', mobileNum='64484848', otherPhone=''}]
    private int id;
    private String companyName;
    private String person;
    private String city;
    private String tocity;
    private String shipperName;
    private String addressDetail;
    private String areacode;
    private String mobileNum;
    private String otherPhone;

    private boolean isUpdoor;
    private boolean isUpfloor;

    public String getTocity() {
        return tocity;
    }

    public void setTocity(String tocity) {
        this.tocity = tocity;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public boolean isUpdoor() {
        return isUpdoor;
    }

    public void setIsUpdoor(boolean isUpdoor) {
        this.isUpdoor = isUpdoor;
    }

    public boolean isUpfloor() {
        return isUpfloor;
    }

    public void setIsUpfloor(boolean isUpfloor) {
        this.isUpfloor = isUpfloor;
    }

    @Override
    public String toString() {
        return "Consignee{" +
                "id=" + id +
                ", companyName='" + companyName + '\'' +
                ", person='" + person + '\'' +
                ", city='" + city + '\'' +
                ", addressDetail='" + addressDetail + '\'' +
                ", areacode='" + areacode + '\'' +
                ", mobileNum='" + mobileNum + '\'' +
                ", otherPhone='" + otherPhone + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOtherPhone() {
        return otherPhone;
    }

    public void setOtherPhone(String otherPhone) {
        this.otherPhone = otherPhone;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public static void saveIfNotExit(Consignee consignee){

        try{
            List<Consignee> consignees = DataSupport.select("companyName")
                    .where("companyName = ?", consignee.getCompanyName()).find(Consignee.class);
            if (consignees == null || consignees.size() == 0) {
                consignee.save();
            }else {
                Consignee.deleteAll(Consignee.class, "companyName = ?", consignee.getCompanyName());
                consignee.save();
            }
        }catch (Exception e){
            LogU.e(e.getMessage());
        }

    }

    public static Consignee queryByName(String consigneeName){
        try{
            List<Consignee> consignees = DataSupport
                    .where("companyName = ?", consigneeName).find(Consignee.class);
            if (consignees != null && consignees.size() != 0) {
                return consignees.get(0);
            }
        }catch (Exception e){
            LogU.e(e.getMessage());
        }
        return null;
    }
}
