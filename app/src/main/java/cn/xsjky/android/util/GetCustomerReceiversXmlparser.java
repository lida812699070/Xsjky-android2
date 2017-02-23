package cn.xsjky.android.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.model.Address;
import cn.xsjky.android.model.CustomReceivers;
import cn.xsjky.android.model.DocumentEntity;

/**
 * Created by ${lida} on 2016/7/22.
 */
public class GetCustomerReceiversXmlparser extends DefaultHandler {
    private String tag;
    private Address address;
    private List<CustomReceivers> list;
    private CustomReceivers user;

    public void startDocument() throws SAXException {
        list = new ArrayList<>();
    }

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag=qName;//给标签初始化
        if ("CustomerReceivers".equals(tag)) {//如果标签是“student”那么就new一个学生用于后面赋值存储
            user=new CustomReceivers();
        }else if ("Address".equals(tag)){
            if (address!=null){
                return;
            }
            address=new Address();
        }
    }

   public void endElement(String uri, String localName, String qName)
            throws SAXException {
        tag="";//
        if ("CustomerReceivers".equals(qName)) {
            list.add(user);
        }else if ("Address".equals(qName)){
            user.setAddress(address);
            LogU.e(user.toString());
        }
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String content=new String(ch,start,length);//标签里面的内容
        //如果标签是student的属性就进行对new出来的那个学生进行设置属性
        if ("ReceiverId".equals(tag)) {
            user.setReceiverId(content);
        }else if ("CustomerId".equals(tag)) {
            user.setCustomerId(content);
        }else if ("ReceiverName".equals(tag)) {
            user.setReceiverName(content);
        }else if ("AreaCode".equals(tag)) {
            user.setAreaCode(content);
        }else if ("TelNumber".equals(tag)) {
            user.setTelNumber(content);
        }else if ("AddressId".equals(tag)) {
            address.setAddressId(Integer.valueOf(content));
        }else if ("Province".equals(tag)) {
            address.setProvince(content);
        }else if ("City".equals(tag)) {
            address.setCity(content);
        }else if ("District".equals(tag)) {
            address.setDistrict(content);
        }else if ("Address".equals(tag)) {
            address.setAddress(content);
        }else if ("Longitude".equals(tag)) {
            address.setLongitude(content);
        }else if ("Latitude".equals(tag)) {
            address.setLatitude(content);
        }else if ("PostCode".equals(tag)) {
            address.setPostCode(content);
        }
    }

    public List<CustomReceivers> getList() {
        return list;
    }
}
