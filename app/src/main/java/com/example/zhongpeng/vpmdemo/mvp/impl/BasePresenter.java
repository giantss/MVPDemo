package com.example.zhongpeng.vpmdemo.mvp.impl;

import android.content.Context;

public class BasePresenter {
    Context mContext;
    public void attach(Context context){
        mContext = context;
    }
    public void onPause(){}
    public void onResume(){}
    public void onDestroy(){
        mContext = null;
    }
}
