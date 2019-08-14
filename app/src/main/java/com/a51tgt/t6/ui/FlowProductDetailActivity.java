package com.a51tgt.t6.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a51tgt.t6.R;
import com.a51tgt.t6.bean.FlowProductInfo;
import com.a51tgt.t6.bean.HttpResponseData;
import com.a51tgt.t6.bean.PackageInfo;
import com.a51tgt.t6.bean.UserDataUtils;
import com.a51tgt.t6.bean.WxHttpResponseData;
import com.a51tgt.t6.comm.APIConstants;
import com.a51tgt.t6.net.OkHttpClientManager;
import com.a51tgt.t6.net.SendRequest;
import com.a51tgt.t6.ui.view.NumberButton;
import com.a51tgt.t6.utils.CommUtil;
import com.a51tgt.t6.utils.PayResult;
import com.a51tgt.t6.utils.PayUtil;
import com.a51tgt.t6.utils.TipUtil;
import com.a51tgt.t6.wxapi.WXPayEntryActivity;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.alipay.sdk.app.PayTask;

import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONException;

import java.math.BigDecimal;
import java.util.Map;


/**
 * Created by liu_w on 2017/9/15.
 */

public class FlowProductDetailActivity extends BaseActivity implements View.OnClickListener {

    private int[] pic = new int[]{R.drawable.scene1, R.drawable.scene2, R.drawable.scene3,
            R.drawable.scene4};


    private ProgressBar pb_progress;
    private NumberButton numberButton;
    private  String productcount;

    private ImageView iv_back, iv_close, iv_result, iv_product_pic;
    private WebView webView;
    private TextView tv_title, tv_price, tv_price_type, tv_package_name, tv_pay_price,
            tv_sn, tv_money_type, tv_start_date,
            tv_coverage, tv_product_name, tv_effective_days, tv_total_flow;
    private Button bt_commit, bt_buy_now, bt_close;
    private Context mContext;
    private String url, title, price, price_type, payType, coverage,
            total_flow, effective_days, notice, product_name, coverImage,
            productnumber, activedays, producttype, product_id;

    private CheckBox wechatCheck, paypalCheck, alipayCheck, cardCheck;
    boolean creditCardAvailable = false;

    private LinearLayout ll_pay, ll_pay_info, ll_order_status, ll_wechat_tools, payPalLayout, alipayLayout,purchaseCountLayout;
    private RelativeLayout rl_card_tools;

    // 配置各种支付类型，一般就沙盒测试的和正式的
    private static final String CONFIG_ENVIRONMENT = /*BuildConfig.DEBUG ? PayPalConfiguration.ENVIRONMENT_SANDBOX :*/ PayPalConfiguration.ENVIRONMENT_PRODUCTION;

    // note that these credentialswill differ between live & sandbox environments.
    // 这是在第一步时候注册创建得来的Client ID
    private static final String CONFIG_CLIENT_ID = /*BuildConfig.DEBUG ? "AbpeJgzsjhKYOEBsClpMmd9koLInA9edZijfbjnaWGsoYpysI4IsOGl0WLPCKr2qF_Fa8Q8Ye90b5XO2" :*/ "AYnWniLiu_BrY9QgKLOiXXHgHSQ8Ndn7sXTrCcy9JAlmUWz0HWOCacKjMCn800wzHLc9VJYIWpeNBcVK";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;

    private HttpResponseData wxPayResponseData;
    private IWXAPI iWXApi;

