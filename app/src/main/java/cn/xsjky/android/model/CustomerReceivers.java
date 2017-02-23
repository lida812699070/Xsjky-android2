package cn.xsjky.android.model;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.util.XmlParserUtil;

public class CustomerReceivers extends BaseModel {
	public int ReceiverId;
    public int CustomerId;
    public String ReceiverName;
    public String ContactPerson;
    public  String AreaCode;
    public String TelNumber;
    public Address Address;
    
    public static List<CustomerReceivers> parserXmlList(String xml){
		List<CustomerReceivers> list = new ArrayList<CustomerReceivers>();
		parser = XmlParserUtil.getInstance();
		parser.parse(xml);
		resultPrefix = "/soap:Envelope/soap:Body/FindCustomerReceiverResponse/FindCustomerReceiverResult/ReturnList/";
		int count = getXmlCount("CustomerReceivers");
		for(int i = 0; i < count; i++){
			CustomerReceivers c = new CustomerReceivers();
			String prefix = "CustomerReceivers#" + i + "/";
			c.setCustomerId(getXmlValueInt(prefix + "CustomerId"));
			c.setReceiverId(getXmlValueInt(prefix + "ReceiverId"));
			c.setReceiverName(getXmlValueStr(prefix + "ReceiverName"));
			c.setContactPerson(getXmlValueStr(prefix + "ContactPerson"));
			c.setAreaCode(getXmlValueStr(prefix + "AreaCode"));
			c.setTelNumber(getXmlValueStr(prefix + "TelNumber"));
			Address address = new Address();
			prefix = "CustomerReceivers#" + i + "/Address/";
            address.setAddressId(getXmlValueInt(prefix + "AddressId"));
            address.setProvince(getXmlValueStr(prefix + "Province"));
            address.setCity(getXmlValueStr(prefix + "City"));
            address.setDistrict(getXmlValueStr(prefix + "District"));
            address.setAddress(getXmlValueStr(prefix + "Address"));
            address.setPostCode(getXmlValueStr(prefix + "PostCode"));
            c.setAddress(address);
            list.add(c);
		}
		return list;
	}
     
	public int getReceiverId() {
		return ReceiverId;
	}
	public void setReceiverId(int receiverId) {
		ReceiverId = receiverId;
	}
	public int getCustomerId() {
		return CustomerId;
	}
	public void setCustomerId(int customerId) {
		CustomerId = customerId;
	}
	public String getReceiverName() {
		return ReceiverName;
	}
	public void setReceiverName(String receiverName) {
		ReceiverName = receiverName;
	}
	public String getContactPerson() {
		return ContactPerson;
	}
	public void setContactPerson(String contactPerson) {
		ContactPerson = contactPerson;
	}
	public String getAreaCode() {
		return AreaCode;
	}
	public void setAreaCode(String areaCode) {
		AreaCode = areaCode;
	}
	public String getTelNumber() {
		return TelNumber;
	}
	public void setTelNumber(String telNumber) {
		TelNumber = telNumber;
	}
	public Address getAddress() {
		return Address;
	}
	public void setAddress(Address address) {
		Address = address;
	}
}
