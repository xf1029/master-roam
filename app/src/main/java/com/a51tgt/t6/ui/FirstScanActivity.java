package com.a51tgt.t6.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.a51tgt.t6.MZApplication;
import com.a51tgt.t6.R;
import com.a51tgt.t6.bean.DeviceInfo;
import com.a51tgt.t6.bean.DeviceInfoForUserData;
import com.a51tgt.t6.bean.HttpResponseData;
import com.a51tgt.t6.bean.UserDataUtils;
import com.a51tgt.t6.comm.APIConstants;
import com.a51tgt.t6.net.OkHttpClientManager;
import com.a51tgt.t6.net.SendRequest;
import com.a51tgt.t6.service.MainService;
import com.a51tgt.t6.ui.view.DeviceInfoView;
import com.a51tgt.t6.utils.CommUtil;
import com.a51tgt.t6.utils.NetWorkUtils;
import com.a51tgt.t6.utils.TipUtil;

import com.znq.zbarcode.CaptureActivity;
import java.util.ArrayList;
import java.util.Map;


public class FirstScanActivity extends com.a51tgt.t6.ui.BaseActivity {

	private String deviceMacAddress = "", alertInfo="";
	private String deviceSN = "";
	private Context mContext;
	private ScrollView sv_device, sv_introduce;
	private LinearLayout ll_device;
	private TextView tv_menu;
	private boolean isEdit = false;
	private WifiManager wifiManager;
	private Dialog mWeiboDialog;

	private boolean quit = false;
	private com.a51tgt.t6.ui.DeviceSetDialog setDialog = new com.a51tgt.t6.ui.DeviceSetDialog();

	//接收广播的处理
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			FirstScanActivity.this.recreate();
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}

	@SuppressLint("InflateParams") @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPermission();

