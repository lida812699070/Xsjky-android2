package cn.xsjky.android.model;

/**
 * Created by ${lida} on 2016/5/11.
 */
public class QueryHandoverInfoResult {

    private String FromNetwork;
    private String FromNetworkId;
    private String NetworkDocumentCount;
    private String FromTruck;
    private String TruckLoadedCount;

    @Override
    public String toString() {
        return "QueryHandoverInfoResult{" +
                "FromNetwork='" + FromNetwork + '\'' +
                ", FromNetworkId='" + FromNetworkId + '\'' +
                ", NetworkDocumentCount='" + NetworkDocumentCount + '\'' +
                ", FromTruck='" + FromTruck + '\'' +
                ", TruckLoadedCount='" + TruckLoadedCount + '\'' +
                '}';
    }

    public String getFromNetwork() {
        return FromNetwork;
    }

    public void setFromNetwork(String fromNetwork) {
        FromNetwork = fromNetwork;
    }

    public String getFromNetworkId() {
        return FromNetworkId;
    }

    public void setFromNetworkId(String fromNetworkId) {
        FromNetworkId = fromNetworkId;
    }

    public String getNetworkDocumentCount() {
        return NetworkDocumentCount;
    }

    public void setNetworkDocumentCount(String networkDocumentCount) {
        NetworkDocumentCount = networkDocumentCount;
    }

    public String getFromTruck() {
        return FromTruck;
    }

    public void setFromTruck(String fromTruck) {
        FromTruck = fromTruck;
    }

    public String getTruckLoadedCount() {
        return TruckLoadedCount;
    }

    public void setTruckLoadedCount(String truckLoadedCount) {
        TruckLoadedCount = truckLoadedCount;
    }
}
