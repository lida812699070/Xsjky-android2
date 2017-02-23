package cn.xsjky.android.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CoordinateBean extends DataSupport {
    private int id;
    private double Longitude;
    private double Latitude;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CoordinateBean{" +
                "Longitude=" + Longitude +
                ", Latitude=" + Latitude +
                '}';
    }

    public static CoordinateBean objectFromData(String str) {

        return new Gson().fromJson(str, CoordinateBean.class);
    }

    public static CoordinateBean objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(key).toString(), CoordinateBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<CoordinateBean> arrayCoordinateBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<CoordinateBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<CoordinateBean> arrayCoordinateBeanFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<CoordinateBean>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(key).toString(), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double Longitude) {
        this.Longitude = Longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double Latitude) {
        this.Latitude = Latitude;
    }
}