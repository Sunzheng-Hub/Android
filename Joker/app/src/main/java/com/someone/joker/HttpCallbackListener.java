package com.someone.joker;

/**
 * Created by Anonymous on 2016/3/29.
 */
public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);
}
