package com.umengshared.lwc.myumengshared.Tools.DownLoadTools;

/**
 * 下载回调
 * Created by lingwancai on
 * 2018/8/1 08:32
 */
public interface DownLoadListener {

    /**
     * 下载进度
     * @param progress
     */
    void onProgress(int progress);

    /**
     * 下载成功
     */
    void onSuccess();

    /**
     * 下载失败
     */
    void onFailer();

    /**
     * 暂停
     */
    void onPaused();

    /**
     * 取消
     */
    void onCanceled();
}
