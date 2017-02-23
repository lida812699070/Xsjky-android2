package cn.xsjky.android.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.util.StrKit;
import cn.xsjky.android.util.XmlParserUtil;

public class Document extends BaseModel implements Serializable {
    private int RecordId;
    private String DocumentNumber;
    private int ShipperCode;
    private String ShipperName;
    private String ShipperContactName;
    private Address ShipperAddress;
    private String ShipperAreaCode;
    private String ShipperPhoneNumber;
    private boolean NeedSignUpNotifyMessage;
    private int ConsigneeCode;
    private String ConsigneeName;
    private String ConsigneeContactPerson;
    private Address ConsigneeAddress;
    private WoodenFrame woodenFrame;
    private String ConsigneeAreaCode;
    private String ConsigneePhoneNumber;
    private Network FromNetwork;
    private Network ToNetwork;
    private ShippingMode ShippingMode;
    private double Weight;
    private double Volumn;
    private String ProductName;
    private int Quantity;
    private double Carriage;
    private double TotalCharge;
    private boolean NeedDelivery;
    private boolean Upstair;
    private String CountWeight;

    public String getCountWeight() {
        return CountWeight;
    }

    public void setCountWeight(String countWeight) {
        CountWeight = countWeight;
    }

    public boolean isUpstair() {
        return Upstair;
    }

    public void setUpstair(boolean upstair) {
        Upstair = upstair;
    }

    private Double DeliveryCharge;
    private int GoodsAmount;
    private List<ShipperCost> CarriageItems;
    private List<WoodenFrame> WoodenFrames;
    private String BalanceMode;
    private int MonthlyBalanceAccount;
    private boolean NeedInsurance;
    private int InsuranceAmt;
    private double Premium;
    private String Remarks;
    private String PickupBy;
    private String DeliveredBy;
    private String CreateTime;
    private int Creator;
    private boolean IsCarriageBalance;
    private String BalanceTime;
    private int BalanceBy;
    private boolean EditLock;
    private int EditingUser;
    private String LockTime;
    private String ShippingStatus; //IsApplied or ReceiverAssigned or IsReceived or IsShipping or IsArrived or IsDelivering or IsSignUp</ShippingStatus>
    private boolean IsConfirm;
    private boolean IsFinished;
    private String Abnormal;
    private String AbnormalTime;

    public boolean isDeliveryAfterNotify() {
        return DeliveryAfterNotify;
    }

    public void setDeliveryAfterNotify(boolean deliveryAfterNotify) {
        DeliveryAfterNotify = deliveryAfterNotify;
    }

    private boolean DeliveryAfterNotify;
    private String FromCity;
    private String ToCity;

    public static List<Document> parserXmlList(String xml, String prefix) {
        List<Document> list = new ArrayList<Document>();
        if (StrKit.isBlank(xml))
            return null;

        parser = XmlParserUtil.getInstance();
        parser.parse(xml);//resultPrefix
        resultPrefix = prefix;
        int count = getXmlCount("ShippingDocument");
        for (int i = 0; i < count; i++) {
            resultPrefix = prefix + "ShippingDocument#" + i + "/";
            Document d = new Document();
            parserDocument(d);
            list.add(d);
        }
        return list;
    }

    public static List<Document> parserXmlList1(String xml) {
        List<Document> list = new ArrayList<Document>();
        if (StrKit.isBlank(xml))
            return null;

        parser = XmlParserUtil.getInstance();
        parser.parse(xml);//resultPrefix
        String prefix = "/soap:Envelope/soap:Body/GetApplyDocumentsResponse/GetApplyDocumentsResult/ReturnList/";
        resultPrefix = prefix;
        int count = getXmlCount("ShippingDocument");
        for (int i = 0; i < count; i++) {
            resultPrefix = prefix + "ShippingDocument#" + i + "/";
            Document d = new Document();
            parserDocument(d);
            list.add(d);
        }
        return list;
    }

    public List<WoodenFrame> getWoodenFrames() {
        return WoodenFrames;
    }

