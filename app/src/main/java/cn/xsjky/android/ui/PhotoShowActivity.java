package cn.xsjky.android.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;

import cn.xsjky.android.R;
import cn.xsjky.android.util.BitmapUtils;

public class PhotoShowActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_show);
        ImageView ivPhoto= (ImageView) findViewById(R.id.iv_show_photo);
        ivPhoto.setImageBitmap(BitmapFactory.decodeFile(ActivityDocumentDetailActivity.mFilePath));
    }
}
