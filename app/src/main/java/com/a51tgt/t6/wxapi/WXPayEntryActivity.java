package com.a51tgt.t6.wxapi;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


import com.a51tgt.t6.R;
import com.a51tgt.t6.bean.HttpResponseData;
import com.a51tgt.t6.comm.APIConstants;
import com.a51tgt.t6.net.OkHttpClientManager;
import com.a51tgt.t6.net.SendRequest;
import com.a51tgt.t6.ui.BaseActivity;
import com.a51tgt.t6.ui.FlowProductDetailActivity;
import com.a51tgt.t6.utils.PayUtil;
import com.a51tgt.t6.utils.TipUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
	
	private IWXAPI api;
	Context mContext;
	private int res_code = BaseResp.ErrCode.ERR_OK;
	private boolean queryAgain = false;
	private static final String TAG = "WXPayEntryActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_wx_entry);
		Log.e(TAG, "Running onCreate");
		mContext = this;

		api = WXAPIFactory.createWXAPI(this, APIConstants.WX_APP_ID);
		api.registerApp(APIConstants.WX_APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	public void onReq(BaseReq req)
	{
	}

	@Override
	public void onResp(BaseResp resp)
	{
		if (resp.getType() != ConstantsAPI.COMMAND_PAY_BY_WX)
			return;

		String msg = resp.errCode+"";
		res_code = resp.errCode;
		Log.e("WXPayEntryActivity", "ERRORRRR"+resp.errCode);
		switch (resp.errCode)
		{
			case BaseResp.ErrCode.ERR_OK:
				msg = getString(R.string.tip_wxpay_success);
				Log.e(msg, ""+BaseResp.ErrCode.ERR_OK);
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				msg = getString(R.string.tip_wxpay_cancel);
				Log.e(msg, ""+BaseResp.ErrCode.ERR_USER_CANCEL);
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				msg = getString(R.string.tip_wxpay_denied);
				Log.e(msg, ""+BaseResp.ErrCode.ERR_AUTH_DENIED);
				break;
			case BaseResp.ErrCode.ERR_SENT_FAILED:
				msg = getString(R.string.tip_wxpay_failed);
				Log.e(msg, ""+BaseResp.ErrCode.ERR_SENT_FAILED);
				break;
			case BaseResp.ErrCode.ERR_UNSUPPORT:
				msg = getString(R.string.tip_wxpay_unsupport);
				Log.e(msg, ""+BaseResp.ErrCode.ERR_UNSUPPORT);
				break;
			default:
				Log.e(msg, ""+resp.errCode);
				break;
		}

//		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		if(resp.errCode == BaseResp.ErrCode.ERR_OK) {
//			SendQuery();
			Intent intent = new Intent();
			intent.setAction(APIConstants.BR_ORDER_STATUS);
			intent.putExtra("order_status", true);
			sendBroadcast(intent);
			queryAgain = false;
//			finish();
		}else {

			Intent intent = new Intent();
			intent.setAction(APIConstants.BR_ORDER_STATUS);
			intent.putExtra("order_status", false);
			sendBroadcast(intent);
//			finish();

		}
	}

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
//			if (responseData == null || responseData.status < 0 || responseData.data == null) {
//				return;
//			}

			switch (msg.what){
				case 1:
					if (responseData.code == 0) {
						Intent intent = new Intent();
						intent.setAction(APIConstants.BR_ORDER_STATUS);
						intent.putExtra("order_status", true);
						sendBroadcast(intent);
					} else {
						Intent intent = new Intent();
						Log.d(TAG, "handleMessage: "+responseData.msg);
						intent.setAction(APIConstants.BR_ORDER_STATUS);
						intent.putExtra("order_status", false);
						sendBroadcast(intent);
					}
					break;
			}
			finish();
		}
	}

	private void SendQuery() {
		OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[2];
		params[0] = new OkHttpClientManager.Param("out_order_no", APIConstants.OUT_TRADE_NO);
		params[1] = new OkHttpClientManager.Param("device_no", APIConstants.sn);
		new SendRequest(APIConstants.Query, params, new MyHandler(), 1);
	}
}