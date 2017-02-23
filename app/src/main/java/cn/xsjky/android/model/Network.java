package cn.xsjky.android.model;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.util.XmlParserUtil;

/**
 * 网点
 * @author Jerry
 *
 */
public class Network extends BaseModel{
	private int NetworkId;
	private String NetworkCode;
	private String NetworkName;
	private int ParentId;
	private String Remarks;
	private int ManagerId;
	private boolean IsDisabled;
	private String Tel;
	private String Fax;
	private String PinYinCode;
	private String WuBiCode;
	/*
	private String BASE_TEMPLET = "<Network>"
            +"<NetworkId>${NetworkId}</NetworkId>"
            +"<NetworkCode>${NetworkCode}</NetworkCode>"
            +"<NetworkName>${NetworkName}</NetworkName>"
            +"<ParentId>${ParentId}</ParentId>"
            +"<Remarks>${Remarks}</Remarks>"
            +"<ManagerId>${ManagerId}</ManagerId>"
            +"<IsDisabled>${IsDisabled}</IsDisabled>"
            +"<Tel>${Tel}</Tel>"
            +"<Fax>${Fax}</Fax>"
            +"<PinYinCode>${PinYinCode}</PinYinCode>"
            +"<WuBiCode>${WuBiCode}</WuBiCode>"
          +"</Network>";
	*/
	public String toBaseTemplet(){
		return "";
	}
	
	public static List<Network> parserXmlList(String xml){
		List<Network> list = new ArrayList<Network>();
		parser = XmlParserUtil.getInstance();
		parser.parse(xml);
		resultPrefix = "/soap:Envelope/soap:Body/GetNetworksResponse/GetNetworksResult/ReturnList/";
		int count = getXmlCount("Network");
		for(int i = 0; i < count; i++){
			Network n = new Network();
			String prefix = "Network#" + i + "/";
			n.setNetworkId(getXmlValueInt(prefix + "NetworkId"));
			n.setNetworkCode(getXmlValueStr(prefix + "NetworkCode"));
			n.setNetworkName(getXmlValueStr(prefix + "NetworkName"));
			n.setParentId(getXmlValueInt(prefix + "ParentId"));
			n.setRemarks(getXmlValueStr(prefix + "Remarks"));
			n.setManagerId(getXmlValueInt(prefix + "ManagerId"));
			n.setIsDisabled(getXmlValueBool(prefix + "IsDisabled"));
			n.setTel(getXmlValueStr(prefix + "Tel"));
			n.setFax(getXmlValueStr(prefix + "Fax"));
			n.setPinYinCode(getXmlValueStr(prefix + "PinYinCode"));
			n.setWuBiCode(getXmlValueStr(prefix + "WuBiCode"));
			list.add(n);
		}
		return list;
	}

	public int getNetworkId() {
		return NetworkId;
	}

	public void setNetworkId(int networkId) {
		NetworkId = networkId;
	}

	public String getNetworkCode() {
		return NetworkCode;
	}

	public void setNetworkCode(String networkCode) {
		NetworkCode = networkCode;
	}

	public String getNetworkName() {
		return NetworkName;
	}

	public void setNetworkName(String networkName) {
		NetworkName = networkName;
	}

	public int getParentId() {
		return ParentId;
	}

	public void setParentId(int parentId) {
		ParentId = parentId;
	}

	public String getRemarks() {
		return Remarks;
	}

	public void setRemarks(String remarks) {
		Remarks = remarks;
	}

	public int getManagerId() {
		return ManagerId;
	}

	public void setManagerId(int managerId) {
		ManagerId = managerId;
	}

	public boolean isIsDisabled() {
		return IsDisabled;
	}

	public void setIsDisabled(boolean isDisabled) {
		IsDisabled = isDisabled;
	}

	public String getTel() {
		return Tel;
	}

	public void setTel(String tel) {
		Tel = tel;
	}

	public String getFax() {
		return Fax;
	}

	public void setFax(String fax) {
		Fax = fax;
	}

	public String getPinYinCode() {
		return PinYinCode;
	}

	public void setPinYinCode(String pinYinCode) {
		PinYinCode = pinYinCode;
	}

	public String getWuBiCode() {
		return WuBiCode;
	}

	public void setWuBiCode(String wuBiCode) {
		WuBiCode = wuBiCode;
	}
	
}
