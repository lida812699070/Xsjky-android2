package cn.xsjky.android.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by ${lida} on 2016/6/13.
 */
public class Coordinate extends DataSupport implements Serializable {
    private int id;
    private String Longitude;
    private String Latitude;

    @Override
    public String toString() {
        return "Coordinate{" +
                "Longitude='" + Longitude + '\'' +
                ", Latitude='" + Latitude + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }
}
