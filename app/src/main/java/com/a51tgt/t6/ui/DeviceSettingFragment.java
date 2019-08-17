package com.a51tgt.t6.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a51tgt.t6.MZApplication;
import com.a51tgt.t6.R;
import com.a51tgt.t6.abstract_face.OnNoticeUI;
import com.a51tgt.t6.bean.DeviceInfo;
import com.a51tgt.t6.bean.DevicePackageInfo;
import com.a51tgt.t6.bean.FlowProductInfo;
import com.a51tgt.t6.bean.HttpResponseData;
import com.a51tgt.t6.bluetooth.BluetoothChatService;
import com.a51tgt.t6.bluetooth.BluetoothUtil;
import com.a51tgt.t6.comm.APIConstants;
import com.a51tgt.t6.comm.TcpConfig;
import com.a51tgt.t6.net.OkHttpClientManager;
import com.a51tgt.t6.net.SendRequest;
import com.a51tgt.t6.service.MainService;
import com.a51tgt.t6.ui.view.BottomPopupWindow;
import com.a51tgt.t6.ui.view.OtherWiFiActivity;
import com.a51tgt.t6.utils.CommUtil;
import com.a51tgt.t6.utils.TipUtil;
import com.a51tgt.t6.net.TcpUtil;
import org.json.JSONObject;

/**
 * Created by liu_w on 2017/9/13.
 */

public class DeviceSettingFragment extends Fragment implements View.OnClickListener, OnNoticeUI {

    private TextView tv_sn, tv_translate_language, tv_tip_language,tv_blockSwitch;
    private LinearLayout ll_modify_hotspot_password, ll_reset,ll_set_apn,
            ll_set_blacklist, ll_set_translate_language,
            ll_flow_orders, ll_set_language, ll_card_settings, ll_set_device_language;
    private LinearLayout ll_instruction, ll_software_version, ll_about_us,ll_product;
    private com.a51tgt.t6.ui.DeviceSetDialog setDialog = new com.a51tgt.t6.ui.DeviceSetDialog();

