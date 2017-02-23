package cn.xsjky.android.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ${lida} on 2016/5/24.
 */
public class SimpleDocument implements Serializable{
    private String DocumentNumber;
    private String FromCity;
    private String CountWeight;
    private String ToCity;
    private String ShipperName;
    private String ShipperCode;
    private String ShipperContactName;
    private Address ShipperAddress;
    private String ShipperPhoneNumber;
    private String ShipperAreaCode;
    private String ConsigneeAreaCode;
    private String ConsigneeName;
    private String ConsigneePhoneNumber;
    private String ConsigneeContactPerson;
    private Address ConsigneeAddress;
    private ShippingMode ShippingMode;
    private String ProductName;
    private List<ShipperCost> CarriageItems;
    private String Volumn;
    private String Weight;
    private String Quantity;
    private String DeliveryCharge;
    private String InsuranceAmt;
    private String BalanceMode;
    private String RecordId;
    private boolean NeedDelivery;
    private boolean Upstair;
    private boolean DeliveryAfterNotify;
    private boolean NeedInsurance;
    private boolean NeedSignUpNotifyMessage;
    private String Remarks;

    public String getCountWeight() {
        return CountWeight;
    }

    public void setCountWeight(String countWeight) {
        CountWeight = countWeight;
    }

    public String getShipperCode() {
        return ShipperCode;
    }

    public void setShipperCode(String shipperCode) {
        ShipperCode = shipperCode;
    }

    public String getRecordId() {
        return RecordId;
    }

    public void setRecordId(String recordId) {
        RecordId = recordId;
    }

    public String getShipperAreaCode() {
        return ShipperAreaCode;
    }

    public void setShipperAreaCode(String shipperAreaCode) {
        ShipperAreaCode = shipperAreaCode;
    }

    public String getConsigneeAreaCode() {
        return ConsigneeAreaCode;
    }

    public void setConsigneeAreaCode(String consigneeAreaCode) {
        ConsigneeAreaCode = consigneeAreaCode;
    }

    public boolean isNeedSignUpNotifyMessage() {
        return NeedSignUpNotifyMessage;
    }

    public String getBalanceMode() {
        return BalanceMode;
    }

    public void setBalanceMode(String balanceMode) {
        BalanceMode = balanceMode;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public void setNeedSignUpNotifyMessage(boolean needSignUpNotifyMessage) {
        NeedSignUpNotifyMessage = needSignUpNotifyMessage;
    }

    public boolean isNeedInsurance() {
        return NeedInsurance;
    }

    public void setNeedInsurance(boolean needInsurance) {
        NeedInsurance = needInsurance;
    }

    public String getInsuranceAmt() {
        return InsuranceAmt;
    }

    public void setInsuranceAmt(String insuranceAmt) {
        InsuranceAmt = insuranceAmt;
    }

    public String getDocumentNumber() {
        return DocumentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        DocumentNumber = documentNumber;
    }

    public boolean isDeliveryAfterNotify() {
        return DeliveryAfterNotify;
    }

    public void setDeliveryAfterNotify(boolean deliveryAfterNotify) {
        DeliveryAfterNotify = deliveryAfterNotify;
    }

    public boolean isUpstair() {
        return Upstair;
    }

    public void setUpstair(boolean upstair) {
        Upstair = upstair;
    }

    public boolean isNeedDelivery() {
        return NeedDelivery;
    }

    public void setNeedDelivery(boolean needDelivery) {
        NeedDelivery = needDelivery;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getDeliveryCharge() {
        return DeliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        DeliveryCharge = deliveryCharge;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getVolumn() {
        return Volumn;
    }

    public void setVolumn(String volumn) {
        Volumn = volumn;
    }

    public List<ShipperCost> getCarriageItems() {
        return CarriageItems;
    }

    public void setCarriageItems(List<ShipperCost> shipperCosts) {
        CarriageItems = shipperCosts;
    }

    public cn.xsjky.android.model.ShippingMode getShippingMode() {
        return ShippingMode;
    }

    public void setShippingMode(cn.xsjky.android.model.ShippingMode shippingMode) {
        ShippingMode = shippingMode;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public Address getConsigneeAddress() {
        return ConsigneeAddress;
    }

    public void setConsigneeAddress(Address consigneeAddress) {
        ConsigneeAddress = consigneeAddress;
    }

    public String getConsigneeContactPerson() {
        return ConsigneeContactPerson;
    }

    public void setConsigneeContactPerson(String consigneeContactPerson) {
        ConsigneeContactPerson = consigneeContactPerson;
    }

    public String getConsigneePhoneNumber() {
        return ConsigneePhoneNumber;
    }

    public void setConsigneePhoneNumber(String consigneePhoneNumber) {
        ConsigneePhoneNumber = consigneePhoneNumber;
    }

    public String getConsigneeName() {
        return ConsigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        ConsigneeName = consigneeName;
    }

    public String getShipperPhoneNumber() {
        return ShipperPhoneNumber;
    }

    public void setShipperPhoneNumber(String shipperPhoneNumber) {
        ShipperPhoneNumber = shipperPhoneNumber;
    }

    public Address getShipperAddress() {
        return ShipperAddress;
    }

    public void setShipperAddress(Address shipperAddress) {
        ShipperAddress = shipperAddress;
    }

    public String getShipperContactName() {
        return ShipperContactName;
    }

    public void setShipperContactName(String shipperContactName) {
        ShipperContactName = shipperContactName;
    }

    public String getShipperName() {
        return ShipperName;
    }

    public void setShipperName(String shipperName) {
        ShipperName = shipperName;
    }

    public String getToCity() {
        return ToCity;
    }

    public void setToCity(String toCity) {
        ToCity = toCity;
    }

    public String getFromCity() {
        return FromCity;
    }

    public void setFromCity(String fromCity) {
        FromCity = fromCity;
    }
}
