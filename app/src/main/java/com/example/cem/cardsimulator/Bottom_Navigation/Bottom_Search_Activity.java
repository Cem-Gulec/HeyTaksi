package com.example.cem.cardsimulator.Bottom_Navigation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cem.cardsimulator.MapsActivity;
import com.example.cem.cardsimulator.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class Bottom_Search_Activity extends FragmentActivity implements OnMapReadyCallback {

    Button btn_Clear,btn_Back,btn_Go;
    TextView tv_location;

    private GoogleMap mMap;
    private final static int REQUEST_lOCATION=90;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom__search_);

        //Defining map elements
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Layout elements
        btn_Clear = (Button)findViewById(R.id.btn_Clear);
        tv_location = (TextView)findViewById(R.id.et_location);
        btn_Back = (Button)findViewById(R.id.btn_Back);
        btn_Go = (Button)findViewById(R.id.btn_Go);

        tv_location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.toString().trim().length()>0){
                    btn_Clear.setBackgroundResource(R.drawable.ic_close_black_24dp);
                }else{
                    btn_Clear.setBackgroundResource(R.drawable.ic_search_black_24dp);
                }
            }
        });

        btn_Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_location.setText("");
            }
        });

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Bottom_Search_Activity.this, MapsActivity.class);
                startActivity(i);
            }
        });

        btn_Go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLocation();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                Intent info = new Intent(Bottom_Search_Activity.this, MapsActivity.class);
                startActivity(info);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        backPressedEvent();

    }

    //btn_Go Event
    private void goToLocation(){

        EditText etLocation=(EditText)findViewById(R.id.et_location);
        String location=etLocation.getText().toString();
        if(location!=null && !location.equals("")){
            List<Address> adressList=null;
            Geocoder geocoder=new Geocoder(Bottom_Search_Activity.this);
            try {
                adressList=geocoder.getFromLocationName(location,1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address=adressList.get(0);
            LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());
            //mMap.addMarker(new MarkerOptions().position(latLng).title("Burası "+location));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    private void backPressedEvent(){

        Intent i = new Intent(Bottom_Search_Activity.this, MapsActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