    /**
     * 支付宝账户登录授权业务：入参pid值
     */
    public static final String PID = "";
    /**
     * 支付宝账户登录授权业务：入参target_id值
     */
    public static final String TARGET_ID = "";
    public static final String RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC1JsC0lfKJRToX1T7KJjtX5qNeGzUwlJHAzKjJTERH9ucjLMXnGTZQKIF+LtybNDhEWLfHCu+UVdbDjblRbVUEKOXellZNxU6eAYA+BliAp5NR/E3lM8iChXO2fNZik9pEsW6g5qASif8eNVukUGgMQsiSevPFla9p/ER8jcvFOaVtQZd9z3N41JcD56keGhLgD8jKwFkksL1BGUTeHzT9nfgOWBfIyZXDjTA3NJq2PxLnKZpvFdYm9UhwoXOdn+FQvMnTszvEopdVnH+cWmmQVQHzD4YnupbotPjeMmxjLh9nsDL0WtR5oqwc/SaaMl1dDB0n42PYJ7aEP3Y7qyqBAgMBAAECggEAML4GqBCYu/rWkMceKAJ1gUdbudyUSUgQl9H5sIBG9x4mPz6y2WXdSWA8x5kQXuqEIU/F01bL9BvMt72QmapTb9UZVPUar75hUv6QZSAL/EIaz7qVsxBl6UjDQ9z/SnMCDe24GK81bweWj6hHykSgWnw1v8Q5h3apRllMjD8TDzPpjFob6cZrah11JtMTOZfcJGCM5mvISW9dv8oZS9NiMDW9F2XNBHhdyeZgxgR4WxIHGnWb31ULxSJ1dWYzgwoVjddIQntPi7TQdwiM+FvxdHLzzw27j50hdYiRSIivkMvWg2WU8/Hpqsi3bYRCMvZZpIWXVr7LzqIra0Taqy1oKQKBgQDkHnAQhiccwR8gY9TJFPnCzWY48f31LzIyjX4ThJiUXsFVt+6InnKf9kTDSKLJdDwvV/oOdm6fDyXPsNWWbWW5Mz0VnVk7sDsR4LKlDhF42ARwMzJOaTL2Zow0+Jj54ejXF+ilvG4Xbm9287wOKs9KaX0E9kC23X293YQBwvuxCwKBgQDLSsIxZ0qnt40OvA9ceBxPUIaHAYsZD3xJ91i+zvY8sEinX4Qtmr4PHctGkjiHqUhWcWopynl4KEyEGpfZdDQb3RZkqQubHGI8B6QGYppxmY+ZpZgiju6CxGMkc2B6wiSg2ZPOd3C8gtFi4zoSuf+e+ULLA7NN/EpNAW7crUuiIwKBgQDB+Be2FTxAuP3snraaXNmbCOg415vn1b/LOIDRu9e5oXCnoISVapVw6wjBPIwe3FUotJgNoCqj2U9/cI/NHyx189mm/WqY1FsF8pg0vOeiZ5wug3ntLoqE9eGWUSRSpgNEkjHia3z5M5BYBxwKt+AXF+eURof0dhWrUiYjEVbOzQKBgDG2/5SLWRN47NMMpIkNiobO/4cmlnizc8a26YoIHOmshqf/0NhCPnZ958dDXPW4LUuOgtLjhB8hY8zun/H62/9watiFCqSNJWzexXsWRATB8/80IudYovVxx3kTiiWbMs/vRZk5VmWTbFdNkFciC2Q6Ot5g8kmBCD6auhzXeGaXAoGASNjiPGuNmJpG0t+W8xa6jmttCakoCSdbsQ+8jma1R/Lt9zOty90IQD5xIu4YWZsiNNQ4oxWmKgImRMRdaxFGXGlXttcpBfiA8E8I9hqOsLIn/J4PnQTlx9x8uWyYXilZhxD/jS4U/r4kWxw4h0rwbKB8l+idCEuahpSPC4GgoOo=";
//
//    public static final String RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCgeTwGE/ztiNxjbph5snUcL8+juopcChxCwvqSpa31+otF7aoMF8Du7d9UbEdugxSQ0dYWaqxOuilI8RDBlvo77bqjgzmNYoDneJltp7tTpPgXYY39C8olKm/3WqlA3TTXiye8+qoQ5WN4NWrF9Wd9bo+ODZmV0LC4SZ+1D+zwTqncH/nLntPFIV0iZJUIYMB2PGT9/6KFWQ23Vy9wOPaMcdANIL5g57ha14BasSnHftbiId1xIPt4/iF0S8BBZ4R54pBtBe7HYp1YFw0yoHNp1/A4RxiPBwdCgyDqSaJx1iQOljv9EAqeKtdTcfYUQ3c1D+JhbxUN+zxjeg7t2dQDAgMBAAECggEAeutiZuV4oBptdlyIvu9LdUPKMQf6Y3X7H8hqV1pBXmGDvRTHZGvthRcAMpKeRLBzWFrjOliLk+JVLWI6C4+yLaVziTxDIWGKi8N9AtmwS+nZf09ii6o0NTPEgBwZ+W13xIL69W6sRjTvdGy2VNwdYZ8GpzETrAYm14LNqsuAGrFwNJVw5APJxNzpgfzQHK5qnz6kYf7ged1GMmUM70b+qYPXLA8uFxmiPzJs3d73GJTpFKt5eYl+q5LP4/As0B27MF1hT4VHxnNlSbYu1T8bbizYpEYc7wwRyNR8m5+0H+r14VW0xRTtF1C+e51DVeidks21ISoaxxmZPqirdzmOwQKBgQDMsxYnbaV71P8/4lfnkgi+/dks5p/S9uSXtJ0Q19c3C+HP/SMl51e3umVUWdIWfBT7Itrt31fNVpHeWJXGzn84WZ3uzFyDxZVOskxZ+R8El6gsmihItYsccWgPtW1Xv9o7+BPiedALupz+mOOgWVISfRr6nGiw374almpcZAenHQKBgQDIsL2V26UmcrsHbfRaG1Vl9vlAVDn6waqZGPxLnOPmgFiDWNyz0UbtsVpTaV+PXCka+WOqjndXO1eZ7iP1cwRO1A6Gt62qGcY69R+3xsEx4mwb0I0zsZ7VyA7Cd9OvV8lzwRpotzdq0MxwrLTvHC6xexIyozVUD0rg+TwIYBndnwKBgFPp7mEHb9lNvb2AmiySDwfMpGTlvgsLrzYJkoG/Gbi1JELRMMFvDuSAgt2LXLZe83LoHPt6nVHN9UpC4lN3f2uoQTgNirdyHfz+1pHjsDV/vi9amr0Dvx1P2CroYwSf32TKE8y5xvGn9IfiQR4Qv7ZYluC5kalAb3wSwVNSplh5AoGAKa1akcZ9c5Cb+p0BA71tbLFvbcaQC90wOn8P1fwM8A9zUkOgAc/SNQaBxZS6faEcADgOVJsleX4fYR4tZYmvOVecdKmsbQjRO+kv3xBtipiE7vnOeQ/H9omHVydfVKicc2eEvn5eCda2vid/o7Bsmfr++nkeWAic+j+IEg0zEe0CgYEAxhN+jXcXeQcP6Tb9vmz2JiXynbdXa16aRqrsf4GJXqiwWcWGDK/MyAyRO7TBfGpyzqV+oBQhpk8eqEZSdiZvQFbbOuXH5yXc1/74LxsNGPnDbQqfhA0La8vJsoiZ63MabE56KX46Y/xvZP7RpIa6ClyDRXW1i9I9JoT+552+DlM=";
//    public static final String RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC1JsC0lfKJRToX1T7KJjtX5qNeGzUwlJHAzKjJTERH9ucjLMXnGTZQKIF+LtybNDhEWLfHCu+UVdbDjblRbVUEKOXellZNxU6eAYA+BliAp5NR/E3lM8iChXO2fNZik9pEsW6g5qASif8eNVukUGgMQsiSevPFla9p/ER8jcvFOaVtQZd9z3N41JcD56keGhLgD8jKwFkksL1BGUTeHzT9nfgOWBfIyZXDjTA3NJq2PxLnKZpvFdYm9UhwoXOdn+FQvMnTszvEopdVnH+cWmmQVQHzD4YnupbotPjeMmxjLh9nsDL0WtR5oqwc/SaaMl1dDB0n42PYJ7aEP3Y7qyqBAgMBAAECggEAML4GqBCYu/rWkMceKAJ1gUdbudyUSUgQl9H5sIBG9x4mPz6y2WXdSWA8x5kQXuqEIU/F01bL9BvMt72QmapTb9UZVPUar75hUv6QZSAL/EIaz7qVsxBl6UjDQ9z/SnMCDe24GK81bweWj6hHykSgWnw1v8Q5h3apRllMjD8TDzPpjFob6cZrah11JtMTOZfcJGCM5mvISW9dv8oZS9NiMDW9F2XNBHhdyeZgxgR4WxIHGnWb31ULxSJ1dWYzgwoVjddIQntPi7TQdwiM+FvxdHLzzw27j50hdYiRSIivkMvWg2WU8/Hpqsi3bYRCMvZZpIWXVr7LzqIra0Taqy1oKQKBgQDkHnAQhiccwR8gY9TJFPnCzWY48f31LzIyjX4ThJiUXsFVt+6InnKf9kTDSKLJdDwvV/oOdm6fDyXPsNWWbWW5Mz0VnVk7sDsR4LKlDhF42ARwMzJOaTL2Zow0+Jj54ejXF+ilvG4Xbm9287wOKs9KaX0E9kC23X293YQBwvuxCwKBgQDLSsIxZ0qnt40OvA9ceBxPUIaHAYsZD3xJ91i+zvY8sEinX4Qtmr4PHctGkjiHqUhWcWopynl4KEyEGpfZdDQb3RZkqQubHGI8B6QGYppxmY+ZpZgiju6CxGMkc2B6wiSg2ZPOd3C8gtFi4zoSuf+e+ULLA7NN/EpNAW7crUuiIwKBgQDB+Be2FTxAuP3snraaXNmbCOg415vn1b/LOIDRu9e5oXCnoISVapVw6wjBPIwe3FUotJgNoCqj2U9/cI/NHyx189mm/WqY1FsF8pg0vOeiZ5wug3ntLoqE9eGWUSRSpgNEkjHia3z5M5BYBxwKt+AXF+eURof0dhWrUiYjEVbOzQKBgDG2/5SLWRN47NMMpIkNiobO/4cmlnizc8a26YoIHOmshqf/0NhCPnZ958dDXPW4LUuOgtLjhB8hY8zun/H62/9watiFCqSNJWzexXsWRATB8/80IudYovVxx3kTiiWbMs/vRZk5VmWTbFdNkFciC2Q6Ot5g8kmBCD6auhzXeGaXAoGASNjiPGuNmJpG0t+W8xa6jmttCakoCSdbsQ+8jma1R/Lt9zOty90IQD5xIu4YWZsiNNQ4oxWmKgImRMRdaxFGXGlXttcpBfiA8E8I9hqOsLIn/J4PnQTlx9x8uWyYXilZhxD/jS4U/r4kWxw4h0rwbKB8l+idCEuahpSPC4GgoOo=";
    public static final String RSA_PRIVATE = "";
    private static final int SDK_PAY_FLAG = 1;


