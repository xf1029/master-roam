package com.a51tgt.t6.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.a51tgt.t6.MZApplication;
import com.a51tgt.t6.R;
import com.a51tgt.t6.bean.LocaleUtils;
import com.a51tgt.t6.bluetooth.BluetoothUtil;
import com.a51tgt.t6.comm.APIConstants;
import com.a51tgt.t6.comm.TcpConfig;
import com.a51tgt.t6.service.MainService;
import com.a51tgt.t6.ui.BaseActivity;
import com.a51tgt.t6.ui.MainActivity;
import com.a51tgt.t6.utils.AppLanguageUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class DeviceLanguageActivity extends BaseActivity implements View.OnClickListener{

    ImageView iv_set_chinese,iv_set_english, iv_lan_back;

    int langInt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_language);
        iv_set_chinese = findViewById(R.id.iv_set_chinese);
        iv_set_english = findViewById(R.id.iv_set_english);
        iv_lan_back = findViewById(R.id.iv_lan_back);
        iv_lan_back.setOnClickListener(this);
        iv_set_chinese.setOnClickListener(this);
        iv_set_english.setOnClickListener(this);
        initImage();
    }


    public void setLanguage(int setDeviceLanguageInt){
        JSONObject object = new JSONObject();
        try{
            object.put(TcpConfig.SET_DEVICE_LANGUAGE, setDeviceLanguageInt);
            MainService.getInstance().addRequest(object.toString());
        }catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        Toast.makeText(this,getResources().getString(R.string.tip_operation_success),Toast.LENGTH_SHORT).show();
    }

    public void initImage(){
        iv_set_english.setImageResource(R.mipmap.gray);
        iv_set_chinese.setImageResource(R.mipmap.gray);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_lan_back:
                finish();
                break;

            case R.id.iv_set_chinese:
                findViewById(R.id.btn_set_lang_ok).setEnabled(true);
                findViewById(R.id.btn_set_lang_ok).setAlpha(1);
                iv_set_chinese.setImageResource(R.mipmap.red);
                iv_set_english.setImageResource(R.mipmap.gray);
                langInt = 2;
                break;

            case R.id.iv_set_english:
                findViewById(R.id.btn_set_lang_ok).setEnabled(true);
                findViewById(R.id.btn_set_lang_ok).setAlpha(1);
                iv_set_english.setImageResource(R.mipmap.red);
                iv_set_chinese.setImageResource(R.mipmap.gray);
                langInt = 3;
                break;

            case R.id.btn_set_lang_ok:
                if (langInt != 0){
                    setLanguage(langInt);
                }else {

                    Toast.makeText(this,"请选择语言",Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                return;
        }
    }
}
