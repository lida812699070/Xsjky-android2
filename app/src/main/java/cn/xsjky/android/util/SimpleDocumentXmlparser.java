package cn.xsjky.android.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

import cn.xsjky.android.model.Address;
import cn.xsjky.android.model.FreightItem;
import cn.xsjky.android.model.QueryHandoverInfoResult;
import cn.xsjky.android.model.ShipperCost;
import cn.xsjky.android.model.ShippingMode;
import cn.xsjky.android.model.SimpleDocument;

/**
 * Created by ${lida} on 2016/8/17.
 */
public class SimpleDocumentXmlparser extends DefaultHandler {
    private String tag;//标签（变化的）
    private SimpleDocument user;
    private ArrayList<ShipperCost> shipperCosts;
    private Address ConsigneeAddress;
    private Address ShipperAddress;
    private ShippingMode ShippingMode;
    private ShipperCost ShipperCost;

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        user = new SimpleDocument();
    }

    public SimpleDocument getUser() {
        return user;
    }

    private boolean isConsignee = false;
    private boolean isShipper = false;

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag = qName;//给标签初始化
        if ("CarriageItems".equals(qName)) {
            shipperCosts = new ArrayList<>();
        } else if ("ConsigneeAddress".equals(qName)) {
            isConsignee = true;
            ConsigneeAddress = new Address();
        } else if ("ShipperAddress".equals(qName)) {
            ShipperAddress = new Address();
            isShipper = true;
        } else if ("ShippingMode".equals(qName)) {
            ShippingMode = new ShippingMode();
        } else if ("ShipperCost".equals(qName)) {
            ShipperCost = new ShipperCost();
        }
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        //System.out.println("uri="+uri+",lcaolName="+localName+"qNmae="+qName);
        tag = "";
        //如果再次读取到student标签那么代表这个学生对象已经读取完毕然后进行存储。
        if ("CarriageItems".equals(qName)) {
            user.setCarriageItems(shipperCosts);
        } else if ("ConsigneeAddress".equals(qName)) {
            user.setConsigneeAddress(ConsigneeAddress);
            isConsignee = false;
        } else if ("ShipperAddress".equals(qName)) {
            user.setShipperAddress(ShipperAddress);
            isShipper = false;
        } else if ("ShippingMode".equals(qName)) {
            user.setShippingMode(ShippingMode);
        } else if ("ShipperCost".equals(qName)) {
            shipperCosts.add(ShipperCost);
        }

    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String content = new String(ch, start, length);//标签里面的内容
        //如果标签是student的属性就进行对new出来的那个学生进行设置属性
        if ("DocumentNumber".equals(tag)) {
            user.setDocumentNumber(content);
        } else if ("FromCity".equals(tag)) {
            user.setFromCity(content);
        } else if ("ShipperCode".equals(tag)) {
            user.setShipperCode(content);
        } else if ("ToCity".equals(tag)) {
            user.setToCity(content);
        } else if ("ShipperName".equals(tag)) {
            user.setShipperName(content);
        } else if ("ShipperContactName".equals(tag)) {
            user.setShipperContactName(content);
        } else if ("DeliveryAfterNotify".equals(tag)) {
            user.setDeliveryAfterNotify(Boolean.valueOf(content));
        } else if ("Address".equals(tag)) {
            if (isConsignee) {
                ConsigneeAddress.setAddress(content);
            }
            if (isShipper) {
                ShipperAddress.setAddress(content);
            }
        } else if ("City".equals(tag)) {
            if (isConsignee) {
                ConsigneeAddress.setCity(content);
            }
            if (isShipper) {
                ShipperAddress.setCity(content);
            }
        } else if ("District".equals(tag)) {
            if (isConsignee) {
                ConsigneeAddress.setDistrict(content);
            }
            if (isShipper) {
                ShipperAddress.setDistrict(content);
            }
        } else if ("Province".equals(tag)) {
            if (isConsignee) {
                ConsigneeAddress.setProvince(content);
            }
            if (isShipper) {
                ShipperAddress.setProvince(content);
            }
        } else if ("ShipperPhoneNumber".equals(tag)) {
            user.setShipperPhoneNumber(content);
        } else if ("ConsigneeName".equals(tag)) {
            user.setConsigneeName(content);
        } else if ("ConsigneeContactPerson".equals(tag)) {
            user.setConsigneeContactPerson(content);
        } else if ("ConsigneePhoneNumber".equals(tag)) {
            user.setConsigneePhoneNumber(content);
        } else if ("ModeId".equals(tag)) {
            ShippingMode.setModeId(Integer.valueOf(content));
        } else if ("ModeName".equals(tag)) {
            ShippingMode.setModeName(content);
        } else if ("ProductName".equals(tag)) {
            user.setProductName(content);
        } else if ("CountWeight".equals(tag)) {
            user.setCountWeight(content);
        } else if ("Quantity".equals(tag)) {
            user.setQuantity(content);
        } else if ("Weight".equals(tag)) {
            user.setWeight(content);
        } else if ("Volumn".equals(tag)) {
            user.setVolumn(content);
        } else if ("InsuranceAmt".equals(tag)) {
            user.setInsuranceAmt(content);
        } else if ("ChargeValue".equals(tag)) {
            ShipperCost.setChargeValue(Double.valueOf(content));
        } else if ("ItemId".equals(tag)) {
            FreightItem freightItem = new FreightItem();
            freightItem.setItemId(Integer.valueOf(content));
            ShipperCost.setFreightItem(freightItem);
        } else if ("Upstair".equals(tag)) {
            user.setUpstair(Boolean.valueOf(content));
        } else if ("NeedDelivery".equals(tag)) {
            user.setNeedDelivery(Boolean.valueOf(content));
        } else if ("NeedInsurance".equals(tag)) {
            user.setNeedInsurance(Boolean.valueOf(content));
        } else if ("NeedSignUpNotifyMessage".equals(tag)) {
            user.setNeedSignUpNotifyMessage(Boolean.valueOf(content));
        } else if ("Remarks".equals(tag)) {
            user.setRemarks(content);
        } else if ("BalanceMode".equals(tag)) {
            user.setBalanceMode(content);
        }else if ("DeliveryCharge".equals(tag)) {
            user.setDeliveryCharge(content);
        }else if ("ShipperAreaCode".equals(tag)) {
            user.setShipperAreaCode(content);
        }else if ("ConsigneeAreaCode".equals(tag)) {
            user.setConsigneeAreaCode(content);
        }else if ("RecordId".equals(tag)) {
            user.setRecordId(content);
        }

    }

}
