package com.example.zhongpeng.vpmdemo;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhongpeng.vpmdemo.model.Phone;
import com.example.zhongpeng.vpmdemo.mvp.MVPMainView;
import com.example.zhongpeng.vpmdemo.mvp.impl.MainPresenter;


public class MainActivity extends FragmentActivity implements View.OnClickListener, MVPMainView {

    EditText input_phone;
    Button btn_search;
    TextView result_phone;
    TextView result_province;
    TextView result_type;
    TextView result_carrier;

    MainPresenter mainPresenter;
    ProgressDialog progressDialog;

    Phone phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPresenter = new MainPresenter(this);
        mainPresenter.attach(this);
        init();
    }

//   @SuppressLint("StringFormatInvalid")
   private void init() {
       input_phone = (EditText)findViewById(R.id.input_phone);
       btn_search = (Button)findViewById(R.id.btn_search);
       result_phone = (TextView)findViewById(R.id.result_phone);
       result_province = (TextView)findViewById(R.id.result_province);
       result_type = (TextView)findViewById(R.id.result_type);
       result_carrier= (TextView)findViewById(R.id.result_carrier);

       result_phone.setText(getString(R.string.result_phone, "xxxx"));
       result_province.setText(getString(R.string.result_province, "xxxx"));
       result_type.setText(getString(R.string.result_type, "xxxx"));
       result_carrier.setText(getString(R.string.result_carrier, "xxxx"));

       btn_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mainPresenter.searchPhoneInfo(input_phone.getText().toString());
    }

    @Override
    public void showToast(String msg) {

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateView() {
        phone = mainPresenter.getmPhone();
        result_phone.setText(getString(R.string.result_phone, phone.getTelString()));
        result_province.setText(getString(R.string.result_province, phone.getProvince()));
        result_type.setText(getString(R.string.result_type, phone.getCatName()));
        result_carrier.setText(getString(R.string.result_carrier, phone.getCarrier()));

    }

    @Override
    public void showLoading() {
        if(progressDialog == null){
            progressDialog = ProgressDialog.show(this, "", "正在加载中...", true, false);
        }else if(progressDialog.isShowing()){
            progressDialog.setTitle("");
            progressDialog.setMessage("正在加载中...");
        }

    }

    @Override
    public void hidenLoading() {
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

}
