package com.someone.funnytime;

public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);
}
