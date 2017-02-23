package cn.xsjky.android.model;
//交接记录明细
public class HandOverItem {
	private String HandOverRecord;
	private String RecordId;
	private int DocumentId;
	private int Quantity;
	private double Weight;
	private double Volumn;
	private String IssueRemarks;
	private String ReceiveRemarks;
	public String getHandOverRecord() {
		return HandOverRecord;
	}
	public void setHandOverRecord(String handOverRecord) {
		HandOverRecord = handOverRecord;
	}
	public String getRecordId() {
		return RecordId;
	}
	public void setRecordId(String recordId) {
		RecordId = recordId;
	}
	public int getDocumentId() {
		return DocumentId;
	}
	public void setDocumentId(int documentId) {
		DocumentId = documentId;
	}
	public int getQuantity() {
		return Quantity;
	}
	public void setQuantity(int quantity) {
		Quantity = quantity;
	}
	public double getWeight() {
		return Weight;
	}
	public void setWeight(double weight) {
		Weight = weight;
	}
	public double getVolumn() {
		return Volumn;
	}
	public void setVolumn(double volumn) {
		Volumn = volumn;
	}
	public String getIssueRemarks() {
		return IssueRemarks;
	}
	public void setIssueRemarks(String issueRemarks) {
		IssueRemarks = issueRemarks;
	}
	public String getReceiveRemarks() {
		return ReceiveRemarks;
	}
	public void setReceiveRemarks(String receiveRemarks) {
		ReceiveRemarks = receiveRemarks;
	}
}
