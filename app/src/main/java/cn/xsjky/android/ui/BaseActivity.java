package cn.xsjky.android.ui;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.R;
import cn.xsjky.android.util.CallBack;
import cn.xsjky.android.util.CallBackString;
import cn.xsjky.android.util.DialoginOkCallBack;
import cn.xsjky.android.util.RetrueCodeHandler;
import cn.xsjky.android.util.RetruenUtils;
import cn.xsjky.android.weiget.LoadingDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.Timer;
import java.util.TimerTask;

public class BaseActivity extends Activity {
    private ProgressDialog progressDialog;
    private ImageView mFlip;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.activityList.add(this);
    }

    protected void showError(String info) {
        Message msg = new Message();
        msg.what = SHOW_INFO;
        msg.obj = info;
        handler.sendMessage(msg);
    }

    public void flash(){

    }

    public void Tos(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showProgressDialog() {
        if (isFinishing()) {
            return;
        }
        if ((!isFinishing()) && (this.progressDialog == null)) {
            this.progressDialog = new ProgressDialog(this);
        }
        this.progressDialog.setTitle("正在更新数据");
        this.progressDialog.setMessage("请稍后");
        this.progressDialog.setCanceledOnTouchOutside(false);
        this.progressDialog.show();

    }

    public void closeProgressDialog() {
        if (this.progressDialog != null)
            this.progressDialog.dismiss();
    }

    protected void setHandler(int code, Object obj) {
        Message msg = new Message();
        msg.what = code;
        msg.obj = obj;
        handler.sendMessage(msg);
    }

    protected final int SHOW_LOADING = 1;
    protected final int HIDE_LOADING = 2;
    protected final int SHOW_INFO = 3;
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_LOADING:
                    LoadingDialog.show(BaseActivity.this, false, false);
                    break;
                case HIDE_LOADING:
                    LoadingDialog.dismiss();
                    break;
                case SHOW_INFO:
                    Toast.makeText(BaseActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private Context context;

    protected void initToolbar(String title) {
        mFlip = (ImageView) this.findViewById(R.id.head_flip);
        mFlip.setVisibility(View.VISIBLE);
        mFlip.setImageResource(R.drawable.ic_flipper_head_back);
        mFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitle = (TextView) this.findViewById(R.id.head_title);
        mTitle.setText(title);
    }

    public void getData(String url, RequestParams params, final CallBackString callBack) {
        context = this;
        showProgressDialog();
        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST,
                url,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        RetrueCodeHandler parser = RetruenUtils.getReturnInfo(responseInfo.result, new RetrueCodeHandler());
                        if (parser != null && parser.getString().equals("0")) {
                            callBack.httFinsh(responseInfo.result);
                        } else if (parser != null && parser.getString().equals("-1")) {
                            Toast.makeText(context, parser.getError(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "数据获取错误", Toast.LENGTH_SHORT).show();
                        }
                        closeProgressDialog();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        closeProgressDialog();
                    }
                });
    }

    public void getData(String url, RequestParams params, final CallBackString callBack,boolean progressDissmiss) {
        context = this;
        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST,
                url,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        RetrueCodeHandler parser = RetruenUtils.getReturnInfo(responseInfo.result, new RetrueCodeHandler());
                        if (parser != null && parser.getString().equals("0")) {
                            callBack.httFinsh(responseInfo.result);
                        } else if (parser != null && parser.getString().equals("-1")) {
                            Toast.makeText(context, parser.getError(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "数据获取错误", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getData(String url, RequestParams params, final CallBackString callBack, final CallBackString errcallback) {
        context = this;
        showProgressDialog();
        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST,
                url,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        RetrueCodeHandler parser = RetruenUtils.getReturnInfo(responseInfo.result, new RetrueCodeHandler());
                        if (parser != null && parser.getString().equals("0")) {
                            callBack.httFinsh(responseInfo.result);
                        } else if (parser != null && parser.getString().equals("-1")) {
                            Toast.makeText(context, parser.getError(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "数据获取错误", Toast.LENGTH_SHORT).show();
                        }
                        closeProgressDialog();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                        errcallback.httFinsh(msg);
                        closeProgressDialog();
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        BaseApplication.activityList.remove(this);
        super.onDestroy();
    }
   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            //exitBy2Click(); //调用双击退出函数
        }
        return false;
    }*/

    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

    public void showDialog(final DialoginOkCallBack callBack, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage(message + ""); //设置内容
        //builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss(); //关闭dialog
                //Toast.makeText(BaseMapActivity.this, "确认" + which, Toast.LENGTH_SHORT).show();
                callBack.onClickOk(dialog, which);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //Toast.makeText(BaseMapActivity.this, "取消" + which, Toast.LENGTH_SHORT).show();
            }
        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }

    protected void showDialog(final DialoginOkCallBack callBack, String message, final DialoginOkCallBack cancelcallbak) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage(message + ""); //设置内容
        //builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss(); //关闭dialog
                //Toast.makeText(BaseMapActivity.this, "确认" + which, Toast.LENGTH_SHORT).show();
                callBack.onClickOk(dialog, which);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (cancelcallbak==null){
                    return;
                }
                cancelcallbak.onClickOk(dialog, which);
                //Toast.makeText(BaseMapActivity.this, "取消" + which, Toast.LENGTH_SHORT).show();
            }
        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }

}
