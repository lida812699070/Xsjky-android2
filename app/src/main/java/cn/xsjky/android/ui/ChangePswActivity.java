package cn.xsjky.android.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.util.CallBackString;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.RetrueCodeHandler;
import cn.xsjky.android.util.RetruenUtils;

public class ChangePswActivity extends BaseActivity implements View.OnClickListener {

    private EditText etOldPsw;
    private EditText etNewPsw1;
    private EditText etNewPsw2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_psw);

        Button btn= (Button) findViewById(R.id.btnChangePsw);
        btn.setOnClickListener(this);
        etOldPsw = (EditText) findViewById(R.id.etOldPsw);
        etNewPsw1 = (EditText) findViewById(R.id.etNewPsw1);
        etNewPsw2 = (EditText) findViewById(R.id.etNewPsw2);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnChangePsw:
                if (TextUtils.isEmpty(etNewPsw1.getText().toString()) ||
                        TextUtils.isEmpty(etOldPsw.getText().toString()) ||
                        TextUtils.isEmpty(etNewPsw2.getText().toString())) {
                    Tos("密码不能为空");
                    return;
                }
                if (!etNewPsw2.getText().toString().equals(etNewPsw1.getText().toString())) {
                    Tos("新密码输入不一致");
                    return;
                }
                RequestParams params = new RequestParams();
                params.addBodyParameter("sessionId", BaseApplication.loginInfo.getSessionId());
                params.addBodyParameter("userId ", BaseApplication.loginInfo.getUserId() + "");
                params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
                params.addBodyParameter("oldPassword", etOldPsw.getText().toString());
                params.addBodyParameter("newPassword", etNewPsw1.getText().toString());
                String url = Urls.ChangePassword+
                        "?sessionId="+BaseApplication.loginInfo.getSessionId()+
                        "&userId="+BaseApplication.loginInfo.getUserId()+
                        "&clientName="+BaseSettings.CLIENT_NAME+
                        "&oldPassword="+etOldPsw.getText().toString()+
                        "&newPassword="+etNewPsw1.getText().toString();
                showProgressDialog();
                BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogU.e(responseInfo.result);
                        closeProgressDialog();
                        RetrueCodeHandler parser = RetruenUtils.getReturnInfo(responseInfo.result, new RetrueCodeHandler());
                        if (parser!=null && parser.getString().equals("0")){
                            success();
                        }else if (parser!=null && parser.getString().equals("-1")){
                            Tos(parser.getError());
                        }
                        closeProgressDialog();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Tos(msg);
                        closeProgressDialog();
                    }
                });
        }
    }

    private void success() {
        Tos("修改成功");
        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("data", etNewPsw1.getText().toString());
        edit.commit();
        finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public void back(View view) {
        finish();
    }
}
