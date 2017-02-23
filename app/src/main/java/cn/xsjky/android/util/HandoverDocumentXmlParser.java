package cn.xsjky.android.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.model.HandoverDocument;

/**
 * Created by ${lida} on 2016/5/11.
 */
public class HandoverDocumentXmlParser extends DefaultHandler {
    private List<HandoverDocument> list;
    private String tag;//标签（变化的）
    private HandoverDocument user;

    public void startDocument() throws SAXException {
        list = new ArrayList<HandoverDocument>();
    }

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag = qName;//给标签初始化
        if ("HandoverDocument".equals(tag)) {//如果标签是“student”那么就new一个学生用于后面赋值存储
            user = new HandoverDocument();
        }
    }

    //判断当前解析的标签是否在集合中
    private boolean flag = true;

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag = "";//
        //如果再次读取到student标签那么代表这个学生对象已经读取完毕然后进行存储。
        if ("HandoverDocument".equals(qName)) {
            list.add(user);
        } else if ("ReturnList".equals(qName)) {
            flag = false;
        }
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String content = new String(ch, start, length);//标签里面的内容
        //如果标签是student的属性就进行对new出来的那个学生进行设置属性
        if ("DocumentId".equals(tag) && flag) {
            user.setDocumentId(content);
        } else if ("HandoverQty".equals(tag) && flag) {
            user.setHandoverQty(content);
        } else if ("HandoverVolumn".equals(tag) && flag) {
            user.setHandoverVolumn(content);
        } else if ("DocumentNumber".equals(tag) && flag) {
            user.setDocumentNumber(content);
        } else if ("ToCity".equals(tag) && flag) {
            user.setToCity(content);
        } else if ("Remarks".equals(tag) && flag) {
            user.setRemarks(content);
        }
    }

    public List<HandoverDocument> getList() {
        return list;
    }
}
