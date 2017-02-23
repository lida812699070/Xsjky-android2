package cn.xsjky.android.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.model.TruckLoadedCargo;

/**
 * Created by OK on 2016/4/7.
 */
public class GetDriverLoadedDocumentsXmlParser extends DefaultHandler {

    private List<TruckLoadedCargo> list;
    private String tag;//标签（变化的）
    private TruckLoadedCargo user;

    public void startDocument() throws SAXException {
        list = new ArrayList<TruckLoadedCargo>();
    }

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag = qName;//给标签初始化
        if ("TruckLoadedCargo".equals(tag)) {//如果标签是“student”那么就new一个学生用于后面赋值存储
            user = new TruckLoadedCargo();
        }
    }

    //判断当前解析的标签是否在集合中
    private boolean flag = true;

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag = "";//
        //如果再次读取到student标签那么代表这个学生对象已经读取完毕然后进行存储。
        if ("TruckLoadedCargo".equals(qName)) {
            list.add(user);
        } else if ("ReturnList".equals(qName)) {
            flag = false;
        }
    }

    private int itemIndex = 0;

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String content = new String(ch, start, length);//标签里面的内容
        //如果标签是student的属性就进行对new出来的那个学生进行设置属性
        if ("TruckNumber".equals(tag) && flag) {
            user.setTruckNumber(content);
        } else if ("LoadedQty".equals(tag) && flag) {
            user.setLoadedQty(content);
        } else if ("LoadedWeight".equals(tag) && flag) {
            user.setLoadedWeight(content);
        } else if ("LoadedVolumn".equals(tag) && flag) {
            user.setLoadedVolumn(content);
        } else if ("DocumentId".equals(tag) && flag) {
            user.setDocumentId(content);
        } else if ("DocumentNumber".equals(tag) && flag) {
            user.setDocumentNumber(content);
        } else if ("Quantity".equals(tag) && flag) {
            user.setQuantity(content);
        } else if ("Weight".equals(tag) && flag) {
            user.setWeight(content);
        } else if ("Volumn".equals(tag) && flag) {
            user.setVolumn(content);
        } else if ("ToCity".equals(tag) && flag) {
            user.setToCity(content);
        }
    }

    public List<TruckLoadedCargo> getList() {
        return list;
    }
}
