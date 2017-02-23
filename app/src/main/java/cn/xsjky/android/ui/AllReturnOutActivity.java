package cn.xsjky.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import cn.xsjky.android.R;
import cn.xsjky.android.model.TruckLoadedCargo;
import cn.xsjky.android.util.LogU;

public class AllReturnOutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_rreturn_out);
        Intent intent = getIntent();
        List<TruckLoadedCargo> list= (List<TruckLoadedCargo>) intent.getSerializableExtra("data");
        //LogU.e("list==="+list.toString());
    }
}
