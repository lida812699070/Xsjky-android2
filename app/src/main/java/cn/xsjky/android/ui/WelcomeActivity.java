package cn.xsjky.android.ui;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.R;
import cn.xsjky.android.model.Login;
import cn.xsjky.android.util.StrKit;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_welcome);
		init();
	}
	
	private void init(){
		SharedPreferences sp = BaseApplication.getApplication().getSharedPreferences();
		int userId = sp.getInt("userId", -1);
		String sessionId = sp.getString("sessionId", "");
		//if(userId < 0 || StrKit.isBlank(sessionId)){
			Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
			this.startActivity(intent);
			this.finish();
		/*}else{
			Login login = new Login();
			login.setUserId(userId);
			login.setSessionId(sessionId);
			BaseApplication.getApplication().setLogin(login);
			Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
			this.startActivity(intent);
		}*/
	}
}
