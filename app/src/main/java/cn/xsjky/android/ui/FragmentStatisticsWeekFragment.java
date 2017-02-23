package cn.xsjky.android.ui;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jq.printer.esc.Image;

import java.util.ArrayList;
import java.util.Arrays;

import cn.xsjky.android.R;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.view.HealthyTablesView;


public class FragmentStatisticsWeekFragment extends Fragment implements View.OnClickListener {


    private SwipeRefreshLayout mSwipeLayout;
    private static final int REFRESH_COMPLETE = 0X110;
    private static final int INITPOINT = 0X111;
    private ArrayList<ImageView> mPoints;
    private HealthyTablesView mHealthyTablesView;
    private RelativeLayout mRl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics_week, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findviews(view);
        mPoints = new ArrayList<>();
    }

    private void findviews(View view) {
        mHealthyTablesView = (HealthyTablesView) view.findViewById(R.id.week_fr_ht);
        mRl = (RelativeLayout) view.findViewById(R.id.rl);
        mHealthyTablesView.setData(new String[]{"02", "03", "04"}, new int[]{33, 35, 24});
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.id_swipe_ly);
        mSwipeLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
            }
        });
        mHandler.sendEmptyMessageDelayed(INITPOINT, 500);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initPoint() {
        for (int i = 0; i < mHealthyTablesView.getPoints().size(); i++) {
            if (mPoints.size() == mHealthyTablesView.getPoints().size()) {
                LogU.e("size = " + mPoints.size());
                return;
            }
            String strPoint = mHealthyTablesView.getPoints().get(i);
            String[] split = strPoint.split(",");
            int x = Integer.valueOf(split[0].substring(0, split[0].indexOf(".")));
            int y = Integer.valueOf(split[1].substring(0, split[1].indexOf(".")));
            final ImageView imgPoint = new ImageView(getActivity());
            imgPoint.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.icon_point));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = x - 30 - 8;
            layoutParams.topMargin = y - 30 - 10;
            mRl.addView(imgPoint, layoutParams);
            mPoints.add(imgPoint);
            imgPoint.setPadding(30, 30, 30, 30);
            imgPoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    otherGone();
                    imgPoint.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.icon_point));
                }
            });
            imgPoint.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.icon_point_gray));
        }
    }

    private void otherGone() {
        for (int i = 0; i < mPoints.size(); i++) {
            mPoints.get(i).setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.icon_point_gray));
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    mHealthyTablesView.getPoints().clear();
                    mHealthyTablesView.setData(new String[]{"02", "03", "04", "05"}, new int[]{19, 33, 35, 24});
                    mSwipeLayout.setRefreshing(false);
                    mHandler.sendEmptyMessageDelayed(INITPOINT, 500);
                    break;
                case INITPOINT:
                    clearPoint();
                    mPoints.clear();
                    initPoint();
                    break;

            }
        }
    };

    private void clearPoint() {
        if (mPoints != null && mPoints.size() != 0){
            for (int i = 0; i < mPoints.size(); i++) {
                mRl.removeView(mPoints.get(i));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}
