package cn.xsjky.android.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.model.LoginInfo;

/**
 * Created by OK on 2016/3/16.
 */
public class MyHandler extends DefaultHandler {
    private List<LoginInfo> list;
    private String tag;//标签（变化的）
    private LoginInfo user;
    public void startDocument() throws SAXException {
        list=new ArrayList<LoginInfo>();
    }
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag=qName;//给标签初始化
        if ("Value".equals(tag)) {//如果标签是“student”那么就new一个学生用于后面赋值存储
            user=new LoginInfo();
        }
    }
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag="";//
        //如果再次读取到student标签那么代表这个学生对象已经读取完毕然后进行存储。
        if ("Value".equals(qName)) {
            list.add(user);
        }
    }
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String content=new String(ch,start,length);//标签里面的内容
        //如果标签是student的属性就进行对new出来的那个学生进行设置属性
        if ("RoleData".equals(tag)) {
            user.setRoleData(Integer.valueOf(content));
        }else if ("SessionId".equals(tag)) {
            user.setSessionId(content);
        }else if ("ClientName".equals(tag)) {
            user.setClientName(content);
        }else if ("UserId".equals(tag)) {
            user.setUserId(Integer.valueOf(content));
        }
    }
    public List<LoginInfo>getList(){
        return list;
    }
}
