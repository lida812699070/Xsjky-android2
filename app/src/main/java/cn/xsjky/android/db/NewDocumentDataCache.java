package cn.xsjky.android.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NotNull;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by ${lida} on 2016/8/18.
 */
@Table(name = "tbl_NewDocumentDataCache")
public class NewDocumentDataCache {
    @Id
    private int id;

    @Column
    private String documentInit;
    @Column
    private String initShippingModeCache;

    @Override
    public String toString() {
        return "NewDocumentDataCache{" +
                "id=" + id +
                "initShippingModeCache=" + initShippingModeCache +
                ", documentInit='" + documentInit + '\'' +
                '}';
    }

    public String getInitShippingModeCache() {
        return initShippingModeCache;
    }

    public void setInitShippingModeCache(String initShippingModeCache) {
        this.initShippingModeCache = initShippingModeCache;
    }

    public String getDocumentInit() {
        return documentInit;
    }

    public void setDocumentInit(String documentInit) {
        this.documentInit = documentInit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
