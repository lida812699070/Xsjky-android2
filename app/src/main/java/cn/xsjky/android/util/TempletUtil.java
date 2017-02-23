package cn.xsjky.android.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 模板工具类
 * @author Jerry
 *
 */
public class TempletUtil {
	private static final String TEMPLET_PREFIX = "${";
	private static final String TEMPLET_SUFFIX = "}";
     
    /**
     * 使用context中对应的值替换templet中用split包围的变量名(也是context的key)
     * @param templet 模板
     * @param split 用于标识变量名的标志
     * @param context 用于替换模板中的变量
     * @return 例如  参数 : dddd${aaa}${bbb}ccc$$, $$, {<aaa, value1>, <bbb, value2>}  结果:ddddvalue1value2ccc$$
     */
    public static String render(String templet, Map<String, String> context) {
        Set<String> paramNames = getParamNames(templet);
        if(paramNames == null)
        	return "";
        for (String name : paramNames) {
            String value = context.get(name);
            value = value == null ? "" : value;
            String regex = "\\Q" + TEMPLET_PREFIX + name + TEMPLET_SUFFIX + "\\E";
            templet = templet.replaceAll(regex, value);
        }
 
        return templet;
    }
     
    /**
     * 根据分割符从模板中取得变量的名字(${变量名}) eg:
     * ${aaa}${bbb}ccc$$ 返回   aaa,bbb
     * @param templet 模板
     * @param split 包围变量名的字符串
     * @return 模板中的变量名
     */
    private static Set<String> getParamNames(String templet) {
        Set<String> paramNames = new HashSet<String>();
         
        int start = 0, end = 0;
        while (end < templet.length()) {
            start = templet.indexOf(TEMPLET_PREFIX, end);
            if (start == -1) {
                break;
            }
            start = start + TEMPLET_PREFIX.length();
            end = templet.indexOf(TEMPLET_SUFFIX, start);
            if (end == -1) {
                break;
            }
            paramNames.add(templet.substring(start, end).trim());
            end = end + TEMPLET_SUFFIX.length();
        }
        return paramNames;
    }
}
