package main.DownloadFiles;
// Created by LJF on 2017/4/17.

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class DownUtil {

    private String path;
    private String targetFile;
    private int threadNum;
    private DownThread[] threads;
    private int fileSize;
    private class DownThread extends Thread{
        private int startPos;
        private int currentPartSize;
        private RandomAccessFile currentPart;
        public int length;
        public DownThread(int startPos, int currentPartSize, RandomAccessFile currentPart){
            this.startPos = startPos;
            this.currentPartSize = currentPartSize;
            this.currentPart = currentPart;
        }
        public void run(){
            try{
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5 * 1000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty(
                    "Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, "
                    + "application/x-shockwave-flash, application/xaml+xml, "
                    + "application/vnd.ms-xpsdocument, application/x-ms-xbap, "
                    + "application/x-ms-application, application/vnd.ms-excel, "
                    + "application/vnd.ms-powerpoint, application/msword, */*");
                conn.setRequestProperty("Accept-Language", "zh-CN");
                conn.setRequestProperty("Charset", "UTF-8");
                InputStream inStream = conn.getInputStream();
                inStream.skip(this.startPos);
                byte[] buffer = new byte[1024];
                int hasRead = 0;
                // Read network data and write to a local file

                while(length < currentPartSize && (hasRead = inStream.read(buffer)) !=  -1){
                    currentPart.write(buffer, 0, hasRead);
                    length += hasRead;
                }
                currentPart.close();
                inStream.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public DownUtil(String path, String targetFile, int threadNum) {
        this.path = path;
        this.threadNum = threadNum;
        this.threads = new DownThread[threadNum];
        this.targetFile = targetFile;
    }

    public void download() {
        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty(
                    "Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, "
                    + "application/x-shockwave-flash, application/xaml+xml, "
                    + "application/vnd.ms-xpsdocument, application/x-ms-xbap, "
                    + "application/x-ms-application, application/vnd.ms-excel, "
                    + "application/vnd.ms-powerpoint, application/msword, */*");
            conn.setRequestProperty("Accept-Language", "zh-CN");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Connection", "Keep-Alive");

            fileSize = conn.getContentLength();
            conn.disconnect();
            int currentPartSize = fileSize / threadNum + 1;
            RandomAccessFile file = new RandomAccessFile(targetFile, "rw");
            file.setLength(fileSize);
            file.close();
            // Multi-threaded download
            for(int i = 0; i < threadNum; ++i){
                int startPos = i * currentPartSize;
                RandomAccessFile currentPart = new RandomAccessFile(targetFile, "rw");
                currentPart.seek(startPos);
                // File file1 = new File("E:/jee-mars/project/NetworkAboutSocket/res/" + targetFile);
                threads[i] = new DownThread(startPos, currentPartSize, currentPart);
                threads[i].start();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println("The target file could not be found.");
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Gets the percentage of completion of the download
    public double getCompleteRate(){
        int sumSize = 0;
        for(int i = 0; i < threadNum; ++i){
            sumSize += threads[i].length;
        }
        return sumSize * 1.0 / fileSize;
    }
}
