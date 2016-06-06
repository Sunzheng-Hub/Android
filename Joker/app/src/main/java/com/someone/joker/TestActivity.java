package com.someone.joker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Anonymous on 2016/6/5.
 */
public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";
    public static final String ADDRESS = "http://japi.juhe.cn/joke/content/list.from";
    public static final String APPKEY = "068a0d6d2fee153e2cf012d5b279a30e";
    private String currentTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.id_testBtn)
    protected void test(){
        currentTime = String.valueOf((new Date().getTime()) - 100000000000l).substring(0,10);
        String result = null;
        Log.d(TAG, "currentTime:  " + currentTime);
        new HttpUtil(this, ADDRESS, APPKEY, currentTime).sendHttpRequest(new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d(TAG, "onFinish:  " + response);
                new Util().parseJson(response);
            }

            @Override
            public void onError(Exception e) {

            }
        });

        Toast.makeText(this,currentTime,Toast.LENGTH_SHORT)
                .show();

    }

}
