package cn.xsjky.android.constant;

import cn.xsjky.android.model.WoodenFrame;

/**
 * Created by OK on 2016/3/23.
 */
public class SoapInfo {
    public static final String SET_IS_DEILVERY = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soap:Body>\n" +
            "    <SetIsDelivery xmlns=\"http://www.xsjky.cn/\">\n" +
            "      <loginInfo>\n" +
            "        <UserId>UserIdValue</UserId>\n" +
            "        <ClientName>ClientNameValue</ClientName>\n" +
            "        <SessionId>SessionIdValue</SessionId>\n" +
            "        <RoleData>RoleDataValue</RoleData>\n" +
            "      </loginInfo>\n" +
            "      <documentId>documentIdValue</documentId>\n" +
            "      <returnUpdateRecord>returnUpdateRecordValue</returnUpdateRecord>\n" +
            "    </SetIsDelivery>\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";
    public static final String SET_IS_SIGNUP = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soap:Body>\n" +
            "    <SetIsSignUp xmlns=\"http://www.xsjky.cn/\">\n" +
            "      <loginInfo>\n" +
            "        <UserId>UserIdValue</UserId>\n" +
            "        <ClientName>ClientNameValue</ClientName>\n" +
            "        <SessionId>SessionIdValue</SessionId>\n" +
            "        <RoleData>RoleDataValue</RoleData>\n" +
            "      </loginInfo>\n" +
            "      <documentId>documentIdValue</documentId>\n" +
            "      <returnUpdateRecord>returnUpdateRecordValue</returnUpdateRecord>\n" +
            "      <signName>signNameValue</signName>\n" +
            "      <signUpTime>signUpTimeValue</signUpTime>\n" +
            "      <signUpPictureData>signUpPictureDataValue</signUpPictureData>\n" +
            "      <lat>latValue</lat>\n" +
            "      <lon>lonValue</lon>\n" +
            "    </SetIsSignUp>\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";
    public static final String GET_APPLY_DOCUMENT = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soap:Body>\n" +
            "    <GetApplyDocuments xmlns=\"http://www.xsjky.cn/\">\n" +
            "      <loginInfo>\n" +
            "        <UserId>UserIdValue</UserId>\n" +
            "        <ClientName>ClientNameValue</ClientName>\n" +
            "        <SessionId>SessionIdValue</SessionId>\n" +
            "        <RoleData>RoleDataValue</RoleData>\n" +
            "      </loginInfo>\n" +
            "      <pageNumber>pageNumberValue</pageNumber>\n" +
            "      <pageSize>pageSizeValue</pageSize>\n" +
            "      <sortDesendFlag>sortDesendFlagValue</sortDesendFlag>\n" +
            "    </GetApplyDocuments>\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";
    public static final String QueryCustomerSpecifications="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soap:Body>\n" +
            "    <QueryCustomerSpecifications xmlns=\"http://www.xsjky.cn/\">\n" +
            "        <userId>UserIdValue</userId>\n" +
            "        <clientName>ClientNameValue</clientName>\n" +
            "        <sessionId>SessionIdValue</sessionId>\n" +
            "      <customerId>customerIdValue</customerId>\n" +
            "      <recordId>recordIdValue</recordId>\n" +
            "    </QueryCustomerSpecifications>\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";
    public static final String GETDOCUMENTBYNUM = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soap:Body>\n" +
            "    <GetDocumentByNumber xmlns=\"http://www.xsjky.cn/\">\n" +
            "      <loginInfo>\n" +
            "        <UserId>UserIdValue</UserId>\n" +
            "        <ClientName>ClientNameValue</ClientName>\n" +
            "        <SessionId>SessionIdValue</SessionId>\n" +
            "        <RoleData>RoleDataValue</RoleData>\n" +
            "      </loginInfo>\n" +
            "      <documentNumber>documentNumberValue</documentNumber>\n" +
            "    </GetDocumentByNumber>\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";
    public static final String SaveCustomer = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soap:Body>\n" +
            "    <SaveCustomer xmlns=\"http://www.xsjky.cn/\">\n" +
            "      <securityInfo>\n" +
            "        <UserId>UserIdValue</UserId>\n" +
            "        <ClientName>ClientNameValue</ClientName>\n" +
            "        <SessionId>SessionIdValue</SessionId>\n" +
            "        <RoleData>RoleDataValue</RoleData>\n" +
            "      </securityInfo>\n" +
            "      <customer>\n" +
            "        <CustomerId>CustomerIdValue</CustomerId>\n" +
            "        <CustomerName>CustomerNameValue</CustomerName>\n" +
            "        <Fax>FaxValue</Fax>\n" +
            "        <TelAreaCode>TelAreaCodeValue</TelAreaCode>\n" +
            "        <Tel>TelValue</Tel>\n" +
            "        <ContactPerson>ContactPersonValue</ContactPerson>\n" +
            "        <Remarks>RemarksValue</Remarks>\n" +
            "        <IsMonthlyBalance>true</IsMonthlyBalance>\n" +
            "        <InsuranceRate>0</InsuranceRate>\n" +
            "        <NeedInsuranced>true</NeedInsuranced>\n" +
            "        <Address>AddressValue</Address>\n" +
            "        <Coordinate>\n" +
            "          <Longitude>LongitudeValue</Longitude>\n" +
            "          <Latitude>LatitudeValue</Latitude>\n" +
            "        </Coordinate>\n" +
            "        <IsCustomRate>true</IsCustomRate>\n" +
            "        <OperatorCount>0</OperatorCount>\n" +
            "        <BalancePeriod></BalancePeriod>\n" +
            "        <SendSmsToReceiver>SendSmsToReceiverValue</SendSmsToReceiver>\n" +
            "        <IsForbidden>true</IsForbidden>\n" +
            "      </customer>\n" +
            "    </SaveCustomer>\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";

