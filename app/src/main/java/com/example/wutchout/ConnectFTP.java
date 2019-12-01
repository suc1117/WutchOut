package com.example.wutchout;

import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;

public class ConnectFTP {
    private final String TAG = "Connect FTP";
    public FTPClient mFTPClient = null;

    public ConnectFTP(){
        mFTPClient = new FTPClient();
    }

    public boolean ftpConnect(String host, String username, String password, int port) {
        boolean result = false;
        try{
            mFTPClient.connect(host, port);
            if(FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
                result = mFTPClient.login(username, password);
                mFTPClient.enterLocalPassiveMode();
            }
        }
        catch (Exception e) { Log.d(TAG, "Couldn't connect to host"); }
        return result;
    }

    public boolean ftpDisconnect() {
        boolean result = false;
        try {
            mFTPClient.logout();
            mFTPClient.disconnect();
            result = true;
        }
        catch (Exception e) { Log.d(TAG, "Failed to disconnect with server"); }
        return result;
    }

    public boolean ftpChangeDirctory(String directory) {
        try{
            mFTPClient.changeWorkingDirectory(directory);
            return true;
        }catch (Exception e){
            Log.d(TAG, "Couldn't change the directory");
        }
        return false;
    }

    public String ftpGetDirectory(){
        String directory = null;
        try{
            directory = mFTPClient.printWorkingDirectory();
        } catch (Exception e){
            Log.d(TAG, "Couldn't get current directory");
        }
        return directory;
    }

    public String[][] ftpGetFileList(String directory) {
        String[][] fileList = null;
        int i = 0;
        try {
            FTPFile[] ftpFiles = mFTPClient.listFiles(directory);
            fileList = new String[ftpFiles.length][2];
            for(FTPFile file : ftpFiles) {
                String fileName = file.getName();
                if (file.isFile()) {
                    fileList[i][0] = fileName;
                    fileList[i][1] = "(File)";
                }
                else {
                    fileList[i][0] = fileName;
                    fileList[i][1] = "(Directory)";
                }
                i++;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return fileList;
    }
}
