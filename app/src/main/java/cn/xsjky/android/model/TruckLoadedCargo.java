package cn.xsjky.android.model;

import java.io.Serializable;

/**
 * Created by OK on 2016/4/7.
 */
public class TruckLoadedCargo implements Serializable{
    private String TruckNumber;
    private String LoadedQty;
    private String LoadedWeight;
    private String LoadedVolumn;
    private String DocumentId;
    private String DocumentNumber;
    private String Quantity;
    private String Weight;
    private String Volumn;
    private String ToCity;
    @Override
    public String toString() {
        return "TruckLoadedCargo{" +
                "TruckNumber='" + TruckNumber + '\'' +
                ", LoadedQty='" + LoadedQty + '\'' +
                ", LoadedWeight='" + LoadedWeight + '\'' +
                ", LoadedVolumn='" + LoadedVolumn + '\'' +
                ", DocumentId='" + DocumentId + '\'' +
                ", DocumentNumber='" + DocumentNumber + '\'' +
                ", Quantity='" + Quantity + '\'' +
                ", Weight='" + Weight + '\'' +
                ", Volumn='" + Volumn + '\'' +
                ", ToCity='" + ToCity + '\'' +
                '}';
    }

    public String getLoadedWeight() {
        return LoadedWeight;
    }

    public void setLoadedWeight(String loadedWeight) {
        LoadedWeight = loadedWeight;
    }



    public String getTruckNumber() {
        return TruckNumber;
    }

    public void setTruckNumber(String truckNumber) {
        TruckNumber = truckNumber;
    }

    public String getToCity() {
        return ToCity;
    }

    public void setToCity(String toCity) {
        ToCity = toCity;
    }

    public String getVolumn() {
        return Volumn;
    }

    public void setVolumn(String volumn) {
        Volumn = volumn;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getDocumentNumber() {
        return DocumentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        DocumentNumber = documentNumber;
    }

    public String getDocumentId() {
        return DocumentId;
    }

    public void setDocumentId(String documentId) {
        DocumentId = documentId;
    }

    public String getLoadedVolumn() {
        return LoadedVolumn;
    }

    public void setLoadedVolumn(String loadedVolumn) {
        LoadedVolumn = loadedVolumn;
    }

    public String getLoadedQty() {
        return LoadedQty;
    }

    public void setLoadedQty(String loadedQty) {
        LoadedQty = loadedQty;
    }


}
