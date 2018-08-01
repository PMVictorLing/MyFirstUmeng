package com.umengshared.lwc.myumengshared.Tools;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.view.WindowManager;

import com.umengshared.lwc.myumengshared.App;

/**
 * Created by Administrator on 2016/9/19.
 */
public class Tool {

    public static final String WX_APPSECRET = "e703e9e133b1fe69cb684a965f62ac35";

    public static final String WX_APPID = "wx10ffaf8a4f304b67";

    public static final String QQ_APPID = "1105627227";

    public static final String QQ_APPKEY = "JFcUt9DlvcFR2EwF";

    /**
     * 判断输入对象不为空或不为空字符
     *
     * @param object
     * @return
     */
    public static boolean isNull(Object object) {
        if (object == null || object.equals("") || object.equals("null")) {
            return true;
        }
        return false;
    }
    /**
     * 设置界面透明度
     *
     * @param activity
     * @param alpha
     */
    public static void setWindow(Activity activity, float alpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = alpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 判断要分享的应用是否存在
     *
     * @param packageName 应用包名
     * @return
     */
    public static boolean checkInstallation(String packageName) {
        try {
            App.getInstance().getApplicationContext().getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);

            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

}
