package cn.xsjky.android.model;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.util.XmlParserUtil;

/**
 * 客户
 * @author Jerry
 *
 */
public class Customer extends BaseModel{
	private int customerId;
	private String customerName;
	private String fax;
	private String telAreaCode;
	private String tel;
	private String contactPerson;
	private String remarks;
	private boolean isMonthlyBalance;
	private double insuranceRate;
	private boolean needInsuranced;
	private Address address;
	private boolean isCustomRate;
	
	public static List<Customer> parserXmlList(String xml){
		List<Customer> list = new ArrayList<Customer>();
		parser = XmlParserUtil.getInstance();
		parser.parse(xml);
		resultPrefix = "/soap:Envelope/soap:Body/GetCustomersResponse/GetCustomersResult/ReturnList/";
		int count = getXmlCount("Customer");
		for(int i = 0; i < count; i++){
			Customer c = new Customer();
			String prefix = "Customer#" + i + "/";
			c.setCustomerId(getXmlValueInt(prefix + "CustomerId"));
			c.setCustomerName(getXmlValueStr(prefix + "CustomerName"));
			c.setFax(getXmlValueStr(prefix + "Fax"));
			c.setTelAreaCode(getXmlValueStr(prefix + "TelAreaCode"));
			c.setTel(getXmlValueStr(prefix + "Tel"));
			c.setContactPerson(getXmlValueStr(prefix + "ContactPerson"));
			c.setRemarks(getXmlValueStr(prefix + "Remarks"));
			c.setMonthlyBalance(getXmlValueBool(prefix + "IsMonthlyBalance"));
			c.setInsuranceRate(getXmlValueDouble(prefix + "InsuranceRate"));
			c.setNeedInsuranced(getXmlValueBool(prefix + "NeedInsuranced"));
			c.setCustomRate(getXmlValueBool(prefix + "IsCustomRate"));
			Address address = new Address();
			prefix = "Customer#" + i + "/Address/";
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
	
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getTelAreaCode() {
		return telAreaCode;
	}
	public void setTelAreaCode(String telAreaCode) {
		this.telAreaCode = telAreaCode;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public boolean isMonthlyBalance() {
		return isMonthlyBalance;
	}
	public void setMonthlyBalance(boolean isMonthlyBalance) {
		this.isMonthlyBalance = isMonthlyBalance;
	}
	public double getInsuranceRate() {
		return insuranceRate;
	}
	public void setInsuranceRate(double insuranceRate) {
		this.insuranceRate = insuranceRate;
	}
	public boolean isNeedInsuranced() {
		return needInsuranced;
	}
	public void setNeedInsuranced(boolean needInsuranced) {
		this.needInsuranced = needInsuranced;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public boolean isCustomRate() {
		return isCustomRate;
	}
	public void setCustomRate(boolean isCustomRate) {
		this.isCustomRate = isCustomRate;
	}
	
	
}
