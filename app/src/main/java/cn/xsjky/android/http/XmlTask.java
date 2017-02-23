package cn.xsjky.android.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import cn.xsjky.android.BaseSettings;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;


/**
 * xml任务处理器
 * @author Jerry
 *
 */
public class XmlTask {
	
	public static void parserXml(final String requestEntity, final String action, final HttpCallback callback){
			new AsyncTask<Void, Void, String>(){

				@Override
				protected void onPreExecute() {
					if(callback != null)
						callback.onHttpStart();
				}

				@Override
				protected String doInBackground(Void... params) {
					if(true)
						return "";
		DefaultHandler handler = new ConfigDefaultHandler();
		try {
			 /* URL url = new URL(BaseSettings.WEBSERVICE_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();  
            urlConnection.setReadTimeout(60000);  
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(60000);  */
            //urlConnection.setRequestProperty("Accept", "*/*");  
            /*urlConnection.setRequestProperty("Content-Type", "text/xml;charset=utf-8");  
            urlConnection.setRequestProperty("SOAPAction", action);
            urlConnection.setRequestProperty("Content-Length",  
                    String.valueOf(requestEntity.getBytes().length));  
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出  
            urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入  
                                            //setDoInput的默认值就是true  
            //urlConnection.connect();  
            //获取输出流  
            OutputStream os = urlConnection.getOutputStream();  
            os.write(requestEntity.getBytes());
            os.flush(); */
             
            File file = new File(Environment.getExternalStorageDirectory(),"text.txt"); 
            InputStream in = new FileInputStream(file); 
            //InputStream in = new BufferedInputStream(urlConnection.getInputStream());  
            InputStreamReader isr = new InputStreamReader(in, "utf-8");  
            SAXParserFactory factory = SAXParserFactory.newInstance();  
            XMLReader reader = null;  
            reader = factory.newSAXParser().getXMLReader();  
            reader.setContentHandler(handler);// 解析类  
            reader.parse(new InputSource(isr));  
            in.close();  
            //isr.close();  
            return "";
        } catch (Exception e) {  
        	e.printStackTrace();
        	return "";
        }
				}
						
						@Override
						protected void onPostExecute(String result) {
							if(callback != null)
								callback.onHttpFinish(result);
						}

						
					}.execute();
		};
}
