package main.DownloadFiles;
// Created by LJF on 2017/5/18.

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DownloadNetworkFiles {

    private String url;

    public DownloadNetworkFiles(String url){
        this.url = url;
    }

    public void downloadUrlFile(){
        try {
            if(url == null || url.equals("")){
                return;
            }
            url = url.trim();  // Remove the space
            if(!url.contains("http://")){
                url = "http://" + url;
            }

            int beginIndex = url.indexOf("//");
            String subUrl = url.substring(beginIndex + 2, url.length());
            int secondIndex = subUrl.indexOf("/");
            String targetFileName = subUrl.substring(secondIndex + 1, subUrl.length());

            System.out.println(targetFileName);

            // Set the thread pool to download the config file firstly
            ExecutorService threadPool = Executors.newCachedThreadPool();
            DownUtil downUtil = new DownUtil(url, targetFileName, 1);
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    downloadThread(downUtil);
                    System.out.println("The thread that downloaded the config file is finished.");
                }
            });
            threadPool.shutdown();
            threadPool.awaitTermination(10, TimeUnit.SECONDS);

            if(targetFileName.contains(".txt")){

            }else if(targetFileName.contains(".jpg")){

            }else if(targetFileName.contains(".png")){

            }

            System.out.println("The thread of interface is complete.");

        } catch (InterruptedException ie) {
            ie.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void downloadThread(DownUtil downUtil) {
        try {
            downUtil.download();
            new Thread(()->{
                while(downUtil.getCompleteRate() < 1){
                    System.out.println("The child thread of download files is finished: " + downUtil.getCompleteRate());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