    public void setWoodenFrames(List<WoodenFrame> woodenFrames) {
        this.WoodenFrames = woodenFrames;
    }

    private static void parserDocument(Document d) {
        d.setRecordId(getXmlValueInt("RecordId"));
        d.setShipperCode(getXmlValueInt("ShipperCode"));
        d.setShipperName(getXmlValueStr("ShipperName"));
        d.setShipperAreaCode(getXmlValueStr("ShipperAreaCode"));
        d.setShipperContactName(getXmlValueStr("ShipperContactName"));
        d.setShipperPhoneNumber(getXmlValueStr("ShipperPhoneNumber"));
        d.setDocumentNumber(getXmlValueStr("DocumentNumber"));
        d.setNeedSignUpNotifyMessage(getXmlValueBool("NeedSignUpNotifyMessage"));
        d.setConsigneeCode(getXmlValueInt("ConsigneeCode"));
        d.setConsigneeName(getXmlValueStr("ConsigneeName"));
        d.setConsigneeContactPerson(getXmlValueStr("ConsigneeContactPerson"));
        d.setConsigneeAreaCode(getXmlValueStr("ConsigneeAreaCode"));
        d.setConsigneePhoneNumber(getXmlValueStr("ConsigneePhoneNumber"));
        d.setFromCity(getXmlValueStr("FromCity"));
        d.setToCity(getXmlValueStr("ToCity"));
        d.setProductName(getXmlValueStr("ProductName"));
        d.setCountWeight(getXmlValueStr("CountWeight"));

        d.setDeliveryCharge(getXmlValueDouble("DeliveryCharge"));
        d.setNeedDelivery(getXmlValueBool("NeedDelivery"));
        d.setUpstair(getXmlValueBool("Upstair"));
        d.setTotalCharge(getXmlValueDouble("TotalCharge"));
        d.setGoodsAmount(getXmlValueInt("GoodsAmount"));

        Address shipperAddress = new Address();
        shipperAddress.setAddressId(getXmlValueInt("ShipperAddress/AddressId"));
        shipperAddress.setProvince(getXmlValueStr("ShipperAddress/Province"));
        shipperAddress.setCity(getXmlValueStr("ShipperAddress/City"));
        shipperAddress.setDistrict(getXmlValueStr("ShipperAddress/District"));
        shipperAddress.setAddress(getXmlValueStr("ShipperAddress/Address"));
        shipperAddress.setPostCode(getXmlValueStr("ShipperAddress/PostCode"));
        d.setShipperAddress(shipperAddress);

        ShippingMode shippingMode = new ShippingMode();
        shippingMode.setModeId(getXmlValueInt("ShippingMode/ModeId"));
        shippingMode.setModeName(getXmlValueStr("ShippingMode/ModeName"));
        shippingMode.setIsEnabled(getXmlValueBool("ShippingMode/IsEnabled"));
        d.setShippingMode(shippingMode);

        Address consigneeAddress = new Address();
        consigneeAddress.setAddressId(getXmlValueInt("ConsigneeAddress/AddressId"));
        consigneeAddress.setProvince(getXmlValueStr("ConsigneeAddress/Province"));
        consigneeAddress.setCity(getXmlValueStr("ConsigneeAddress/City"));
        consigneeAddress.setDistrict(getXmlValueStr("ConsigneeAddress/District"));
        consigneeAddress.setAddress(getXmlValueStr("ConsigneeAddress/Address"));
        consigneeAddress.setPostCode(getXmlValueStr("ConsigneeAddress/PostCode"));
        d.setConsigneeAddress(consigneeAddress);

        Network fromNetwork = new Network();
        fromNetwork.setNetworkId(getXmlValueInt("FromNetwork/NetworkId"));
        fromNetwork.setNetworkCode(getXmlValueStr("FromNetwork/NetworkCode"));
        fromNetwork.setNetworkName(getXmlValueStr("FromNetwork/NetworkName"));
        fromNetwork.setParentId(getXmlValueInt("FromNetwork/ParentId"));
        fromNetwork.setManagerId(getXmlValueInt("FromNetwork/ManagerId"));
        fromNetwork.setIsDisabled(getXmlValueBool("FromNetwork/IsDisabled"));
        fromNetwork.setPinYinCode(getXmlValueStr("FromNetwork/PinYinCode"));
        fromNetwork.setWuBiCode(getXmlValueStr("FromNetwork/WuBiCode"));
        d.setFromNetwork(fromNetwork);

        if (StrKit.notBlank(getXmlValueStr("ToNetwork/NetworkId"))) {
            Network toNetwork = new Network();
            toNetwork.setNetworkId(getXmlValueInt("ToNetwork/NetworkId"));
            toNetwork.setNetworkCode(getXmlValueStr("ToNetwork/NetworkCode"));
            toNetwork.setNetworkName(getXmlValueStr("ToNetwork/NetworkName"));
            toNetwork.setParentId(getXmlValueInt("ToNetwork/ParentId"));
            toNetwork.setManagerId(getXmlValueInt("ToNetwork/ManagerId"));
            toNetwork.setIsDisabled(getXmlValueBool("ToNetwork/IsDisabled"));
            toNetwork.setPinYinCode(getXmlValueStr("ToNetwork/PinYinCode"));
            toNetwork.setWuBiCode(getXmlValueStr("ToNetwork/WuBiCode"));
            d.setToNetwork(toNetwork);
        }

        d.setWeight(getXmlValueDouble("Weight"));
        d.setVolumn(getXmlValueDouble("Volumn"));
        d.setQuantity(getXmlValueInt("Quantity"));
        d.setCarriage(getXmlValueDouble("Carriage"));

        List<ShipperCost> carriageItems = new ArrayList<ShipperCost>();
        int count = getXmlCount("CarriageItems/ShipperCost");
        for (int i = 0; i < count; i++) {
            ShipperCost sc = new ShipperCost();
            String prefix = "CarriageItems/ShipperCost#" + i + "/";
            sc.setRecordId(getXmlValueInt(prefix + "RecordId"));
            sc.setDocumentId(getXmlValueInt(prefix + "DocumentId"));

            FreightItem f = new FreightItem();
            f.setItemId(getXmlValueInt(prefix + "FreightItem/ItemId"));
            f.setItemName(getXmlValueStr(prefix + "FreightItem/ItemName"));
            f.setIsCalculate(getXmlValueBool(prefix + "FreightItem/IsCalculate"));
            f.setAllowEdit(getXmlValueBool(prefix + "FreightItem/AllowEdit"));
            f.setIsEnabled(getXmlValueBool(prefix + "FreightItem/IsEnabled"));
            sc.setFreightItem(f);
            sc.setChargeValue(getXmlValueDouble(prefix + "ChargeValue"));
            carriageItems.add(sc);
        }
        d.setCarriageItems(carriageItems);

        List<WoodenFrame> woodenFrames = new ArrayList<>();
        int num = getXmlCount("WoodenFrames/WoodenFrame");
        for (int i = 0; i < num; i++) {
            WoodenFrame sc = new WoodenFrame();
            String prefix = "WoodenFrames/WoodenFrame#" + i + "/";
            sc.setRecordId(getXmlValueStr(prefix + "RecordId"));
            sc.setLength(getXmlValueStr(prefix + "Length"));
            sc.setWidth(getXmlValueStr(prefix + "Width"));
            sc.setHeight(getXmlValueStr(prefix + "Height"));
            sc.setQuantity(getXmlValueStr(prefix + "Quantity"));
            sc.setRemarks(getXmlValueStr(prefix + "Remarks"));
            sc.setCreator(getXmlValueStr(prefix + "Creator"));
            sc.setHandlerId(getXmlValueStr(prefix + "HandlerId"));
            sc.setHandler(getXmlValueStr(prefix + "Handler"));
            sc.setState(getXmlValueStr(prefix + "State"));
            sc.setAssignTime(getXmlValueStr(prefix + "AssignTime"));
            sc.setAcceptTime(getXmlValueStr(prefix + "AcceptTime"));
            sc.setFinishedTime(getXmlValueStr(prefix + "FinishedTime"));

            woodenFrames.add(sc);
        }
        d.setWoodenFrames(woodenFrames);

        d.setBalanceMode(getXmlValueStr("BalanceMode"));
        d.setMonthlyBalanceAccount(getXmlValueInt("MonthlyBalanceAccount"));
        d.setNeedInsurance(getXmlValueBool("NeedInsurance"));
        d.setInsuranceAmt(getXmlValueInt("InsuranceAmt"));
        d.setPremium(getXmlValueDouble("Premium"));
        d.setCreateTime(getXmlValueStr("CreateTime"));
        d.setCreator(getXmlValueInt("Creator"));
        d.setIsCarriageBalance(getXmlValueBool("IsCarriageBalance"));
        d.setBalanceTime(getXmlValueStr("BalanceTime"));
        d.setBalanceBy(getXmlValueInt("BalanceBy"));
        d.setEditLock(getXmlValueBool("EditLock"));
        d.setEditingUser(getXmlValueInt("EditingUser"));
        d.setLockTime(getXmlValueStr("LockTime"));
        d.setShippingStatus(getXmlValueStr("ShippingStatus"));
        d.setIsConfirm(getXmlValueBool("IsConfirm"));
        d.setIsFinished(getXmlValueBool("IsFinished"));
        d.setAbnormalTime(getXmlValueStr("AbnormalTime"));
        d.setDeliveryAfterNotify(getXmlValueBool("DeliveryAfterNotify"));
    }

