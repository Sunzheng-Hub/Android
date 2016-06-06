package com.someone.joker;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static final String ADDRESS = "http://japi.juhe.cn/joke/content/list.from";
    public static final String APPKEY = "068a0d6d2fee153e2cf012d5b279a30e";
    private String currentTime;

    @Bind(R.id.id_recyclerView)
    RecyclerView mRecyclerView;

    private List<String> mDatas = new ArrayList<String>();
    private DataAdapter mAdapter;
    private Util mUtil = new Util();
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // 加载笑话显示
        loadJoker();
        // 设置RecyclerView以ListView方式显示
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 设置RecyclerView的Item之间的分隔线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
    }

    // 测试RecyclerView的临时数据
    protected void initData()
    {
        mDatas = new ArrayList<String>();
        for (int i = 'A'; i <= 'z'; i++)
        {
            mDatas.add("" + (char) i);
        }
    }

    private void loadJoker(){
        // 取得当前时间，并进行处理
        currentTime = String.valueOf((new Date().getTime()) - 100000000000l).substring(0,10);
        //        Log.d(TAG, "currentTime:  " + currentTime);
        new HttpUtil(MainActivity.this, ADDRESS, APPKEY, currentTime).sendHttpRequest(new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                Log.d(TAG, "onFinish:  " + response);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDatas = mUtil.parseJson(response);
                        mAdapter = new DataAdapter(MainActivity.this,mDatas);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    /**
     * RecyclerView的数据适配器
     */
    class DataAdapter extends RecyclerView.Adapter<MyViewHolder>{

        private LayoutInflater mInflater;
        private Context mContext;
        private List<String> mDatas;

        public DataAdapter(Context context, List<String> datas) {
            this.mContext = context;
            this.mDatas = datas;
            mInflater = LayoutInflater.from(context);
        }

        // 创建ViewHolder
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.activity_item,parent,false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        // 绑定ViewHolder
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.tv.setText(mDatas.get(position));

        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

    }

    /**
     * 数据适配器的ViewHolder类
     */
    class MyViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.id_text)
        TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
