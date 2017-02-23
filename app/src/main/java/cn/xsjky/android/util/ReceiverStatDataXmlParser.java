package cn.xsjky.android.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

import cn.xsjky.android.model.ReceiverStatData;

/**
 * Created by ${lida} on 2016/11/28.
 */
public class ReceiverStatDataXmlParser extends DefaultHandler {

    private String tag = "";
    private ArrayList<ReceiverStatData> mDatas;
    private ReceiverStatData mReceiverStatData;

    public ArrayList<ReceiverStatData> getDatas() {
        return mDatas;
    }

    @Override
    public void startDocument() throws SAXException {
        mDatas = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        tag = qName;
        if ("ReceiverStatData".equals(tag)) {
            mReceiverStatData = new ReceiverStatData();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("ReceiverStatData".equals(qName)) {
            mDatas.add(mReceiverStatData);
        }
        tag = "";
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String content=new String(ch,start,length);//标签里面的内容
        //如果标签是student的属性就进行对new出来的那个学生进行设置属性
        if ("StatPeriod".equals(tag)){
            mReceiverStatData.setStatPeriod(content);
        }else if ("TicketCount".equals(tag)){
            mReceiverStatData.setTicketCount(content);
        }else if ("TotalQuantity".equals(tag)){
            mReceiverStatData.setTotalQuantity(content);
        }else if ("TotalWeight".equals(tag)){
            mReceiverStatData.setTotalWeight(content);
        }else if ("TotalVolumn".equals(tag)){
            mReceiverStatData.setTotalVolumn(content);
        }else if ("TotalGoodsAmt".equals(tag)){
            mReceiverStatData.setTotalGoodsAmt(content);
        }else if ("TotalPremium".equals(tag)){
            mReceiverStatData.setTotalPremium(content);
        }else if ("TotalCarriage".equals(tag)){
            mReceiverStatData.setTotalCarriage(content);
        }else if ("TotalAmount".equals(tag)){
            mReceiverStatData.setTotalAmount(content);
        }
    }
}
