package cn.xsjky.android.http;

/**
 * HTTP回调
 * @author Jerry
 *
 */
public interface HttpCallback {
	public void onHttpStart();					//开始
	public void onHttpFinish(String data);		//完成
	public void onHttpError(String msg);		//错误
	public void onHttpEnd();					//结束
}
