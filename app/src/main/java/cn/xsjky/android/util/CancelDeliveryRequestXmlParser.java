package cn.xsjky.android.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.model.CancelDeliveryRequest;
import cn.xsjky.android.model.TruckLoadedCargo;

/**
 * Created by OK on 2016/4/27.
 */
public class CancelDeliveryRequestXmlParser extends DefaultHandler {

    private String tag;//标签（变化的）
    private CancelDeliveryRequest user;

    @Override
    public void startDocument() throws SAXException {
        user = new CancelDeliveryRequest();
        super.startDocument();
    }

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag = qName;//给标签初始化
        
    }

    //判断当前解析的标签是否在集合中
    private boolean flag = true;

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag = "";//
        //如果再次读取到student标签那么代表这个学生对象已经读取完毕然后进行存储。

    }

    private int itemIndex = 0;

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String content = new String(ch, start, length);//标签里面的内容
        //如果标签是student的属性就进行对new出来的那个学生进行设置属性
        if ("ReturnCode".equals(tag) && flag) {
            user.setReturnCode(content);
        } else if ("Error".equals(tag) && flag) {
            user.setError(content);
        } else if ("StackTrace".equals(tag) && flag) {
            user.setStackTrace(content);
        } else if ("ErrorSource".equals(tag) && flag) {
            user.setErrorSource(content);
        }
    }

    public CancelDeliveryRequest getCancelDeliveryRequest() {
        return user;
    }
}
