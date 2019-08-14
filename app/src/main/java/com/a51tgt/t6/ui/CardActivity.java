package com.a51tgt.t6.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.a51tgt.t6.R;
import com.a51tgt.t6.bean.HttpResponseData;
import com.a51tgt.t6.comm.APIConstants;
import com.a51tgt.t6.net.OkHttpClientManager;
import com.a51tgt.t6.net.SendRequest;
import com.stripe.android.TokenCallback;

import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardMultilineWidget;


public class CardActivity extends BaseActivity{
	CardMultilineWidget mCardMultilineWidget;
	private Dialog mWeiboDialog;
	String price, cardNum, cardBrand;

	String productid, productcount, currency, payment_schema;
	private static final String TAG = "CardActivity";
	private static final String runningOnCreate = "Running OnCreate";

	@SuppressLint("InflateParams") @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_input_widget1);
		//字符串截取
		Log.e(TAG, runningOnCreate);
		price = getIntent().getStringExtra("price");
		productid = getIntent().getStringExtra("productid");
		productcount  = getIntent().getStringExtra("productcount");
		currency  = getIntent().getStringExtra("currency");
		payment_schema  = getIntent().getStringExtra("payment_schema");

		Log.i("prcieeee", "onCreate: " + price+productcount);

		mCardMultilineWidget = findViewById(R.id.card_multiline_widget);
		findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		findViewById(R.id.save_payment).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveCard();
			}
		});
	}
	private void pay(String token) {
		OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[6];
		params[0] = new OkHttpClientManager.Param("productid", productid);
		params[1] = new OkHttpClientManager.Param("productcount", productcount);
		params[2] = new OkHttpClientManager.Param("currency", currency);
		params[3] = new OkHttpClientManager.Param("payment_schema", payment_schema);
		params[4] = new OkHttpClientManager.Param("device_no", APIConstants.sn);
		params[5] = new OkHttpClientManager.Param("tokenid", token);
		Log.e("Card", token);
		mWeiboDialog = WeiboDialogUtils.createLoadingDialog(CardActivity.this,getResources().getString(R.string.loading));
		new SendRequest(APIConstants.Payment_Card, params, new MyHandler(), 1);
	}

	private void saveCard() {
		Card card = mCardMultilineWidget.getCard();

		if (card == null) {
			return;
		}
		cardNum = card.getNumber();
		cardBrand = card.getBrand()+" card";
		mWeiboDialog = WeiboDialogUtils.createLoadingDialog(CardActivity.this,getResources().getString(R.string.loading));
//		mHandler.sendEmptyMessageDelayed(1, 2000);
//		pk_live_sMAkefrTEHc8BWoApoD5PD5L
		Stripe stripe = new Stripe(this, APIConstants.Stripe_key);
		stripe.createToken(
				card,
				new TokenCallback() {
					public void onSuccess(Token token) {
						WeiboDialogUtils.closeDialog(mWeiboDialog);
						Log.i("tokentntnntnt:",token.getId());
						pay(token.getId());
					}
					public void onError(Exception error) {
						Log.i("tokenerrrr:",error.getMessage());
						WeiboDialogUtils.closeDialog(mWeiboDialog);
//						CardActivity.this.finish();
						Toast.makeText(CardActivity.this, error.getMessage(),
								Toast.LENGTH_LONG).show();
					}
				}
		);
	}


	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			WeiboDialogUtils.closeDialog(mWeiboDialog);

			Log.e("CARD", "hello");
			if (msg == null || msg.obj == null) {
				return;
			}
			if(msg.what == -10){
				return;
			}

			HttpResponseData responseData = new HttpResponseData((String) msg.obj);
			if (responseData == null || responseData.status < 0 || responseData.data == null) {
				Log.e("card", "failed");
				return;
			}

			switch (msg.what){
				case 1:
					Log.e("uuuuuuuuu",responseData.toString());

					String url = responseData.data.get("receipt_url").toString();

					if (responseData.code == 0) {
						Log.e("SUCCESS",responseData.toString());
						Intent intent = new Intent();
						intent.setAction(APIConstants.BR_ORDER_STATUS);
						intent.putExtra("order_status", true);
						finish();
						sendBroadcast(intent);
					} else {
						Intent intent = new Intent();
						intent.setAction(APIConstants.BR_ORDER_STATUS);
						intent.putExtra("order_status", false);
						finish();
						sendBroadcast(intent);
					}
					break;
				default:
					break;
			}
		}
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
}

