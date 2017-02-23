package cn.xsjky.android.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by OK on 2016/4/6.
 */
public class DownLoadActors implements Serializable{
    private List<String>list;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
