package com.umengshared.lwc.myumengshared.Tools.DownLoadTools;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.umengshared.lwc.myumengshared.R;
import com.umengshared.lwc.myumengshared.Tools.MyToast;
import com.umengshared.lwc.myumengshared.activity.ZTNHMainActivity;

import java.io.File;

/**
 * @Description: DownLoadFileService 文件下载服务
 * @Author: lingwancai
 * @Time: 2018/8/1 10:12
 */

public class DownLoadFileService extends Service {

    /**
     * 下载监听
     */
    private DownLoadListener loadListener = new DownLoadListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1,getNotification("DownLoading...",progress));
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSuccess() {
            mDownLoadTask = null;
            //关闭前台通知 并创建一个成功通知
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("DownLoad success",-1));
            MyToast.showToast("DownLoad success");

        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onFailer() {
            mDownLoadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("DownLoad failer",-1));
            MyToast.showToast("DownLaod failer");

        }

        @Override
        public void onPaused() {
            mDownLoadTask = null;
            MyToast.showToast("Paused");

        }

        @Override
        public void onCanceled() {
            mDownLoadTask = null;
            stopForeground(true);
            MyToast.showToast("Cancel");


        }
    };
    private DownLoadTask mDownLoadTask;
    private String downloadurl = null;

    public DownLoadFileService() {
    }

    /**
     * DownLoadBinder
     * <p>
     * 建立与activity通信
     *
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mloadBinder;
    }

    DownLoadBinder mloadBinder = new DownLoadBinder();

    public class DownLoadBinder extends Binder {

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void startDownLoad(String url) {
            if (mDownLoadTask == null) {
                downloadurl = url;
                mDownLoadTask = new DownLoadTask(loadListener);
                mDownLoadTask.execute(downloadurl);
                startForeground(1, getNotification("DownLoading...", 0));
                MyToast.showToast("startDownLoad");
            }

        }

        public void pauseDownLoad() {
            if (mDownLoadTask != null) {
                mDownLoadTask.pausedDownload();
            }
        }

        public void cancelDownLoad() {
            if (mDownLoadTask != null) {
                mDownLoadTask.canceDownload();
            }

            //取消下载需删除文件 并关闭通知
            if (downloadurl != null) {
                String fileName = downloadurl.substring(downloadurl.lastIndexOf("/"));
                String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                File file = new File(directory + fileName);
                if (file.exists()) {
                    file.delete();
                }
                getNotificationManager().cancel(1);
                stopForeground(true);
                MyToast.showToast("cancelDownLoad");
            }
        }

    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    /**
     * 创建前台通知
     *
     * @param title
     * @param progress
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification getNotification(String title, int progress) {

        //分组（可选）
        //groupId要唯一
        String groupId = "group_001";
        NotificationChannelGroup group = new NotificationChannelGroup(groupId, "广告");

        //创建group
        getNotificationManager().createNotificationChannelGroup(group);

        //channelId要唯一
        String channelId = "channel_001";

        NotificationChannel adChannel = new NotificationChannel(channelId,
                "推广信息", NotificationManager.IMPORTANCE_DEFAULT);
        //补充channel的含义（可选）
        adChannel.setDescription("推广信息");
        //将渠道添加进组（先创建组才能添加）
        adChannel.setGroup(groupId);
        //创建channel
        getNotificationManager().createNotificationChannel(adChannel);

        Intent intent = new Intent(this, ZTNHMainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        builder.setContentTitle(title);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(pendingIntent);
        builder.setChannelId(channelId);
        if (progress > 0) {//大于0才显示进度
            builder.setContentText(progress + "%");
            builder.setProgress(100,progress,false);
        }
        return builder.build();
    }
}