    public static final String CreateHandoverRecord = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soap:Body>\n" +
            "    <CreateHandoverRecord xmlns=\"http://www.xsjky.cn/\">\n" +
            "      <sessionId>sessionIdValue</sessionId>\n" +
            "      <userId>userIdValue</userId>\n" +
            "      <clientName>clientNameValue</clientName>\n" +
            "      <receiverCode>receiverCodeValue</receiverCode>\n" +
            "      <documents>\n" +
            "documentsValue" +
            "      </documents>\n" +
            "      <downloadActors>\n" +
            "downloadActorsValue" +
            "      </downloadActors>\n" +
            "    </CreateHandoverRecord>\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";

    public static final String GetDriverLoadedDocumentCount = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soap:Body>\n" +
            "    <GetDriverLoadedDocumentCount xmlns=\"http://www.xsjky.cn/\">\n" +
            "      <loginInfo>\n" +
            "        <UserId>UserIdValue</UserId>\n" +
            "        <ClientName>ClientNameValue</ClientName>\n" +
            "        <SessionId>SessionIdValue</SessionId>\n" +
            "        <RoleData>RoleDataValue</RoleData>\n" +
            "      </loginInfo>\n" +
            "    </GetDriverLoadedDocumentCount>\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";
    public static String Handovers = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soap:Body>\n" +
            "    <ApplyHandover xmlns=\"http://www.xsjky.cn/\">\n" +
            "      <sessionId>sessionIdValue</sessionId>\n" +
            "      <userId>userIdValue</userId>\n" +
            "      <clientName>clientNameValue</clientName>\n" +
            "      <handovers>\n" +
            "        <SourceNetwork>SourceNetworkValue</SourceNetwork>\n" +
            "        <SourceTruck>SourceTruckValue</SourceTruck>\n" +
            "        <TakeoverCode>TakeoverCodeValue</TakeoverCode>\n" +
            "        <Documents>\n" +
            "DocumentsValue" +
            "        </Documents>\n" +
            "        <Actors>\n" +
            "ActorsValue" +
            "        </Actors>\n" +
            "      </handovers>\n" +
            "      <returnValue>returnValueValue</returnValue>\n" +
            "    </ApplyHandover>\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";
    public final static String ConfirmReceived = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soap:Body>\n" +
            "    <ConfirmReceived xmlns=\"http://www.xsjky.cn/\">\n" +
            "      <sessionId>sessionIdValue</sessionId>\n" +
            "      <userId>userIdValue</userId>\n" +
            "      <clientName>clientNameValue</clientName>\n" +
            "      <returnValue>returnValueValue</returnValue>\n" +
            "      <handoverId>handoverIdValue</handoverId>\n" +
            "      <actors>\n" +
            "actorsValue" +
            "      </actors>\n" +
            "<receiveNetwork>receiveNetworkValue</receiveNetwork>\n" +
            "      <receiveTruck>receiveTruckValue</receiveTruck>" +
            "    </ConfirmReceived>\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";