    public void parserEditXml(String xml) {
        parser = XmlParserUtil.getInstance();
        parser.parse(xml);
        resultPrefix = "/soap:Envelope/soap:Body/SaveDocumentResponse/SaveDocumentResult/Value/";
        parserDocument(this);
    }

    public Document parserDeliverXML(String xml) {
        if (StrKit.isBlank(xml))
            return null;
        parser = XmlParserUtil.getInstance();
        parser.parse(xml);
        resultPrefix = "/soap:Envelope/soap:Body/NewDocumentResponse/GetDocumentByNumber/Value/";
        String code = parser.getNodeValue("/soap:Envelope/soap:Body/GetDocumentByNumber/NewDocumentResult/ReturnCode");
        if (code != null && !code.equals("0"))
            return null;
        parserDocument(this);
        return this;
    }

    public Document parser2XML(String xml) {
        if (StrKit.isBlank(xml))
            return null;
        parser = XmlParserUtil.getInstance();
        parser.parse(xml);
        resultPrefix = "/soap:Envelope/soap:Body/GetDocumentByNumberResponse/GetDocumentByNumberResult/Value/";
        String code = parser.getNodeValue("/soap:Envelope/soap:Body/GetDocumentByNumberResponse/GetDocumentByNumberResult/ReturnCode");
        String DeliveryCharge = parser.getNodeValue("/soap:Envelope/soap:Body/NewDocumentResponse/NewDocumentResult/Value/DeliveryCharge");
        if (code != null && !code.equals("0"))
            return null;
        parserDocument(this);
        //this.setDeliveryCharge(Integer.valueOf(DeliveryCharge));
        return this;
    }

