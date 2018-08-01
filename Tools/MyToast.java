package com.umengshared.lwc.myumengshared.Tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.umengshared.lwc.myumengshared.App;
import com.umengshared.lwc.myumengshared.activity.JavaCallJsActivity;

//import com.umengshared.lwc.myumengshared.SampleApplicationLike;


/**
 * Toast
 *
 * @author 凌万财
 * @version v0.1
 * @date 2016/4/27 9:22
 * @since v0.1
 */

public class MyToast {

    /**
     * 正常显示的Toast
     *
     * @param msg
     */
    public static void showToast(String msg) {
        if (!Tool.isNull(msg)) {
            msg = msg.trim();
            if (msg.equals(("java.net.ConnectException: failed to " +
                    "connect to /192.168.1.189 (port 887) after 2500ms: connect failed:" +
                    " ENETUNREACH (Network is unreachable)").trim())) {
                msg = "网络异常";
            }
            MyLog.e("bug", "" + msg);
            Toast.makeText(App.getInstance().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 测试热更新 异常
     *
     * @param msg
     */
    public static void showToastNull(String msg) {
        if  (!Tool.isNull(msg)) {
            if (msg.equals(("java.net.ConnectException: failed to " +
                    "connect to /192.168.1.189 (port 887) after 2500ms: connect failed:" +
                    " ENETUNREACH (Network is unreachable)").trim())) {
                msg = "网络异常";
            }
            MyLog.e("bug", "" + msg);
            Toast.makeText(App.getInstance().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    }

    /**
     *  弹框测试
     */
    public static void showMyDialog(Activity activity){
        AlertDialog.Builder b = new AlertDialog.Builder(activity);
        b.setTitle("测试弹框");
        b.setMessage("111");
        b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.setCancelable(false);
        b.create().show();
    }

}
