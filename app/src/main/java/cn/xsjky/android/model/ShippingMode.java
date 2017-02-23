package cn.xsjky.android.model;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.util.XmlParserUtil;

/**
 * 输方式运
 * @author Jerry
 *
 */
public class ShippingMode extends BaseModel{
	private int ModeId;
	private String ModeName;
	private boolean IsEnabled;
	
	public static List<ShippingMode> parserXmlList(String xml){
		try {
			List<ShippingMode> list = new ArrayList<ShippingMode>();
			parser = XmlParserUtil.getInstance();
			parser.parse(xml);
			resultPrefix = "/soap:Envelope/soap:Body/GetShippingModesResponse/GetShippingModesResult/ReturnList/";
			int count = getXmlCount("ShippingMode");
			for(int i = 0; i < count; i++){
				ShippingMode sm = new ShippingMode();
				String prefix = "ShippingMode#" + i + "/";
				sm.setModeId(getXmlValueInt(prefix + "ModeId"));
				sm.setModeName(getXmlValueStr(prefix + "ModeName"));
				sm.setIsEnabled(getXmlValueBool(prefix + "IsEnabled"));
				list.add(sm);
			}
			return list;
		} catch (Exception e) {
			return null;
		}
	}

	public int getModeId() {
		return ModeId;
	}

	public void setModeId(int modeId) {
		ModeId = modeId;
	}
	public String getModeName() {
		return ModeName;
	}
	public void setModeName(String modeName) {
		ModeName = modeName;
	}
	public boolean isIsEnabled() {
		return IsEnabled;
	}
	public void setIsEnabled(boolean isEnabled) {
		IsEnabled = isEnabled;
	}
}
