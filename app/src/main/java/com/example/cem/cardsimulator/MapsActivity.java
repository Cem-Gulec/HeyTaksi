package com.example.cem.cardsimulator;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cem.cardsimulator.Bottom_Navigation.Bottom_Account_Activity;
import com.example.cem.cardsimulator.Bottom_Navigation.Bottom_Calculator_Activity;
import com.example.cem.cardsimulator.Bottom_Navigation.Bottom_Gettaxi_Activity;
import com.example.cem.cardsimulator.Bottom_Navigation.Bottom_Info_Activity;
import com.example.cem.cardsimulator.Bottom_Navigation.Bottom_Search_Activity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import android.Manifest;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    View mapView;


    private GoogleMap mMap;
    private final static int REQUEST_lOCATION=90;

    private TextView mTextMessage;



    @Override
    public void onBackPressed() {

        backPressedEvent();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Bottom navigation
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_surucuara);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Defining map elements
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //Initialize buttons
        final Button btn_MapType=(Button) findViewById(R.id.btn_Sat);

        //Button events
        btn_MapType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMapType();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
        LatLng turkey = new LatLng(41.08181, 29.01584);
        mMap.addMarker(new MarkerOptions().position(turkey).title("Marker in Turkey"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(turkey, 15));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {

            mMap.setMyLocationEnabled(true);
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_lOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==REQUEST_lOCATION){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                    mMap.setMyLocationEnabled(true);
                }
            }else{
                Toast.makeText(getApplicationContext(),"Kullanıcı konum iznini vermedi",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_account:
                    Intent account = new Intent(MapsActivity.this, Bottom_Account_Activity.class);
                    startActivity(account);
                    return true;

                case R.id.navigation_search:
                    Intent search = new Intent(MapsActivity.this, Bottom_Search_Activity.class);
                    startActivity(search);
                    return true;

                case R.id.navigation_surucuara:
                    Intent surucu = new Intent(MapsActivity.this, Bottom_Gettaxi_Activity.class);
                    startActivity(surucu);
                    return true;

                case R.id.navigation_calculator:
                    Intent calculator = new Intent(MapsActivity.this, Bottom_Calculator_Activity.class);
                    startActivity(calculator);
                    return true;

                case R.id.navigation_info:
                    Intent info = new Intent(MapsActivity.this, Bottom_Info_Activity.class);
                    startActivity(info);
                    return true;

            }
            return false;
        }
    };

    private void changeMapType(){

        if(mMap.getMapType()==GoogleMap.MAP_TYPE_NORMAL){
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }else{
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    private void backPressedEvent(){

        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                    .setCancelable(false);
            dialog.setMessage("Are you sure ?");


            LayoutInflater inflater = LayoutInflater.from(this);
            View exit_layout = inflater.inflate(R.layout.layout_exit,null);

            dialog.setView(exit_layout);

            //set button
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                }
            });

            dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });




            dialog.show();



        }
    }

}
