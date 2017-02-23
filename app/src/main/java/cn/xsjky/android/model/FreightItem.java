package cn.xsjky.android.model;

public class FreightItem {
	private int ItemId;
	private String ItemName;
	private boolean IsCalculate;
	private boolean AllowEdit;
	private boolean IsEnabled;
	public int getItemId() {
		return ItemId;
	}
	public void setItemId(int itemId) {
		ItemId = itemId;
	}
	public String getItemName() {
		return ItemName;
	}
	public void setItemName(String itemName) {
		ItemName = itemName;
	}
	public boolean isIsCalculate() {
		return IsCalculate;
	}
	public void setIsCalculate(boolean isCalculate) {
		IsCalculate = isCalculate;
	}
	public boolean isAllowEdit() {
		return AllowEdit;
	}
	public void setAllowEdit(boolean allowEdit) {
		AllowEdit = allowEdit;
	}
	public boolean isIsEnabled() {
		return IsEnabled;
	}
	public void setIsEnabled(boolean isEnabled) {
		IsEnabled = isEnabled;
	}
	
	
}
