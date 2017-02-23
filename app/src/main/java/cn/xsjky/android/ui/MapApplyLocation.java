package cn.xsjky.android.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.navisdk.adapter.BNOuterLogUtil;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.adapter.ItemLbslocationLvAdapter;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.map.DrivingRouteOverlay;
import cn.xsjky.android.model.BastLocationJson;
import cn.xsjky.android.model.LocationInfo;
import cn.xsjky.android.util.DateFormatUtils;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.RetruenUtils;
import cn.xsjky.android.util.getPointXmlParser;

public class MapApplyLocation extends Activity implements OnGetRoutePlanResultListener, AdapterView.OnItemClickListener, View.OnClickListener {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    public static BDLocation bdLocation;
    private boolean isFirstLoc = true;
    private LatLng ll;
    private String name;
    private String date;
    private String shipperStr;
    private String phone;
    private BitmapDescriptor bdicon;
    private Overlay overlay;
    private RoutePlanSearch routePlanSearch;
    public static LatLng location;
    private ListView lvLbsLocations;
    private List<BastLocationJson> list;
    private Button btnShowLocations;
    private LatLng latLng;
    private String point;
    private Button btnOpenLocations;

    private int time = 5;
    public static LatLng start;
    private ArrayList<LatLng> pointList;
    public static List<Activity> activityList = new LinkedList<Activity>();
    private String mSDCardPath;
    private String authinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map_apply_location);
        //clearOverlay();
        lvLbsLocations = (ListView) findViewById(R.id.lv_LBS_locations);
        btnShowLocations = (Button) findViewById(R.id.btn_show_LBSlv);
        btnShowLocations.setBackgroundColor(Color.GRAY);
        btnOpenLocations = (Button) findViewById(R.id.map_open_LBS);
        //getPoints();
        btnOpenLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
                btnOpenLocations.setClickable(false);
                btnOpenLocations.setBackgroundColor(Color.BLUE);
                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (time == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    time = 5;
                                    btnOpenLocations.setClickable(true);
                                    btnOpenLocations.setText("开启导航");
                                    btnOpenLocations.setBackgroundColor(Color.GRAY);
                                    timer.cancel();
                                }
                            });

                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnOpenLocations.setText("" + time-- + "s");
                                }
                            });
                        }

                    }
                }, 0, 1000);
            }
        });

        lvLbsLocations.setOnItemClickListener(this);
        btnShowLocations.setOnClickListener(this);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        date = intent.getStringExtra("date");
        shipperStr = intent.getStringExtra("shipperStr");
        String str = shipperStr;
        //shipperStr = str.replaceAll(" ", "");
        shipperStr = str.replace(" ", "");
        phone = intent.getStringExtra("phone");
        point = intent.getStringExtra("point");

        if (point != null) {
            String[] split = point.split(",");
            if (Double.valueOf(split[0]) == 0 && Double.valueOf(split[1]) == 0) {
                point = null;
            }
        }
        //point = "22.76175,113.827692";
        //LogU.e(name + "-" + date + "-" + shipperStr + "" + phone);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        setLocationMap();
        //poiSearch();
        if (point == null) {
            getBestLocaltion();
        }

        initLbs();
    }

    private void initLbs() {
        BNOuterLogUtil.setLogSwitcher(false);
        if (initDirs()) {
            initNavi();
        }
    }

    private void initNavi() {

        BNOuterTTSPlayerCallback ttsCallback = null;

        /*// 申请权限
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            if (!hasBasePhoneAuth()) {

                this.requestPermissions(authBaseArr, authBaseRequestCode);
                return;

            }
        }*/

        BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败, " + msg;
                }
                MapApplyLocation.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(MapApplyLocation.this, authinfo, Toast.LENGTH_LONG).show();
                    }
                });
            }

            public void initSuccess() {
                Toast.makeText(MapApplyLocation.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                hasInitSuccess = true;
                initSetting();
            }

            public void initStart() {
                Toast.makeText(MapApplyLocation.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
            }

            public void initFailed() {
                Toast.makeText(MapApplyLocation.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
            }

        }, null, ttsHandler, ttsPlayStateListener);

    }


    private void initSetting() {
        BNaviSettingManager
                .setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Novice);
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
    }

    /**
     * 内部TTS播报状态回调接口
     */
    private BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {

        @Override
        public void playEnd() {
            // showToastMsg("TTSPlayStateListener : TTS play end");
        }

        @Override
        public void playStart() {
            // showToastMsg("TTSPlayStateListener : TTS play start");
        }
    };
    /**
     * 内部TTS播报状态回传handler
     */
    private Handler ttsHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
                    // showToastMsg("Handler : TTS play start");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
                    // showToastMsg("Handler : TTS play end");
                    break;
                }
                default:
                    break;
            }
        }
    };

    public void showToastMsg(final String msg) {
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(MapApplyLocation.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private BNOuterTTSPlayerCallback mTTSCallback = new BNOuterTTSPlayerCallback() {

        @Override
        public void stopTTS() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "stopTTS");
        }

        @Override
        public void resumeTTS() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "resumeTTS");
        }

        @Override
        public void releaseTTSPlayer() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "releaseTTSPlayer");
        }

        @Override
        public int playTTSText(String speech, int bPreempt) {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "playTTSText" + "_" + speech + "_" + bPreempt);

            return 1;
        }

        @Override
        public void phoneHangUp() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "phoneHangUp");
        }

        @Override
        public void phoneCalling() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "phoneCalling");
        }

        @Override
        public void pauseTTS() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "pauseTTS");
        }

        @Override
        public void initTTSPlayer() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "initTTSPlayer");
        }

        @Override
        public int getTTSState() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "getTTSState");
            return 1;
        }
    };

    /* @Override
     public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
         // TODO Auto-generated method stub
         super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         if (requestCode == authBaseRequestCode) {
             for (int ret : grantResults) {
                 if (ret == 0) {
                     continue;
                 } else {
                     Toast.makeText(BNDemoMainActivity.this, "缺少导航基本的权限!", Toast.LENGTH_SHORT).show();
                     return;
                 }
             }
             initNavi();
         } else if (requestCode == authComRequestCode) {
             for (int ret : grantResults) {
                 if (ret == 0) {
                     continue;
                 }
             }
             routeplanToNavi(mCoordinateType);
         }

     }*/
    private static final String APP_FOLDER_NAME = "Xsjky-android2";

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    private void getBestLocaltion() {
        String url = BaseSettings.plaseUrl + shipperStr + BaseSettings.plaseUrlParameter;
        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST,
                url,
                //params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject jsonObject = new JSONObject(responseInfo.result);
                            String status = jsonObject.getString("status");
                            if (status.equals("0")) {
                                list = BastLocationJson.arrayBastLocationJsonFromData(jsonObject.getString("results"));
                                if (list.size() == 0) {
                                    //poiSearch();
                                    Toast.makeText(MapApplyLocation.this, "无法找到目的地点", Toast.LENGTH_SHORT).show();
                                    lvLbsLocations.setVisibility(View.GONE);
                                    showLbsLocationFlag = false;
                                    return;
                                }
                                ArrayList<String> strings = new ArrayList<>();
                                for (int i = 0; i < list.size(); i++) {
                                    strings.add(list.get(i).getName());
                                }
                                ItemLbslocationLvAdapter<String> adapter = new ItemLbslocationLvAdapter<>(MapApplyLocation.this, strings);
                                lvLbsLocations.setAdapter(adapter);
                                selectIndex(0);
                                lvLbsLocations.setVisibility(View.VISIBLE);
                                showLbsLocationFlag = true;
                                line();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MapApplyLocation.this, "地址不明确无法定位", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(MapApplyLocation.this, "无法获取数据", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //清除覆盖物
    public void clearOverlay() {
        if (mBaiduMap != null)
            mBaiduMap.clear();
        overlay = null;
    }

    //添加覆盖物
    private void addoverlay(final LatLng latLng) {
        clearOverlay();//因为只需要一个覆盖物，所以我们每次添加前需要清理
        bdicon = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_marka);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)//坐标点
                .icon(bdicon)
                .zIndex(9)//缩放比例
                .draggable(false);//是否可以拖拽
        markerOptions.animateType(MarkerOptions.MarkerAnimateType.none);//设置进入的动画类型
        overlay = mBaiduMap.addOverlay(markerOptions);
    }

    private void drawLocation(LatLng latLng) {
        OverlayOptions markerOptions = new TextOptions()
                .bgColor(Color.RED)
                .fontSize(24)
                .position(latLng)//坐标点
                .text(".")
                .zIndex(9);//缩放比例
        overlay = mBaiduMap.addOverlay(markerOptions);
    }

    private void setLocationMap() {
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //这样设置就会有小箭头指定方向
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null));
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation == null || mMapView == null) {
                    return;
                }
                MyLocationData myLocationData = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())//获取定位半径
                        .direction(100)
                        .latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude())
                        .build();
                mBaiduMap.setMyLocationData(myLocationData);
                MapApplyLocation.bdLocation = bdLocation;
                if (isFirstLoc) {
                    isFirstLoc = false;
                    //返回当前初始位置
                    //addoverlay(bdLocation);
                    backLocation(bdLocation);
                }
            }
        });
        initLocation();
        mLocationClient.start();
    }

    private void backLocation(BDLocation bdLocation) {
        ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(15);

        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        if (point != null) {
            String[] latlngPoint = point.split(",");
            latLng = new LatLng(Double.valueOf(latlngPoint[0]), Double.valueOf(latlngPoint[1]));
            location = latLng;
            line();
            addoverlay(latLng);
        }
    }

    //设置定位服务所需要的服务
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
       /* option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );*///可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 30000;//毫秒定位一次
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
        mBaiduMap.setMyLocationEnabled(false);
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        btnOpenLocations.setText("开启导航");
        btnOpenLocations.setClickable(true);
        btnOpenLocations.setBackgroundColor(Color.GRAY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MapApplyLocation.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        try {
            DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
            //把时间传递类OverLay
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        } catch (Exception e) {
            Toast.makeText(this, "未获取到位置", 1).show();
        }

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    public void search() {
        if (point == null) {
            if (list == null || list.size() == 0) {
                Toast.makeText(MapApplyLocation.this, "当前无目的地", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        //startActivity(new Intent(MapApplyLocation.this, BNDemoMainActivity.class));
        start = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
        NaviParaOption para = new NaviParaOption()
                .startPoint(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude())).endPoint(location)
                .startName("起点").endName("终点");
        try {
            //调起百度地图骑行导航
            //BaiduMapNavigation.openBaiduMapNavi(para, this);

            routeplanToNavi(BNRoutePlanNode.CoordinateType.BD09LL, location, bdLocation);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            //showDialog();
            Toast.makeText(this, "导航错误", Toast.LENGTH_SHORT).show();
        }
    }

    private final static String authComArr[] = {Manifest.permission.READ_PHONE_STATE};

    private boolean hasCompletePhoneAuth() {
        // TODO Auto-generated method stub

        PackageManager pm = this.getPackageManager();
        for (String auth : authComArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private boolean hasInitSuccess = false;
    private boolean hasRequestComAuth = false;

    private void routeplanToNavi(BNRoutePlanNode.CoordinateType coType, LatLng location, BDLocation bdLocation) {
        if (!hasInitSuccess) {
            Toast.makeText(MapApplyLocation.this, "还未初始化!", Toast.LENGTH_SHORT).show();
        }
        // 权限申请
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // 保证导航功能完备
            if (!hasCompletePhoneAuth()) {
                if (!hasRequestComAuth) {
                    hasRequestComAuth = true;
                    //this.requestPermissions(authComArr, authComRequestCode);
                    return;
                } else {
                    Toast.makeText(MapApplyLocation.this, "没有完备的权限!", Toast.LENGTH_SHORT).show();
                }
            }

        }

        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;

        sNode = new BNRoutePlanNode(bdLocation.getLongitude(), bdLocation.getLatitude(), "起点", null, coType);
        eNode = new BNRoutePlanNode(location.longitude, location.latitude, "终点", null, coType);


        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
        }
    }

    public static final String ROUTE_PLAN_NODE = "routePlanNode";

    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            /*
             * 设置途径点以及resetEndNode会回调该接口
             */

            for (Activity ac : activityList) {

                if (ac.getClass().getName().endsWith("BNDemoGuideActivity")) {

                    return;
                }
            }
            Intent intent = new Intent(MapApplyLocation.this, BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            Toast.makeText(MapApplyLocation.this, "算路失败", Toast.LENGTH_SHORT).show();
        }
    }

    public void line() {
        if (bdLocation == null) {
            Toast.makeText(MapApplyLocation.this, "还未定位好，请在次点击", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            routePlanSearch = RoutePlanSearch.newInstance();
            routePlanSearch.setOnGetRoutePlanResultListener(this);
            DrivingRoutePlanOption option = new DrivingRoutePlanOption();
            PlanNode from = PlanNode.withLocation(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude()));
            PlanNode to = PlanNode.withLocation(location);
            option.from(from);
            option.to(to);
            option.policy(DrivingRoutePlanOption.DrivingPolicy.ECAR_DIS_FIRST);
            routePlanSearch.drivingSearch(option);
        } catch (Exception e) {
            Toast.makeText(MapApplyLocation.this, "地址错误无法定位", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        clearOverlay();
        selectIndex(position);
    }

    private void selectIndex(int position) {
        try {
            BastLocationJson.LocationEntity location = list.get(position).getLocation();
            this.location = new LatLng(location.getLat(), location.getLng());
            addoverlay(this.location);
            lvLbsLocations.setVisibility(View.GONE);
            showLbsLocationFlag = false;
            line();
        } catch (Exception e) {
            Toast.makeText(MapApplyLocation.this, "地址不明确无法定位", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean showLbsLocationFlag = true;

    @Override
    public void onClick(View v) {
        if (showLbsLocationFlag) {
            lvLbsLocations.setVisibility(View.GONE);
            showLbsLocationFlag = false;
        } else {
            lvLbsLocations.setVisibility(View.VISIBLE);
            showLbsLocationFlag = true;
        }
    }
}

