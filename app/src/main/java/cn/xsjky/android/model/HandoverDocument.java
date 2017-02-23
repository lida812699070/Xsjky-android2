package cn.xsjky.android.model;

import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by ${lida} on 2016/5/11.
 */
public class HandoverDocument {

    private boolean IsCheck=false;
    private String DocumentId;
    private String HandoverQty;
    private String HandoverVolumn;
    private String DocumentNumber;
    private String ToCity;
    private String Remarks;

    public boolean isCheck() {
        return IsCheck;
    }

    public void setIsCheck(boolean isCheck) {
        IsCheck = isCheck;
    }

    @Override
    public String toString() {
        return "HandoverDocument{" +
                "DocumentId='" + DocumentId + '\'' +
                ", HandoverQty='" + HandoverQty + '\'' +
                ", HandoverVolumn='" + HandoverVolumn + '\'' +
                ", DocumentNumber='" + DocumentNumber + '\'' +
                ", ToCity='" + ToCity + '\'' +
                ", Remarks='" + Remarks + '\'' +
                '}';
    }

    public String getDocumentId() {
        return DocumentId;
    }

    public void setDocumentId(String documentId) {
        DocumentId = documentId;
    }

    public String getHandoverQty() {
        return HandoverQty;
    }

    public void setHandoverQty(String handoverQty) {
        HandoverQty = handoverQty;
    }

    public String getHandoverVolumn() {
        return HandoverVolumn;
    }

    public void setHandoverVolumn(String handoverVolumn) {
        HandoverVolumn = handoverVolumn;
    }

    public String getDocumentNumber() {
        return DocumentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        DocumentNumber = documentNumber;
    }

    public String getToCity() {
        return ToCity;
    }

    public void setToCity(String toCity) {
        ToCity = toCity;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }
}
