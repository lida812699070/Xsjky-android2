package cn.xsjky.android.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import cn.xsjky.android.model.QueryHandoverInfoResult;

/**
 * Created by ${lida} on 2016/5/11.
 */
public class QueryHandoverInfoXmlParser extends DefaultHandler {

    private String tag;//标签（变化的）
    private QueryHandoverInfoResult user;

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        user = new QueryHandoverInfoResult();
    }

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag = qName;//给标签初始化

    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag = "";//
        //如果再次读取到student标签那么代表这个学生对象已经读取完毕然后进行存储。

    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String content = new String(ch, start, length);//标签里面的内容
        //如果标签是student的属性就进行对new出来的那个学生进行设置属性
        if ("FromNetwork".equals(tag)) {
            user.setFromNetwork(content);
        } else if ("FromNetworkId".equals(tag)) {
            user.setFromNetworkId(content);
        } else if ("NetworkDocumentCount".equals(tag)) {
            user.setNetworkDocumentCount(content);
        } else if ("FromTruck".equals(tag)) {
            user.setFromTruck(content);
        } else if ("TruckLoadedCount".equals(tag)) {
            user.setTruckLoadedCount(content);
        }
    }

    public QueryHandoverInfoResult getQueryHandoverInfoResult() {

        return user;
    }
}
