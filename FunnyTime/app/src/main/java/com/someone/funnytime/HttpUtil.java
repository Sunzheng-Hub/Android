package com.someone.funnytime;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HttpUtil {

    private static final String TAG = "HttpUtil";

    private String address;
    private String key;
    private String num;
    private Activity activity;

    public HttpUtil(Activity activity,String address,String key,String num){
        this.activity = activity;
        this.address = address;
        this.key = key;
        this.num = num;
    }

    public void sendHttpRequest(final HttpCallbackListener listener) {



        // 检测网络状态，如果有网络，进行http请求
        if (isNetworkAvailable(activity)){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 拼接发送的地址
                    address = address + "?page=" + num;
                    // 打印发送的地址
//                    Log.d(TAG, "Send Address: " + address);

                    BufferedReader reader = null;
                    String result = null;
                    StringBuffer sbf = new StringBuffer();

                    HttpURLConnection conn = null;
                    try {
                        URL url = new URL(address);
                        // 打开链接
                        conn = (HttpURLConnection) url.openConnection();
                        // 设置请求方式
                        conn.setRequestMethod("GET");
                        // 把apikey加入到http请求头中
                        conn.setRequestProperty("apikey",key);
                        // 设置连接最长等待时间
                        conn.setConnectTimeout(8000);
                        // 设置输入流读取完成最长等待时间
                        conn.setReadTimeout(8000);
                        // 设置连接是否可以输入
                        conn.setDoInput(true);
                        // 设置连接是否可以输出
                        conn.setDoOutput(true);
                        // 得到输入流
                        InputStream is = conn.getInputStream();
                        // 将输入流以utf-8格式转成BufferedReader
                        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                        String strRead = null;
                        while ((strRead = reader.readLine()) != null) {
                            sbf.append(strRead);
                            sbf.append("\r\n");
                        }
                        // 释放资源
                        reader.close();
                        is.close();
                        result = sbf.toString();

//                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
//                        StringBuffer buffer = new StringBuffer();
//                        String line;
//                        while ((line = reader.readLine()) != null) {
//                            buffer.append(line);
//                        }
//
                        if (listener != null){
                            //listener.onFinish(buffer.toString());
                            listener.onFinish(result);
                        }


                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        if (listener != null){
                            listener.onError(e);
                        }
                        e.printStackTrace();
                    } finally {
                        if (conn != null) {
                            conn.disconnect();
                        }
                    }
                }
            }).start();
        } else {
            // 没有网络连接，提示用户没有网络连接
            Toast.makeText(activity,"无可用网络连接!",Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * 判断是否有网络连接
     * @param activity 当前活动
     * @return 是否有网络连接
     */
    private boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null){
            return false;
        } else {
            NetworkInfo[] networkInfos = manager.getAllNetworkInfo();
            if (networkInfos != null && networkInfos.length > 0){
                for (int i = 0; i < networkInfos.length; i++) {
                    if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
