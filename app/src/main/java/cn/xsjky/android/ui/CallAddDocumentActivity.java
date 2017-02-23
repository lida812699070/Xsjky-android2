package cn.xsjky.android.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.R;
import cn.xsjky.android.model.RecordEntity;

public class CallAddDocumentActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_add_document);

        queryCallHistory();
    }

    private void queryCallHistory() {
        ContentResolver contentResolver = this.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(
                    // CallLog.Calls.CONTENT_URI, Columns, null,
                    // null,CallLog.Calls.DATE+" desc");
                    CallLog.Calls.CONTENT_URI, null, null, null,
                    CallLog.Calls.DATE + " desc");
            if (cursor == null)
                return ;
            List<RecordEntity> mRecordList = new ArrayList<RecordEntity>();
            while (cursor.moveToNext()) {
                RecordEntity record = new RecordEntity();
                record.name = cursor.getString(cursor
                        .getColumnIndex(CallLog.Calls.CACHED_NAME));
                record.number = cursor.getString(cursor
                        .getColumnIndex(CallLog.Calls.NUMBER));
                record.type = cursor.getInt(cursor
                        .getColumnIndex(CallLog.Calls.TYPE));
                record.lDate = cursor.getLong(cursor
                        .getColumnIndex(CallLog.Calls.DATE));
                record.duration = cursor.getLong(cursor
                        .getColumnIndex(CallLog.Calls.DURATION));
                record._new = cursor.getInt(cursor
                        .getColumnIndex(CallLog.Calls.NEW));
                Log.e("my", record.toString());
//                      int photoIdIndex = cursor.getColumnIndex(CACHED_PHOTO_ID);
//                      if (photoIdIndex >= 0) {
//                          record.cachePhotoId = cursor.getLong(photoIdIndex);
//                      }

                mRecordList.add(record);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
