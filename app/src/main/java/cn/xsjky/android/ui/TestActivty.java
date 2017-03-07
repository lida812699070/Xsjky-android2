package cn.xsjky.android.ui;

import android.app.Activity;
import android.os.Bundle;

import java.util.List;

import cn.xsjky.android.R;
import cn.xsjky.android.model.Test;
import cn.xsjky.android.util.LogU;

public class TestActivty extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_activty);
        Test test = new Test();
        test.setName("111");
        test.save();

        List<Test> all = Test.findAll(Test.class);

        LogU.e(all.toString());
    }
}