    String TAG = "FlowProductDetailActivity";


    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID);
    // The following are only used inPayPalFuturePaymentActivity.
// 下面的这些都是要用到授权支付才用到的，不用就注释掉可以了
//        .merchantName("Example Merchant")
//        .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
//            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));


    //接收广播的处理-------支付成功后处理
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean order_status = intent.getBooleanExtra("order_status", false);
            if (order_status) {
                ll_pay_info.setVisibility(View.GONE);
                ll_order_status.setVisibility(View.VISIBLE);
                iv_result.setBackground(getResources().getDrawable(R.drawable.checking));
                AnimationDrawable frameAnimation = (AnimationDrawable) iv_result.getBackground();  //获得动画图片列表animation-list
                frameAnimation.start();
                Toast.makeText(mContext, R.string.tip_wxpay_success, Toast.LENGTH_LONG).show();
            } else {
                ll_pay_info.setVisibility(View.GONE);
                ll_order_status.setVisibility(View.VISIBLE);
                iv_result.setBackground(getResources().getDrawable(R.drawable.failing));
                AnimationDrawable frameAnimation = (AnimationDrawable) iv_result.getBackground();  //获得动画图片列表animation-list
                frameAnimation.start();
                Toast.makeText(mContext, R.string.tip_wxpay_failed, Toast.LENGTH_LONG).show();
            }
        }
    };

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
                    Log.e(TAG, "正在支付");
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
                    } else {


                        Intent intent = new Intent();
                        intent.setAction(APIConstants.BR_ORDER_STATUS);
                        intent.putExtra("order_status", false);
                        sendBroadcast(intent);
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

    private void showNormalDialogOne() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */

        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
        normalDialog.setTitle("");
        normalDialog.setMessage(R.string.tip_pay_failed);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // delehistory();//dosomething
                    }
                });
        normalDialog.show();
    }

    private void SendQuery() {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[2];
        params[0] = new OkHttpClientManager.Param("out_order_no", APIConstants.OUT_TRADE_NO);
        Log.d(TAG, "SendQuery: "+APIConstants.OUT_TRADE_NO+APIConstants.sn);
        params[1] = new OkHttpClientManager.Param("device_no", APIConstants.sn);
        new SendRequest(APIConstants.Query, params, new MyHandler(), 3);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_product_detail);

        String runningOnCreate = "Running OnCreate";

        mContext = FlowProductDetailActivity.this;
        iWXApi = WXAPIFactory.createWXAPI(this, APIConstants.WX_APP_ID);

        webView = (WebView) findViewById(R.id.webview);
        pb_progress = (ProgressBar) findViewById(R.id.pb_progress);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        rl_card_tools = findViewById(R.id.rl_card_tools);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_price = (TextView) findViewById(R.id.tv_price);

        tv_product_name = findViewById(R.id.tv_product_name);
        tv_coverage = findViewById(R.id.tv_coverage);
        tv_effective_days = findViewById(R.id.tv_effective_days);

        bt_commit = (Button) findViewById(R.id.bt_commit);
        payPalLayout = (LinearLayout) findViewById(R.id.paypal_layout);
        alipayLayout = (LinearLayout) findViewById(R.id.alipay_tools);
        ll_wechat_tools = findViewById(R.id.ll_wechat_tools);

        wechatCheck = (CheckBox) findViewById(R.id.wechat_check);
        paypalCheck = (CheckBox) findViewById(R.id.paypal_check);
        alipayCheck = (CheckBox) findViewById(R.id.alipay_check);
        cardCheck = findViewById(R.id.card_check);

        bt_buy_now = (Button) findViewById(R.id.bt_buy_now);
        tv_price_type = (TextView) findViewById(R.id.tv_price_type);
        tv_package_name = (TextView) findViewById(R.id.tv_package_name);
        tv_pay_price = (TextView) findViewById(R.id.tv_pay_price);
        tv_sn = (TextView) findViewById(R.id.tv_sn);
        tv_money_type = (TextView) findViewById(R.id.tv_money_type);
        iv_result = (ImageView) findViewById(R.id.iv_result);
        bt_close = (Button) findViewById(R.id.bt_close);
        tv_start_date = (TextView) findViewById(R.id.tv_start_date);
        tv_total_flow = (TextView) findViewById(R.id.tv_total_flow);

        ll_pay = (LinearLayout) findViewById(R.id.ll_pay);
        ll_pay.setVisibility(View.GONE);
        ll_pay_info = (LinearLayout) findViewById(R.id.ll_pay_info);
        ll_order_status = (LinearLayout) findViewById(R.id.ll_order_status);

        purchaseCountLayout = findViewById(R.id.layout_purchase_day);
        numberButton = findViewById(R.id.number_button);





        product_id = getIntent().getStringExtra("product_id");
        url = APIConstants.server_host + getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        price = getIntent().getStringExtra("price");
        price_type = getIntent().getStringExtra("price_type");
        activedays = getIntent().getStringExtra("activedays");

        coverage = getIntent().getStringExtra("coverage");
        total_flow = getIntent().getStringExtra("total_flow");
        if (total_flow == null || total_flow.isEmpty() || total_flow.equals("unlimited")){
            total_flow = getResources().getString(R.string.tag_unlimited);
        }
        effective_days = getIntent().getStringExtra("effective_days");
        notice = getIntent().getStringExtra("notice");
        product_name = title;
        coverImage = getIntent().getStringExtra("coverImage");
        productnumber = getIntent().getStringExtra("productnumber");
        activedays = getIntent().getStringExtra("activedays");
        producttype = getIntent().getStringExtra("producttype");

        int position = getIntent().getIntExtra("position", 0);
        iv_product_pic =findViewById(R.id.iv_product_pic);
        iv_product_pic.setImageResource(pic[position % 4]);

        if (price_type.equals("RMB") || price_type.equals("CNY")) {
            creditCardAvailable = false;
        } else {
            creditCardAvailable = true;
        }
        if (producttype.equals("DAILY")&&effective_days.equals("1")){

            purchaseCountLayout.setVisibility(View.VISIBLE);
            numberButton.setBuyMax(Integer.parseInt(activedays));
            if (activedays.equals("1")){


            }
            numberButton.setOnWarnListener(new NumberButton.OnWarnListener() {
                @Override
                public void onWarningForInventory(int inventory) {
                    ImageView add= numberButton.findViewById(R.id.button_add);
//                    add.setImageDrawable(getResources().getDrawable(R.mipmap.icon_max));
                }

                @Override
                public void onWarningForBuyMax(int max) {

                 ImageView add= numberButton.findViewById(R.id.button_add);
//                    add.setImageDrawable(getResources().getDrawable(R.mipmap.icon_max));

//                 add.setColorFilter(Color.GRAY);
                    String string = getString(R.string.title_buy_warn)+max+getString(R.string.tilte_number);


                   Toast.makeText(FlowProductDetailActivity.this,string,Toast.LENGTH_SHORT).show();

                }

                @Override
                public void numberAdd(int number) {


//                    Toast.makeText(FlowProductDetailActivity.this,"数量增加",Toast.LENGTH_SHORT).show();


                }

                @Override
                public void numberSub(int number) {
                    ImageView add= numberButton.findViewById(R.id.button_add);
//                    add.setColorFilter(Color.WHITE);


//                    add.setImageDrawable(getResources().getDrawable(R.mipmap.increase_eleme));
//                    Toast.makeText(FlowProductDetailActivity.this,"数量减少",Toast.LENGTH_SHORT).show();

                }
            });

        }

        tv_title.setText(product_name);
        tv_price.setText(price);
        tv_price_type.setText(price_type);
        tv_package_name.setText(product_name);
        tv_pay_price.setText(price);
        tv_sn.setText(getResources().getText(R.string.tip_device_sn) + APIConstants.sn);
        tv_money_type.setText(price_type);
        tv_total_flow.setText(total_flow);
        tv_product_name.setText(product_name);
        tv_coverage.setText(coverage);
        tv_effective_days.setText(effective_days + " " + getResources().getString(R.string.day));

        if (creditCardAvailable) {
            rl_card_tools.setVisibility(View.VISIBLE);
            cardCheck.setChecked(true);
            payType = "CARD";

            payPalLayout.setVisibility(View.GONE);
            alipayLayout.setVisibility(View.GONE);
            ll_wechat_tools.setVisibility(View.GONE);
        } else {
            alipayLayout.setVisibility(View.VISIBLE);
//            ll_wechat_tools.setVisibility(View.GONE);
            ll_wechat_tools.setVisibility(View.VISIBLE);
            wechatCheck.setChecked(true);
            payType = "WXPAY";

            rl_card_tools.setVisibility(View.GONE);
            payPalLayout.setVisibility(View.GONE);
        }

        bt_buy_now.setVisibility(View.VISIBLE);

        iv_back.setOnClickListener(this);
        bt_commit.setOnClickListener(this);
        bt_buy_now.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        bt_close.setOnClickListener(this);
        tv_start_date.setOnClickListener(this);

        wechatCheck.setOnClickListener(this);
        paypalCheck.setOnClickListener(this);
        alipayCheck.setOnClickListener(this);
        cardCheck.setOnClickListener(this);

        webView.getSettings().setJavaScriptEnabled(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);//关键点
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setLoadWithOverviewMode(true);
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Toast.makeText(mContext, getResources().getText(R.string.system_webview_error), Toast.LENGTH_SHORT).show();
            }
        });

        webView.loadUrl(url);

        //注册接受的广播
        IntentFilter filter = new IntentFilter(APIConstants.BR_ORDER_STATUS);
        FlowProductDetailActivity.this.registerReceiver(broadcastReceiver, filter);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
    }

