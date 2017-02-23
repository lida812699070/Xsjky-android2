package cn.xsjky.android.model;

import java.io.Serializable;

/**
 * Created by OK on 2016/3/22.
 */
public class DocumentEntity implements Serializable{
    @Override
    public String toString() {
        return "DocumentEntity{" +
                "documentId=" + documentId +
                ", DocumentNumber='" + DocumentNumber + '\'' +
                ", ConsigneeName='" + ConsigneeName + '\'' +
                ", ConsigneeContactName='" + ConsigneeContactName + '\'' +
                ", ConsigneeTel='" + ConsigneeTel + '\'' +
                ", AddressLine1='" + AddressLine1 + '\'' +
                ", AddressLine2='" + AddressLine2 + '\'' +
                ", TotalAmount='" + TotalAmount + '\'' +
                ", Quantity='" + Quantity + '\'' +
                ", Weight='" + Weight + '\'' +
                ", Volumn='" + Volumn + '\'' +
                ", ShippingMode='" + ShippingMode + '\'' +
                ", ShippingStatus='" + ShippingStatus + '\'' +
                ", Remarks='" + Remarks + '\'' +
                '}';
    }

    int documentId;
        String DocumentNumber;
        String ConsigneeName;
        String ConsigneeContactName;
        String ConsigneeTel;
        String AddressLine1;
        String AddressLine2;
        String TotalAmount;
        String Quantity;
        String Weight;
        String Volumn;
        String ShippingMode;
        String ShippingStatus;
        String Remarks;

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public String getDocumentNumber() {
        return DocumentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        DocumentNumber = documentNumber;
    }

    public String getConsigneeName() {
        return ConsigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        ConsigneeName = consigneeName;
    }

    public String getConsigneeContactName() {
        return ConsigneeContactName;
    }

    public void setConsigneeContactName(String consigneeContactName) {
        ConsigneeContactName = consigneeContactName;
    }

    public String getConsigneeTel() {
        return ConsigneeTel;
    }

    public void setConsigneeTel(String consigneeTel) {
        ConsigneeTel = consigneeTel;
    }

    public String getAddressLine1() {
        return AddressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        AddressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return AddressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        AddressLine2 = addressLine2;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
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

    public String getShippingMode() {
        return ShippingMode;
    }

    public void setShippingMode(String shippingMode) {
        ShippingMode = shippingMode;
    }

    public String getShippingStatus() {
        return ShippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        ShippingStatus = shippingStatus;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }
}
