package com.a51tgt.t6.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a51tgt.t6.R;
import com.a51tgt.t6.bean.HttpResponseData;
import com.a51tgt.t6.comm.APIConstants;
import com.a51tgt.t6.net.OkHttpClientManager;
import com.a51tgt.t6.net.SendRequest;
import com.a51tgt.t6.utils.PayResult;
import com.a51tgt.t6.utils.PayUtil;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

public class SubmitOrderActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBack,pay_img,iv_alipay_select,iv_wechat_select;
    private TextView tv_packageName,tv_use_day,tv_total_price,tv_packageCount,tv_pay_title;

    private String  title, price, price_type, payType,
             effective_days,
             activedays,  product_id;
    private  Integer productnumber;
    private Button btn_buy;

    private  LinearLayout ll_alipay,ll_wechat;
    private static final int SDK_PAY_FLAG = 1;

    private HttpResponseData wxPayResponseData;
    private IWXAPI iWXApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_order);


        iWXApi = WXAPIFactory.createWXAPI(this, APIConstants.WX_APP_ID);

        product_id = getIntent().getStringExtra("product_id");
        title = getIntent().getStringExtra("title");
        price = getIntent().getStringExtra("price");
        productnumber = getIntent().getIntExtra("product_count",1);
        price_type = getIntent().getStringExtra("price_type");
        activedays = getIntent().getStringExtra("activedays");

        mBack = findViewById(R.id.iv_header_left);
        btn_buy = findViewById(R.id.buy_flow);
        tv_packageName = findViewById(R.id.tv_package_name);
        tv_packageCount = findViewById(R.id.tv_package_count);
        tv_use_day = findViewById(R.id.tv_user_day);
        tv_total_price = findViewById(R.id.tv_actual_price);
        ll_alipay = findViewById(R.id.ll_alipay);
        ll_wechat = findViewById(R.id.ll_wechat);
        tv_pay_title = findViewById(R.id.pay_title);
        pay_img = findViewById(R.id.pay_image);
        iv_alipay_select = findViewById(R.id.alipay_select);
        iv_wechat_select = findViewById(R.id.wechat_select);


        tv_packageName.setText(title);
        tv_use_day.setText(effective_days);
        tv_packageCount.setText("✖"+productnumber);
        tv_total_price.setText(price);

        if (price_type.equals("RMB")){

        payType = "ALIPAY";

        }else {
            payType = "CARD";


            pay_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_card));
            ll_wechat.setVisibility(View.GONE);
            tv_pay_title.setText(R.string.title_credit_card);


        }
