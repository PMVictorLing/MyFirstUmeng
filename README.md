# 文件下载工具类
项目中直接可以用
1.运用异步任务类（AsyncTask）结合前台服务service

 /**
     * 执行下载逻辑
     *
     * @param strings
     * @return
     */
    @Override
    protected Integer doInBackground(String... strings) {

        InputStream in = null;
        RandomAccessFile saveFile = null;

        //记录文件下载的长度
        long downLoadLength = 0;
        //文件下载路径
        String downLoadUrl = strings[0];
        //获取文件名称
        String fileName = downLoadUrl.substring(downLoadUrl.lastIndexOf("/"));
        //获取系统文件目录
        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        File file = new File(directory + fileName);
        if (file.exists()) {
            downLoadLength = file.length();
        }
        try {
            long contentLength = getContentLength(downLoadUrl);
            if (contentLength == 0) {
                return TYPE_FAILER;
            } else if (contentLength == downLoadLength) {//下载字节和文件总字节相等 下载已完成
                return TYPE_SUCCUSS;
            }
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    //断点下载，指定从那个字节开始
                    .addHeader("RANGE", "bytes=" + downLoadLength + "-")
                    .url(downLoadUrl).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response != null) {
                //获取输入流
                in = response.body().byteStream();
                //输出流保存file
                saveFile = new RandomAccessFile(file, "rw");
                //跳过已下载的字节
                saveFile.seek(downLoadLength);
                byte[] bytes = new byte[1024];
                int total = 0;
                int len = 0;
                while ((len = in.read(bytes)) != -1) {
                    if (isCanceled) {
                        return TYPE_CANCELED;
                    } else if (isPaused) {
                        return TYPE_PAUSED;
                    } else {
                        total += len;
                        saveFile.write(bytes, 0, len);
                        //计算已下载的百分比
                        int progress = (int) ((total + downLoadLength) * 100 / contentLength);
                        publishProgress(progress);
                    }
                }
                response.body().close();
                return TYPE_SUCCUSS;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭io流
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (saveFile != null) {
                try {
                    saveFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            if (isCanceled && file != null) {
                file.delete();
            }
        }

        return TYPE_FAILER;
    }

    /**
     * 获取待下载文件的总长度
     *
     * @param downLoadUrl
     */
    private long getContentLength(String downLoadUrl) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(downLoadUrl).build();
        Response response = okHttpClient.newCall(request).execute();
        if (response != null && response.isSuccessful()) {
            long contentLength = response.body().contentLength();
            response.body().close();
            return contentLength;
        }
        return 0;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        switch (integer) {
            case TYPE_SUCCUSS:
                mLoadListener.onSuccess();
                break;
            case TYPE_FAILER:
                mLoadListener.onFailer();
                break;
            case TYPE_PAUSED:
                mLoadListener.onPaused();
                break;
            case TYPE_CANCELED:
                mLoadListener.onCanceled();
                break;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        int progress = values[0];
        if (progress > mLastProgress) {
            mLoadListener.onProgress(progress);
            mLastProgress = progress;
        }

    }


2.具体使用请参考项目代码。
