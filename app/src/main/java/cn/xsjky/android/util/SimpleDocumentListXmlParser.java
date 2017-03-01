package cn.xsjky.android.util;

import android.text.TextUtils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

import cn.xsjky.android.model.SimpleDocumentBysales;


/**
 * Created by ${lida} on 2017/3/1.
 */
public class SimpleDocumentListXmlParser extends DefaultHandler {
    private String tag = "";
    private ArrayList<SimpleDocumentBysales> list;
    private SimpleDocumentBysales user;

    public ArrayList<SimpleDocumentBysales> getList() {
        return list;
    }

    public void startDocument() throws SAXException {
        list = new ArrayList<>();
    }

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag = qName;//给标签初始化
        if ("SimpleDocument".equals(tag)) {
            user = new SimpleDocumentBysales();
        }
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag = "";//
        if ("SimpleDocument".equals(qName)) {
            list.add(user);
        }

    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String content = new String(ch, start, length);//标签里面的内容
        //如果标签是student的属性就进行对new出来的那个学生进行设置属性
        if ("DocumentNumber".equals(tag)) {
            user.setDocumentNumber(content);
        } else if ("CreateTime".equals(tag)) {
            user.setCreateTime(content);
        } else if ("ShipperName".equals(tag)) {
            user.setShipperName(content);
        } else if ("ConsigneeName".equals(tag)) {
            user.setConsigneeName(content);
        } else if ("ConsigneeContactPerson".equals(tag)) {
            user.setConsigneeContactPerson(content);
        } else if ("ToCity".equals(tag)) {
            user.setToCity(content);
        } else if ("ShippingMode".equals(tag)) {
            user.setShippingMode(content);
        }else if ("ShippingStatus".equals(tag)) {
            user.setShippingStatus(content);
        }
    }
}