    public DeviceSettingFragment(){

    }
    public static DeviceSettingFragment newInstance(){
        DeviceSettingFragment fragment = new DeviceSettingFragment();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        tv_sn = rootView.findViewById(R.id.tv_sn);
        tv_blockSwitch = rootView.findViewById(R.id.blockSwitch);
        ll_modify_hotspot_password = rootView.findViewById(R.id.ll_modify_hotspot_password);
        ll_set_apn = rootView.findViewById(R.id.ll_set_apn);
        ll_set_blacklist = rootView.findViewById(R.id.ll_set_blacklist);
        ll_card_settings = rootView.findViewById(R.id.ll_card_setting);
        ll_reset = rootView.findViewById(R.id.ll_reset);
        ll_flow_orders = rootView.findViewById(R.id.ll_flow_orders);
        ll_set_device_language = rootView.findViewById(R.id.ll_set_device_language);
//      ll_detection_device = rootView.findViewById(R.id.ll_detection_device);
        ll_set_language = rootView.findViewById(R.id.ll_set_language);
        ll_software_version  = rootView.findViewById(R.id.ll_software_version);
        ll_about_us  = rootView.findViewById(R.id.ll_about_us);
        ll_product  = rootView.findViewById(R.id.ll_about_product);
        ll_instruction  = rootView.findViewById(R.id.ll_instruction);

        if(APIConstants.deviceInfo != null){
            tv_sn.setText(APIConstants.sn);
        }
        try {
            if (!APIConstants.deviceInfo.isBlockSwitch() &&
                    APIConstants.deviceInfo != null){
                tv_blockSwitch.setText(R.string.title_saveflow_close);
            }
            else {
                tv_blockSwitch.setText(R.string.title_saveflow_open);
            }
        } catch (Exception E){
        }



//        if (APIConstants.deviceInfo.)
//tv_blockSwitch.setText();
        ll_modify_hotspot_password.setOnClickListener(this);
        ll_set_apn.setOnClickListener(this);
        ll_set_blacklist.setOnClickListener(this);
        ll_reset.setOnClickListener(this);
        ll_card_settings.setOnClickListener(this);
        ll_flow_orders.setOnClickListener(this);
        tv_blockSwitch.setOnClickListener(this);
//        ll_detection_device.setOnClickListener(this);
        ll_set_language.setOnClickListener(this);
        ll_software_version.setOnClickListener(this);
        ll_about_us.setOnClickListener(this);
        ll_product.setOnClickListener(this);
        ll_instruction.setOnClickListener(this);
        ll_set_device_language.setOnClickListener(this);


        if(APIConstants.deviceInfo != null && TextUtils.isEmpty(APIConstants.deviceInfo.getAppVersion())){
            MZApplication.getInstance().setOnNoticeUiListener(this, OnNoticeUI.KEY_TYPE_DEVICE_SETTING_FRAGMENT);

            OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[1];
//
            PackageManager packageManager = getActivity().getPackageManager();
            PackageInfo packInfo = null;
            try {
                String str =   getContext().getPackageName();

                packInfo = packageManager.getPackageInfo(str,0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            String version = packInfo.versionName;
            params[0] = new OkHttpClientManager.Param("ver",version);
            Log.i("xiazaidizhi","====="+"t6_"+version);

            new SendRequest(APIConstants.Get_FYJApp_Info, params, new MyHandler(), 1);
        }

        return rootView;
    }
    //版本号

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
            if(APIConstants.deviceInfo != null){
                tv_sn.setText(APIConstants.sn);
            }
        } else {
            //相当于Fragment的onPause
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_modify_hotspot_password:
            case R.id.ll_set_apn:
            case R.id.ll_set_device_language:
            case R.id.blockSwitch:
            case R.id.ll_set_blacklist:
            case R.id.ll_connect_wifi:
            case R.id.ll_card_setting:
            case R.id.ll_reset:
//                if (MB.getState() != BluetoothChatService.STATE_CONNECTED) {
//                if(TcpUtil.getInstance().socketTemp==null){
                if(APIConstants.socket==null){

                    String STR;
                    if (!APIConstants.alertInfo.equals("")){

                       STR =  APIConstants.alertInfo;
                    }else {
                        STR =getActivity().getResources().getText(R.string.error_no_device_connect).toString();
                    }

                    TipUtil.showAlert(getActivity(),
                            getActivity().getResources().getText(R.string.tip_title).toString(),
                           STR,
                            getActivity().getResources().getText(R.string.commit_button).toString(),
                            new TipUtil.OnAlertSelected() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    ((com.a51tgt.t6.ui.MainActivity)getActivity()).mViewPager.setCurrentItem(0);
                                    dialog.dismiss();
                                }
                            });
                    return;
                }
                break;
        }

        switch (v.getId()){
            case R.id.ll_set_language:

                Intent intentLan = new Intent(getActivity(), OtherWiFiActivity.class);

                getActivity().startActivity(intentLan);

                break;
            case R.id.ll_modify_hotspot_password:
//                if(APIConstants.isBluetoothConnection)
                    setDialog.ModifyDeviceHotSpotPasswordDialog(getActivity());
                break;
            case R.id.ll_set_apn:
//                if(APIConstants.isBluetoothConnection) //未完成蓝牙方式

                    setDialog.SetApnDialog(getActivity());
//                BluetoothUtil.getInstance().sendMessage(TcpConfig.BT_APN_QUERY);

                break;

            case R.id.ll_set_blacklist:
                Intent intent1 = new Intent(getActivity(), com.a51tgt.t6.ui.SaveFlowActivity.class);

                getActivity().startActivity(intent1);
                break;
//                if(APIConstants.isBluetoothConnection) //未完成蓝牙方式
//                   setDialog.SetBlackListDialog(getActivity());
//               break;
            case R.id.ll_card_setting:
                Intent cardIntent = new Intent(getActivity(), com.a51tgt.t6.ui.SIMCardActivity.class);
                startActivity(cardIntent);
                break;

            case R.id.ll_reset:
                Intent productIntent = new Intent(getActivity(), com.a51tgt.t6.ui.ReductionActivity.class);
                startActivity(productIntent);
                break;

            case R.id.ll_set_device_language:
                Intent deviceLanguageIntent = new Intent(getActivity(), com.a51tgt.t6.ui.DeviceLanguageActivity.class);
                startActivity(deviceLanguageIntent);
                break;

            case R.id.ll_connect_wifi:

                break;

            case R.id.ll_flow_orders:
                Intent intent_device_orders = new Intent(getActivity(), com.a51tgt.t6.ui.DeviceOrdersActivity.class);
                getActivity().startActivity(intent_device_orders);
                break;

            case R.id.ll_software_version:
//                if(APIConstants.isBluetoothConnection)
                    setDialog.showVersionInfo(getActivity());
                break;
            case R.id.ll_about_us:
                Intent intent_about_us = new Intent(getActivity(), com.a51tgt.t6.ui.AboutUsActivity.class);
                getActivity().startActivity(intent_about_us);
                break;
            case R.id.ll_about_product:
                Intent intent_product = new Intent(getActivity(), com.a51tgt.t6.ui.ProductActivity.class);
                getActivity().startActivity(intent_product);
                break;
            case R.id.ll_instruction:
                Intent intent_instruction = new Intent(getActivity(), com.a51tgt.t6.ui.InstructionCateActivity.class);
                getActivity().startActivity(intent_instruction);
                break;
            default:
                break;
        }
    }

    private void showOptions(View view, int arrayId) {
        BottomPopupWindow optionsWindow = new BottomPopupWindow(getActivity());
        String[] options = getResources().getStringArray(arrayId);
        optionsWindow.showOptions(view, options, createListener(arrayId));
    }

    private BottomPopupWindow.OnOptionsSelectedListener createListener(int arrayId) {
        BottomPopupWindow.OnOptionsSelectedListener listener = null;
        final String[] options = getResources().getStringArray(arrayId);
        switch (arrayId) {
            case R.array.translate_language:
                listener = new BottomPopupWindow.OnOptionsSelectedListener() {
                    @Override
                    public void onOptionSelected(int index) {
                        tv_translate_language.setText(options[index]);
                    }
                };
                break;
            case R.array.tip_language:
                listener = new BottomPopupWindow.OnOptionsSelectedListener() {
                    @Override
                    public void onOptionSelected(int index) {
                        int type=0;
                        if (index==0){
                            type=2;
                        }else if (index==1){
                            type=3;
                        }else if (index==2){
                            type=1;
                        }
                        tv_tip_language.setText(options[index]);
                        JSONObject object = new JSONObject();
                        try {
                            object.put(TcpConfig.SET_DEVICE_LANGUAGE, type);
                            if(APIConstants.isBluetoothConnection)
                            BluetoothUtil.getInstance().sendMessage(object.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                break;
            default:
                break;
        }
        return listener;
    }

    @Override
   public void onNotice(int NOTICE_TYPE, Object object) {
        switch (NOTICE_TYPE){
            case OnNoticeUI.NOTICE_TYPE_NET_CHANGE:
             String   curSsid = object.toString();
                String strNew = APIConstants.sn.replaceAll("[a-zA-Z]","").replace("*","");
                Log.i("SGEGEWGWEGEHGWE:", curSsid+"   "+strNew);
//|| object.toString().contains(deviceSSID.replace("TGT","TUGE"))
                if (!TextUtils.isEmpty(strNew) && (object.toString().contains(strNew) )&&APIConstants.deviceInfo!=null) {


//                    Log.i("SGEGEWGWEGEHGWE11111:", curSsid+"   "+deviceSSID);

//                    startMainservice();
//                    int status = ll_device_connecting.getVisibility();
                }else  {
                    TcpUtil.getInstance().closeSocket();

                    APIConstants.socket =null;
                    APIConstants.alertInfo="";

                    TcpUtil.getInstance().socketTemp=null;

                }
                    break;

            case 34:
                tv_blockSwitch.setText(APIConstants.deviceInfo.isBlockSwitch()?R.string.title_saveflow_open:R.string.title_saveflow_close);
                break;

            case 44:
                Toast.makeText(MZApplication.getInstance(),object.toString().contains("true")? "设置设备语言成功":"设置设备语言失败",Toast.LENGTH_SHORT).show();
                break;

            case OnNoticeUI.NOTICE_TYPE_CLOSE_WIFIAP:
            case NOTICE_TYPE_SET_BLACK_LIST:
                tv_blockSwitch.setText(APIConstants.deviceInfo.isBlockSwitch()?R.string.title_saveflow_open:R.string.title_saveflow_close);
                break;

            case OnNoticeUI.NOTICE_TYPE_BT_APN_QUERY:
                setDialog.RefreshAPNUi(object);
                break;
                case  OnNoticeUI.NOTICE_TYPE_SET_WIFIANDPASSWORD:
                    Toast.makeText(MZApplication.getInstance(), R.string.tip_operation_success, Toast.LENGTH_SHORT).show();

                    break;

            case NOTICE_TYPE_SET_APN:
                String value = object == null ? "" : (String)object;
                if(value.equals("true")) {
                    Toast.makeText(MZApplication.getInstance(), R.string.tip_operation_success, Toast.LENGTH_SHORT).show();
                    //在修改密码成功后，将ApiConstants.deviceInfo中的密码修改
                    if(NOTICE_TYPE == OnNoticeUI.NOTICE_TYPE_SET_WIFIANDPASSWORD && APIConstants.deviceInfo != null && !TextUtils.isEmpty(APIConstants.newPassword)){
                        APIConstants.deviceInfo.setPassword(APIConstants.newPassword);
                    }
                }
                else
                    Toast.makeText(MZApplication.getInstance(), R.string.tip_operation_failed, Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void showAlert(String deviceSSID){
        TipUtil.showAlert(getActivity(),
                getActivity().getResources().getText(R.string.tip_title).toString(),
                "Please connect device WiFi",
                getActivity().getResources().getText(R.string.commit_button).toString(),
                new TipUtil.OnAlertSelected() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
//        TipUtil.showAlert(getActivity(),
//                getActivity().getResources().getText(R.string.tip_title).toString(),
//                "Please connect WiFi name:"+deviceSSID,
//                getActivity().getResources().getText(R.string.commit_button).toString(),
//                new TipUtil.OnAlertSelected() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        dialog.dismiss();
//                    }
//                });
    }

    public void startMainservice() {
        try {
            Intent intent = new Intent(getActivity(), MainService.class);
            getActivity().startService(intent);

        } catch (Exception E) {
        }
    }

    int setDeviceLanguageInt;

    private void setDevicelanguage() {
        final String items[] = {"简体中文", "英文","日文"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // 先得到构造器
        builder.setTitle("设置语言"); // 设置标题
        setDeviceLanguageInt = 2;
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            setDeviceLanguageInt = 2;
                        } else if (which == 1) {
                            setDeviceLanguageInt = 3;
                        } else if (which == 2) {
                            setDeviceLanguageInt = 4;
                        }
                    }
                });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                JSONObject object = new JSONObject();
                try {
                    object.put(TcpConfig.SET_DEVICE_LANGUAGE, setDeviceLanguageInt);
                    if(APIConstants.isBluetoothConnection){
                        BluetoothUtil.getInstance().sendMessage(object.toString());
                    } //未完成蓝牙方式

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (msg == null || msg.obj == null) {
                return;
            }
            if(msg.what == -10){
                return;
            }
            HttpResponseData responseData = new HttpResponseData((String) msg.obj);
            if (responseData == null || responseData.status < 0 || responseData.data == null) {
                return;
            }
            switch (msg.what){
                case 1:
                    Log.i("responsedevicesetting2",responseData.toString());
//                    APIConstants.deviceVersion = "T4_V2.1.6";
//                    responseData.data.get("new_version").toString();
                    if(responseData.data.containsKey("new_version") && !TextUtils.isEmpty(responseData.data.get("new_version").toString())){
                        APIConstants.deviceVersion = responseData.data.get("new_version").toString();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
