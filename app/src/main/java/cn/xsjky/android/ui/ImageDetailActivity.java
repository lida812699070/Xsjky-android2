package cn.xsjky.android.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.model.DocumentPicture;
import cn.xsjky.android.util.DocumentPictureXmlparser;
import cn.xsjky.android.util.RetruenUtils;

public class ImageDetailActivity extends BaseActivity {

    private String document;
    private ImageView ivPic;
    private int currindex = 0;
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        initToolbar("查看图片");
        Intent intent = getIntent();
        document = intent.getStringExtra("document");
        Button btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmaps.size() == 0) {
                    Tos("没有图片");
                    return;
                }
                getData();
            }
        });
        ivPic = (ImageView) findViewById(R.id.iv_pic);
        getData();
    }

    private int pageNum = 1;

    private void getData() {
        String url = Urls.GetDocumentPicturesByNumber;
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionId", BaseApplication.loginInfo.getSessionId());
        params.addBodyParameter("userId", BaseApplication.loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("pageNumber", pageNum++ + "");
        params.addBodyParameter("pageSize", "10");
        params.addBodyParameter("documentNumber", document);
        showProgressDialog();
        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST,
                url,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        DocumentPictureXmlparser parser = RetruenUtils.getReturnInfo(responseInfo.result, new DocumentPictureXmlparser());
                        if (parser != null) {
                            ArrayList<DocumentPicture> list = parser.getList();
                            bitmaps = new ArrayList<>();
                            for (int i = 0; i < list.size(); i++) {
                                Bitmap bitmap = base64ToBitmap(list.get(i).getIamgeData());
                                bitmaps.add(bitmap);
                            }
                            if (bitmaps.size() != 0) {
                                //vpPic.setAdapter(new PicPagerAdapter(bitmaps, DocumentDetailActivity.this));
                                ivPic.setImageBitmap(bitmaps.get(0));
                            } else {
                                Tos("无图片");
                            }
                        }
                        closeProgressDialog();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Tos(msg);
                        closeProgressDialog();
                    }
                });
    }

    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
