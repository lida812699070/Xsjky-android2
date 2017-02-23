package cn.xsjky.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.List;

import cn.xsjky.android.R;

public abstract class BaseRecycleviewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //既有下拉刷新也有上拉加载
    public static final int MODE_BOTH=0;
    //只有下拉刷新
    public static final int MODE_DOWN=1;
    public int mode=1;
    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    //上拉加载更多状态-默认为0
    private int load_more_status = 0;
    private LayoutInflater mInflater;
    protected List<T> mdatas = null;
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private int layoutId;

    public BaseRecycleviewAdapter(Context context, List<T> mdatas, int layoutId) {
        this.mInflater = LayoutInflater.from(context);
        this.mdatas = mdatas;
        this.layoutId=layoutId;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
    abstract ItemViewHolder getItemView(View view);
    /**
     * item显示类型
     *
     * @param parent
     * @param viewType
     * @return
     */
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View  
        if (viewType == TYPE_ITEM) {
            View view = mInflater.inflate(layoutId, parent, false);

            return getItemView(view);
        } else if (viewType == TYPE_FOOTER) {
            View foot_view = mInflater.inflate(R.layout.recycler_load_more_layout, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定  
            //view.setBackgroundColor(Color.RED);
            //可以只要下拉刷新不要上拉加载
            if (mode==MODE_DOWN){
                foot_view.setVisibility(View.GONE);
            }else {
                foot_view.setVisibility(View.VISIBLE);
            }
            FootViewHolder footViewHolder = new FootViewHolder(foot_view);
            return footViewHolder;
        }
        return null;
    }

    /**
     * 数据的绑定显示
     *
     * @param holder
     * @param position
     */
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            bindData(holder, position);
            holder.itemView.setTag(position);
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (load_more_status) {
                case PULLUP_LOAD_MORE:
                    footViewHolder.foot_view_item_tv.setText("已经到达底部...");
                    footViewHolder.foot_view_item_pb.setVisibility(View.GONE);
                    break;
                case LOADING_MORE:
                    footViewHolder.foot_view_item_tv.setText("正在加载更多数据...");
                    footViewHolder.foot_view_item_pb.setVisibility(View.VISIBLE);
                    break;
            }
        }

        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    protected abstract void bindData(RecyclerView.ViewHolder holder, int position);

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    /**
     * 进行判断是普通Item视图还是FootView视图
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return mdatas.size() + 1;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static abstract class ItemViewHolder extends RecyclerView.ViewHolder {
        public ItemViewHolder(View view) {
            super(view);
            findViews(view);
        }
        abstract void findViews(View view);
    }
    /**
     * 底部FootView布局
     */
    public static class FootViewHolder extends RecyclerView.ViewHolder {
        private TextView foot_view_item_tv;
        private ProgressBar foot_view_item_pb;

        public FootViewHolder(View view) {
            super(view);
            foot_view_item_tv = (TextView) view.findViewById(R.id.foot_view_item_tv);
            foot_view_item_pb = (ProgressBar) view.findViewById(R.id.pb);
        }
    }

    //添加数据  
    public void addItem(List<T> newDatas) {
        //mTitles.add(position, data);  
        //notifyItemInserted(position);  
        newDatas.addAll(mdatas);
        mdatas.removeAll(mdatas);
        mdatas.addAll(newDatas);
        notifyDataSetChanged();
    }

    public void clear(){
        mdatas.clear();
        notifyDataSetChanged();
    }
    public void addData(int position, T t) {
        mdatas.add(position, t);
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        if (position == mdatas.size()) {
            return;
        }
        mdatas.remove(position);
        notifyItemRemoved(position);
    }

    public void addMoreItem(List<T> newDatas) {
        mdatas.addAll(newDatas);
        notifyDataSetChanged();
    }

    /**
     * //上拉加载更多
     * PULLUP_LOAD_MORE=0;
     * //正在加载中
     * LOADING_MORE=1;
     * //加载完成已经没有更多数据了
     * NO_MORE_DATA=2;
     *
     * @param status
     */
    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }
}  