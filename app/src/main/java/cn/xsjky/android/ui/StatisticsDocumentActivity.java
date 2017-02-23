package cn.xsjky.android.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import cn.xsjky.android.R;

public class StatisticsDocumentActivity extends AppCompatActivity {


    private RadioGroup mRb;
    private Fragment[] mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_document);
        findViewById();
    }

    private Fragment currFragment;

    private void findViewById() {
        mFragments = new Fragment[2];
        mRb = (RadioGroup) findViewById(R.id.statistics_rg);
        mRb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (currFragment != null) {
                    transaction.hide(currFragment);
                }
                switch (checkedId) {
                    case R.id.statistics_rb_day:
                        if (mFragments[0] == null) {
                            mFragments[0] = new FragmentStatisticsDayFragment();
                            transaction.add(R.id.statistics_fl,mFragments[0]);
                        }
                        currFragment = mFragments[0];
                        break;
                 /*   case R.id.statistics_rb_week:
                        if (mFragments[1] == null) {
                            mFragments[1] = new FragmentStatisticsWeekFragment();
                            transaction.add(R.id.statistics_fl,mFragments[1]);
                        }
                        currFragment = mFragments[1];
                        break;*/
                    case R.id.statistics_rb_month:
                        if (mFragments[1] == null) {
                            mFragments[1] = new FragmentStatisticsMonthFragment();
                            transaction.add(R.id.statistics_fl,mFragments[1]);
                        }
                        currFragment = mFragments[1];
                        break;
                }
                transaction.show(currFragment);
                transaction.commit();
            }
        });
        mRb.getChildAt(0).performClick();
    }
}
