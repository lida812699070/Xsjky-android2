package cn.xsjky.android.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

import cn.xsjky.android.model.WoodenFrame;

/**
 * Created by ${lida} on 2016/11/23.
 */
public class WoodenFrameXmlparser extends DefaultHandler {

    private String tag;
    private ArrayList<WoodenFrame> mWoodenFrames;
    private WoodenFrame mWoodenFrame;

    public ArrayList<WoodenFrame> getWoodenFrames() {
        return mWoodenFrames;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        mWoodenFrames = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        tag=qName;
        if ("WoodenFrame".equals(tag)){
            mWoodenFrame = new WoodenFrame();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("WoodenFrame".equals(qName)){
            mWoodenFrames.add(mWoodenFrame);
        }
        tag="";
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String content=new String(ch,start,length);//标签里面的内容
        //如果标签是student的属性就进行对new出来的那个学生进行设置属性
        if ("RecordId".equals(tag)){
            mWoodenFrame.setRecordId(content);
        }else if ("DocumentNumber".equals(tag)){
            mWoodenFrame.setDocumentNumber(content);
        }else if ("Length".equals(tag)){
            mWoodenFrame.setLength(content);
        }else if ("Width".equals(tag)){
            mWoodenFrame.setWidth(content);
        }else if ("Height".equals(tag)){
            mWoodenFrame.setHeight(content);
        }else if ("Quantity".equals(tag)){
            mWoodenFrame.setQuantity(content);
        }else if ("Remarks".equals(tag)){
            mWoodenFrame.setRemarks(content);
        }
    }
}
