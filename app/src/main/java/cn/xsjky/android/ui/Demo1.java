package cn.xsjky.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.lidroid.xutils.exception.DbException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.R;
import cn.xsjky.android.db.Adress;
import cn.xsjky.android.db.DBManager;
import cn.xsjky.android.db.MyAdapter;
import cn.xsjky.android.db.MyListItem;
import cn.xsjky.android.model.CityModel;
import cn.xsjky.android.model.ProvinceModel;

public class Demo1 extends Activity {

    private ListView listView1;
    private ListView listView2;
    private ArrayAdapter<String> adapter1;
    private List<String> list1=new ArrayList<>();
    private List<String> list2=new ArrayList<>();
    private List<String> list3=new ArrayList<>();
    private ArrayAdapter<String> adapter2;
    private List<ProvinceModel> provinceList;
    private int currPro=0;
    private int currCity=0;
    private ListView listView3;
    private ArrayAdapter<String> adapter3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo1);
        initData();
        listView1 = (ListView) findViewById(R.id.lv1);
        listView2 = (ListView) findViewById(R.id.lv2);
        listView3 = (ListView) findViewById(R.id.lv3);
        adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list1);
        adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list2);
        adapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list3);
        listView1.setAdapter(adapter1);
        listView2.setAdapter(adapter2);
        listView3.setAdapter(adapter3);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currPro = position;
                list2.clear();
                ProvinceModel provinceModel = provinceList.get(currPro);
                List<CityModel> cityList = provinceModel.getCityList();
                for (int i = 0; i < cityList.size(); i++) {
                    list2.add(cityList.get(i).getName());
                }
                adapter2.notifyDataSetChanged();
            }
        });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currCity=position;
                list3.clear();
                CityModel cityModel = provinceList.get(currPro).getCityList().get(currCity);
                for (int i = 0; i < cityModel.getDistrictList().size(); i++) {
                    list3.add(cityModel.getDistrictList().get(i).getName());
                }
                adapter3.notifyDataSetChanged();
            }
        });
        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(Demo1.this,list1.get(currPro)+"-"+list2.get(currCity)+"-"+list3.get(position),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Demo1.this, NewActivity.class);
                intent.putExtra("result",list1.get(currPro)+","+list2.get(currCity)+","+list3.get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initData() {
        provinceList = BaseApplication.getApplication().getProvinceList();
        for (int i = 0; i < provinceList.size(); i++) {
            ProvinceModel provinceModel = provinceList.get(i);
            list1.add(provinceModel.getName());
        }

    }

}
