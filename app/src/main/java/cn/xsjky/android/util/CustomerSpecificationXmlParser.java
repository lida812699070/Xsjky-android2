package cn.xsjky.android.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.model.CustomerSpecification;

/**
 * Created by ${lida} on 2017/2/15.
 */
public class CustomerSpecificationXmlParser extends DefaultHandler {
    private String tag = "";
    private CustomerSpecification user;
    private List<CustomerSpecification> list;

    public List<CustomerSpecification> getList() {
        return list;
    }


    public void startDocument() throws SAXException {
        list = new ArrayList<>();
    }

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag = qName;//给标签初始化
        if ("CustomerSpecification".equals(tag)) {
            user = new CustomerSpecification();
        }
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag = "";//
        //如果再次读取到student标签那么代表这个学生对象已经读取完毕然后进行存储。
        if ("CustomerSpecification".equals(qName)) {
            list.add(user);
        }
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String content = new String(ch, start, length);//标签里面的内容
        //如果标签是student的属性就进行对new出来的那个学生进行设置属性
        if ("ProductId".equals(tag)) {
            user.setProductId(content);
        } else if ("ProductDescription".equals(tag)) {
            user.setProductDescription(content);
        } else if ("Weight".equals(tag)) {
            try {
                user.setWeight(Double.valueOf(content));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("Length".equals(tag)) {
            try {
                user.setLength(Double.valueOf(content));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("Width".equals(tag)) {
            try {
                user.setWidth(Double.valueOf(content));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("Height".equals(tag)) {
            try {
                user.setHeight(Double.valueOf(content));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
