package com.a51tgt.t6.utils;

/**
 * Created by liuke on 11/10/2016.
 */
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.a51tgt.t6.comm.APIConstants;

import java.util.HashMap;
import java.util.Locale;

public class AppLanguageUtils {

    private static HashMap<String, Locale> mAllLanguages = new HashMap<String, Locale>(7) {{
        put(APIConstants.ENGLISH, Locale.ENGLISH);
        put(APIConstants.SIMPLIFIED_CHINESE, Locale.SIMPLIFIED_CHINESE);

    }};

    @SuppressWarnings("deprecation")
    public static void changeAppLanguage(Context context, String newLanguage) {
        Log.i("dangqin", "changeAppLanguage: "+context);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();

        // app locale
        Locale locale = getLocaleByLanguage(newLanguage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }

        // updateConfiguration
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);
    }


    private static boolean isSupportLanguage(String language) {
        return mAllLanguages.containsKey(language);
    }

    public static String getSupportLanguage(String language) {
        if (isSupportLanguage(language)) {
            return language;
        }

        return APIConstants.ENGLISH;
    }

    /**
     * 获取指定语言的locale信息，如果指定语言不存在{@link #mAllLanguages}，返回本机语言，如果本机语言不是语言集合中的一种{@link #mAllLanguages}，返回英语
     *
     * @param language language
     * @return
     */
    public static Locale getLocaleByLanguage(String language) {
        if (isSupportLanguage(language)) {
            return mAllLanguages.get(language);
        } else {
            Locale locale = Locale.getDefault();
            for (String key : mAllLanguages.keySet()) {
                if (TextUtils.equals(mAllLanguages.get(key).getLanguage(), locale.getLanguage())) {
                    return locale;
                }
            }
        }
        return Locale.ENGLISH;
    }


    public static Context attachBaseContext(Context context, String language) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        } else {
            return context;
        }
    }


    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Resources resources = context.getResources();
        Locale locale = AppLanguageUtils.getLocaleByLanguage(language);
        Log.i("locallll",locale.toString());
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }
}