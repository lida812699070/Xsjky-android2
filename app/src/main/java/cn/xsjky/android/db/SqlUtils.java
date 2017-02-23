package cn.xsjky.android.db;

import android.text.TextUtils;
import android.widget.Toast;

import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.model.SimpleDocument;
import cn.xsjky.android.ui.MainActivity;
import cn.xsjky.android.util.RetruenUtils;
import cn.xsjky.android.util.SimpleDocumentXmlparser;

/**
 * Created by ${lida} on 2016/8/18.
 */
public class SqlUtils {
    public static boolean ErrorDocumentDel(String strDocuments) {
        try {
            List<ErrorDocument> errorDocuments = BaseApplication.mdbUtils.findAll(ErrorDocument.class);
            if (errorDocuments == null) {
                return false;
            }
            for (int i = 0; i < errorDocuments.size(); i++) {
                if (strDocuments.equals(errorDocuments.get(i).getDocumentNumber())) {
                    BaseApplication.mdbUtils.delete(errorDocuments.get(i));
                    return true;
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean ErrorDocumentUpDate(String strDocuments,ErrorDocument errorDocument) {
        try {
            List<ErrorDocument> errorDocuments = BaseApplication.mdbUtils.findAll(ErrorDocument.class);
            if (errorDocuments == null) {
                errorDocuments = new ArrayList<ErrorDocument>();
            } else {
                int size = errorDocuments.size();
                for (int i = 0; i < size; i++) {
                    if (strDocuments.equals(errorDocuments.get(i).getDocumentNumber())) {
                        errorDocument.setId(errorDocuments.get(i).getId());
                        BaseApplication.mdbUtils.update(errorDocument, "documentcontent");
                        return true;
                    }
                }
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    //订单缓存字符串转成SimpleDocument
    public static SimpleDocument queryErrorDocument(String strDocuments) {
        if (!TextUtils.isEmpty(strDocuments)) {

            try {
                List<ErrorDocument> errorDocuments = BaseApplication.mdbUtils.findAll(ErrorDocument.class);
                if (errorDocuments == null) {
                    return null;
                }
                for (int i = 0; i < errorDocuments.size(); i++) {
                    if (strDocuments.equals(errorDocuments.get(i).getDocumentNumber())) {
                        SimpleDocumentXmlparser xmlparser = RetruenUtils.getReturnInfo(errorDocuments.get(i).getDocumentcontent(), new SimpleDocumentXmlparser());
                        if (xmlparser != null) {
                            SimpleDocument user = xmlparser.getUser();
                            return user;
                        }
                    }
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static ErrorDocument errorDocumentQuery(String strDocuments) {
        if (!TextUtils.isEmpty(strDocuments)) {

            try {
                List<ErrorDocument> errorDocuments = BaseApplication.mdbUtils.findAll(ErrorDocument.class);
                if (errorDocuments == null) {
                    return null;
                }
                for (int i = 0; i < errorDocuments.size(); i++) {
                    if (strDocuments.equals(errorDocuments.get(i).getDocumentNumber())) {
                        return errorDocuments.get(i);
                    }
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
