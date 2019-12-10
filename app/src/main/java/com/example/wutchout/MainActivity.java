package com.example.wutchout;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.lang.String;


public class MainActivity extends AppCompatActivity {

    float warnDistance=100;

    Activity ac = this;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerViewAdapter recyclerViewAdapter;
    Location locAccident, locUser;
    double longitude;
    double latitude;
    boolean status, threadStop=false;
    int last_index=0;
    float distance;
    private ConnectFTP connectFTP;
    private final String TAG = "FTP ";
    private SharedPreferences sharePref;
    private SharedPreferences.Editor editor;
    LocationManager lm;
    private NotificationManager notifManager;
    private long time= 0;

    String latelyAccidentFile, currentPath, makeFilePath, time_val, gps_lat, gps_lon, distanceUnit;
    String[] FileParsingArray;
    String[] FileGpsArray;
    String[][] currentFileList;
    ImageView imageView, obstacleImageView;
    Button bgStart;
    Thread mainThread, getImageThread;
    File file;
    TextView textView_gps_lat, textView_gps_lon, textView_gps_lat_user, textView_gps_lon_user, textView_time, textView_distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectFTP = new ConnectFTP();

        sharePref = getSharedPreferences("SHARE_PREF", MODE_PRIVATE);
        editor = sharePref.edit();

        obstacleImageView = findViewById(R.id.imageView1);
        recyclerView = findViewById(R.id.recyclerView);
        textView_gps_lat = findViewById(R.id.textview_gps_lat);
        textView_gps_lon = findViewById(R.id.textview_gps_lon);
        textView_gps_lat_user = findViewById(R.id.textview_gps_lat_user);
        textView_gps_lon_user = findViewById(R.id.textview_gps_lon_user);
        textView_time = findViewById(R.id.textview_time);
        textView_distance = findViewById(R.id.textview_distance);
        bgStart = findViewById(R.id.btn_bgStart);

        linearLayoutManager = new LinearLayoutManager(this);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, linearLayoutManager.getOrientation()));
        recyclerView.setLayoutManager(linearLayoutManager);

        imageView = findViewById(R.id.imageView1);

        bgStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "App Start In Background...", Toast.LENGTH_SHORT).show();
            }
        });

        obstacleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GoogleMapActivity.class);
                intent.putExtra("lat",gps_lat);
                intent.putExtra("lon",gps_lon);
                startActivity(intent);
            }
        });

        mainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!threadStop) {
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
                        currentFileList = connectFTP.ftpGetFileList(currentPath);
                        last_index = currentFileList.length - 1;
                        latelyAccidentFile = currentFileList[last_index][0];
                        if (!(sharePref.getString("latelyAccident", "0").equals(latelyAccidentFile))) {
                            editor.putString("latelyAccident", latelyAccidentFile);
                            editor.commit();
                            getImageThread.start();
                        }

                        // Update Files List
                        for (int i = 0; i < currentFileList.length; i++) {
                            myfile.add(new myFile(currentFileList[i][0], currentFileList[i][1]));
                        }

                        // Update UI & ImageView
                        recyclerViewAdapter = new RecyclerViewAdapter(ac, myfile);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!threadStop) {
                                    recyclerView.setAdapter(recyclerViewAdapter);
                                    File imgFile = new File(getApplicationContext().getFilesDir().toString(), "main.png");
                                    if (imgFile.exists()) {
                                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.toString());
                                        imageView.setImageBitmap(myBitmap);
                                    }
                                    getMyLocation();
                                    FileParsingArray = latelyAccidentFile.split(" ");
                                    time_val = FileParsingArray[0] + " " + FileParsingArray[1];
                                    FileGpsArray = latelyAccidentFile.split(",");
                                    gps_lat = FileGpsArray[1];
                                    gps_lon = FileGpsArray[2].substring(0, FileGpsArray[2].length() - 4);

                                    locAccident = new Location("accident point");
                                    locAccident.setLatitude(Double.parseDouble(gps_lat));
                                    locAccident.setLongitude(Double.parseDouble(gps_lon));

                                    textView_time.setText(time_val);
                                    textView_gps_lat.setText(gps_lat);
                                    textView_gps_lon.setText(gps_lon);
                                }
                            }
                        });
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            Log.d(TAG, "Thread dead");
                        }
                    }
                }
            }
        });
        mainThread.start();

        getImageThread = new Thread(new Runnable() {
            public void run() {
                makeFilePath = getApplicationContext().getFilesDir().toString();
                file = new File(makeFilePath,"main.png");
                File parent_dir = file.getParentFile();
                if (parent_dir != null) {
                    parent_dir.mkdirs();
                }
                try {
                    mainThread.join();
                    file.createNewFile();
                } catch (Exception e){}
                connectFTP.ftpDownloadFile(currentPath +currentFileList[last_index][0], file.toString());
            }
        });
    }

    private void getMyLocation() {
        LocationListener gpsLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                float accuracy = location.getAccuracy();
                locUser = new Location("User point");
                if ( 18<= accuracy && accuracy <=20 ) {
                    textView_gps_lat_user.setText(Math.round(latitude * 1000000) / 1000000.0 + "");
                    textView_gps_lon_user.setText(Math.round(longitude * 1000000) / 1000000.0 + "");
                    locUser.setLatitude(latitude);
                    locUser.setLongitude(longitude);
                    distance = locAccident.distanceTo(locUser);
                    if (distance/1000.0 > 1.0) {
                        distance/=1000.0;
                        distanceUnit="KM";
                    }
                    else
                        distanceUnit="M";
                    textView_distance.setText(distance+" "+distanceUnit);
                }
                if (distance != 0 && distance <= warnDistance && distanceUnit=="M" && threadStop!=true) {
                    String strDistance = String.format("%.2f", distance);
                    createNotification("WARNING! Remain Distance To Accident : "+strDistance+"M !!!",MainActivity.this);
                    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                    boolean isScreenOn = Build.VERSION.SDK_INT >= 20 ? pm.isInteractive() : pm.isScreenOn(); // check if screen is on
                    if (!isScreenOn) {
                        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "myApp:notificationLock");
                        wl.acquire(3000); //set your time in milliseconds
                    }
                }

            }
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            public void onProviderEnabled(String provider) { }
            public void onProviderDisabled(String provider) { }
        };

        // Register the listener with the Location Manager to receive location updates
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( MainActivity.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                    0 );
        }
        else{
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
        }
    }

    @Override
    protected void onDestroy() {
        threadStop=true;
        mainThread.interrupt();
        finish();
        super.onDestroy();
    }

    public void createNotification(String aMessage, Context context) {
        final int NOTIFY_ID = 0; // ID of notification
        String id = "0"; // default_channel_id
        String title = "title"; // Default Channel
        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle(aMessage)                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                    .setContentText(context.getString(R.string.app_name)) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        }
        else {
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle(aMessage)                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                    .setContentText(context.getString(R.string.app_name)) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        }
        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);
    }

    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis()-time>=2000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"Press the back button again to exit...",Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis()-time<2000){
            finish();
        }
    }
}

