package cn.xsjky.android.model;

//搬运工记录
public class HandOverActor {
    private int RecordId;
    private Employee Actor;
    private String HandOverRecord;
	public int getRecordId() {
		return RecordId;
	}
	public void setRecordId(int recordId) {
		RecordId = recordId;
	}
	public Employee getActor() {
		return Actor;
	}
	public void setActor(Employee actor) {
		Actor = actor;
	}
	public String getHandOverRecord() {
		return HandOverRecord;
	}
	public void setHandOverRecord(String handOverRecord) {
		HandOverRecord = handOverRecord;
	}
}
