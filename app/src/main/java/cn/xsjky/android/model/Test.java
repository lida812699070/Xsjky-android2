package cn.xsjky.android.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by ${lida} on 2017/3/7.
 */
public class Test extends DataSupport implements Serializable {
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Test{" +
                ", name='" + name + '\'' +
                '}';
    }
}
