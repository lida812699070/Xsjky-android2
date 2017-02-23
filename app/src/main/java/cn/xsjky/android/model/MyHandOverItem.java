package cn.xsjky.android.model;

import java.io.Serializable;

/**
 * Created by OK on 2016/4/6.
 */
public class MyHandOverItem implements Serializable{

    private String DocumentNumber;
    private String ToCity;
    private String Quantity;
    private String Weight;
    private String Volumn;
    private String IssueRemarks;

    @Override
    public String toString() {
        return "MyHandOverItem{" +
                "DocumentNumber='" + DocumentNumber + '\'' +
                ", ToCity='" + ToCity + '\'' +
                ", Quantity='" + Quantity + '\'' +
                ", Weight='" + Weight + '\'' +
                ", Volumn='" + Volumn + '\'' +
                ", IssueRemarks='" + IssueRemarks + '\'' +
                '}';
    }

    public String getDocumentNumber() {
        return DocumentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        DocumentNumber = documentNumber;
    }

    public String getIssueRemarks() {
        return IssueRemarks;
    }

    public void setIssueRemarks(String issueRemarks) {
        IssueRemarks = issueRemarks;
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

    public String getToCity() {
        return ToCity;
    }

    public void setToCity(String toCity) {
        ToCity = toCity;
    }
}
