package cn.xsjky.android.model;

import java.util.HashMap;
import java.util.Map;

import cn.xsjky.android.util.TempletUtil;

/**
 * 运费项目
 * @author Jerry
 *
 */
public class ShipperCost {
	
	public String toTemplet(){
		final String TEMPLET = "<ShipperCost>"
                +"<RecordId>${recordId}</RecordId>"
                +"<DocumentId>${documentId}</DocumentId>"
                +"<FreightItem>"
                    +"<ItemId>${itemId}</ItemId>"
                    +"<ItemName>${itemName}</ItemName>"
                    +"<IsCalculate>${isCalculate}</IsCalculate>"
                    +"<AllowEdit>${allowEdit}</AllowEdit>"
                    +"<IsEnabled>${isEnabled}</IsEnabled>"
                +"</FreightItem>"
                +"<ChargeValue>${chargeValue}</ChargeValue>"
            +"</ShipperCost>";
		Map<String, String> map = new HashMap<String, String>();
		map.put("recordId", String.valueOf(RecordId));
		map.put("documentId", String.valueOf(DocumentId));
		map.put("itemId", String.valueOf(FreightItem.getItemId()));
		map.put("itemName", FreightItem.getItemName());
		map.put("isCalculate", String.valueOf(FreightItem.isIsCalculate()));
		map.put("allowEdit", String.valueOf(FreightItem.isAllowEdit()));
		map.put("isEnabled", String.valueOf(FreightItem.isIsEnabled()));
		map.put("chargeValue", String.valueOf(ChargeValue));
		return TempletUtil.render(TEMPLET, map);
	}
	
	private int RecordId;
	private int DocumentId;
	private double ChargeValue;
	private FreightItem FreightItem;
	public int getRecordId() {
		return RecordId;
	}
	public void setRecordId(int recordId) {
		RecordId = recordId;
	}
	public int getDocumentId() {
		return DocumentId;
	}
	public void setDocumentId(int documentId) {
		DocumentId = documentId;
	}
	public double getChargeValue() {
		return ChargeValue;
	}
	public void setChargeValue(double chargeValue) {
		ChargeValue = chargeValue;
	}
	public FreightItem getFreightItem() {
		return FreightItem;
	}
	public void setFreightItem(FreightItem freightItem) {
		FreightItem = freightItem;
	}
}
