package cn.xsjky.android.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

import cn.xsjky.android.model.Coordinate;
import cn.xsjky.android.model.Custom;

/**
 * Created by ${lida} on 2016/9/19.
 */
public class SaveCutomerXmlparser extends DefaultHandler {


    private String tag;//标签（变化的）
    private Custom user;
    private Coordinate coordinate;

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        user = new Custom();
    }

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag = qName;//给标签初始化
        if ("Coordinate".equals(tag)) {
            coordinate = new Coordinate();
        }

    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag = "";//
        //如果再次读取到student标签那么代表这个学生对象已经读取完毕然后进行存储。
        if ("Coordinate".equals(qName)) {
            user.setCoordinate(coordinate);
        }

    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String content = new String(ch, start, length);//标签里面的内容
        //如果标签是student的属性就进行对new出来的那个学生进行设置属性
        if ("CustomerId".equals(tag)) {
            user.setCustomerId(content);
        } else if ("CustomerName".equals(tag)) {
            user.setCustomerName(content);
        } else if ("Fax".equals(tag)) {
            user.setFax(content);
        } else if ("TelAreaCode".equals(tag)) {
            user.setTelAreaCode(content);
        } else if ("Tel".equals(tag)) {
            user.setTel(content);
        } else if ("ContactPerson".equals(tag)) {
            user.setContactPerson(content);
        } else if ("Remarks".equals(tag)) {
            user.setRemarks(content);
        } else if ("IsMonthlyBalance".equals(tag)) {
            user.setIsMonthlyBalance(content);
        } else if ("InsuranceRate".equals(tag)) {
            user.setInsuranceRate(content);
        } else if ("NeedInsuranced".equals(tag)) {
            user.setNeedInsuranced(content);
        } else if ("IsCustomRate".equals(tag)) {
            user.setIsCustomRate(content);
        } else if ("Address".equals(tag)) {
            user.setAddress(content);
        } else if ("OperatorCount".equals(tag)) {
            user.setOperatorCount(content);
        } else if ("OperatorCount".equals(tag)) {
            user.setOperatorCount(content);
        } else if ("SendSmsToReceiver".equals(tag)) {
            user.setSendSmsToReceiver(content);
        } else if ("IsForbidden".equals(tag)) {
            user.setIsForbidden(content);
        } else if ("Longitude".equals(tag)) {
            coordinate.setLongitude(content);
        } else if ("Latitude".equals(tag)) {
            coordinate.setLatitude(content);
        }
    }

    public Custom getUser() {
        return user;
    }
}
