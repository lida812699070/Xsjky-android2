package cn.xsjky.android.model;

/**
 * Created by ${lida} on 2016/9/7.
 */
public class SynchronizeData {
    private String ModifyData;
    private String DeleteData;

    @Override
    public String toString() {
        return "SynchronizeData{" +
                "ModifyData='" + ModifyData + '\'' +
                ", DeleteData='" + DeleteData + '\'' +
                '}';
    }

    public String getModifyData() {
        return ModifyData;
    }

    public void setModifyData(String modifyData) {
        ModifyData = modifyData;
    }

    public String getDeleteData() {
        return DeleteData;
    }

    public void setDeleteData(String deleteData) {
        DeleteData = deleteData;
    }
}
