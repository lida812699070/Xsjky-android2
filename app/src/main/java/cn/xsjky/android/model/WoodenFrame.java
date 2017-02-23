package cn.xsjky.android.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cn.xsjky.android.util.TempletUtil;

/**
 * Created by OK on 2016/4/21.
 */
public class WoodenFrame implements Serializable{
    private String RecordId;
    private String Length;
    private String DocumentNumber;
    private String Width;
    private String Height;
    private String Quantity;
    private String Remarks;
    private String Creator;
    private String HandlerId;
    private String Handler;
    private String State;
    private String AssignTime;
    private String AcceptTime;
    private String FinishedTime;
    public String toTemplet(){
        final String TEMPLET = "<WoodenFrame>"
                +"<RecordId>${recordId}</RecordIld>"
                +"<Length>${length}</Length>"
                +"<Width>${width}</Width>"
                +"<Height>${height}</Height>"
                +"<Quantity>${quantity}</Quantity>"
                +"<Remarks>${remarks}</Remarks>"
                +"<Creator>${creator}</Creator>"
                +"<HandlerId>${handlerId}</HandlerId>"
                +"<Handler>${handler}</Handler>"
                +"<State>${state}</State>"
                +"<AssignTime>${assignTime}</AssignTime>"
                +"<AcceptTime>${acceptTime}</AcceptTime>"
                +"<FinishedTime>${finishedTime}</AcceptTime>"
                +"</WoodenFrame>";
        Map<String, String> map = new HashMap<String, String>();
        map.put("recordId", String.valueOf(RecordId));
        map.put("length", String.valueOf(Length));
        map.put("width", String.valueOf(Width));
        map.put("height", String.valueOf(Height));
        map.put("quantity", String.valueOf(Quantity));
        map.put("remarks", String.valueOf(Remarks));
        map.put("creator", String.valueOf(Creator));
        map.put("handlerId", String.valueOf(HandlerId));
        map.put("handler", String.valueOf(Handler));
        map.put("state", String.valueOf(State));
        map.put("assignTime", String.valueOf(AssignTime));
        map.put("acceptTime", String.valueOf(AcceptTime));
        map.put("finishedTime", String.valueOf(FinishedTime));
        return TempletUtil.render(TEMPLET, map);
    }
    @Override
    public String toString() {
        return "WoodenFrame{" +
                "RecordId='" + RecordId + '\'' +
                ", DocumentNumber='" + DocumentNumber + '\'' +
                ", Length='" + Length + '\'' +
                ", Width='" + Width + '\'' +
                ", Height='" + Height + '\'' +
                ", Quantity='" + Quantity + '\'' +
                ", Remarks='" + Remarks + '\'' +
                ", Creator='" + Creator + '\'' +
                ", HandlerId='" + HandlerId + '\'' +
                ", Handler='" + Handler + '\'' +
                ", State='" + State + '\'' +
                ", AssignTime='" + AssignTime + '\'' +
                ", AcceptTime='" + AcceptTime + '\'' +
                ", FinishedTime='" + FinishedTime + '\'' +
                '}';
    }

    public String getDocumentNumber() {
        return DocumentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        DocumentNumber = documentNumber;
    }

    public String getRecordId() {
        return RecordId;
    }

    public void setRecordId(String recordId) {
        RecordId = recordId;
    }

    public String getFinishedTime() {
        return FinishedTime;
    }

    public void setFinishedTime(String finishedTime) {
        FinishedTime = finishedTime;
    }

    public String getAcceptTime() {
        return AcceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        AcceptTime = acceptTime;
    }

    public String getAssignTime() {
        return AssignTime;
    }

    public void setAssignTime(String assignTime) {
        AssignTime = assignTime;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getHandler() {
        return Handler;
    }

    public void setHandler(String handler) {
        Handler = handler;
    }

    public String getHandlerId() {
        return HandlerId;
    }

    public void setHandlerId(String handlerId) {
        HandlerId = handlerId;
    }

    public String getCreator() {
        return Creator;
    }

    public void setCreator(String creator) {
        Creator = creator;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    public String getWidth() {
        return Width;
    }

    public void setWidth(String width) {
        Width = width;
    }

    public String getLength() {
        return Length;
    }

    public void setLength(String length) {
        Length = length;
    }
}
