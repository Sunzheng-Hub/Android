package com.someone.funnytime.JsonEntity;

/**
 * Author: Anonymous
 * Email : someone_xiaole@sina.com
 * Date  : Created on 2016/6/13
 */
public class Showapi {

    private int showapi_res_code;
    private String showapi_res_error;
    private ShowapiBody showapi_res_body;

    public int getShowapi_res_code() {
        return showapi_res_code;
    }

    public void setShowapi_res_code(int showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public ShowapiBody getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(ShowapiBody showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }
}