    public Document parserXML(String xml) {
        if (StrKit.isBlank(xml))
            return null;
        parser = XmlParserUtil.getInstance();
        parser.parse(xml);
        resultPrefix = "/soap:Envelope/soap:Body/NewDocumentResponse/NewDocumentResult/Value/";
        String code = parser.getNodeValue("/soap:Envelope/soap:Body/NewDocumentResponse/NewDocumentResult/ReturnCode");
        String DeliveryCharge = parser.getNodeValue("/soap:Envelope/soap:Body/NewDocumentResponse/NewDocumentResult/Value/DeliveryCharge");
        if (code != null && !code.equals("0"))
            return null;
        parserDocument(this);
        //this.setDeliveryCharge(Integer.valueOf(DeliveryCharge));
        return this;
        /*this.RecordId = getXmlValueInt("RecordId");
        this.ShipperCode = getXmlValueInt("ShipperCode");
		this.DocumentNumber = getXmlValueStr("DocumentNumber");
		this.NeedSignUpNotifyMessage = getXmlValueBool("NeedSignUpNotifyMessage");
		this.ConsigneeCode = getXmlValueInt("ConsigneeCode");
		FromNetwork = new Network();
		FromNetwork.setNetworkId(getXmlValueInt("FromNetwork/NetworkId"));
		FromNetwork.setNetworkCode(getXmlValueStr("FromNetwork/NetworkCode"));
		FromNetwork.setNetworkName(getXmlValueStr("FromNetwork/NetworkName"));
		FromNetwork.setParentId(getXmlValueInt("FromNetwork/ParentId"));
		FromNetwork.setManagerId(getXmlValueInt("FromNetwork/ManagerId"));
		FromNetwork.setIsDisabled(getXmlValueBool("FromNetwork/IsDisabled"));
		FromNetwork.setPinYinCode(getXmlValueStr("FromNetwork/PinYinCode"));
		FromNetwork.setWuBiCode(getXmlValueStr("FromNetwork/WuBiCode"));
		
		ToNetwork = new Network();
		ToNetwork.setNetworkId(getXmlValueInt("ToNetwork/NetworkId"));
		ToNetwork.setNetworkCode(getXmlValueStr("ToNetwork/NetworkCode"));
		ToNetwork.setNetworkName(getXmlValueStr("ToNetwork/NetworkName"));
		ToNetwork.setParentId(getXmlValueInt("ToNetwork/ParentId"));
		ToNetwork.setManagerId(getXmlValueInt("ToNetwork/ManagerId"));
		ToNetwork.setIsDisabled(getXmlValueBool("ToNetwork/IsDisabled"));
		ToNetwork.setPinYinCode(getXmlValueStr("ToNetwork/PinYinCode"));
		ToNetwork.setWuBiCode(getXmlValueStr("ToNetwork/WuBiCode"));
		
		this.Weight = getXmlValueInt("Weight");
		this.Volumn = getXmlValueInt("Volumn");
		this.Quantity = getXmlValueInt("Quantity");
		this.Carriage = getXmlValueInt("Carriage");
		
		CarriageItems = new ArrayList<ShipperCost>();
		int count = getXmlCount("CarriageItems/ShipperCost");
		for(int i = 0; i < count; i++){
			ShipperCost sc = new ShipperCost();
			String prefix = "CarriageItems/ShipperCost#" + i + "/";
			sc.setRecordId(getXmlValueInt(prefix + "RecordId"));
			sc.setDocumentId(getXmlValueInt(prefix + "DocumentId"));
			FreightItem f = new FreightItem();
			f.setItemId(getXmlValueInt(prefix + "FreightItem/ItemId"));
			f.setItemName(getXmlValueStr(prefix + "FreightItem/ItemName"));
			f.setIsCalculate(getXmlValueBool(prefix + "FreightItem/IsCalculate"));
			f.setAllowEdit(getXmlValueBool(prefix + "FreightItem/AllowEdit"));
			f.setIsEnabled(getXmlValueBool(prefix + "FreightItem/IsEnabled"));
			sc.setFreightItem(f);
			sc.setChargeValue(getXmlValueInt(prefix + "ChargeValue"));
			CarriageItems.add(sc);
		}
		this.setBalanceMode(getXmlValueStr("BalanceMode"));
		this.MonthlyBalanceAccount = getXmlValueInt("MonthlyBalanceAccount");
		this.NeedInsurance = getXmlValueBool("NeedInsurance");
		this.InsuranceAmt = getXmlValueInt("InsuranceAmt");
		this.Premium = getXmlValueInt("Premium");
		this.CreateTime = getXmlValueStr("CreateTime");
		this.Creator = getXmlValueInt("Creator");
		this.IsCarriageBalance = getXmlValueBool("IsCarriageBalance");
		this.BalanceTime = getXmlValueStr("BalanceTime");
		this.BalanceBy = getXmlValueInt("BalanceBy");
		this.EditLock = getXmlValueBool("EditLock");
		this.EditingUser = getXmlValueInt("EditingUser");
		this.LockTime = getXmlValueStr("LockTime");
		this.ShippingStatus = getXmlValueStr("ShippingStatus");
		this.IsConfirm = getXmlValueBool("IsConfirm");
		this.IsFinished = getXmlValueBool("IsFinished");
		this.AbnormalTime = getXmlValueStr("AbnormalTime");
		return this;*/
    }

