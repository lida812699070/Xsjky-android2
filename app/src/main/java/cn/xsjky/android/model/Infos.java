package cn.xsjky.android.model;

import cn.xsjky.android.BaseSettings;

/**
 * Created by OK on 2016/3/22.
 */
public class Infos {
    public static final String CLIENTNAME = BaseSettings.CLIENT_NAME;

    public static final String QueryTraceData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soap:Body>\n" +
            "    <QueryTraceData xmlns=\"http://www.xsjky.cn/\">\n" +
            "      <sessionId>sessionIdValue</sessionId>\n" +
            "      <userId>userIdValue</userId>\n" +
            "      <clientName>clientNameValue</clientName>\n" +
            "      <queryNumbers>\n" +
            "        <string>stringValue</string>\n" +
            "      </queryNumbers>\n" +
            "    </QueryTraceData>\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";
    public static final String GetDocumentByNumber = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soap:Body>\n" +
            "    <GetDocumentByNumber xmlns=\"http://www.xsjky.cn/\">\n" +
            "      <loginInfo>\n" +
            "        <UserId>UserIdValue</UserId>\n" +
            "        <ClientName>ClientNameValue</ClientName>\n" +
            "        <SessionId>SessionIdValue</SessionId>\n" +
            "        <RoleData>0</RoleData>\n" +
            "      </loginInfo>\n" +
            "      <documentNumber>documentNumberValue</documentNumber>\n" +
            "    </GetDocumentByNumber>\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";

    public static final String DeleteCustomer = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soap:Body>\n" +
            "    <DeleteCustomer xmlns=\"http://www.xsjky.cn/\">\n" +
            "      <securityInfo>\n" +
            "        <UserId>UserIdValue</UserId>\n" +
            "        <ClientName>ClientNameValue</ClientName>\n" +
            "        <SessionId>SessionIdValue</SessionId>\n" +
            "        <RoleData>0</RoleData>\n" +
            "      </securityInfo>\n" +
            "      <customerId>customerIdValue</customerId>\n" +
            "    </DeleteCustomer>\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";
    public static final String DELIVERABLE = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soap:Body>\n" +
            "    <GetDeliverableDocuments xmlns=\"http://www.xsjky.cn/\">\n" +
            "      <loginInfo>\n" +
            "        <UserId>UserIdValue</UserId>\n" +
            "        <ClientName>ClientNameValue</ClientName>\n" +
            "        <SessionId>SessionIdValue</SessionId>\n" +
            "        <RoleData>0</RoleData>\n" +
            "      </loginInfo>\n" +
            "      <returnData>returnDataValue</returnData>\n" +
            "    </GetDeliverableDocuments>\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";
    public static final String SEND_LOCATION = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soap:Body>\n" +
            "    <ReportCoordinate xmlns=\"http://www.xsjky.cn/\">\n" +
            "      <loginInfo>\n" +
            "        <UserId>UserIdValue</UserId>\n" +
            "        <ClientName>ClientNameValue</ClientName>\n" +
            "        <SessionId>SessionIdValue</SessionId>\n" +
            "        <RoleData>RoleDataValue</RoleData>\n" +
            "      </loginInfo>\n" +
            "      <longitude>longitudeValue</longitude>\n" +
            "      <latitude>latitudeValue</latitude>\n" +
            "      <province>provinceValue</province>\n" +
            "      <city>cityValue</city>\n" +
            "      <district>null</district>\n" +
            "      <address>null</address>\n" +
            "    </ReportCoordinate>\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";
}
