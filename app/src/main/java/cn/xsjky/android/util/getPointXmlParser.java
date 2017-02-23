package cn.xsjky.android.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

import cn.xsjky.android.model.AcceptRequestResult;
import cn.xsjky.android.model.LocationInfo;
import cn.xsjky.android.model.RequestMarks;

/**
 * Created by ${lida} on 2016/5/5.
 */
public class getPointXmlParser extends DefaultHandler {
    private String tag;//标签（变化的）
    private LocationInfo user;
    private ArrayList<LocationInfo> list = new ArrayList<>();

    @Override
    public void startDocument() throws SAXException {

    }

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag = qName;//给标签初始化
        if ("LocationInfo".equals(tag)) {
            user = new LocationInfo();
        }
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag = "";//
        //如果再次读取到student标签那么代表这个学生对象已经读取完毕然后进行存储。
        if ("LocationInfo".equals(qName)) {
            list.add(user);
        }
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String content = new String(ch, start, length);//标签里面的内容
        //如果标签是student的属性就进行对new出来的那个学生进行设置属性
        if ("Lon".equals(tag)) {
            user.setLon(content);
        } else if ("Lat".equals(tag)) {
            user.setLat(content);
        }
    }

    public ArrayList<LocationInfo> getPoints() {
        return list;
    }
}
