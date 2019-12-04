package com.example.wutchout;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button btn_return;
    Double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        lat = Double.parseDouble(intent.getExtras().getString("lat"));
        lon = Double.parseDouble(intent.getExtras().getString("lon"));

        btn_return = findViewById(R.id.btn_return);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override //구글맵을 띄울준비가 됬으면 자동호출된다.
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        oneMarker();
    }

    //마커하나찍는 기본 예제
    public void oneMarker() {
        // 서울 여의도에 대한 위치 설정
        LatLng Accident = new LatLng(lat, lon);

        // 구글 맵에 표시할 마커에 대한 옵션 설정  (알파는 좌표의 투명도이다.)
        MarkerOptions makerOptions = new MarkerOptions();
        makerOptions
                .position(Accident)
                .title("Accident !")
                .snippet(lat+" : "+lon)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .alpha(0.5f);

        mMap.addMarker(makerOptions).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Accident, 16));
    }

}