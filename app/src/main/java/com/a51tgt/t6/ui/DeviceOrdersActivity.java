package com.a51tgt.t6.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a51tgt.t6.R;
import com.a51tgt.t6.adapter.DeviceOrdersAdapter;
import com.a51tgt.t6.adapter.RecyclerviewAdapter;
import com.a51tgt.t6.bean.DeviceOrderHttpResponseData;
import com.a51tgt.t6.bean.PackageInfo;
import com.a51tgt.t6.comm.APIConstants;
import com.a51tgt.t6.net.OkHttpClientManager;
import com.a51tgt.t6.net.SendRequest;
import com.a51tgt.t6.ui.view.PackageInfoView;
import com.a51tgt.t6.ui.view.PackageInfoView2;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;

public class DeviceOrdersActivity extends BaseActivity implements View.OnClickListener{

	private ImageView iv_back;
	private RecyclerView rv_device_orders;
	private DeviceOrdersAdapter deviceOrderAdapter;
	private static final String TAG = "DeviceOrdersActivity";
	private static final String runningOnCreate = "Running OnCreate";
	private Dialog mWeiboDialog;
	private RecyclerviewAdapter recyclerviewAdapter;


	private Context mContext;
	@SuppressLint("InflateParams") @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_orders);

		//CommUtil.setStatusBarBackgroundColor(DeviceOrdersActivity.this);

		mContext = DeviceOrdersActivity.this;
		iv_back = findViewById(R.id.iv_back);
		rv_device_orders = findViewById(R.id.rv_device_orders);
        iv_back.setOnClickListener(this);

		String sn = APIConstants.sn;

		String lan;

		if (APIConstants.currentLan.contains("zh")){
			lan = "zh-CN";
		}else{
			lan = "en";
		}

		OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[3];
		params[0] = new OkHttpClientManager.Param("device_no", sn);
		params[1] = new OkHttpClientManager.Param("lang", lan);
		params[2] = new OkHttpClientManager.Param("using","0");

		Log.i("pachgeurl", APIConstants.currentLan);
		mWeiboDialog = WeiboDialogUtils.createLoadingDialog(this, getResources().getString(R.string.loading));
		new SendRequest(APIConstants.Get_Flow_Package, params, new MyHandler(), 1);
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

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.iv_back:
				finish();
				break;
			default:
				break;
		}
	}

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			WeiboDialogUtils.closeDialog(mWeiboDialog);
			if (msg == null || msg.obj == null) {
				return;
			}
			if(msg.what == -10){
				return;
			}

			DeviceOrderHttpResponseData responseData = new DeviceOrderHttpResponseData((String) msg.obj);
			if (responseData == null || responseData.data == null) {
				return;
			}
			Log.i("responsedeviceorder",msg.toString() + msg.what);
			switch (msg.what){
				case 1:
					rv_device_orders.removeAllViews();
					ArrayList<LinkedTreeMap<String, Object>> deviceOrders = responseData.data;
					if (deviceOrders == null || deviceOrders.size() == 0) {
						rv_device_orders.setVisibility(View.GONE);
						return;
					}
					recyclerviewAdapter  = new RecyclerviewAdapter(R.layout.item_device_order, deviceOrders);
					rv_device_orders.setLayoutManager(new LinearLayoutManager(DeviceOrdersActivity.this, LinearLayoutManager.VERTICAL, false));
					rv_device_orders.setAdapter(recyclerviewAdapter);
					recyclerviewAdapter.notifyDataSetChanged();

					recyclerviewAdapter.setOnCallBackData(new RecyclerviewAdapter.OnCallBackData() {
						@Override
						public void convertView(BaseViewHolder helper, Object item) {
							LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) item;
							final PackageInfo packageInfo = new PackageInfo(data);
							String[] array =  packageInfo.countries.split(",");
							if(packageInfo != null){
								helper.setText(R.id.tv_package_name, packageInfo.product_name);
								if(packageInfo.flow_count.equals("unlimited")){
									helper.setText(R.id.tv_flow_count,
											getResources().getString(R.string.tag_unlimited));
								} else
									helper.setText(R.id.tv_flow_count, packageInfo.flow_count);

								if(packageInfo.left_flow_count.equals("unlimited")){
									helper.setText(R.id.tv_flow_left,
											getResources().getString(R.string.tag_unlimited));
								} else
									helper.setText(R.id.tv_flow_left, packageInfo.left_flow_count);

								final TextView more = helper.getView(R.id.tv_countries_more);
								final TextView countriesTV = helper.getView(R.id.tv_flow_countries);

								if (array.length>2){
									Log.i("countries:", packageInfo.countries.split(",")[0]);
									helper.setText(R.id.tv_flow_countries,array[0]+","+array[1]);
									more.setVisibility(View.VISIBLE);
									countriesTV.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String[] array =  packageInfo.countries.split(",");
											countriesTV.setText(array[0]+","+array[1]);
											more.setVisibility(View.VISIBLE);
										}
									});
									more.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											if (countriesTV.getText().length() <= 45) {
												countriesTV.setText(packageInfo.countries);
												more.setVisibility(View.GONE);
											}
										}
									});
								}else {
									helper.setText(R.id.tv_flow_countries,packageInfo.countries);
									more.setVisibility(View.GONE);
								}
								helper.setText(R.id.tv_package_starttime,packageInfo.start_date);
								helper.setText(R.id.tv_package_endtime,packageInfo.end_date);
							}
						}
					});
					break;
				default:
					break;
			}
		}
	}
}

