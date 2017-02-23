package cn.xsjky.android.model;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.util.StrKit;
import cn.xsjky.android.util.XmlParserUtil;

public class Contact extends BaseModel{
	//单位名称
    public String CompanyName;
    //电话区号
    public String AreaCode;
    //电话号码
    public String Tel;
    //姓名
    public String ContactPerson;
    //地址
    public Address address;
    
    public static List<Contact> parserXmlList(String xml){
    	if(StrKit.isBlank(xml))
    		return null;
    	List<Contact> list = new ArrayList<Contact>();
    	parser = XmlParserUtil.getInstance();
		parser.parse(xml);
		String prefix = "/soap:Envelope/soap:Body/FindContactsResponse/FindContactsResult/ReturnList/"; 
		resultPrefix = prefix;
		int count = getXmlCount("ContactInfo");
		for(int i = 0; i < count; i++){
			resultPrefix = prefix + "ContactInfo#" + i + "/";
			Contact c = new Contact();
			c.setCompanyName(getXmlValueStr("CompanyName"));
			c.setAreaCode(getXmlValueStr("AreaCode"));
			c.setTel(getXmlValueStr("Tel"));
			c.setContactPerson(getXmlValueStr("ContactPerson"));
			String addressPrefix = "Address/";
			Address a = new Address();
			a.setAddressId(getXmlValueInt(addressPrefix + "AddressId"));
			a.setProvince(getXmlValueStr(addressPrefix + "Province"));
			a.setCity(getXmlValueStr(addressPrefix + "City"));
			a.setDistrict(getXmlValueStr(addressPrefix + "District"));
			a.setAddress(getXmlValueStr(addressPrefix + "Address"));
			a.setPostCode(getXmlValueStr(addressPrefix + "PostCode"));
			c.setAddress(a);
			list.add(c);
		}
		
		return list;
    }
    
	public String getCompanyName() {
		return CompanyName;
	}
	public void setCompanyName(String companyName) {
		CompanyName = companyName;
	}
	public String getAreaCode() {
		return AreaCode;
	}
	public void setAreaCode(String areaCode) {
		AreaCode = areaCode;
	}
	public String getTel() {
		return Tel;
	}
	public void setTel(String tel) {
		Tel = tel;
	}
	public String getContactPerson() {
		return ContactPerson;
	}
	public void setContactPerson(String contactPerson) {
		ContactPerson = contactPerson;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
}
