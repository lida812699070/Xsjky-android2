package cn.xsjky.android.model;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by ${lida} on 2016/10/8.
 */
public class Company extends DataSupport {
    private String name;
    private String people;
    private String province;
    private String address;
    private String areaCode;
    private String tel;
    private String otherTel;

    public static void save(Company company){
        List<Company> customs = DataSupport.select("name")
                .where("name = ?", company.getName()).find(Company.class);
        if (customs == null || customs.size() == 0) {
            company.save();
        }else {
            Company.deleteAll(Company.class, "name = ?", company.getName());
            company.save();
        }
    }
    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", people='" + people + '\'' +
                ", province='" + province + '\'' +
                ", address='" + address + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", tel='" + tel + '\'' +
                ", otherTel='" + otherTel + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOtherTel() {
        return otherTel;
    }

    public void setOtherTel(String otherTel) {
        this.otherTel = otherTel;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }
}
