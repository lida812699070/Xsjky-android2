package cn.xsjky.android.model;

/**
 * Created by ${lida} on 2016/5/5.
 */
public class LocationInfo {
    private String Lon;
    private String Lat;

    @Override
    public String toString() {
        return "LocationInfo{" +
                "Lon='" + Lon + '\'' +
                ", Lat='" + Lat + '\'' +
                '}';
    }

    public String getLon() {
        return Lon;
    }

    public void setLon(String lon) {
        Lon = lon;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }
}