//        tv_total_price.setText(price_type+" "+String.valueOf(actual_price));



        mBack.setOnClickListener(this);
        btn_buy.setOnClickListener(this);
        ll_alipay.setOnClickListener(this);
        ll_wechat.setOnClickListener(this);





    }
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {


                        Intent intent = new Intent();
                        intent.setAction(APIConstants.BR_ORDER_STATUS);
                        intent.putExtra("order_status", true);
                        sendBroadcast(intent);
//                        SendQuery();
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        Toast.makeText(FlowProductDetailActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.equals(resultStatus, "4000")) {

                        Intent intent = new Intent();
                        intent.setAction(APIConstants.BR_ORDER_STATUS);
                        intent.putExtra("order_status", false);
                        sendBroadcast(intent);


                    }else  {


                        Toast.makeText(SubmitOrderActivity.this, "支付取消", Toast.LENGTH_SHORT).show();

                        Log.i("paystatus", payResult.toString());
//                        showNormalDialogOne();
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                        Toast.makeText(FlowProductDetailActivity.this, R.string.tip_pay_failed, Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };
    private  void  pay(){


        if (payType.equals("WXPAY") || payType.equals("ALIPAY")) {
            int paramCount = 5;
            int what=1;

            OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[paramCount];
            params[0] = new OkHttpClientManager.Param("device_no", APIConstants.sn);
            params[1] = new OkHttpClientManager.Param("productid", product_id);
            params[2] = new OkHttpClientManager.Param("productcount", String.valueOf(productnumber));
            params[3] = new OkHttpClientManager.Param("currency", price_type);
            params[4] = new OkHttpClientManager.Param("payment_schema", payType);
            if (payType.equals("WXPAY")){

                what=4;
            }else {


            }

            new SendRequest(APIConstants.Payment, params, new MyHandler(), what);

        } else if (payType.equals("CARD")){
            Intent paymentIntent;
            paymentIntent = new Intent(this, CardActivity.class);
            paymentIntent.putExtra("productid", product_id);
            paymentIntent.putExtra("productcount", String.valueOf(productnumber));
            paymentIntent.putExtra("currency", price_type);
            paymentIntent.putExtra("payment_schema", payType);
            paymentIntent.putExtra("price", price);
            startActivity(paymentIntent);
        }



    }
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (msg == null || msg.obj == null) {
                return;
            }
            if (msg.what == -10) {
                return;
            }

            final HttpResponseData responseData = new HttpResponseData((String) msg.obj);
            Log.i("RESPOSEDATA,", String.valueOf(responseData.code));
            if(responseData == null){
                return;
            }

            if(responseData.status < 0){
                Log.e("reponseData.status", "<0");
                return;
            }

            if (responseData.data == null) {
                Log.e("reponseData.data", "empty");
//                return;
            }

            switch (msg.what) {
                case 1:
                    Log.i("products:",responseData.data.toString());
                    if (responseData.code == 0) {
                        Log.e("Alipay:", responseData.data.toString());
                        APIConstants.OUT_TRADE_NO = responseData.data.get("orderno").toString();
                        Runnable payRunnable = new Runnable() {
                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(SubmitOrderActivity.this);
                                Map<String, String> result = alipay.payV2(responseData.data.get("body").toString(), true);
                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        };
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
                    } else {

                        Log.i("errorMessage", responseData.msg);
                        Toast.makeText(SubmitOrderActivity.this, responseData.msg, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 4:
                    Log.e("WXPAY:",responseData.data.toString());
                    if (responseData.code == 0) {
                        Log.e("WXPAY000:", "0000000");
                        if(
                                responseData.data.containsKey("out_order_no")
                                        && responseData.data.containsKey("prepayid")
                                        && responseData.data.containsKey("partnerid")
                                        && responseData.data.containsKey("noncestr")
                                        && responseData.data.containsKey("timestamp")
                                        && responseData.data.containsKey("sign")
                        ){
                            Log.e("WXPAY000000","ok");
                            wxPayResponseData = responseData;
                            APIConstants.OUT_TRADE_NO = responseData.data.get("out_order_no").toString();
                            PayUtil.WXPayForOrder(SubmitOrderActivity.this,
                                    iWXApi, APIConstants.WX_APP_ID,
                                    responseData.data.get("prepayid").toString(),
                                    responseData.data.get("partnerid").toString(),
                                    responseData.data.get("noncestr").toString(),
                                    responseData.data.get("timestamp").toString(),
                                    responseData.data.get("sign").toString());
                        }
                    } else {

                        Log.i("errorMessage", responseData.msg);
                        Toast.makeText(SubmitOrderActivity.this, responseData.msg, Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.buy_flow:

                pay();
                break;

            case R.id.iv_header_left:
                finish();
                break;

            case R.id.ll_alipay:

                if (price_type.equals("RMB")){

                    payType = "ALIPAY";


                }

                iv_alipay_select.setImageDrawable(getResources().getDrawable(R.drawable.ic_select));
                iv_wechat_select.setImageDrawable(getResources().getDrawable(R.drawable.ic_unselect));

                break;
            case  R.id.ll_wechat:
                payType = "WXPAY";

                iv_wechat_select.setImageDrawable(getResources().getDrawable(R.drawable.ic_select));
                iv_alipay_select.setImageDrawable(getResources().getDrawable(R.drawable.ic_unselect));
                break;

                default:
                    break;
        }

    }
}