//		Toast.makeText(this,string,Toast.LENGTH_LONG).show();

		PackageManager packageManager =  getPackageManager();
		PackageInfo packInfo = null;
		try {
			String str = this.getPackageName();
			packInfo = packageManager.getPackageInfo(str,0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		String version = packInfo.versionName;
		OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[1];
		params[0] = new OkHttpClientManager.Param("ver","t6_"+version);
//		new SendRequest(APIConstants.Get_FYJApp_Info, params, new MyHandler(), 1);
		//注册接受的广播
		IntentFilter filter = new IntentFilter(APIConstants.BR_LAN_STATUS);
		registerReceiver(broadcastReceiver, filter);

		setContentView(R.layout.activity_first_scan);

//		CommUtil.setStatusBarBackgroundColor(FirstScanActivity.this);
		mContext = FirstScanActivity.this;
		sv_device = (ScrollView) findViewById(R.id.sv_device);
		sv_introduce = (ScrollView) findViewById(R.id.sv_introduce);
		ll_device = (LinearLayout) findViewById(R.id.ll_device);
		tv_menu = (TextView) findViewById(R.id.tv_menu);

		findViewById(R.id.connect_Scan).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				int net_type = NetWorkUtils.getAPNType(mContext);
				if(net_type == 0 || net_type == 2){
					Toast.makeText(mContext, R.string.error_network_is_not_available, Toast.LENGTH_LONG).show();
				}
				else{

					Intent intent = new Intent(mContext, CaptureActivity.class);
					startActivityForResult(intent, APIConstants.SCANNING_REQUEST_CODE);
				}
			}
		});
		findViewById(R.id.connect_Text).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				int net_type = NetWorkUtils.getAPNType(mContext);
				if(net_type == 0 || net_type == 2){
					Toast.makeText(mContext, R.string.error_network_is_not_available, Toast.LENGTH_LONG).show();
				}
				else{

					final EditText inputServer = new EditText(FirstScanActivity.this);
					AlertDialog.Builder builder = new AlertDialog.Builder(FirstScanActivity.this);
					builder.setTitle(getResources().getString(R.string.tip_input_device_num)).setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
							.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
					builder.setPositiveButton(R.string.button_commit, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							String text = inputServer.getText().toString();
							parameterValidation(text);
						}

						});
                builder.show();
				//zheli
				}
			}
		});
		tv_menu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(isEdit == false){
					isEdit = true;
					tv_menu.setText(R.string.tip_device_list_menu2);
					for(int i = 0; i < ll_device.getChildCount(); i++){
						DeviceInfoView deviceInfoView = (DeviceInfoView)ll_device.getChildAt(i);
						deviceInfoView.openMenu();
					}
				}
				else {
					isEdit = false;
					tv_menu.setText(R.string.tip_device_list_menu);
					for(int i = 0; i < ll_device.getChildCount(); i++){
						DeviceInfoView deviceInfoView = (DeviceInfoView)ll_device.getChildAt(i);
						deviceInfoView.closeMenu();
					}
				}
			}
		});

		getDeviceList();

		MZApplication.getInstance().addActivity(this);
	}

	private void parameterValidation(String sn) {
		sn = sn.toUpperCase();

		if (!sn.contains("TGT")&&!sn.contains("TUGE")) {
			Toast.makeText(FirstScanActivity.this, getResources().getString(R.string.tip_input_corret_device_num), Toast.LENGTH_LONG).show();
			return;
		}
		if (sn.contains("TUGE")){
			sn = sn.replace("TUGE", "TGT");
		}
		if (sn.contains("*")){
			sn = sn.replace("*", "");
		}
		APIConstants.sn = sn;
		deviceSN = sn;

		APIConstants.deviceInfo = new DeviceInfo(deviceSN);
		OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[1];
		params[0] = new OkHttpClientManager.Param("device_no", APIConstants.sn);
		alertInfo = getResources().getString(R.string.tip_can_not_find_the_device);
		Log.e("FirstScan", "Get_Device_Status");
		mWeiboDialog = WeiboDialogUtils.createLoadingDialog(this, getResources().getString(R.string.loading));
		new SendRequest(APIConstants.Get_Device_Status, params, new MyHandler(), 1);

	}

	private void operation() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("shoudong")
				.setPositiveButton(R.string.tip_connect2, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//集合里的删除点击的条目
						Intent intent = new Intent(mContext, CaptureActivity.class);
						startActivityForResult(intent, APIConstants.SCANNING_REQUEST_CODE);

					}
				})
				.setNegativeButton(R.string.tip_device_info_menu, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {


						final AlertDialog isExit = new AlertDialog.Builder(mContext).create();
						isExit.setTitle(R.string.tip_title);
						isExit.setMessage(getResources().getString(R.string.tip_unbind_the_device));


					}}).show();
	}
	private void startMainservice(){
		Intent intent = new Intent(this, MainService.class);
		startService(intent);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				MainService.getInstance().startHeartBeat();


				try {
//					MainService.getInstance().startHeartBeat();

				}
				catch (Exception e){

					Log.i("exception",e.toString());
				}

			}
		},500);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
	}

	private void showNotFindDialog(String string)
	{
		final AlertDialog isExit = new AlertDialog.Builder(mContext).create();
		isExit.setTitle(R.string.tip_title);
		isExit.setMessage(string);
		isExit.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.
				commit_button), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				isExit.dismiss();
			}
		});
		isExit.show();
	}

	@Override
	public void onBackPressed() {
		if (!quit) {
			quit = true;
			Toast.makeText(this, R.string.toast_back_exit, Toast.LENGTH_SHORT).show();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					quit = false;
				}
			}, 2000);
		} else {
			MZApplication.getInstance().exit();
		}
	}

	private void getDeviceList(){
		ArrayList<DeviceInfoForUserData> devices = UserDataUtils.getInstance(mContext).getDeviceList();
		if(devices != null && devices.size() > 0){
			sv_introduce.setVisibility(View.GONE);
			sv_device.setVisibility(View.VISIBLE);
			ll_device.removeAllViews();
			for(int i = 0; i < devices.size(); i++){
				final DeviceInfoForUserData device = devices.get(i);
				final DeviceInfoView deviceInfoView = new DeviceInfoView(mContext, device.Ssid, device.MacAddress);
				ll_device.addView(deviceInfoView);
				deviceInfoView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						duihukuan(device, deviceInfoView);

						/*wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
						if(!wifiManager.isWifiEnabled())
							wifiManager.setWifiEnabled(true);
						if(isEdit == true){
							final AlertDialog isExit = new AlertDialog.Builder(mContext).create();
							isExit.setTitle(R.string.tip_title);
							isExit.setMessage(getResources().getString(R.string.tip_unbind_the_device));
							isExit.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel_button), new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									isExit.dismiss();
								}
							});
							isExit.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.commit_button), new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
//									BluetoothDevice device = BluetoothUtil.getInstance().getBTDevice(deviceInfoView.getMacAddress());
//									try {
//										boolean res = ClsUtils.removeBond(device.getClass(), device);
//									} catch (Exception e) {
//										e.printStackTrace();
//									}
									UserDataUtils.getInstance(mContext).removeFromDeviceList(deviceInfoView.getSsid(), deviceInfoView.getMacAddress());
									isExit.dismiss();
									getDeviceList();

								}
							});

							isExit.show();
						}
						else{
							APIConstants.deviceInfo = new DeviceInfo(device.Ssid);
							Intent intent = new Intent(mContext, com.a51tgt.t6.ui.MainActivity.class);
							startActivity(intent);
						}*/
					}
				});
			}
			isEdit = true;
