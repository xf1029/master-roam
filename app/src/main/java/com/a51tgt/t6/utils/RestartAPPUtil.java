package com.a51tgt.t6.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.a51tgt.t6.service.killSelfService;

/**
 * Created by liu_w on 2017/9/24.
 */

public class RestartAPPUtil {
    /**
     * 重启整个APP
     * @param context
     */
    public static void restartAPP(Context context){
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
    /**
     * *重启整个APP
     * @param Delayed 延迟多少毫秒
     * */
    public static void restartAPP(Context context, long Delayed){
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + Delayed, restartIntent); // 1秒钟后重启应用
        System.exit(0);
    }
}
