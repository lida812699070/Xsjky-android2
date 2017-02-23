package cn.xsjky.android.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.SyncBasicHttpContext;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.util.StrKit;

/**
 * 网络请求
 *
 * @author Jerry
 */
public class HttpPro {
    public static final int DEFAULT_SOCKET_BUFFER_SIZE = 500 * 1024; //8KB
    private int maxConnections = 10; //http请求最大并发连接数
    private int socketTimeout = 10 * 1000; //超时时间，默认10秒
    private int maxRetries = 5;//错误尝试次数，错误异常表请在RetryHandler添加

    private final DefaultHttpClient httpClient;

    public HttpPro() {
        BasicHttpParams httpParams = new BasicHttpParams();

        ConnManagerParams.setTimeout(httpParams, socketTimeout);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(maxConnections));
        ConnManagerParams.setMaxTotalConnections(httpParams, maxConnections);

        HttpConnectionParams.setSoTimeout(httpParams, socketTimeout);
        HttpConnectionParams.setConnectionTimeout(httpParams, socketTimeout);
        HttpConnectionParams.setTcpNoDelay(httpParams, true);
        HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);

        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);

        httpClient = new DefaultHttpClient(cm, httpParams);
    }

    public void postAsync(String requestEntity, String action, HttpCallback callback) {
        if (StrKit.isBlank(requestEntity) || StrKit.isBlank(action)) {
            if (callback != null)
                callback.onHttpError("参数错误");
            return;
        }
        try {
            HttpPost httpPost = new HttpPost(BaseSettings.WEBSERVICE_URL);
            StringEntity se = new StringEntity(requestEntity, HTTP.UTF_8);
            se.setContentType("text/xml;charset=utf-8");
            httpPost.addHeader("SOAPAction", action);
            httpPost.setEntity(se);
            sendRequest(httpPost, callback);
        } catch (Exception e) {
            if (callback != null)
                callback.onHttpError("请求错误，请重试!");
        }
    }

    private void sendRequest(final HttpPost httpPost, final HttpCallback callback) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                if (callback != null)
                    callback.onHttpStart();
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    HttpEntity resEntity = httpResponse.getEntity();
                    String response = EntityUtils.toString(resEntity);
                    //byte[] b = EntityUtils.toByteArray(resEntity);
                    //Log.e("response", "response="+response);
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    if (callback != null)
                        callback.onHttpError("请求错误，请重试!");
                    return "";
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if (callback != null)
                    callback.onHttpFinish(result);
            }
        }.execute();
    }

    private void urlSendRequest(final String requestEntity, final String action, final HttpCallback callback) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                if (callback != null)
                    callback.onHttpStart();
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    try {

                        // 请求的地址
                        String spec = BaseSettings.WEBSERVICE_URL;
                        // 根据地址创建URL对象
                        URL url = new URL(spec);
                        // 根据URL对象打开链接
                        HttpURLConnection urlConnection = (HttpURLConnection) url
                                .openConnection();
                        // 设置请求的方式
                        urlConnection.setRequestMethod("POST");
                        // 设置请求的超时时间
                        urlConnection.setReadTimeout(60000);
                        urlConnection.setConnectTimeout(60000);
                        // 传递的数据
                        /*
			            String data = "username=" + URLEncoder.encode(userName, "UTF-8")  
			                    + "&userpass=" + URLEncoder.encode(userPass, "UTF-8");  */
                        // 设置请求的头
                        urlConnection.setRequestProperty("Accept", "*/*");
                        // 设置请求的头
                        urlConnection.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
                        urlConnection.setRequestProperty("SOAPAction", action);
                        // 设置请求的头
                        urlConnection.setRequestProperty("Content-Length",
                                String.valueOf(requestEntity.getBytes().length));
                        // 设置请求的头
                        //urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
                        urlConnection.setUseCaches(false);
                        urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
                        urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入
                        //setDoInput的默认值就是true
                        //获取输出流
                        OutputStream os = urlConnection.getOutputStream();
                        os.write(requestEntity.getBytes());
                        os.flush();
                        if (urlConnection.getResponseCode() == 200) {
                            Log.e("length", String.valueOf(urlConnection.getContentLength()));
                            // 获取响应的输入流对象
                            InputStream is = urlConnection.getInputStream();
                            File file = new File(Environment.getExternalStorageDirectory(),
                                    "text.txt");
                            // 创建字节输出流对象
                            FileOutputStream baos = new FileOutputStream(file);
                            // 定义读取的长度
                            int len = 0;
                            // 定义缓冲区
                            byte buffer[] = new byte[1024];
                            // 按照缓冲区的大小，循环读取
                            StringBuffer sb = new StringBuffer();
                            while ((len = is.read(buffer)) != -1) {
                                // 根据读取的长度写入到os对象中
                                baos.write(buffer, 0, len);
                            }

                            is.close();
                            baos.close();
                            //Log.e("toByteArray", String.valueOf(baos.toByteArray().length));
                            // 返回字符串
                            final String result = new String("");
                            Log.e("String", String.valueOf(result.length()));
                            //System.out.println(sb);
                            // 通过runOnUiThread方法进行修改主线程的控件内容
                            callback.onHttpFinish(result);
                            return result;
                        } else {
                            return "链接失败.........";
                        }
                    } catch (Exception e) {
                        return "链接失败.........";
                    }

                } catch (Exception e) {
                    if (callback != null)
                        callback.onHttpError("请求错误，请重试!");
                    return "";
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if (callback != null)
                    callback.onHttpFinish(result);
            }


        }.execute();
    }

    /**
     * POST请求操作
     *
     * @param userName
     * @param userPass
     */
    public void urlPostAsync(String requestEntity, String action, HttpCallback callback) {
        urlSendRequest(requestEntity, action, callback);

    }

}
