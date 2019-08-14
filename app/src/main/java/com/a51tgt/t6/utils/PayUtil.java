package com.a51tgt.t6.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.a51tgt.t6.R;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.a51tgt.t6.MZApplication;

/**
 * Created by liu_w on 2018/1/18.
 */

public class PayUtil {
    public static void WXPayForOrder(String app_id, String prepay_id, String partner_id, String package_info, String nonce_str, String time_stamp, String app_sign ){
        PayReq req = new PayReq();
        req.appId = app_id;
        req.partnerId = partner_id;
        req.prepayId = prepay_id;
        req.packageValue = "Sign=WXPay";
        req.nonceStr = nonce_str;
        req.timeStamp = time_stamp;
        req.sign = app_sign;

        boolean res = MZApplication.getInstance().getWxApi().sendReq(req);
        Log.i("wx_res", res + "");
    }

    public static void WXPayForOrder(Context context, IWXAPI iWxApi, String app_id,
                                     String prepay_id, String partner_id, String nonce_str, String time_stamp, String app_sign ) {
        //{
//        app_id = "wxb4ba3c02aa476ea1";
//        partner_id = "1900006771";
//        nonce_str = "4827656dad9c8098a4901e38818d6898";
//        time_stamp = "1516276100";
//        prepay_id = "wx201801181948203bed234e100041303818";
//        app_sign = "BA5972F5CEB226718F8755659E3B9E8A";

        Log.e("PayReq", "WXWXWX");
        PayReq req = new PayReq();
        req.appId = app_id;
        req.partnerId = partner_id;
        req.prepayId = prepay_id;
        req.packageValue = "Sign=WXPay";
        req.nonceStr = nonce_str;
        req.timeStamp = time_stamp;
//        req.packageValue = "WXPay";
        req.sign = app_sign;

        Log.e("PayUtil", "req.appId: " + req.appId);
        Log.e("PayUtil", "req.partnerId: " + req.partnerId);
        Log.e("PayUtil", "req.prepayId: " + req.prepayId);
        Log.e("PayUtil", "req.nonceStr: " + req.nonceStr);
        Log.e("PayUtil", "req.timeStamp: "+req.timeStamp);
        Log.e("PayUtil", "req.packageValue: "+req.packageValue);
        Log.e("PayUtil", "req.sign: " + req.sign);

        if (iWxApi.isWXAppInstalled()) {
            Log.e("PayUtil", "已安装微信");
            boolean res = iWxApi.sendReq(req);
            Log.i("wx_res", res + "");
        } else {
            Toast.makeText(context, R.string.tip_unistall_wetchat, Toast.LENGTH_LONG).show();

//            AlertDialog.Builder dialog = new AlertDialog.Builder(PaymentActivity);
//            dialog.setTitle("标题");
//            dialog.setMessage("具体信息");
//            isExit.setMessage(getResources().getString(R.string.tip_can_not_find_the_device));
//            isExit.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.commit_button), new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    isExit.dismiss();
//                }
//            });
//            isExit.show();

            Log.i("tag", "没有安装微信");

        }
    }
}
