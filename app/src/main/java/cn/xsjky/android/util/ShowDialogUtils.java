package cn.xsjky.android.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by ${lida} on 2016/8/19.
 */
public class ShowDialogUtils {

    public static void showDialog(final DialoginOkCallBack callBack, String message,Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);  //先得到构造器
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
}