//			tv_menu.setVisibility(View.VISIBLE);
			tv_menu.performClick();
		}
		else{
//			tv_menu.setVisibility(View.GONE);
			sv_introduce.setVisibility(View.VISIBLE);
			sv_device.setVisibility(View.GONE);
		}
	}
	private void duihukuan(final DeviceInfoForUserData device, final DeviceInfoView deviceInfoView) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(R.string.tip_device_operation_title)
				.setPositiveButton(R.string.tip_connect2, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//集合里的删除点击的条目
						APIConstants.sn = device.Ssid;
						APIConstants.deviceInfo = new DeviceInfo(device.Ssid);
						Intent intent = new Intent(mContext, MainActivity.class);
						startActivity(intent);

					}
				})
				.setNegativeButton(R.string.tip_device_info_menu, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						final AlertDialog isExit = new AlertDialog.Builder(mContext).create();
						isExit.setTitle(R.string.tip_title);
						isExit.setMessage(getResources().getString(R.string.tip_unbind_the_device));
						isExit.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								isExit.dismiss();
							}
						});
						isExit.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.button_commit), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {

								UserDataUtils.getInstance(mContext).removeFromDeviceList(deviceInfoView.getSn(), deviceInfoView.getMacAddress());
								isExit.dismiss();
								getDeviceList();

							}
						});

						isExit.show();
					}
				}).show();


	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case APIConstants.SCANNING_REQUEST_CODE:
				if (resultCode == RESULT_OK) {
					String result = data.getStringExtra(CaptureActivity.EXTRA_STRING);
					Log.i("wufaeegegeg",result);

					if (!TextUtils.isEmpty(result)) {
						//http://aifi.51tgt.com:9988/downloadapp.html?sn=TGT24170833157&wmac=A4D8CA7B7F88&bmac=4045DA963CB8&imei1=864772030440548&imei2=864772030440549
						//http://aifi.51tgt.com:9988/downloadapp.html?sn=TGT24170833232&wmac=A4D8CA7B7F88&bmac=4045DA7CAEE5&imei1=864772030440548&imei2=864772030440549
						Map<String, String> maps = CommUtil.URLRequest(result);
						if (result != null  && result.contains("sn")) {
							String temp = maps.get("sn");
//							if (temp.length() == 12 || temp.length() == 17) {
//								if (temp.length() == 12) {
//
//									deviceMacAddress = temp.substring(0, 2) + ":" + temp.substring(2, 4) + ":" + temp.substring(4, 6) + ":" + temp.substring(6, 8) + ":" + temp.substring(8, 10) + ":" + temp.substring(10, 12);
//
//								} else {
//									deviceMacAddress = temp;
//
//
//								}
//							}

//							}
							deviceSN = temp;
//							System.out.println("ooooo" + deviceMacAddress + deviceSSID);
						}else if(result!=null && result.contains("TGT")) {

							deviceSN = result;
						}else if(result!=null && result.contains("TUGE")) {

							deviceSN = result;
						}
						else
						{
							deviceSN = "";
						}
					}
					if (TextUtils.isEmpty(deviceSN)) {
						showNotFindDialog(getResources().getString(R.string.wrong_qrcode));
						return;
					} else {
//						UserDataUtils.getInstance(mContext).setDeviceList(deviceSSID, deviceMacAddress);
//						getDeviceList();

						APIConstants.sn = deviceSN;
						Log.i("temp", "onActivityResult: " + deviceSN);

//						if (deviceSSID.toString().contains("TGT")){
////							temp = deviceSSID;
////
////							deviceSSID = deviceSSID.replace("TGT","GTWiFi");
//
//						}else{
//							temp = deviceSSID.replace("GTWiFi","TGT");
////							deviceSSID = deviceSSID;
//						}

						APIConstants.deviceInfo = new DeviceInfo(deviceSN);
						OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[1];
						params[0] = new OkHttpClientManager.Param("device_no", deviceSN);
						Log.i("temp", "onActivityResult: "+ deviceSN);
						alertInfo = getResources().getString(R.string.tip_can_not_find_the_device);
						mWeiboDialog = WeiboDialogUtils.createLoadingDialog(this, getResources().getString(R.string.loading));
						new SendRequest(APIConstants.Get_Device_Status, params, new MyHandler(), 1);
					}
				} else {
					//showNotFindDialog();
				}
				break;
		}
	}

	private void initPermission() {
		String[] permissions = {
				Manifest.permission.ACCESS_NETWORK_STATE,
				Manifest.permission.ACCESS_WIFI_STATE,
				Manifest.permission.ACCESS_WIFI_STATE,
				Manifest.permission.CHANGE_WIFI_STATE,
				Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.READ_EXTERNAL_STORAGE,
				Manifest.permission.ACCESS_FINE_LOCATION
		};
		ArrayList<String> toApplyList = new ArrayList<String>();

		for (String perm : permissions) {
			if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
				toApplyList.add(perm);
				// 进入到这里代表没有权限.
			}
		}
		String[] tmpList = new String[toApplyList.size()];
		if (!toApplyList.isEmpty()) {
			ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
		}
	}

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (mWeiboDialog!=null)
			WeiboDialogUtils.closeDialog(mWeiboDialog);


			if (msg == null || msg.obj == null) {
				Toast.makeText(FirstScanActivity.this,getResources().getString(R.string.time_out),Toast.LENGTH_SHORT).show();
				return;
			}
			if (msg.what == -10) {
				return;
			}

			HttpResponseData responseData = new HttpResponseData((String) msg.obj);
