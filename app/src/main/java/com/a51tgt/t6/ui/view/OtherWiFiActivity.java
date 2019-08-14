package com.a51tgt.t6.ui.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.a51tgt.t6.MZApplication;
import com.a51tgt.t6.R;
import com.a51tgt.t6.bean.LocaleUtils;
import com.a51tgt.t6.bluetooth.BluetoothUtil;
import com.a51tgt.t6.comm.APIConstants;
import com.a51tgt.t6.comm.TcpConfig;
import com.a51tgt.t6.ui.BaseActivity;
import com.a51tgt.t6.ui.MainActivity;
import com.a51tgt.t6.utils.AppLanguageUtils;

import org.w3c.dom.Text;

import java.util.Locale;

public class OtherWiFiActivity extends BaseActivity implements View.OnClickListener{

    private ImageView iv_back;
    private ImageView tv_chinese,tv_english;
    private Button btn_set;
    String lan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        iv_back = (ImageView) findViewById(R.id.iv_back1);
        iv_back.setOnClickListener(this);
        tv_chinese = findViewById(R.id.iv_set_dev_lang_chinese);
        tv_english= findViewById(R.id.iv_set_dev_lang_english);
        btn_set = findViewById(R.id.but_set_dev_lang_ok);
        btn_set.setOnClickListener(this);
        tv_chinese.setOnClickListener(this);
        tv_english.setOnClickListener(this);
        Locale curLocale = getResources().getConfiguration().locale;
        SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
        String name = preferences.getString("lang","");//getSt


        TextView tv_version = findViewById(R.id.tv_version);

        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(this.getPackageName(),0);
            String version = packInfo.versionName;
            Log.e("hello", getResources().getString(R.string.app_name));
            tv_version.setText(getResources().getString(R.string.app_name)+"\n"+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (name.equals("")) {
            if (curLocale.equals(Locale.SIMPLIFIED_CHINESE)) {
                tv_chinese.setImageResource(R.mipmap.red);

            } else {

                tv_english.setImageResource(R.mipmap.red);
                Log.d("语言与啊哈哈哈", Locale.getDefault().getLanguage());

            }
        }else{

            if (name.equals("zh")) {
                tv_chinese.setImageResource(R.mipmap.red);

            } else {

                tv_english.setImageResource(R.mipmap.red);

                Log.d("语言与啊哈哈哈", Locale.getDefault().getLanguage());

            }

        }
//        tv_name = (EditText) findViewById(R.id.tv_name);
//        tv_password = (EditText) findViewById(R.id.tv_password);
//        iv_back.setOnClickListener(this);
//        btn_join = (Button)findViewById(R.id.btn_joinWifi);
//        btn_join.setOnClickListener(this);
    }


    private void reloadPageAfterChangeLanguage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (MZApplication.getContext() instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) MZApplication.getContext() ;
                mainActivity.recreate();
            }

        } else {
//            EventBus.getDefault().post(new AppEvent.ChangeLanguage());
//            ChangeLanguagePage.this.removeAllViews();
//            initView(mContext);
//            checkItem();
        }
    }
    private void onChangeAppLanguage(String newLanguage) {
        Intent intentLan = new Intent();
        intentLan.setAction(APIConstants.BR_LAN_STATUS);
//        intent.putExtra("order_status", true);
        sendBroadcast(intentLan);
//        finish();
//        getc
        APIConstants.currentLan = newLanguage;

        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("lang",newLanguage);
        editor.commit();
        Log.i("当前语言", String.valueOf(APIConstants.socket));

        AppLanguageUtils.changeAppLanguage(this, newLanguage);
        AppLanguageUtils.changeAppLanguage(MZApplication.getContext(), newLanguage);
//        reloadPageAfterChangeLanguage();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        APIConstants.isReStartService = true;

    }

    void setAppLang(){
        if (lan.equals("zh")) {

            if (LocaleUtils.needUpdateLocale(this, LocaleUtils.LOCALE_CHINESE)) {
                LocaleUtils.updateLocale(this, LocaleUtils.LOCALE_CHINESE);
                restartAct();
                APIConstants.currentLan = "zh";
            }
        }else {

        if (LocaleUtils.needUpdateLocale(this, LocaleUtils.LOCALE_ENGLISH)) {
                LocaleUtils.updateLocale(this, LocaleUtils.LOCALE_ENGLISH);
                restartAct();
                APIConstants.currentLan = "en";
            }
        }
}


    private void restartAct() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
//        Intent _Intent = new Intent(this, MainActivity.class);
//        startActivity(_Intent);
//        //清除Activity退出和进入的动画
        overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.iv_back1:

                finish();

                break;
            case R.id.iv_set_dev_lang_chinese:

                tv_chinese.setImageResource(R.mipmap.red);
                tv_english.setImageResource(R.mipmap.gray);
                lan = "zh";

                break;
            case  R.id.iv_set_dev_lang_english:
                tv_chinese.setImageResource(R.mipmap.gray);
                tv_english.setImageResource(R.mipmap.red);
                lan = "en";

                break;

            case R.id.but_set_dev_lang_ok:
//setAppLang();
                onChangeAppLanguage(lan);
                break;



        }



    }
}
