package cn.xsjky.android.model;

/**
 * Created by ${lida} on 2016/7/22.
 */
public class CustomReceivers {
    private String ReceiverId;
    private String CustomerId;
    private String ReceiverName;
    private String ContactPerson;
    private String AreaCode;
    private String TelNumber;
    private Address address;

    @Override
    public String toString() {
        return "CustomReceivers{" +
                "ReceiverId='" + ReceiverId + '\'' +
                ", CustomerId='" + CustomerId + '\'' +
                ", ReceiverName='" + ReceiverName + '\'' +
                ", ContactPerson='" + ContactPerson + '\'' +
                ", AreaCode='" + AreaCode + '\'' +
                ", TelNumber='" + TelNumber + '\'' +
                ", address=" + address +
                '}';
    }

    public String getReceiverId() {
        return ReceiverId;
    }

    public void setReceiverId(String receiverId) {
        ReceiverId = receiverId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getTelNumber() {
        return TelNumber;
    }

    public void setTelNumber(String telNumber) {
        TelNumber = telNumber;
    }

    public String getAreaCode() {
        return AreaCode;
    }

    public void setAreaCode(String areaCode) {
        AreaCode = areaCode;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String contactPerson) {
        ContactPerson = contactPerson;
    }

    public String getReceiverName() {
        return ReceiverName;
    }

    public void setReceiverName(String receiverName) {
        ReceiverName = receiverName;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }
}
