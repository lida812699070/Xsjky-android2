package cn.xsjky.android.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by ${lida} on 2016/10/18.
 */
public class PrintDocumentInfo extends DataSupport implements Serializable{

    private int id;
    private String documentNumber;
    private String quantity;
    private String recordId;
    private String fromCity;
    private String toCity;
    private String consigneeContactPerson;
    private String consigneePhoneNumber;
    private String consigneeAddress;
    private String district;

    @Override
    public String toString() {
        return "PrintDocumentInfo{" +
                "id=" + id +
                ", documentNumber='" + documentNumber + '\'' +
                ", quantity='" + quantity + '\'' +
                ", district='" + district + '\'' +
                ", recordId='" + recordId + '\'' +
                ", fromCity='" + fromCity + '\'' +
                ", toCity='" + toCity + '\'' +
                ", consigneeContactPerson='" + consigneeContactPerson + '\'' +
                ", consigneePhoneNumber='" + consigneePhoneNumber + '\'' +
                ", consigneeAddress='" + consigneeAddress + '\'' +
                '}';
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public String getConsigneePhoneNumber() {
        return consigneePhoneNumber;
    }

    public void setConsigneePhoneNumber(String consigneePhoneNumber) {
        this.consigneePhoneNumber = consigneePhoneNumber;
    }

    public String getConsigneeContactPerson() {
        return consigneeContactPerson;
    }

    public void setConsigneeContactPerson(String consigneeContactPerson) {
        this.consigneeContactPerson = consigneeContactPerson;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
}
