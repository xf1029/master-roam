package com.a51tgt.t6.utils;

/**
 * Created by Administrator on 2017/8/21.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SPUtils {

    private static final String NAME = "jinding";
    private static final String KEY_USER = "key_user";
    private static final String KEY_DAY = "key_day";
    private static final String KEY_WASH = "key_wash";
    public static final String WELCOME = "welcome";
    public static final String KEY_ZXING = "key_zxing";
    public static final String KEY_PUSH = "key_push";
    public static final String KEY_SEARCH = "key_search";

    /**
     * 支付的标记判断
     * 1、扫码支付
     * 2、购卡支付
     * 3、商家购买服务
     */
    public static final String KEY_PAY = "key_pay";
    public static final String PAY_zxing = "1";
    public static final String PAY_card = "2";
    public static final String PAY_shop = "3";
    public static final String PAY_order = "4";
    public static final String PAY_seller = "5";


    static SharedPreferences sp;

    private SPUtils() {
    }

    public static SPUtils getInstance(Context context) {
        sp = context.getApplicationContext().getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return InnerUtils.objectUtils;
    }

    private static class InnerUtils {
        static SPUtils objectUtils = new SPUtils();
    }

    public static ArrayList<String> getSearch() {
        if (sp.contains(KEY_SEARCH)) {
            ArrayList list = new Gson().fromJson(sp.getString(KEY_SEARCH, ""), ArrayList.class);
            if (list == null) {
                list = new ArrayList();
            }
            return list;
        }
        return new ArrayList();
    }
    public static void putSearch(List<String> list) {
        SharedPreferences.Editor editor = sp.edit();
        String text = new Gson().toJson(list);
        //存储
        editor.putString(KEY_SEARCH, text);
        editor.apply();
    }

    public <T> void pushObject(T t, String key) {

        //创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //创建字节对象输出流
        ObjectOutputStream out = null;
        try {
            //然后通过将字对象进行64转码，写入key值为key的sp中
            out = new ObjectOutputStream(baos);
            out.writeObject(t);
            String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key, objectVal);
            editor.apply();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public <T> T popObject(String key) {
        if (sp.contains(key)) {
            String objectVal = sp.getString(key, null);
            byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
            //一样通过读取字节流，创建字节流输入流，写入对象并作强制转换
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                T t = (T) ois.readObject();
                return t;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }


    public void push(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String pop(String key) {
        return sp.getString(key, "");
    }

    public void pushWash(long value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(KEY_WASH, value);
        editor.apply();
    }

    public Long popWash() {
        return sp.getLong(KEY_WASH, 0);
    }


    /**
     * 存储当前是否   有推送消息
     *
     * @param flag
     */
    public void pushMsg(boolean flag) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("push_msg", flag);
        editor.apply();
    }

    public boolean popMsg() {
        return sp.getBoolean("push_msg", false);
    }

}