package cn.xsjky.android.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by OK on 2016/4/6.
 */
public class MyHandOverRecord implements Serializable{
    private String RecordId;
    private String IssueTime;
    private String IssueUserId;
    private String IssueUserName;
    private String IssueEmployee;
    private String ReceiveUserId;
    private String ReceiveUserName;
    private String ReceiveEmployee;
    private String IsReceived;
    private String ReceivedTime;
    private String UploadActors;
    private String TotalQuantity;
    private String TotalWeight;
    private String TotalVolumn;
    private DownLoadActors downLoadActors;

    @Override
    public String toString() {
        return "MyHandOverRecord{" +
                "RecordId='" + RecordId + '\'' +
                ", IssueTime='" + IssueTime + '\'' +
                ", IssueUserId='" + IssueUserId + '\'' +
                ", IssueUserName='" + IssueUserName + '\'' +
                ", IssueEmployee='" + IssueEmployee + '\'' +
                ", ReceiveUserId='" + ReceiveUserId + '\'' +
                ", ReceiveUserName='" + ReceiveUserName + '\'' +
                ", ReceiveEmployee='" + ReceiveEmployee + '\'' +
                ", IsReceived='" + IsReceived + '\'' +
                ", ReceivedTime='" + ReceivedTime + '\'' +
                ", UploadActors='" + UploadActors + '\'' +
                ", TotalQuantity='" + TotalQuantity + '\'' +
                ", TotalWeight='" + TotalWeight + '\'' +
                ", TotalVolumn='" + TotalVolumn + '\'' +
                ", downLoadActors=" + downLoadActors +
                ", listItem=" + listItem +
                '}';
    }

    private List<MyHandOverItem> listItem;

    public List<MyHandOverItem> getListItem() {
        return listItem;
    }

    public void setListItem(List<MyHandOverItem> listItem) {
        this.listItem = listItem;
    }

    public String getRecordId() {
        return RecordId;
    }

    public void setRecordId(String recordId) {
        RecordId = recordId;
    }

    public DownLoadActors getDownLoadActors() {
        return downLoadActors;
    }

    public void setDownLoadActors(DownLoadActors downLoadActors) {
        this.downLoadActors = downLoadActors;
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

    public String getTotalQuantity() {
        return TotalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        TotalQuantity = totalQuantity;
    }

    public String getUploadActors() {
        return UploadActors;
    }

    public void setUploadActors(String uploadActors) {
        UploadActors = uploadActors;
    }

    public String getReceivedTime() {
        return ReceivedTime;
    }

    public void setReceivedTime(String receivedTime) {
        ReceivedTime = receivedTime;
    }

    public String getIsReceived() {
        return IsReceived;
    }

    public void setIsReceived(String isReceived) {
        IsReceived = isReceived;
    }

    public String getReceiveEmployee() {
        return ReceiveEmployee;
    }

    public void setReceiveEmployee(String receiveEmployee) {
        ReceiveEmployee = receiveEmployee;
    }

    public String getReceiveUserName() {
        return ReceiveUserName;
    }

    public void setReceiveUserName(String receiveUserName) {
        ReceiveUserName = receiveUserName;
    }

    public String getReceiveUserId() {
        return ReceiveUserId;
    }

    public void setReceiveUserId(String receiveUserId) {
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

    public String getIssueUserId() {
        return IssueUserId;
    }

    public void setIssueUserId(String issueUserId) {
        IssueUserId = issueUserId;
    }

    public String getIssueTime() {
        return IssueTime;
    }

    public void setIssueTime(String issueTime) {
        IssueTime = issueTime;
    }
}
