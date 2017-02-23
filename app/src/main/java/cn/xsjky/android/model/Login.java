package cn.xsjky.android.model;

import java.util.HashMap;
import java.util.Map;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.util.TempletUtil;

/**
 * 登陆
 * @author Jerry
 *
 */
public class Login {
	/**
	 * 返回登录模板
	 * @return
	 */
	public String toLoginTemplet(){
		Map<String, String> context = new HashMap<String, String>();
		context.put("loginName", loginName);
		context.put("password", password);
		context.put("clientName", clientName);
		context.put("clientVersion", clientVersion);
		context.put("DeviceId", DeviceId);
		return TempletUtil.render(BaseSettings.LOGIN_TEMPLET, context);
	}
	
	public void save(){
		SharedPreferences sp = BaseApplication.getApplication().getSharedPreferences();
		Editor e = sp.edit();
		e.putString("loginName", loginName);
		e.putString("data", password);
		e.putInt("userId", userId);
		e.putString("sessionId", sessionId);
		e.putString("DeviceId", DeviceId);
		e.commit();
	}
	
	private int userId;
	private String loginName;
	private String password;
	private String clientName = "XinShiJi.Mobile.Android";
	private String sessionId;
	private String clientVersion = "1.0";
	private String DeviceId ;

	public String getDeviceId() {
		return DeviceId;
	}

	public void setDeviceId(String deviceId) {
		DeviceId = deviceId;
	}

	public String getClientVersion() {
		return clientVersion;
	}
	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}