//    @Override
//    protected void onRestart(){
//        super.onRestart();
//        wechatCheck.setChecked(false);
//        paypalCheck.setChecked(false);
//        alipayCheck.setChecked(false);
//        cardCheck.setChecked(false);
//    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_commit:
                ll_pay.setVisibility(View.VISIBLE);
                ll_pay_info.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_buy_now:


                if (numberButton.getVisibility()==View.GONE){
                    productcount = "1";
                }else {

                    productcount=String.valueOf( numberButton.getNumber());
                }

                Log.i("999999999999--",productcount);
                String start_date = tv_start_date.getText().toString().replace(getString(R.string.tip_start_date), "").replace(getString(R.string.tip_select_start_date), "");
//                if (TextUtils.isEmpty(start_date)) {
//                    Toast.makeText(mContext, R.string.warning_input_start_date, Toast.LENGTH_LONG).show();
//                    return;
//                }

                if (payType.equals("WXPAY") || payType.equals("ALIPAY")) {
                    int paramCount = 5;
                    int what=1;

                    OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[paramCount];
                    params[0] = new OkHttpClientManager.Param("device_no", APIConstants.sn);
                    params[1] = new OkHttpClientManager.Param("productid", product_id);
                    params[2] = new OkHttpClientManager.Param("productcount", productcount);
                    params[3] = new OkHttpClientManager.Param("currency", price_type);
                    params[4] = new OkHttpClientManager.Param("payment_schema", payType);
                    if (payType.equals("WXPAY")){

                        what=4;
                    }else {


                    }

                    new SendRequest(APIConstants.Payment, params, new MyHandler(), what);

                } else if (payType.equals("CARD")){
                    Intent paymentIntent;
                    paymentIntent = new Intent(FlowProductDetailActivity.this, CardActivity.class);
                    paymentIntent.putExtra("productid", product_id);
                    paymentIntent.putExtra("productcount", productcount);
                    paymentIntent.putExtra("currency", price_type);
                    paymentIntent.putExtra("payment_schema", payType);
                    paymentIntent.putExtra("price", price);
                    startActivity(paymentIntent);
                }

                break;

            case R.id.tv_start_date:
                String start = CommUtil.getDateNow();
                TimeSelector timeSelector = new TimeSelector(mContext, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        String[] temps = time.split(" ");
                        tv_start_date.setText(getString(R.string.tip_start_date) + temps[0]);
//                        Toast.makeText(getApplicationContext(), time, Toast.LENGTH_LONG).show();
                    }
                }, start, "2025-12-31 23:59");
                timeSelector.setMode(TimeSelector.MODE.YMD);
                timeSelector.show();
                break;

            case R.id.wechat_check:
                payType = "WXPAY";
                Log.e(TAG, "payType = " + payType);
                wechatCheck.setChecked(true);
                paypalCheck.setChecked(false);
                alipayCheck.setChecked(false);
                cardCheck.setChecked(false);
                break;

            case R.id.paypal_check:
                payType = "PAYPAL";
                Log.e(TAG, "payType = " + payType);
                wechatCheck.setChecked(false);
                paypalCheck.setChecked(true);
                alipayCheck.setChecked(false);
                cardCheck.setChecked(false);

                break;

            case R.id.alipay_check:
                payType = "ALIPAY";
                Log.e(TAG, "payType = " + payType);
                wechatCheck.setChecked(false);
                paypalCheck.setChecked(false);
                alipayCheck.setChecked(true);
                cardCheck.setChecked(false);

                break;
            case R.id.card_check:
                payType = "CARD";
                Log.e(TAG, "payType = " + payType);
                wechatCheck.setChecked(false);
                paypalCheck.setChecked(false);
                alipayCheck.setChecked(false);
                cardCheck.setChecked(true);
                break;

            case R.id.iv_close:
            case R.id.bt_close:
                ll_pay.setVisibility(View.GONE);
                ll_pay_info.setVisibility(View.VISIBLE);
                ll_order_status.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    ll_pay_info.setVisibility(View.GONE);
                    iv_result.setVisibility(View.VISIBLE);
                    iv_result.setBackground(getResources().getDrawable(R.drawable.checking));
                    PayPalPayment payPalPayment = confirm.getPayment();
                    AnimationDrawable frameAnimation = (AnimationDrawable) iv_result.getBackground();  //获得动画图片列表animation-list
                    frameAnimation.start();

                    try {
                        String mPaymentId = confirm.toJSONObject().getJSONObject("response").getString("id");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    final class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                pb_progress.setVisibility(View.GONE);
            } else {
                if (pb_progress.getVisibility() == View.GONE)
                    pb_progress.setVisibility(View.VISIBLE);
                pb_progress.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    /*
     * This method shows use of optionalpayment details and item list.
     */
    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal("0.01"), "USD", title, paymentIntent);
    }

    private PayPalPayment getStuffToBuy(String paymentIntent) {
        //--- include an item list, payment amountdetails
        PayPalItem[] items =
                {
                        new PayPalItem(title, 1, new BigDecimal(price), "USD",
                                "tfms_paypal_" + product_id)
                };
        BigDecimal subtotal = PayPalItem.getItemTotal(items);
        BigDecimal shipping = new BigDecimal("0.0");
        BigDecimal tax = new BigDecimal("0.0");
        PayPalPaymentDetails paymentDetails = new PayPalPaymentDetails(shipping, subtotal, tax);
        BigDecimal amount = subtotal.add(shipping).add(tax);
        PayPalPayment payment = new PayPalPayment(amount, "USD", title, paymentIntent);
        payment.items(items).paymentDetails(paymentDetails);

        //--- set other optional fields likeinvoice_number, custom field, and soft_descriptor
//        payment.custom("This is text that will be associatedwith the payment that the app can use.");

        return payment;
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
                                PayTask alipay = new PayTask(FlowProductDetailActivity.this);
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
                        final String message;
                        if (responseData.code == -1) {
                            message = getResources().getString(R.string.title_error1);
                        } else if (responseData.code == -2) {
                            message = getResources().getString(R.string.title_error2);
                        } else if (responseData.code == -3) {
                            message = getResources().getString(R.string.title_error3);
                        } else if (responseData.code == -5) {
                            message = getResources().getString(R.string.title_error4);
                        } else if (responseData.code == -6) {
                            message = getResources().getString(R.string.title_error5);
                        } else {
                            message = responseData.msg;
                        }
                        Log.i("errorMessage", responseData.msg);
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    if (responseData.code == 1) {
                        APIConstants.OUT_TRADE_NO = responseData.data.get("trans_no").toString();
                        Intent intent = new Intent(mContext, CreditActivity.class);
                        intent.putExtra("weburl", responseData.data.get("pay_url").toString());
                        startActivity(intent);
                    } else {
                        Log.i("ErrorMessage", responseData.msg);
                        Toast.makeText(mContext, responseData.msg, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:
                    Log.i("eeeeeeee",responseData.msg);

                    if (responseData.code == 0) {
                        Intent intent = new Intent();
                        intent.setAction(APIConstants.BR_ORDER_STATUS);
                        intent.putExtra("order_status", true);
                        sendBroadcast(intent);
                    } else {
                        Intent intent = new Intent();
                        Log.i("eeeeeeee",responseData.msg);
                        intent.setAction(APIConstants.BR_ORDER_STATUS);
                        intent.putExtra("order_status", false);
                        sendBroadcast(intent);
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
                            PayUtil.WXPayForOrder(FlowProductDetailActivity.this,
                                    iWXApi, APIConstants.WX_APP_ID,
                                    responseData.data.get("prepayid").toString(),
                                    responseData.data.get("partnerid").toString(),
                                    responseData.data.get("noncestr").toString(),
                                    responseData.data.get("timestamp").toString(),
                                    responseData.data.get("sign").toString());
                        }
                    } else {
                        final String message;
                        if (responseData.code == -1) {
                            message = getResources().getString(R.string.title_error1);
                        } else if (responseData.code == -2) {
                            message = getResources().getString(R.string.title_error2);
                        } else if (responseData.code == -3) {
                            message = getResources().getString(R.string.title_error3);
                        } else if (responseData.code == -5) {
                            message = getResources().getString(R.string.title_error4);
                        } else if (responseData.code == -6) {
                            message = getResources().getString(R.string.title_error5);
                        } else {
                            message = responseData.msg;
                        }
                        Log.i("errorMessage", responseData.msg);
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}