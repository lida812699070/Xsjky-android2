package cn.xsjky.android.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.model.DownLoadActors;
import cn.xsjky.android.model.MyHandOverItem;
import cn.xsjky.android.model.MyHandOverRecord;

/**
 * Created by OK on 2016/4/6.
 */
public class MyhandOverXmlPaser extends DefaultHandler {

    private List<MyHandOverRecord> list;
    private String tag;//标签（变化的）
    private MyHandOverRecord user;

    public void startDocument() throws SAXException {
        list = new ArrayList<MyHandOverRecord>();
    }

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag = qName;//给标签初始化
        if ("HandOverRecord".equals(tag)) {//如果标签是“student”那么就new一个学生用于后面赋值存储
            user = new MyHandOverRecord();
        } else if ("Items".equals(tag)) {
            user.setListItem(new ArrayList<MyHandOverItem>());
        } else if ("HandOverItem".equals(tag)) {
            user.getListItem().add(new MyHandOverItem());
        } else if ("DownloadActors".equals(tag)) {
            DownLoadActors downLoadActors = new DownLoadActors();
            user.setDownLoadActors(downLoadActors);
        }
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag = "";//
        //如果再次读取到student标签那么代表这个学生对象已经读取完毕然后进行存储。
        if ("HandOverRecord".equals(qName)) {
            list.add(user);
        }
    }

    private int itemIndex = 0;

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String content = new String(ch, start, length);//标签里面的内容
        //如果标签是student的属性就进行对new出来的那个学生进行设置属性
        if ("RecordId".equals(tag)) {
            user.setRecordId(content);
        } else if ("IssueTime".equals(tag)) {
            user.setIssueTime(content);
        } else if ("IssueUserId".equals(tag)) {
            user.setIssueUserId(content);
        } else if ("IssueUserName".equals(tag)) {
            user.setIssueUserName(content);
        } else if ("IssueEmployee".equals(tag)) {
            user.setIssueEmployee(content);
        } else if ("ReceiveUserId".equals(tag)) {
            user.setReceiveUserId(content);
        } else if ("ReceiveUserName".equals(tag)) {
            user.setReceiveUserName(content);
        } else if ("ReceiveEmployee".equals(tag)) {
            user.setReceiveEmployee(content);
        } else if ("IsReceived".equals(tag)) {
            user.setIsReceived(content);
        } else if ("ReceivedTime".equals(tag)) {
            user.setReceivedTime(content);
        } else if ("DocumentNumber".equals(tag)) {
            int size = user.getListItem().size();
            MyHandOverItem handOverItem = user.getListItem().get(size - 1);
            handOverItem.setDocumentNumber(content);
        } else if ("ToCity".equals(tag)) {
            int size = user.getListItem().size();
            MyHandOverItem handOverItem = user.getListItem().get(size - 1);
            handOverItem.setToCity(content);
        } else if ("Quantity".equals(tag)) {
            int size = user.getListItem().size();
            MyHandOverItem handOverItem = user.getListItem().get(size - 1);
            handOverItem.setQuantity(content);
        } else if ("Weight".equals(tag)) {
            int size = user.getListItem().size();
            MyHandOverItem handOverItem = user.getListItem().get(size - 1);
            handOverItem.setWeight(content);
        } else if ("Volumn".equals(tag)) {
            int size = user.getListItem().size();
            MyHandOverItem handOverItem = user.getListItem().get(size - 1);
            handOverItem.setVolumn(content);
        } else if ("IssueRemarks".equals(tag)) {
            int size = user.getListItem().size();
            MyHandOverItem handOverItem = user.getListItem().get(size - 1);
            handOverItem.setIssueRemarks(content);
        } else if ("TotalQuantity".equals(tag)) {
            user.setTotalQuantity(content);
        } else if ("TotalWeight".equals(tag)) {
            user.setTotalWeight(content);
        } else if ("string".equals(tag)) {
            if (user.getDownLoadActors().getList() == null) {
                user.getDownLoadActors().setList(new ArrayList<String>());
            }
            user.getDownLoadActors().getList().add(content);
        }
    }

    public List<MyHandOverRecord> getList() {
        return list;
    }
}