//			if (responseData == null || responseData.status < 0 || responseData.data == null) {
//				return;
//			}
			Log.i("response, firstscan",msg.toString() + msg.what);

			switch (msg.what) {
				case 1:
//					if(responseData.data == null || responseData.code == null){
//						showNotFindDialog(alertInfo);
//						return;
////						UserDataUtils.getInstance(mContext).setDeviceList(deviceSSID, deviceMacAddress);
////						getDeviceList();
////						return;
//					}

					int checkDevice_code = responseData.code;

					if(checkDevice_code == 2){
						//设备未激活
						Log.e("checkDevice_code", "0002");
						final AlertDialog isExit = new AlertDialog.Builder(mContext).create();
						isExit.setTitle(R.string.tip_title);
						isExit.setMessage("("+APIConstants.sn+")"+getResources().getString(R.string.tip_for_active_device));
						isExit.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.tip_active_not), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								isExit.dismiss();
							}
						});
						isExit.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.tip_active_now), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								isExit.dismiss();
								OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[1];
								params[0] = new OkHttpClientManager.Param("device_no", APIConstants.sn);
								mWeiboDialog = WeiboDialogUtils.createLoadingDialog(FirstScanActivity.this, getResources().getString(R.string.loading));
								new SendRequest(APIConstants.Device_Active, params, new MyHandler(), 2);
							}
						});

						isExit.show();
					}
					else if (checkDevice_code == 0)
						{
						//设备已激活
						Log.e("checkDevice_code", "0000");
						UserDataUtils.getInstance(mContext).setDeviceList(APIConstants.sn, deviceMacAddress);
						getDeviceList();
					}
					else{
						Log.e("checkDevice_code", "0001");
						Toast.makeText(mContext,
								getResources().getString(R.string.msg_Device_not_exist),
								Toast.LENGTH_LONG).show();
					}
					break;
				case 2:
//					WeiboDialogUtils.closeDialog(mWeiboDialog);
					int activeDevice_code = responseData.code;
					if (activeDevice_code == 0){
						//成功激活
						Log.e("activeDevice", "设备未激活，成功激活");
						final AlertDialog isExit = new AlertDialog.Builder(mContext).create();
						isExit.setTitle(R.string.tip_active_success);
						isExit.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.tip_start_now), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								isExit.dismiss();
								UserDataUtils.getInstance(mContext).setDeviceList(APIConstants.sn, deviceMacAddress);
								getDeviceList();

							}
						});

						isExit.show();
					}else {
						WeiboDialogUtils.closeDialog(mWeiboDialog);
					    Log.e("无法激活", "无法激活");
						TipUtil.showAlert(mContext,
								mContext.getResources().getText(R.string.tip_title).toString(),
								mContext.getResources().getText(R.string.tip_for_active_device_error).toString(),
								mContext.getResources().getText(R.string.commit_button).toString(),
								new TipUtil.OnAlertSelected() {
									@Override
									public void onClick(DialogInterface dialog, int whichButton) {
										dialog.dismiss();
									}
								});
					}
					break;
				default:
					break;
			}
		}
	}
}

