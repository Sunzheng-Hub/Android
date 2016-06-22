package com.someone.funnytime.JsonEntity;

import java.util.List;

/**
 * Author: Anonymous
 * Email : someone_xiaole@sina.com
 * Date  : Created on 2016/6/13
 */
public class ShowapiBody {

    private int allPages;
    private int ret_code;
    private List<Content> contentlist;
    private int currentPage;
    private int allNum;
    private int maxResult;

    public int getAllPages() {
        return allPages;
    }

    public void setAllPages(int allPages) {
        this.allPages = allPages;
    }

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public List<Content> getContentlist() {
        return contentlist;
    }

    public void setContentlist(List<Content> contentlist) {
        this.contentlist = contentlist;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getAllNum() {
        return allNum;
    }

    public void setAllNum(int allNum) {
        this.allNum = allNum;
    }

    public int getMaxResult() {
        return maxResult;
    }

    public void setMaxResult(int maxResult) {
        this.maxResult = maxResult;
    }
}
