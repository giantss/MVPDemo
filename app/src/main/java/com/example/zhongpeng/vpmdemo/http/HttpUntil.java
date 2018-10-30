package com.example.zhongpeng.vpmdemo.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;


import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUntil {

    String mUrl;
    Map<String, String> mParam;
    HttpResponse mHttpResponse;
    private final OkHttpClient mHttpClient = new OkHttpClient();

    Handler handler = new Handler(Looper.getMainLooper());


    public interface HttpResponse{
        void onSuccess(Object object);
        void onFail(String error);
    }

    public HttpUntil(HttpResponse response){
        mHttpResponse = response;
    }

    public void sendPostHttp(String url, Map<String, String> param){

        sendHttp(url, param, true);
    }

    public void sendGetHttp(String url, Map<String, String> param, boolean b){
        sendHttp(url, param, false);
    }

    private void sendHttp(String url, Map<String, String>param, boolean isPost){
        mUrl = url;
        mParam = param;
        //http请求逻辑
        run(isPost);
    }

    private void run(boolean isPost){

        //request请求创建
        final Request request = createRequest(isPost);
        //创建请求队列
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(mHttpResponse != null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mHttpResponse.onFail("请求错误");
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if(mHttpResponse == null)return;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(!response.isSuccessful()){
                            mHttpResponse.onFail("请求失败:code"+request);
                        }else{
                            String json = null;
                            try {
                                json = response.body().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mHttpResponse.onSuccess(json);
                        }
                    }
                });
            }
        });

    }

    private Request createRequest(boolean isPost){
        Request request;
        if(isPost){
            MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder();
            requestBodyBuilder.setType(MultipartBody.FORM);
            Iterator<Map.Entry<String, String>> iterator = mParam.entrySet().iterator();
            while((iterator.hasNext())){
                Map.Entry<String, String> entry = iterator.next();
                requestBodyBuilder.addFormDataPart(entry.getKey(),entry.getValue());
            }
            request = new okhttp3.Request.Builder().url(mUrl).post(requestBodyBuilder.build()).build();

        }else{
            String urlStr = mUrl+"?"+MapParamToString(mParam);
            request = new Request.Builder().url(urlStr).build();
        }
        return request;
    }

    private String MapParamToString(Map<String, String> param){
        StringBuilder stringBuilder = new StringBuilder();

        Iterator<Map.Entry<String, String>> iterator = param.entrySet().iterator();
        while ((iterator.hasNext())){
            Map.Entry<String, String> entry = iterator.next();
            stringBuilder.append(entry.getKey()+"="+entry.getValue()+"&");
        }

        return stringBuilder.toString().substring(0, stringBuilder.length()-1);
    }

}
