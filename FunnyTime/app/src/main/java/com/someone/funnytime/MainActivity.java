package com.someone.funnytime;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.orhanobut.logger.Logger;
import com.someone.funnytime.JsonEntity.Content;
import com.someone.funnytime.JsonEntity.Showapi;
import com.xw.repo.refresh.PullListView;
import com.xw.repo.refresh.PullToRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@TargetApi(Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity implements
        PullToRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainActivity";
    private String httpUrl = "http://apis.baidu.com/showapi_open_bus/showapi_joke/joke_text";
    private String pagenum = "1";
    private String apikey = "fd36f59b4d11378856311ec9a321d4c0";

    @Bind(R.id.id_pullListView) PullListView mPullListView;
    @Bind(R.id.id_pullToRefreshLayout) PullToRefreshLayout mRefreshLayout;

    private List<String> mStrings;
    private ListAdapter mAdapter;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Logger.init(TAG);

        mStrings = new ArrayList<>();
        mRefreshLayout.setOnRefreshListener(this);

        loadTextByOkHttp(pagenum);
//        loadTextByHttpUtil(pagenum);

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {

                pagenum = "1";
                mStrings.clear();
                loadTextByOkHttp(pagenum);
//                loadTextByHttpUtil(pagenum);

                mRefreshLayout.refreshFinish(true);
                updateListData();
            }
        }, 2000); // 2秒后刷新
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {

                pagenum = String.valueOf(Integer.parseInt(pagenum) + 1);
                loadTextByOkHttp(pagenum);
//                loadTextByHttpUtil(pagenum);

                mRefreshLayout.loadMoreFinish(true);
                updateListData();

            }
        }, 2000); // 2秒后刷新
    }

    private void updateListData() {
        if (mAdapter == null) {
            mAdapter = new ListAdapter(this, mStrings);
            mPullListView.setAdapter(mAdapter);
        } else {
            mAdapter.updateListView(mStrings);
        }
    }

    // 通过自己封装的Http类去加载内容
    private void loadTextByOkHttp(final String num){
        // 开启线程进行网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 创建OkHttpClient实例
                OkHttpClient client = new OkHttpClient();
                // 拼接地址
                String url = httpUrl + "?page=" + num;
                // 设置http请求，添加请求头
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("apikey",apikey)
                        .build();
                // http请求的反馈
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    // 把反馈的内容转换成String
                    String json = response.body().string();
                    // 格式化Json打印日志
                    Logger.json(json);
                    // 使用fastjson把Json转成实体类
                    Showapi showapi = JSON.parseObject(json, Showapi.class);
                    // 把新的数据添加到数据源中
                    for (Content content: showapi.getShowapi_res_body().getContentlist()) {
                        // 处理字符串，去除特殊字符
                        String text = getString(content.getText());
                        mStrings.add(text);
                    }
                    // 返回主线程更新UI
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter = new ListAdapter(MainActivity.this, mStrings);
                            mPullListView.setAdapter(mAdapter);
                            if (Integer.parseInt(num) > 1){
                                mPullListView.setSelection(((Integer.parseInt(pagenum)-1)*20-1));
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    // 处理Json中的特殊字符
    private String getString(String text) {

        if (text.contains("<p>\r\n\t\t")){
            text = text.replace("<p>\r\n\t\t","");
        }
        if (text.contains("</p>")){
            text = text.replace("</p>","");
        }
        if (text.contains("<p>")){
            text = text.replace("<p>","");
        }
        if (text.contains("\t")){
            text = text.replace("\t","");
        }
        return text;
    }

    // 通过自己封装的Http类去加载内容
    private void loadTextByHttpUtil(final String num){

        new HttpUtil(MainActivity.this,httpUrl,apikey,num).sendHttpRequest(new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                // 使用fastjson把Json转成实体类
                Showapi showapi = JSON.parseObject(response, Showapi.class);
                // 把新的数据添加到数据源中
                for (Content content: showapi.getShowapi_res_body().getContentlist()) {
                    // 处理字符串，去除特殊字符
                    String text = getString(content.getText());
                    mStrings.add(text);
                }
                // 返回主线程更新UI
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new ListAdapter(MainActivity.this, mStrings);
                        mPullListView.setAdapter(mAdapter);
                        if (Integer.parseInt(num) > 1){
                            mPullListView.setSelection(((Integer.parseInt(pagenum)-1)*20-1));
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

}
