package com.someone.funnytime;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: Anonymous
 * Email : someone_xiaole@sina.com
 * Date  : Created on 2016/6/15
 */
public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";
    private String httpUrl = "http://apis.baidu.com/showapi_open_bus/showapi_joke/joke_text";
    private String pagenum = "1";
    private String apikey = "fd36f59b4d11378856311ec9a321d4c0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @OnClick(R.id.id_testBtn)
    protected void test(){

        new Thread(new Runnable() {
            @Override
            public void run() {

                Logger.init(TAG);

                OkHttpClient client = new OkHttpClient();
                String url = "http://apis.baidu.com/showapi_open_bus/showapi_joke/joke_text?page=1";
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("apikey",apikey)
                        .build();
                try (Response response = client.newCall(request).execute()){
                    String json = response.body().string();
//                    Log.d(TAG, "test: " + response.body().string());
                    Logger.json(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();



    }



}
