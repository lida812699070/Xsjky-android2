package cn.xsjky.android.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.xsjky.android.R;

public class StatBarDemoActivity extends AppCompatActivity {

    private View mDecorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_bar_demo);
        mDecorView = getWindow().getDecorView();
        mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getActionBar().hide();
    }
}
