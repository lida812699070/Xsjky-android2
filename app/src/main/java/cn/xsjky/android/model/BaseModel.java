package cn.xsjky.android.model;

import cn.xsjky.android.util.XmlParserUtil;

public class BaseModel {
	protected static String resultPrefix;
	protected static XmlParserUtil parser;
	
	protected static String getXmlValueStr(String path){
    	if(parser == null)
    		return "";
    	return parser.getNodeValue(resultPrefix + path);
    }
    
	protected static int getXmlValueInt(String path){
    	String str = getXmlValueStr(path);
    	try {
			int i = Integer.valueOf(str);
			return i;
		} catch (NumberFormatException e) {
			return 0;
		}
    }
	
	protected static Double getXmlValueDouble(String path){
    	String str = getXmlValueStr(path);
    	try {
    		Double i = Double.valueOf(str);
			return i;
		} catch (NumberFormatException e) {
			return 0.0;
		}
    }
    
	protected static boolean getXmlValueBool(String path){
    	String str = getXmlValueStr(path);
    	try {
			boolean i = Boolean.valueOf(str);
			return i;
		} catch (NumberFormatException e) {
			return false;
		}
    }
	
	protected static int getXmlCount(String path){
		return parser.getNodeCount(resultPrefix + path);
	}
}
