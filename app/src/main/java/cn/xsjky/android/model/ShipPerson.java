package cn.xsjky.android.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

import cn.xsjky.android.util.LogU;

/**
 * Created by ${lida} on 2016/11/8.
 */
public class ShipPerson extends DataSupport implements Serializable {
    private String name;
    private String city;
    private String addressDetail;
    private String areaCode;
    private String mobile;
    private String otherPhone;

    @Override
    public String toString() {
        return "ShipPerson{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", addressDetail='" + addressDetail + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", mobile='" + mobile + '\'' +
                ", otherPhone='" + otherPhone + '\'' +
                '}';
    }

    public String getOtherPhone() {
        return otherPhone;
    }

    public void setOtherPhone(String otherPhone) {
        this.otherPhone = otherPhone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static void saveIfNotExit(ShipPerson shipPerson) {

        try{
            List<ShipPerson> shipPersons = DataSupport.select("name")
                    .where("name = ?", shipPerson.getName()).find(ShipPerson.class);
            if (shipPerson == null || shipPersons.size() == 0) {
                shipPerson.save();
            } else {
                ShipPerson.deleteAll(ShipPerson.class, "name = ?", shipPerson.getName());
                shipPerson.save();
            }
        }catch (Exception e){
            LogU.e(e.getMessage());
        }

    }

    public static ShipPerson queryByName(String name) {

        try{
            List<ShipPerson> shipPersons = DataSupport
                    .where("name = ?", name).find(ShipPerson.class);
            if (shipPersons != null && shipPersons.size() != 0) {
                return shipPersons.get(0);
            }
        }catch (Exception e){
            LogU.e(e.getMessage());
        }

        return null;
    }
}