    public static String SaveWoodFrames = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soap:Body>\n" +
            "    <SaveWoodenFrames xmlns=\"http://www.xsjky.cn/\">\n" +
            "      <sessionId>sessionIdValue</sessionId>\n" +
            "      <userId>userIdValue</userId>\n" +
            "      <clientName>clientNameValue</clientName>\n" +
            "      <documentNumber>documentNumberValue</documentNumber>\n" +
            "      <saveRecords>\n" +
            "WoodenFramesValue\n" +
            "      </saveRecords>\n" +
            "    </SaveWoodenFrames>\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";
    public final static String GetUserOwnNetwork = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soap:Body>\n" +
            "    <GetUserOwnNetwork xmlns=\"http://www.xsjky.cn/\">\n" +
            "      <loginInfo>\n" +
            "        <UserId>UserIdValue</UserId>\n" +
            "        <ClientName>ClientNameValue</ClientName>\n" +
            "        <SessionId>SessionIdValue</SessionId>\n" +
            "        <RoleData>RoleDataValue</RoleData>\n" +
            "      </loginInfo>\n" +
            "    </GetUserOwnNetwork>\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";
    public final static String GetUserBindTool = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soap:Body>\n" +
            "    <GetUserBindTool xmlns=\"http://www.xsjky.cn/\">\n" +
            "      <loginInfo>\n" +
            "        <UserId>UserIdValue</UserId>\n" +
            "        <ClientName>ClientNameValue</ClientName>\n" +
            "        <SessionId>SessionIdValue</SessionId>\n" +
            "        <RoleData>RoleDataValue</RoleData>\n" +
            "      </loginInfo>\n" +
            "    </GetUserBindTool>\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";
    public final static String GetUserTruck = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soap:Body>\n" +
            "    <GetUserTruck xmlns=\"http://www.xsjky.cn/\">\n" +
            "      <loginInfo>\n" +
            "        <UserId>UserIdValue</UserId>\n" +
            "        <ClientName>ClientNameValue</ClientName>\n" +
            "        <SessionId>SessionIdValue</SessionId>\n" +
            "        <RoleData>RoleDataValue</RoleData>\n" +
            "      </loginInfo>\n" +
            "    </GetUserTruck>\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";
    public final static String GetCustomerReceivers = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soap:Body>\n" +
            "    <GetCustomerReceivers xmlns=\"http://www.xsjky.cn/\">\n" +
            "      <securityInfo>\n" +
            "        <UserId>UserIdValue</UserId>\n" +
            "        <ClientName>ClientNameValue</ClientName>\n" +
            "        <SessionId>SessionIdValue</SessionId>\n" +
            "        <RoleData>RoleDataValue</RoleData>\n" +
            "      </securityInfo>\n" +
            "      <customerId>customerIdValue</customerId>\n" +
            "      <pageNumber>pageNuberValue</pageNumber>\n" +
            "      <pageSize>10</pageSize>\n" +
            "    </GetCustomerReceivers>\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";

}
