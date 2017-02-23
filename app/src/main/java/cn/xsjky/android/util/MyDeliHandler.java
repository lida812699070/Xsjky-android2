package cn.xsjky.android.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.model.DocumentEntity;

/**
 * Created by OK on 2016/3/30.
 */
public class MyDeliHandler extends DefaultHandler{
    private List<DocumentEntity> list;
    private String tag;//标签（变化的）
    private DocumentEntity user;
    public void startDocument() throws SAXException {
        list=new ArrayList<DocumentEntity>();
    }
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag=qName;//给标签初始化
        if ("Value".equals(tag)) {//如果标签是“student”那么就new一个学生用于后面赋值存储
            user=new DocumentEntity();
        }else if("ShipperAddress".equals(tag)){

        }
    }
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag="";//
        //如果再次读取到student标签那么代表这个学生对象已经读取完毕然后进行存储。
        if ("DeliverableDocument".equals(qName)) {
            list.add(user);
        }
    }
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String content=new String(ch,start,length);//标签里面的内容
        //如果标签是student的属性就进行对new出来的那个学生进行设置属性
        if ("DocumentId".equals(tag)) {
            user.setDocumentId(Integer.valueOf(content));
        }else if ("DocumentNumber".equals(tag)) {
            user.setDocumentNumber(content);
        }else if ("ConsigneeName".equals(tag)) {
            user.setConsigneeName(content);
        }else if ("ConsigneeContactName".equals(tag)) {
            user.setConsigneeContactName(content);
        }else if ("ConsigneeTel".equals(tag)) {
            user.setConsigneeTel(content);
        }else if ("AddressLine1".equals(tag)) {
            user.setAddressLine1(content);
        }else if ("AddressLine2".equals(tag)) {
            user.setAddressLine2(content);
        }else if ("TotalAmount".equals(tag)) {
            user.setTotalAmount(content);
        }else if ("Quantity".equals(tag)) {
            user.setQuantity(content);
        }else if ("Weight".equals(tag)) {
            user.setWeight(content);
        }else if ("Volumn".equals(tag)) {
            user.setVolumn(content);
        }else if ("ShippingMode".equals(tag)) {
            user.setShippingMode(content);
        }else if ("ShippingStatus".equals(tag)) {
            user.setShippingStatus(content);
        }else if ("Remarks".equals(tag)) {
            user.setRemarks(content);
        }
    }
    public List<DocumentEntity>getList(){
        return list;
    }
}