    public double getTotalCharge() {
        return TotalCharge;
    }

    public void setTotalCharge(double totalCharge) {
        TotalCharge = totalCharge;
    }

    public boolean isNeedDelivery() {
        return NeedDelivery;
    }

    public void setNeedDelivery(boolean needDelivery) {
        NeedDelivery = needDelivery;
    }

    public Double getDeliveryCharge() {
        return DeliveryCharge;
    }

    public void setDeliveryCharge(Double deliveryCharge) {
        DeliveryCharge = deliveryCharge;
    }

    public int getGoodsAmount() {
        return GoodsAmount;
    }

    public void setGoodsAmount(int goodsAmount) {
        GoodsAmount = goodsAmount;
    }

    public String getFromCity() {
        return FromCity;
    }

    public void setFromCity(String fromCity) {
        FromCity = fromCity;
    }

    public String getToCity() {
        return ToCity;
    }

    public void setToCity(String toCity) {
        ToCity = toCity;
    }

    public int getRecordId() {
        return RecordId;
    }

    public void setRecordId(int recordId) {
        this.RecordId = recordId;
    }

    public String getDocumentNumber() {
        return DocumentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        DocumentNumber = documentNumber;
    }

    public int getShipperCode() {
        return ShipperCode;
    }

    public void setShipperCode(int shipperCode) {
        ShipperCode = shipperCode;
    }

