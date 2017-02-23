package cn.xsjky.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.R;
import cn.xsjky.android.constant.SoapAction;
import cn.xsjky.android.constant.SoapEndpoint;
import cn.xsjky.android.constant.SoapInfo;
import cn.xsjky.android.model.CountWeightInfo;
import cn.xsjky.android.model.CustomerSpecification;
import cn.xsjky.android.model.Infos;
import cn.xsjky.android.model.LoginInfo;
import cn.xsjky.android.util.CallBackString;
import cn.xsjky.android.util.CustomerSpecificationXmlParser;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.MySoapHttpRequest;
import cn.xsjky.android.util.RetrueCodeHandler;
import cn.xsjky.android.util.RetruenUtils;

public class CountWeightActivity extends BaseActivity {

    @Bind(R.id.ll_ProductDescriptions)
    LinearLayout mLlProductDescriptions;
    @Bind(R.id.ll_contianer)
    LinearLayout mLlContianer;
    @Bind(R.id.ll_Count)
    LinearLayout mLlCount;
    @Bind(R.id.sumOfCount)
    TextView mSumOfCount;
    @Bind(R.id.sumOfWeight)
    TextView mSumOfWeight;
    @Bind(R.id.ll_Sum)
    LinearLayout mLlSum;
    @Bind(R.id.ll_CountSum)
    LinearLayout mLlCountSum;
    @Bind(R.id.btn_ok)
    Button mBtnOk;
    private ImageView mBack;
    @Bind(R.id.ckRemark)
    CheckBox mCkRemark;
    private TextView mTitle;
    private Double[] mSumVols;
    private Double[] mSumWeights;
    private double mSumWeight;
    private double mSumVol;
    private ArrayList<EditText> mEds;
    private List<CustomerSpecification> mList;
    private ArrayList<TextView> mEdsSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_weight);
        ButterKnife.bind(this);
        mBack = (ImageView) this.findViewById(R.id.head_flip);
        mTitle = (TextView) this.findViewById(R.id.head_title);
        mTitle.setText("计费规格");
        mBack.setImageResource(R.drawable.ic_flipper_head_back);
        mBack.setVisibility(View.VISIBLE);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mSumWeight != 0 && mSumVol != 0) {
                        CountWeightInfo countWeightInfo = new CountWeightInfo();
                        if (mEds != null) {
                            int count = 0;
                            for (EditText e : mEds) {
                                count += Integer.valueOf(e.getText().toString());
                            }
                            countWeightInfo.setCount(count);
                        }
                        countWeightInfo.setCountWeight(mSumWeight);
                        countWeightInfo.setVol(mSumVol);
                        if (mCkRemark.isChecked()) {
                            String remark = "";
                            for (int i = 0; i < mEds.size(); i++) {
                                EditText editText = mEds.get(i);
                                if (TextUtils.isEmpty(editText.getText().toString())) {
                                    remark += mList.get(i).getProductDescription() + ":" + 0;
                                } else {
                                    remark += mList.get(i).getProductDescription() + ":" + editText.getText().toString();
                                }
                            }
                            countWeightInfo.setRemark(remark);
                        }


                        Intent mIntent = new Intent();
                        mIntent.putExtra("result", countWeightInfo);
                        // 设置结果，并进行传送
                        setResult(NewActivity.RESULT_OK, mIntent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        String customerId = getIntent().getStringExtra("customerId");
        if (!TextUtils.isEmpty(customerId)) {
            initData(customerId);
        }
    }

    private void sumCount() {
        if (mSumVols != null && mSumWeights != null) {
            mSumVol = 0;
            for (int i = 0; i < mSumVols.length; i++) {
                if (mSumVols[i] == null) {
                    mSumVols[i] = 0d;
                }
                mSumVol += mSumVols[i];
            }
            mSumOfCount.setText("总体积：" + mSumVol);
            mSumWeight = 0;
            for (int i = 0; i < mSumWeights.length; i++) {
                if (mSumWeights[i] == null) {
                    mSumWeights[i] = 0d;
                }
                mSumWeight += mSumWeights[i];
            }
            mSumOfWeight.setText("总重量：" + mSumWeight);

        }
    }

    private void initData(String customerId) {
        showProgressDialog();
        final String endPoint = SoapEndpoint.SET_IS_SIGNUP;
        final String soapAction = SoapAction.QueryCustomerSpecifications;
        final LoginInfo loginInfo = BaseApplication.loginInfo;
        String info = SoapInfo.QueryCustomerSpecifications;
        info = info.replace("UserIdValue", loginInfo.getUserId() + "");
        info = info.replace("ClientNameValue", Infos.CLIENTNAME);
        info = info.replace("SessionIdValue", loginInfo.getSessionId());
        info = info.replace("customerIdValue", customerId);
        info = info.replace("recordIdValue", "-1");
        final String finalInfo = info;
        MySoapHttpRequest.getString(new CallBackString() {
            @Override
            public void httFinsh(final String data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            closeProgressDialog();
                            LogU.e(data);
                            RetrueCodeHandler returnInfo = RetruenUtils.getReturnInfo(data, new RetrueCodeHandler());
                            assert returnInfo != null;
                            if (returnInfo.getString().equals("0")) {
                                CustomerSpecificationXmlParser xmlParser = RetruenUtils.getReturnInfo(data, new CustomerSpecificationXmlParser());
                                assert xmlParser != null;
                                mList = xmlParser.getList();
                                initView(mList);
                            } else if (returnInfo.getString().equals("-1")) {
                                Tos(returnInfo.getError());
                            } else {
                                Tos("提交失败");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Tos("解析失败");
                        }
                    }
                });

                //mAdapter.notifyDataSetChanged();
                setHandler(HIDE_LOADING, "");

            }
        }, endPoint, soapAction, finalInfo);
    }

    private void initView(final List<CustomerSpecification> list) {
        if (list == null || list.size() == 0) {
            Tos("目前没有该公司的运费规格表");
        } else {
            LogU.e(list.toString());
            //规格的名字
            ArrayList<String> specificationStr = new ArrayList<>();
            mEds = new ArrayList<>();
            mEdsSum = new ArrayList<>();
            specificationStr.add("规格");
            for (CustomerSpecification specification : list) {
                specificationStr.add(specification.getProductDescription());
            }
            for (String info : specificationStr) {
                TextView textView = new TextView(this);
                textView.setGravity(Gravity.CENTER);
                textView.setText(info);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
                textView.setLayoutParams(layoutParams);
                mLlProductDescriptions.addView(textView);
            }
            for (int i = 0; i < specificationStr.size(); i++) {
                if (i == 0) {
                    continue;
                }
                EditText editText = new EditText(this);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
                editText.setLayoutParams(layoutParams);
                mLlCount.addView(editText);
                mEds.add(editText);

                TextView editTextSum = new TextView(this);
                editTextSum.setGravity(Gravity.CENTER);
                editTextSum.setInputType(InputType.TYPE_CLASS_NUMBER);
                LinearLayout.LayoutParams layoutParamsSum = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
                editTextSum.setLayoutParams(layoutParamsSum);
                mLlCountSum.addView(editTextSum);
                mEdsSum.add(editTextSum);
            }

            mSumVols = new Double[mEds.size()];
            mSumWeights = new Double[mEds.size()];
            for (int i = 0; i < mEds.size(); i++) {
                try {
                    final CustomerSpecification specification = list.get(i);
                    final int finalI = i;
                    mEds.get(i).addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            try {
                                String str = s.toString();
                                if (TextUtils.isEmpty(str)) {
                                    str = "0";
                                }
                                int count = Integer.valueOf(str);
                                double sumVol = count * specification.getHeight() * specification.getLength() * specification.getWidth();
                                double sumWeight = count * specification.getWeight();

                                mSumVols[finalI] = sumVol;
                                mSumWeights[finalI] = sumWeight;

                                mEdsSum.get(finalI).setText(sumWeight+"");
                                sumCount();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
