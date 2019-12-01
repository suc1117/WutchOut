package com.example.wutchout;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;


public class MainActivity extends AppCompatActivity {

    Activity ac = this;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerViewAdapter recyclerViewAdapter;
    boolean status;
    List<File> file;

    private ConnectFTP connectFTP;
    private final String TAG = "FTP";

    String currentPath;
    String[][] currentFileList;
    ImageView imageView;

    private TextView dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectFTP = new ConnectFTP();
        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, linearLayoutManager.getOrientation()));
        recyclerView.setLayoutManager(linearLayoutManager);

        imageView = findViewById(R.id.imageView1);
        dir = findViewById(R.id.currentDir);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    status = false;
                    status = connectFTP.ftpConnect(
                            "211.229.241.115",
                            "5678",
                            "56785678",
                            21);

                    if (!status == true) {
                        Log.d(TAG, "Connection failed");
                    } else {
                        Log.d(TAG, "Connection Success");
                        List<File> file = new ArrayList<>();
                        currentPath = connectFTP.ftpGetDirectory();
                        dir.setText("Path : " + currentPath);
                        currentFileList = connectFTP.ftpGetFileList(currentPath);

                        for (int i = 0; i < currentFileList.length; i++) {
                            file.add(new File(currentFileList[i][0], currentFileList[i][1]));
                        }
                        recyclerViewAdapter = new RecyclerViewAdapter(ac, file);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setAdapter(recyclerViewAdapter);
                                Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);
                            }
                        });
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}

