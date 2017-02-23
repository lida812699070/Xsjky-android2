package cn.xsjky.android.model;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.util.StrKit;
import cn.xsjky.android.util.XmlParserUtil;

public class HandOverRecord extends BaseModel{
	private String RecordId;
	// / 移交时间
	private String ActionTime;
	// / 移交给网点
	private int ToNetwork;
	// / 移交给员工
	private int ToEmployee;
	// / 移交给货车/航班
	private int ToShippingTools;
	// / 移交清单
	private List<HandOverItem> Items;
	// / 卸货员工记录（转出时有效)
	private List<HandOverActor> DownloadActors;
	// /装货员工记录，（接收时有效）
	private List<HandOverActor> UploadActors;
	// / 操作员
	private int UserId;
	// / 备注
	private String Remarks;
	// / 合计件数
	private int TotalQuantity;
	// / 合计重量
	private int TotalWeight;
	// / 合计体积
	private double TotalVolumn;
	// / 已处理
	private boolean IsProcessed;
	
	public static HandOverRecord parserXml(String xml, String prefix){
		if(StrKit.isBlank(xml))
    		return null;
		parser = XmlParserUtil.getInstance();
		parser.parse(xml);
		resultPrefix = prefix;
		HandOverRecord h = new HandOverRecord();
		parserHandOverRecord(h);
		return h;
	}
	
	private static void parserHandOverRecord(HandOverRecord h){
		h.setRecordId(getXmlValueStr(""));
		h.setActionTime(getXmlValueStr(""));
		h.setToNetwork(getXmlValueInt(""));
		h.setToEmployee(getXmlValueInt(""));
		int icount = getXmlCount("Items/HandOverItem");
		if(icount > 0){
			List<HandOverItem> list = new ArrayList<HandOverItem>();
			for(int i = 0; i < icount; i++){
				HandOverItem hi = new HandOverItem();
				String prefix = "Items/HandOverItem#" + i + "/";
				hi.setHandOverRecord(getXmlValueStr(prefix + "HandOverRecord"));
				hi.setDocumentId(getXmlValueInt(prefix + "DocumentId"));
				hi.setIssueRemarks(getXmlValueStr(prefix + "IssueRemarks"));
				hi.setQuantity(getXmlValueInt(prefix + "Quantity"));
				hi.setReceiveRemarks(getXmlValueStr(prefix + "ReceiveRemarks"));
				hi.setRecordId(getXmlValueStr(prefix + "RecordId"));
				hi.setVolumn(getXmlValueDouble(prefix + "Volumn"));
				hi.setWeight(getXmlValueDouble(prefix + "Weight"));
				list.add(hi);
			}
			h.setItems(list);
		}
		
		int dcount = getXmlCount("DownloadActors/HandOverActor");
		if(dcount > 0){
			List<HandOverActor> list = new ArrayList<HandOverActor>();
			for(int i = 0; i < dcount; i++){
				HandOverActor hi = new HandOverActor();
				String prefix = "DownloadActors/HandOverActor#" + i + "/";
				hi.setHandOverRecord(getXmlValueStr(prefix + "HandOverRecord"));
				//#未知
				//hi.setActor(actor)
				hi.setRecordId(getXmlValueInt(prefix + "RecordId"));
				list.add(hi);
			}
			h.setDownloadActors(list);
		}
		
		int ucount = getXmlCount("UploadActors/HandOverActor");
		if(ucount > 0){
			List<HandOverActor> list = new ArrayList<HandOverActor>();
			for(int i = 0; i < ucount; i++){
				HandOverActor hi = new HandOverActor();
				String prefix = "UploadActors/HandOverActor#" + i + "/";
				hi.setHandOverRecord(getXmlValueStr(prefix + "HandOverRecord"));
				//#未知
				//hi.setActor(actor)
				hi.setRecordId(getXmlValueInt(prefix + "RecordId"));
				list.add(hi);
			}
			h.setUploadActors(list);
		}
		h.setUserId(getXmlValueInt("UserId"));
		h.setRemarks(getXmlValueStr("Remarks"));
		h.setTotalQuantity(getXmlValueInt("TotalQuantity"));
		h.setTotalWeight(getXmlValueInt("TotalWeight"));
		h.setTotalVolumn(getXmlValueDouble("TotalVolumn"));
		h.setIsProcessed(getXmlValueBool("IsProcessed"));
	}
	
	public String getRecordId() {
		return RecordId;
	}
	public void setRecordId(String recordId) {
		RecordId = recordId;
	}
	public String getActionTime() {
		return ActionTime;
	}
	public void setActionTime(String actionTime) {
		ActionTime = actionTime;
	}
	public int getToNetwork() {
		return ToNetwork;
	}
	public void setToNetwork(int toNetwork) {
		ToNetwork = toNetwork;
	}
	public int getToEmployee() {
		return ToEmployee;
	}
	public void setToEmployee(int toEmployee) {
		ToEmployee = toEmployee;
	}
	public int getToShippingTools() {
		return ToShippingTools;
	}
	public void setToShippingTools(int toShippingTools) {
		ToShippingTools = toShippingTools;
	}
	public List<HandOverItem> getItems() {
		return Items;
	}
	public void setItems(List<HandOverItem> items) {
		Items = items;
	}
	public List<HandOverActor> getDownloadActors() {
		return DownloadActors;
	}
	public void setDownloadActors(List<HandOverActor> downloadActors) {
		DownloadActors = downloadActors;
	}
	public List<HandOverActor> getUploadActors() {
		return UploadActors;
	}
	public void setUploadActors(List<HandOverActor> uploadActors) {
		UploadActors = uploadActors;
	}
	public int getUserId() {
		return UserId;
	}
	public void setUserId(int userId) {
		UserId = userId;
	}
	public String getRemarks() {
		return Remarks;
	}
	public void setRemarks(String remarks) {
		Remarks = remarks;
	}
	public int getTotalQuantity() {
		return TotalQuantity;
	}
	public void setTotalQuantity(int totalQuantity) {
		TotalQuantity = totalQuantity;
	}
	public int getTotalWeight() {
		return TotalWeight;
	}
	public void setTotalWeight(int totalWeight) {
		TotalWeight = totalWeight;
	}
	public double getTotalVolumn() {
		return TotalVolumn;
	}
	public void setTotalVolumn(double totalVolumn) {
		TotalVolumn = totalVolumn;
	}
	public boolean isIsProcessed() {
		return IsProcessed;
	}
	public void setIsProcessed(boolean isProcessed) {
		IsProcessed = isProcessed;
	}
}
