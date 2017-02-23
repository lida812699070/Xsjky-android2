package cn.xsjky.android.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.R;
import cn.xsjky.android.util.LogU;

public class AActivity extends Activity {

    private ListView mLv;
    private ArrayList<User> mList;
    private ItemBtnAdapter<User> mAdapter;
    private int start_index;
    private int end_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        mLv = (ListView) findViewById(R.id.lv);
        mList = new ArrayList<>();
        mList.add(new User("user1", "http://img15.3lian.com/2015/c1/83/d/29.jpg"));
        mList.add(new User("user2", "http://img0.pcgames.com.cn/pcgames/1410/09/4411520_7.jpg"));
        mList.add(new User("user3", "http://b.hiphotos.baidu.com/zhidao/pic/item/38dbb6fd5266d016283711a5952bd40735fa3514.jpg"));
        mList.add(new User("user4", "http://pic.baike.soso.com/p/20120714/20120714111118-1620671159.jpg"));
        mList.add(new User("user5", "http://pic12.nipic.com/20110224/6715062_171738237111_2.jpg"));
        mList.add(new User("user6", "http://c.hiphotos.baidu.com/zhidao/pic/item/64380cd7912397dd0bd012915b82b2b7d1a287fe.jpg"));
        mList.add(new User("user7", "http://img.9ku.com/geshoutuji/singertuji/3/31589/31589_10.jpg"));
        mList.add(new User("user8", "http://cdn.duitang.com/uploads/item/201307/22/20130722141354_PiAkC.jpeg"));
        mList.add(new User("user9", "http://img4.duitang.com/uploads/item/201212/26/20121226192117_JQtrn.thumb.600_0.jpeg"));
        mList.add(new User("user10", "http://a3.att.hudong.com/11/15/01300000921826141456155459645_s.jpg"));
        mList.add(new User("user11", "http://images.china.cn/attachement/jpg/site1000/20100905/0019b91ecbef0ded36e72a.jpg"));
        mList.add(new User("user12", "http://pic.87g.com/upload/2016/0302/20160302110114798.jpg"));
        mList.add(new User("user13", "http://www.manhuacheng.com/uploadfile/2013/0115/20130115115017544.jpg"));
        mList.add(new User("user14", "http://pic.87g.com/upload/2016/0302/20160302110105404.jpg"));
        mList.add(new User("user15", "http://img4.duitang.com/uploads/item/201308/14/20130814141332_N8BnH.jpeg"));
        mList.add(new User("user16", "http://pic.87g.com/upload/2016/0310/20160310113633254.jpg"));
        mList.add(new User("user17", "http://t1.niutuku.com/960/43/43-83819.jpg"));
        mList.add(new User("user18", "http://img5.duitang.com/uploads/item/201602/29/20160229174220_4fyEA.jpeg"));
        mList.add(new User("user19", "http://pic.87g.com/upload/2016/0302/20160302110053858.jpg"));
        mList.add(new User("user20", "http://img.article.pchome.net/00/58/83/22/pic_lib/s960x639/Xinshiji02s960x639.jpg"));
        mList.add(new User("user21", "http://f.hiphotos.baidu.com/zhidao/pic/item/3c6d55fbb2fb431678c66f3922a4462308f7d393.jpg"));
        mAdapter = new ItemBtnAdapter<>(this, mList);
        mLv.setAdapter(mAdapter);
        mLv.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 滑动停止
                        LogU.e("滑动停止");
                        BaseApplication.mBitmapUtils.resume();

                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://快速滑动
                        LogU.e("快速滑动");
                        BaseApplication.mBitmapUtils.pause();
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });
    }

    class User {
        private String name;
        private String url;

        public User(String name, String url) {
            this.name = name;
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}

