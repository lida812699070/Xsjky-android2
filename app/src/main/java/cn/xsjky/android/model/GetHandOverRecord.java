package cn.xsjky.android.model;

/**
 * Created by OK on 2016/4/6.
 */
public class GetHandOverRecord {
    private int RecordId;
    private String IssueTime;
    private int IssueUserId;
    private String IssueUserName;
    private String IssueEmployee;
    private int ReceiveUserId;
    private String ReceiveUserName;
    private String RecriveEmployee;
    private Boolean IsReceived;
    private String ReceivedTime;
    private int TotalQuantity;
    private String TotalWeight;
    private String TotalVolumn;

    @Override
    public String toString() {
        return "GetHandOverRecord{" +
                "RecordId=" + RecordId +
                ", IssueTime='" + IssueTime + '\'' +
                ", IssueUserId=" + IssueUserId +
                ", IssueUserName='" + IssueUserName + '\'' +
                ", IssueEmployee='" + IssueEmployee + '\'' +
                ", ReceiveUserId=" + ReceiveUserId +
                ", ReceiveUserName='" + ReceiveUserName + '\'' +
                ", RecriveEmployee='" + RecriveEmployee + '\'' +
                ", IsReceived=" + IsReceived +
                ", ReceivedTime='" + ReceivedTime + '\'' +
                ", TotalQuantity=" + TotalQuantity +
                ", TotalWeight='" + TotalWeight + '\'' +
                ", TotalVolumn='" + TotalVolumn + '\'' +
                '}';
    }

    public int getRecordId() {
        return RecordId;
    }

    public void setRecordId(int recordId) {
        RecordId = recordId;
    }

    public String getTotalVolumn() {
        return TotalVolumn;
    }

    public void setTotalVolumn(String totalVolumn) {
        TotalVolumn = totalVolumn;
    }

    public String getTotalWeight() {
        return TotalWeight;
    }

    public void setTotalWeight(String totalWeight) {
        TotalWeight = totalWeight;
    }

    public int getTotalQuantity() {
        return TotalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        TotalQuantity = totalQuantity;
    }

    public String getReceivedTime() {
        return ReceivedTime;
    }

    public void setReceivedTime(String receivedTime) {
        ReceivedTime = receivedTime;
    }

    public Boolean getIsReceived() {
        return IsReceived;
    }

    public void setIsReceived(Boolean isReceived) {
        IsReceived = isReceived;
    }

    public String getRecriveEmployee() {
        return RecriveEmployee;
    }

    public void setRecriveEmployee(String recriveEmployee) {
        RecriveEmployee = recriveEmployee;
    }

    public String getReceiveUserName() {
        return ReceiveUserName;
    }

    public void setReceiveUserName(String receiveUserName) {
        ReceiveUserName = receiveUserName;
    }

    public int getReceiveUserId() {
        return ReceiveUserId;
    }

    public void setReceiveUserId(int receiveUserId) {
        ReceiveUserId = receiveUserId;
    }

    public String getIssueEmployee() {
        return IssueEmployee;
    }

    public void setIssueEmployee(String issueEmployee) {
        IssueEmployee = issueEmployee;
    }

    public String getIssueUserName() {
        return IssueUserName;
    }

    public void setIssueUserName(String issueUserName) {
        IssueUserName = issueUserName;
    }

    public int getIssueUserId() {
        return IssueUserId;
    }

    public void setIssueUserId(int issueUserId) {
        IssueUserId = issueUserId;
    }

    public String getIssueTime() {
        return IssueTime;
    }

    public void setIssueTime(String issueTime) {
        IssueTime = issueTime;
    }
}
