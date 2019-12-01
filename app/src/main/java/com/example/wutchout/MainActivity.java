package com.example.wutchout;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.lang.String;


public class MainActivity extends AppCompatActivity {

    Activity ac = this;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerViewAdapter recyclerViewAdapter;
    boolean status, image_flag = true;
    int i, last_index=0;
    private ConnectFTP connectFTP;
    private final String TAG = "FTP ";

    String Lately_Accident_File, currentPath, newFilePath, time_val, gps_lat, gps_lon;
    String[] FileParsingArray;
    String[][] currentFileList;
    ImageView imageView;
    Thread getImageThread;
    File file;
    TextView textView_gps_lat, textView_gps_lon, textView_time;

    private TextView dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectFTP = new ConnectFTP();
        recyclerView = findViewById(R.id.recyclerView);
        textView_gps_lat = findViewById(R.id.textview_gps_lat);
        textView_gps_lon = findViewById(R.id.textview_gps_lon);
        textView_time = findViewById(R.id.textview_time);

        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, linearLayoutManager.getOrientation()));
        recyclerView.setLayoutManager(linearLayoutManager);

        imageView = findViewById(R.id.imageView1);
        dir = findViewById(R.id.currentDir);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    status = false;
                    status = connectFTP.ftpConnect(
                            "117.16.174.33", //"211.229.241.115",
                            "5678",
                            "56785678",
                            21);

                    if (!status == true) {
                        Log.d(TAG, "Connection failed");
                    } else {
                        Log.d(TAG, "Connection Success");
                        List<myFile> myfile = new ArrayList<>();
                        currentPath = connectFTP.ftpGetDirectory();
                        dir.setText("Path : " + currentPath);
                        currentFileList = connectFTP.ftpGetFileList(currentPath);
                        for (i=0; i<currentFileList.length; i++) {
                            Log.d(TAG, currentFileList[i][0] + currentFileList[i][1]);
                        }
                        last_index=i;
                        Lately_Accident_File=currentFileList[last_index-1][0];
                        if (image_flag) {
                            getImageThread.start();
                        }

                        for (int i = 0; i < currentFileList.length; i++) {
                            myfile.add(new myFile(currentFileList[i][0], currentFileList[i][1]));
                        }
                        recyclerViewAdapter = new RecyclerViewAdapter(ac, myfile);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setAdapter(recyclerViewAdapter);
                                File imgFile = new  File(getApplicationContext().getFilesDir().toString(),"main.png");
                                if(imgFile.exists()){
                                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.toString());
                                    imageView.setImageBitmap(myBitmap);
                                }
                                FileParsingArray=Lately_Accident_File.split(" ");
                                time_val=FileParsingArray[0]+" "+FileParsingArray[1];
                                gps_lat=FileParsingArray[2];
                                gps_lon=FileParsingArray[3].substring(0,FileParsingArray[3].length()-4);
                                textView_time.setText(time_val);
                                textView_gps_lat.setText(gps_lat);
                                textView_gps_lon.setText(gps_lon);
                            }
                        });
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

        getImageThread = new Thread(new Runnable() {
            public void run() {
                image_flag=false;
                newFilePath = getApplicationContext().getFilesDir().toString();
                file = new File(newFilePath,"main.png");
                Log.d(TAG, "file : "+file);
                File parent_dir = file.getParentFile();
                Log.d(TAG, "parent_dir : "+parent_dir);
                if (parent_dir != null) {
                    parent_dir.mkdirs();
                    Log.d(TAG, "mkdir()");
                }
                Log.d(TAG, "newFilePath==desFilePath : "+newFilePath);
                try {
                    file.createNewFile();
                    Log.d(TAG, "file.createNewFile()");
                } catch (Exception e){}
                connectFTP.ftpDownloadFile(currentPath +currentFileList[last_index-1][0], file.toString());
            }
        });
    }
}

