package cn.xsjky.android.weiget;


import cn.xsjky.android.R;
import cn.xsjky.android.util.StrKit;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoadingDialog {
	private static Dialog loadingDialog;
	/**
	 * 显示一个等待框
	 * 
	 * @param context上下文环境
	 * @param isCancel是否能用返回取消
	 * @param isRighttrue文字在右边false在下面
	 */
	public static void show(Context context, boolean isCancel, boolean isRight) {
		creatDialog(context, "", isCancel, isRight);
	}
	
	public static void dismiss(){
		if(loadingDialog != null && loadingDialog.isShowing())
			loadingDialog.dismiss();
	}

	/**
	 * 显示一个等待框
	 * 
	 * @param context上下文环境
	 * @param msg等待框的文字
	 * @param isCancel是否能用返回取消
	 * @param isRighttrue文字在右边false在下面
	 */
	public static void show(Context context, String msg, boolean isCancel,
			boolean isRight) {
		creatDialog(context, msg, isCancel, isRight);
	}

	private static void creatDialog(Context context, String msg,
			boolean isCancel, boolean isRight) {
		if(loadingDialog != null && loadingDialog.isShowing())
			return;
		LinearLayout.LayoutParams wrap_content = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams wrap_content0 = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		LinearLayout main = new LinearLayout(context);
		main.setBackgroundColor(0x00000000);
		if (isRight) {
			main.setOrientation(LinearLayout.HORIZONTAL);
			wrap_content.setMargins(10, 0, 35, 0);
			wrap_content0.setMargins(35, 25, 0, 25);
		} else {
			main.setOrientation(LinearLayout.VERTICAL);
			wrap_content.setMargins(10, 5, 10, 15);
			wrap_content0.setMargins(35, 25, 35, 0);
		}
		main.setGravity(Gravity.CENTER);
		//main.setAlpha(20);
		ImageView spaceshipImage = new ImageView(context);
		spaceshipImage.setImageResource(R.drawable.ic_loading);
		TextView tipTextView = new TextView(context);
		tipTextView.setTextSize(20);
		tipTextView.setText("正在加载数据...");
		// 加载旋转动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading_animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		if (StrKit.notBlank(msg))
			tipTextView.setText(msg);// 设置加载信息,否则加载默认值
		loadingDialog = new Dialog(context, R.style.LoadDialogStyle);// 创建自定义样式dialog
		loadingDialog.setCancelable(isCancel);// 是否可以用返回键取消
		main.addView(spaceshipImage, wrap_content0);
		main.addView(tipTextView, wrap_content);
		LinearLayout.LayoutParams fill_parent = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		loadingDialog.setContentView(main, fill_parent);// 设置布局
		loadingDialog.show();
	}
}