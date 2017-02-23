package cn.xsjky.android.model;

/**
 * 员工实体类
 * @author Jerry
 *
 */
public class Employee {
    private int EmplId;
    private String EmplCode;
    private String EmplName;
    private boolean IsLeft;
    private int NetworkId;
    private String MobileNumber;
    private String WebChat;
    private String QQ;
    private String EMail;
    private boolean IsDriver;
    private boolean IsBusiness;
    private boolean IsDeliver;
    private boolean IsReceiver;
    private String TruckNumber;
    private int BindTools;
    
    
	public String getTruckNumber() {
		return TruckNumber;
	}
	public void setTruckNumber(String truckNumber) {
		TruckNumber = truckNumber;
	}
	public int getEmplId() {
		return EmplId;
	}
	public void setEmplId(int emplId) {
		EmplId = emplId;
	}
	public String getEmplCode() {
		return EmplCode;
	}
	public void setEmplCode(String emplCode) {
		EmplCode = emplCode;
	}
	public String getEmplName() {
		return EmplName;
	}
	public void setEmplName(String emplName) {
		EmplName = emplName;
	}
	public boolean isIsLeft() {
		return IsLeft;
	}
	public void setIsLeft(boolean isLeft) {
		IsLeft = isLeft;
	}
	public int getNetworkId() {
		return NetworkId;
	}
	public void setNetworkId(int networkId) {
		NetworkId = networkId;
	}
	public String getMobileNumber() {
		return MobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		MobileNumber = mobileNumber;
	}
	public String getWebChat() {
		return WebChat;
	}
	public void setWebChat(String webChat) {
		WebChat = webChat;
	}
	public String getQQ() {
		return QQ;
	}
	public void setQQ(String qQ) {
		QQ = qQ;
	}
	public String getEMail() {
		return EMail;
	}
	public void setEMail(String eMail) {
		EMail = eMail;
	}
	public boolean isIsDriver() {
		return IsDriver;
	}
	public void setIsDriver(boolean isDriver) {
		IsDriver = isDriver;
	}
	public boolean isIsBusiness() {
		return IsBusiness;
	}
	public void setIsBusiness(boolean isBusiness) {
		IsBusiness = isBusiness;
	}
	public boolean isIsDeliver() {
		return IsDeliver;
	}
	public void setIsDeliver(boolean isDeliver) {
		IsDeliver = isDeliver;
	}
	public boolean isIsReceiver() {
		return IsReceiver;
	}
	public void setIsReceiver(boolean isReceiver) {
		IsReceiver = isReceiver;
	}
	public int getBindTools() {
		return BindTools;
	}
	public void setBindTools(int bindTools) {
		BindTools = bindTools;
	}
}
