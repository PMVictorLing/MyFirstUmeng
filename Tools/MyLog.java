package com.umengshared.lwc.myumengshared.Tools;

import android.util.Log;


/**
 * Created by Administrator on 2015/12/4.
 */
public class MyLog {

    private static final String TAG="ztyy";

    public static void i(String tag, String msg){
        if (tag!=null&&msg!=null&& Config.DEBUG){
            Log.i(TAG, tag + "===" + msg);
        }
    }
    public static void e(String tag, String msg){
        if (tag!=null&&msg!=null&& Config.DEBUG){
            Log.e(TAG, tag + "===" + msg);
        }
    }
}