    public String getShipperName() {
        return ShipperName;
    }

    public void setShipperName(String shipperName) {
        ShipperName = shipperName;
    }

    public String getShipperContactName() {
        return ShipperContactName;
    }

    public void setShipperContactName(String shipperContactName) {
        ShipperContactName = shipperContactName;
    }

    public Address getShipperAddress() {
        return ShipperAddress;
    }

    public void setShipperAddress(Address shipperAddress) {
        ShipperAddress = shipperAddress;
    }

    public String getShipperAreaCode() {
        return ShipperAreaCode;
    }

    public void setShipperAreaCode(String shipperAreaCode) {
        ShipperAreaCode = shipperAreaCode;
    }

    public String getShipperPhoneNumber() {
        return ShipperPhoneNumber;
    }

    public void setShipperPhoneNumber(String shipperPhoneNumber) {
        ShipperPhoneNumber = shipperPhoneNumber;
    }

    public boolean isNeedSignUpNotifyMessage() {
        return NeedSignUpNotifyMessage;
    }

    public void setNeedSignUpNotifyMessage(boolean needSignUpNotifyMessage) {
        NeedSignUpNotifyMessage = needSignUpNotifyMessage;
    }

    public int getConsigneeCode() {
        return ConsigneeCode;
    }

    public void setConsigneeCode(int consigneeCode) {
        ConsigneeCode = consigneeCode;
    }

    public String getConsigneeName() {
        return ConsigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        ConsigneeName = consigneeName;
    }

    public String getConsigneeContactPerson() {
        return ConsigneeContactPerson;
    }

    public void setConsigneeContactPerson(String consigneeContactPerson) {
        ConsigneeContactPerson = consigneeContactPerson;
    }

    public Address getConsigneeAddress() {
        return ConsigneeAddress;
    }

    public void setConsigneeAddress(Address consigneeAddress) {
        ConsigneeAddress = consigneeAddress;
    }

    public String getConsigneeAreaCode() {
        return ConsigneeAreaCode;
    }

    public void setConsigneeAreaCode(String consigneeAreaCode) {
        ConsigneeAreaCode = consigneeAreaCode;
    }

    public String getConsigneePhoneNumber() {
        return ConsigneePhoneNumber;
    }

    public void setConsigneePhoneNumber(String consigneePhoneNumber) {
        ConsigneePhoneNumber = consigneePhoneNumber;
    }

