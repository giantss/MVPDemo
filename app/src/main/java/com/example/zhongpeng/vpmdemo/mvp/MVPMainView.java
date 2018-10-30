package com.example.zhongpeng.vpmdemo.mvp;

public interface MVPMainView extends MVPLoadingView {

    void showToast(String msg);
    void updateView();

}
