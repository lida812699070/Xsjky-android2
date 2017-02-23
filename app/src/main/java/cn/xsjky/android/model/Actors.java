package cn.xsjky.android.model;

/**
 * Created by OK on 2016/4/7.
 */
public class Actors {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Actors{" +
                "name='" + name + '\'' +
                '}';
    }
}
