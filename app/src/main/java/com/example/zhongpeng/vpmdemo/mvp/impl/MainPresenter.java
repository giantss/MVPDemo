package com.example.zhongpeng.vpmdemo.mvp.impl;

import com.example.zhongpeng.vpmdemo.R;
import com.example.zhongpeng.vpmdemo.http.HttpUntil;
import com.example.zhongpeng.vpmdemo.model.Phone;
import com.example.zhongpeng.vpmdemo.mvp.MVPMainView;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class MainPresenter extends BasePresenter {

    MVPMainView mvpMainView;
    Phone mPhone;
    String mUrl = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm";
    public MainPresenter(MVPMainView mainView){
        mvpMainView = mainView;
    }

    public Phone getmPhone() {
        return mPhone;
    }

    public void searchPhoneInfo(String phone){
        if(phone.length() != 11){
            mvpMainView.showToast(mContext.getResources().getString(R.string.search_toast));
            return;
        }
        mvpMainView.showLoading();
        //http请求逻辑
        sendHttp(phone);
    }

    private void sendHttp(String phone){
        Map<String, String> map = new HashMap<String, String>();
        map.put("tel", phone);
        HttpUntil httpUntil = new HttpUntil(new HttpUntil.HttpResponse() {
            @Override
            public void onSuccess(Object object) {
                String json = object.toString();
                int index = json.indexOf("{");
                json = json.substring(index, json.length());
                //使用Gson
                mPhone = parseModelWidthGson(json);

                mvpMainView.hidenLoading();
                mvpMainView.updateView();

            }

            @Override
            public void onFail(String error) {
                mvpMainView.showToast(error);
                mvpMainView.hidenLoading();
            }
        });
        httpUntil.sendGetHttp(mUrl, map, false);
    }

    private Phone parseModelWidthGson(String json){
        Gson gson = new Gson();
        Phone phone = gson.fromJson(json, Phone.class);
        return  phone;
    }

}
