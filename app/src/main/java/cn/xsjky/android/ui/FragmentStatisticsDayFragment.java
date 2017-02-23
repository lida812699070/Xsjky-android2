package cn.xsjky.android.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.adapter.ItemStatisticsAdapter;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.model.ReceiverStatData;
import cn.xsjky.android.util.DateMonthTime;
import cn.xsjky.android.util.ReceiverStatDataXmlParser;
import cn.xsjky.android.util.RetrueCodeHandler;
import cn.xsjky.android.util.RetruenUtils;


public class FragmentStatisticsDayFragment extends Fragment {

    private PullToRefreshListView mLvDay;
    private ArrayList<ReceiverStatData> mList;
    private ItemStatisticsAdapter<ReceiverStatData> mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics_day, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findviews(view);
        initdata();
    }

    private void initdata() {
        String endTime = null;
        String firstDateForMonth = null;
        try {
            endTime = DateMonthTime.getBeforeLastMonthdate(monthNumber);
            firstDateForMonth = DateMonthTime.getBeforeFirstMonthdate(monthNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert firstDateForMonth != null;
        final String[] splitDate = firstDateForMonth.split("-");
        String url = Urls.GetReceiverStatData;
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionId", BaseApplication.loginInfo.getSessionId());
        params.addBodyParameter("userId", BaseApplication.loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("statUser", "0");
        params.addBodyParameter("period", "Day");
        params.addBodyParameter("beginDate", firstDateForMonth);
        params.addBodyParameter("endDate", endTime);
        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                RetrueCodeHandler parser = RetruenUtils.getReturnInfo(responseInfo.result, new RetrueCodeHandler());
                if (parser != null && parser.getString().equals("0")) {
                    ReceiverStatDataXmlParser returnInfo = RetruenUtils.getReturnInfo(responseInfo.result, new ReceiverStatDataXmlParser());
                    assert returnInfo != null;
                    mList.addAll(returnInfo.getDatas());
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(),splitDate[1]+"月找到"+returnInfo.getDatas().size()+"条数据",Toast.LENGTH_SHORT).show();
                } else if (parser != null && parser.getString().equals("-1")) {
                    Toast.makeText(getActivity(), parser.getError(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "数据获取错误", Toast.LENGTH_SHORT).show();
                }
                mLvDay.onRefreshComplete();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                mLvDay.onRefreshComplete();
            }
        });
    }

    int monthNumber = 0;

    private void findviews(View view) {
        /*HealthyTablesView healthyTablesView= (HealthyTablesView) view.findViewById(R.id.day_fr_ht);
        healthyTablesView.setData(new String[]{"03","03","04","05","06"},new int[]{33,35,24,12,18});*/
        mLvDay = (PullToRefreshListView) view.findViewById(R.id.frament_day_statistics);
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.listview_empty_view, null);
        mLvDay.setEmptyView(inflate);
        mLvDay.setMode(PullToRefreshBase.Mode.BOTH);
        mLvDay.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                monthNumber = 0;
                mList.clear();
                initdata();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                monthNumber--;
                initdata();
            }
        });
        mList = new ArrayList<>();
        mAdapter = new ItemStatisticsAdapter<>(getActivity(), mList);
        mLvDay.setAdapter(mAdapter);
    }

}