    public Network getFromNetwork() {
        return FromNetwork;
    }

    public void setFromNetwork(Network fromNetwork) {
        FromNetwork = fromNetwork;
    }

    public Network getToNetwork() {
        return ToNetwork;
    }

    public void setToNetwork(Network toNetwork) {
        ToNetwork = toNetwork;
    }

    public ShippingMode getShippingMode() {
        return ShippingMode;
    }

    public void setShippingMode(ShippingMode shippingMode) {
        ShippingMode = shippingMode;
    }

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        Weight = weight;
    }

    public double getVolumn() {
        return Volumn;
    }

    public void setVolumn(double volumn) {
        Volumn = volumn;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public double getCarriage() {
        return Carriage;
    }

    public void setCarriage(double carriage) {
        Carriage = carriage;
    }

    public List<ShipperCost> getCarriageItems() {
        return CarriageItems;
    }

    public void setCarriageItems(List<ShipperCost> carriageItems) {
        CarriageItems = carriageItems;
    }

    public String getBalanceMode() {
        return BalanceMode;
    }

    public void setBalanceMode(String balanceMode) {
        BalanceMode = balanceMode;
    }

    public int getMonthlyBalanceAccount() {
        return MonthlyBalanceAccount;
    }

    public void setMonthlyBalanceAccount(int monthlyBalanceAccount) {
        MonthlyBalanceAccount = monthlyBalanceAccount;
    }

    public boolean isNeedInsurance() {
        return NeedInsurance;
    }

    public void setNeedInsurance(boolean needInsurance) {
        NeedInsurance = needInsurance;
    }

    public int getInsuranceAmt() {
        return InsuranceAmt;
    }

    public void setInsuranceAmt(int insuranceAmt) {
        InsuranceAmt = insuranceAmt;
    }

    public double getPremium() {
        return Premium;
    }

    public void setPremium(double premium) {
        Premium = premium;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getPickupBy() {
        return PickupBy;
    }

    public void setPickupBy(String pickupBy) {
        PickupBy = pickupBy;
    }

    public String getDeliveredBy() {
        return DeliveredBy;
    }

    public void setDeliveredBy(String deliveredBy) {
        DeliveredBy = deliveredBy;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public int getCreator() {
        return Creator;
    }

    public void setCreator(int creator) {
        Creator = creator;
    }

    public boolean isIsCarriageBalance() {
        return IsCarriageBalance;
    }

    public void setIsCarriageBalance(boolean isCarriageBalance) {
        IsCarriageBalance = isCarriageBalance;
    }

    public String getBalanceTime() {
        return BalanceTime;
    }

    public void setBalanceTime(String balanceTime) {
        BalanceTime = balanceTime;
    }

    public int getBalanceBy() {
        return BalanceBy;
    }

    public void setBalanceBy(int balanceBy) {
        BalanceBy = balanceBy;
    }

    public boolean isEditLock() {
        return EditLock;
    }

    public void setEditLock(boolean editLock) {
        EditLock = editLock;
    }

    public int getEditingUser() {
        return EditingUser;
    }

    public void setEditingUser(int editingUser) {
        EditingUser = editingUser;
    }

    public String getLockTime() {
        return LockTime;
    }

    public void setLockTime(String lockTime) {
        LockTime = lockTime;
    }

    public String getShippingStatus() {
        return ShippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        ShippingStatus = shippingStatus;
    }

    public boolean isIsConfirm() {
        return IsConfirm;
    }

    public void setIsConfirm(boolean isConfirm) {
        IsConfirm = isConfirm;
    }

    public boolean isIsFinished() {
        return IsFinished;
    }

    public void setIsFinished(boolean isFinished) {
        IsFinished = isFinished;
    }

    public String getAbnormal() {
        return Abnormal;
    }

    public void setAbnormal(String abnormal) {
        Abnormal = abnormal;
    }

    public String getAbnormalTime() {
        return AbnormalTime;
    }

    public void setAbnormalTime(String abnormalTime) {
        AbnormalTime = abnormalTime;
    }
}
