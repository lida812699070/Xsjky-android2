package cn.xsjky.android.http;

import cn.xsjky.android.BaseSettings;

/**
 * Created by OK on 2016/4/1.
 */
public class Urls {
    public static final String CancelDeliveryRequest = "http://"+ BaseSettings.VERSIONS+".xsjky.cn/DeliveryRequest/DeliveryRequestService.asmx/CancelDeliveryRequest";
    public static final String QueryCustomerRequest = "http://"+ BaseSettings.VERSIONS+".xsjky.cn/DeliveryRequest/DeliveryRequestService.asmx/QueryCustomerRequest";
    public static final String GetRequestHandlerCoordinate = "http://"+ BaseSettings.VERSIONS+".xsjky.cn/DeliveryRequest/DeliveryRequestService.asmx/GetRequestHandlerCoordinate";
    public static final String Update = "http://"+ BaseSettings.VERSIONS+".xsjky.cn/addressbooks.asmx/Update";
    public static final String FindInProgressRequest = "http://"+ BaseSettings.VERSIONS+".xsjky.cn/DeliveryRequest/DeliveryRequestService.asmx/FindInProgressRequest";
    public static final String GetDefault = "http://"+ BaseSettings.VERSIONS+".xsjky.cn/addressbooks.asmx/GetDefault";
    public static final String SetDefault = "http://"+ BaseSettings.VERSIONS+".xsjky.cn/addressbooks.asmx/SetDefault";
    public static final String New = "http://"+ BaseSettings.VERSIONS+".xsjky.cn/addressbooks.asmx/New";
    public static final String Delete = "http://"+ BaseSettings.VERSIONS+".xsjky.cn/addressbooks.asmx/Delete";
    public static final String Query = "http://"+ BaseSettings.VERSIONS+".xsjky.cn/addressbooks.asmx/Query";
    public static final String GetMarkNames = "http://"+ BaseSettings.VERSIONS+".xsjky.cn/DeliveryRequest/DeliveryRequestService.asmx/GetMarkNames";
    public static final String ResetPassword = "http://" + BaseSettings.VERSIONS + ".xsjky.cn/securityservice.asmx/ResetPassword";
    public static final String ChangePassword = "http://" + BaseSettings.VERSIONS + ".xsjky.cn/securityservice.asmx/ChangePassword";
    public static final String GetDocumentPicturesByNumber = "http://"+ BaseSettings.VERSIONS+".xsjky.cn/LogisticsServer.asmx/GetDocumentPicturesByNumber";
    public static final String GetCustomerShippers = "http://" + BaseSettings.VERSIONS + ".xsjky.cn/logisticsserver.asmx/GetCustomerShippers";
    public static final String DocumentNumberExist = "http://" + BaseSettings.VERSIONS + ".xsjky.cn/logisticsserver.asmx/DocumentNumberExist";
    public static final String ApplyAuthenticode = "http://" + BaseSettings.VERSIONS + ".xsjky.cn/LogisticsServer.asmx/ApplyAuthenticode";
    public static final String strUrl = "http://" + BaseSettings.VERSIONS + ".xsjky.cn/LogisticsServer.asmx/ReportCoordinate";
    public static final String AppLogin = "http://" + BaseSettings.VERSIONS + ".xsjky.cn/LogisticsServer.asmx/AppLogin";
    public static final String GetLatestVersion = "http://" + BaseSettings.VERSIONS + ".xsjky.cn/Update/AppUpdateService.asmx/GetLatestVersion";
    public static final String DURLGetHandOverRecords = "http://" + BaseSettings.VERSIONS + ".xsjky.cn/Handovers/Handoverservice.asmx/QueryHandoverRecords";
    public static final String GetDriverLoadedDocuments = "http://" + BaseSettings.VERSIONS + ".xsjky.cn/LogisticsServer.asmx/GetDriverLoadedDocuments";
    public static final String GetDriverLoadedDocument = "http://" + BaseSettings.VERSIONS + ".xsjky.cn/LogisticsServer.asmx/GetHandoverRecord";
    public static final String QueryReceiveHandOverRecords = "http://" + BaseSettings.VERSIONS + ".xsjky.cn/Handovers/Handoverservice.asmx/QueryTakeoverRecords";
    public static final String GetUnfinishRequest = "http://" + BaseSettings.VERSIONS + ".xsjky.cn/deliveryRequest/DeliveryRequestService.asmx/GetUnfinishRequest";
    public static final String CompleteRequest = "http://" + BaseSettings.VERSIONS + ".xsjky.cn/deliveryRequest/DeliveryRequestService.asmx/CompleteRequest";
    public static final String RejectRequest = "http://" + BaseSettings.VERSIONS + ".xsjky.cn/DeliveryRequest/DeliveryRequestService.asmx/RejectRequest";
    public static final String AcceptRequest = "http://" + BaseSettings.VERSIONS + ".xsjky.cn/DeliveryRequest/DeliveryRequestService.asmx/AcceptRequest";
    public static final String QueryTrackData = "http://" + BaseSettings.VERSIONS + ".xsjky.cn/locationTest.asmx/QueryTrackData";
    public static final String GetTruckLoadedDocuments = "http://" + BaseSettings.VERSIONS + ".xsjky.cn/Handovers/Handoverservice.asmx/GetTruckLoadedDocuments";
    public static final String QueryHandoverInfo = "http://" + BaseSettings.VERSIONS + ".xsjky.cn/Handovers/Handoverservice.asmx/QueryHandoverInfo";
    public static final String GetNetworkDocuments = "http://" + BaseSettings.VERSIONS + ".xsjky.cn/Handovers/Handoverservice.asmx/GetNetworkDocuments";
    public static final String ApplyHandover = "http://" + BaseSettings.VERSIONS + ".xsjky.cn/Handovers/Handoverservice.asmx/ApplyHandover";
    public static final String GetCargoInfos= "http://"+ BaseSettings.VERSIONS+".xsjky.cn/DeliveryRequest/DeliveryRequestService.asmx/GetCargoInfos";
    public static final String QueryCustomers="http://" + BaseSettings.VERSIONS + ".xsjky.cn/LogisticsServer.asmx/QueryCustomers";
    public static final String QueryDocument = "http://"+ BaseSettings.VERSIONS+".xsjky.cn/logisticsserver.asmx/QueryDocument";
    public static final String SynchronizeData="http://" + BaseSettings.VERSIONS + ".xsjky.cn/LogisticsServer.asmx/SynchronizeData";
    public static final String GetHandleCustomers="http://" + BaseSettings.VERSIONS + ".xsjky.cn/LogisticsServer.asmx/GetHandleCustomers";
    public static final String GetDocumentByNumber="http://" + BaseSettings.VERSIONS + ".xsjky.cn/LogisticsServer.asmx/GetDocumentByNumber";
    public static final String UploadSignupPicture="http://" + BaseSettings.VERSIONS + ".xsjky.cn/LogisticsServer.asmx/UploadSignupPicture";
    public static final String GetDocumentWoodenFrames="http://" + BaseSettings.VERSIONS + ".xsjky.cn/LogisticsServer.asmx/GetDocumentWoodenFrames";
    public static final String GetReceiverStatData="http://" + BaseSettings.VERSIONS + ".xsjky.cn/Reports.asmx/GetReceiverStatData";
}
