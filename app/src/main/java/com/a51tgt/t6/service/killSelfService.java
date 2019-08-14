package com.a51tgt.t6.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.a51tgt.t6.comm.TcpConfig;
import com.a51tgt.t6.net.TcpUtil;
import com.a51tgt.t6.ui.SplashActivity;
import com.a51tgt.t6.utils.RestartAPPUtil;

/**
 * Created by liu_w on 2017/9/24.
 */

public class killSelfService  extends Service {
    /**关闭应用后多久重新启动*/
    private static  long stopDelayed=2000;
    private Handler handler;
    private String PackageName;

    Runnable restartRunnable = new Runnable(){
        @Override
        public void run() {
            Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(PackageName);
            startActivity(LaunchIntent);
            killSelfService.this.stopSelf();
        }
    };

    public killSelfService() {
        handler=new Handler();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        stopDelayed=intent.getLongExtra("Delayed",2000);
        PackageName=intent.getStringExtra("PackageName");
        handler.postDelayed(restartRunnable,stopDelayed);
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
