package cn.xsjky.android.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NotNull;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Unique;

/**
 * Created by ${lida} on 2016/8/17.
 */
@Table(name = "tbl_errorDocument")
public class ErrorDocument {

    public ErrorDocument(){
        this.IsPrint=false;
    }
    @Id
    private int id;

    @Column
    @NotNull
    private String documentNumber;


    @Column
    @NotNull
    private String documentcontent;

    @Column
    private boolean IsPrint = false;

    @Override
    public String toString() {
        return "ErrorDocument{" +
                ", documentNumber='" + documentNumber + '\'' +
                ", documentcontent='" + documentcontent + '\'' +
                '}';
    }

    public boolean isPrint() {
        return IsPrint;
    }

    public void setIsPrint(boolean isPrint) {
        IsPrint = isPrint;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocumentcontent() {
        return documentcontent;
    }

    public void setDocumentcontent(String documentcontent) {
        this.documentcontent = documentcontent;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
}
