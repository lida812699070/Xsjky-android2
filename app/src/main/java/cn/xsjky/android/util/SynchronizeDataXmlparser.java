package cn.xsjky.android.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import cn.xsjky.android.model.LoginInfo;
import cn.xsjky.android.model.SynchronizeData;

/**
 * Created by ${lida} on 2016/9/7.
 */
public class SynchronizeDataXmlparser extends DefaultHandler {
    private String tag;//标签（变化的）
    private SynchronizeData user;


    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag=qName;//给标签初始化
        if ("Value".equals(tag)) {//如果标签是“student”那么就new一个学生用于后面赋值存储
            user =new SynchronizeData();
        }
    }
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag="";//
        //如果再次读取到student标签那么代表这个学生对象已经读取完毕然后进行存储。

    }
    private StringBuffer mModifyData=new StringBuffer();
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String content=new String(ch,start,length);//标签里面的内容
        //如果标签是student的属性就进行对new出来的那个学生进行设置属性
        if ("ModifyData".equals(tag)) {
            user.setModifyData(mModifyData.append(content).toString());
        }else if ("DeleteData".equals(tag)) {
            user.setDeleteData(content);
        }
    }

    public SynchronizeData getUser() {
        return user;
    }

    public void setUser(SynchronizeData user) {
        this.user = user;
    }

}
