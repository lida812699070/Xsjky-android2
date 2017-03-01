package cn.xsjky.android.model;

/**
 * Created by ${lida} on 2017/3/1.
 */
public class SimpleDocumentBysales {
    private String DocumentNumber;
    private String CreateTime;
    private String ShipperName;
    private String ConsigneeName;
    private String ConsigneeContactPerson;
    private String ToCity;
    private String ShippingMode;
    private String ShippingStatus;

    @Override
    public String toString() {
        return "SimpleDocumentBysales{" +
                "DocumentNumber='" + DocumentNumber + '\'' +
                ", CreateTime='" + CreateTime + '\'' +
                ", ShipperName='" + ShipperName + '\'' +
                ", ConsigneeName='" + ConsigneeName + '\'' +
                ", ConsigneeContactPerson='" + ConsigneeContactPerson + '\'' +
                ", ToCity='" + ToCity + '\'' +
                ", ShippingMode='" + ShippingMode + '\'' +
                ", ShippingStatus='" + ShippingStatus + '\'' +
                '}';
    }

    public String getDocumentNumber() {
        return DocumentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        DocumentNumber = documentNumber;
    }

    public String getShippingStatus() {
        return ShippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        ShippingStatus = shippingStatus;
    }

    public String getShippingMode() {
        return ShippingMode;
    }

    public void setShippingMode(String shippingMode) {
        ShippingMode = shippingMode;
    }

    public String getToCity() {
        return ToCity;
    }

    public void setToCity(String toCity) {
        ToCity = toCity;
    }

    public String getConsigneeContactPerson() {
        return ConsigneeContactPerson;
    }

    public void setConsigneeContactPerson(String consigneeContactPerson) {
        ConsigneeContactPerson = consigneeContactPerson;
    }

    public String getConsigneeName() {
        return ConsigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        ConsigneeName = consigneeName;
    }

    public String getShipperName() {
        return ShipperName;
    }

    public void setShipperName(String shipperName) {
        ShipperName = shipperName;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }
}
