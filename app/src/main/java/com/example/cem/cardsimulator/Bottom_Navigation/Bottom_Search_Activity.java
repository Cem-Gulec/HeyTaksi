package com.example.cem.cardsimulator.Bottom_Navigation;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.cem.cardsimulator.Common.Common;
import com.example.cem.cardsimulator.MainActivity;
import com.example.cem.cardsimulator.MapsActivity;
import com.example.cem.cardsimulator.Path.iGoogleAPI;
import com.example.cem.cardsimulator.R;
import com.example.cem.cardsimulator.final_payment;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Bottom_Search_Activity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        RoutingListener
{

    //sharedPreferences
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String TUTAR_KEY="com.example.burak.erisim.TUTAR";
    private String MAIN_KEY="com.example.burak.erisim.MAIN_DATA";

    private GoogleMap mMap;

    //Play services
    private static final int MY_PERMISSION_REQUEST_CODE = 7000;
    private static final int PLAY_SERVICE_RES_REQUEST = 7001;

    private static int basePrice = 4;
    private static double perkm = 2.5;
    private static int minPrice = 10;

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LatLng Last_Location,Selected_Location;
    private int is_SecondTimeSelect=0;
    private Marker preSelected;

    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;

    double lattitude_current;
    double longitude_current;
    boolean isSearched=false;

    DatabaseReference drivers;
    GeoFire geoFire;

    Marker mCurrent;
    SupportMapFragment mapFragment;

    Button btn_Clear,btn_Back,btn_Go,btn_Info,btn_StartJourney;
    TextView tv_RequestTaxi;
    //TextView tv_location;
    private PlaceAutocompleteFragment places;


    MaterialAnimatedSwitch location_switch;

    //car animation
    private List <LatLng> polyLineList;
    private Marker carMarker;
    private float v;
    private double lat,lng;
    private Handler handler;
    private LatLng startPos,endPos,currentPos;
    private int index,next;
    private String destination;
    private PolylineOptions polyLineOptions,blackPolyLineOptions;
    private Polyline blackPolyline,greyPolyline;

    private iGoogleAPI mService;

    private List<Polyline> polylines;


    String address_sel;
    float distance;
    float duration;
    float price_Info;
    Timer timer;


    /*Runnable drawPathRunnable = new Runnable() {
        @Override
        public void run() {
            if(index<polyLineList.size()-1)
            {
                index++;
                next = index+1;
            }
            if(index <polyLineList.size()-1)
            {
                startPos = polyLineList.get(index);
                endPos = polyLineList.get(next);
            }

            final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,1);
            valueAnimator.setDuration(3000);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    v = valueAnimator.getAnimatedFraction();
                    lng = v*endPos.longitude+ (1-v)*startPos.longitude;
                    lat = v*endPos.latitude+ (1-v)*startPos.latitude;
                    LatLng newPos = new LatLng(lat,lng);

                    carMarker.setPosition(newPos);
                    carMarker.setAnchor(0.5f,0.5f);
                    carMarker.setRotation(getBearing(startPos,newPos));
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(newPos)
                                    .zoom(15.5f)
                                    .build()
                    ));
                }
            });
            valueAnimator.start();
            handler.postDelayed(this,3000);
        }
    };*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom__search_);

        polylines = new ArrayList<>();

        //Defining map elements
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Layout elements
        // btn_Clear = (Button)findViewById(R.id.btn_Clear);
        //tv_location = (TextView) findViewById(R.id.et_location);
        btn_Back = (Button)findViewById(R.id.btn_Back);
        btn_Go = (Button)findViewById(R.id.btn_Go);
        btn_Info = (Button)findViewById(R.id.btn_Info);
        btn_StartJourney = (Button)findViewById(R.id.btn_StartJourney);

        //sharedpreferences
        sharedPreferences=getSharedPreferences(MAIN_KEY, MODE_PRIVATE);
        editor=sharedPreferences.edit();

       /*tv_location.addTextChangedListener(new TextWatcher() {
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
        });*/

       /* btn_Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tv_location.setText("");
            }
        });*/

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

                getRouteToMarker(Selected_Location);

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Selected_Location.latitude,Selected_Location.longitude),11));


            }
        });

        btn_Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoDialogEvent();
            }
        });

        btn_StartJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putFloat(TUTAR_KEY, price_Info);
                editor.commit();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        requestForTaxi();
                    }
                }, 2000);


                btn_StartJourney.setEnabled(false);

            }
        });

        location_switch = (MaterialAnimatedSwitch)findViewById(R.id.location_switch);

        location_switch.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isOnline) {
                if(isOnline){
                    startLocationUpdates();
                    displayLocation();
                    Snackbar.make(mapFragment.getView(),"Online",Snackbar.LENGTH_SHORT).show();
                }
                else{
                    stopLocationUpdates();
                    mCurrent.remove();
                    mMap.clear();
                    Snackbar.make(mapFragment.getView(),"Offline",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        polyLineList = new ArrayList<>();


        //Geo Fire
        drivers = FirebaseDatabase.getInstance().getReference("Drivers");
        geoFire = new GeoFire(drivers);

        setUpLocation();

        mService = Common.getGoogleAPI();

        //Places API
        places = (PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                btn_Go.setEnabled(true);
                btn_Go.setVisibility(View.VISIBLE);

                btn_StartJourney.setEnabled(false);
                btn_StartJourney.setVisibility(View.INVISIBLE);

                if(is_SecondTimeSelect>0)
                    preSelected.remove();

                is_SecondTimeSelect++;
                clearPoly();

                Selected_Location = new LatLng(place.getLatLng().latitude,place.getLatLng().longitude);

                destination = place.getAddress().toString();
                destination = destination.replace(" ","+");

                preSelected =mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_icon))
                        .position(new LatLng(place.getLatLng().latitude,place.getLatLng().longitude))
                        .title("You"));

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatLng().latitude,place.getLatLng().longitude),15));

            }

            @Override
            public void onError(Status status) {
                Toast.makeText(Bottom_Search_Activity.this,""+status.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getRouteToMarker(LatLng selected_location) {

        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(Last_Location, selected_location)
                .build();
        routing.execute();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode)
        {
            case MY_PERMISSION_REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(checkPlayServices())
                    {
                        buildGoogleApiClient();
                        createLocationRequest();
                        // if(location_switch.isChecked())
                        displayLocation();
                    }
                }
        }
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

    private void startLocationUpdates(){

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest, (com.google.android.gms.location.LocationListener) this);
    }

    private void displayLocation(){

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLastLocation!=null){
            //if(location_switch.isChecked()){

            final double latitude = mLastLocation.getLatitude();
            final double longitude = mLastLocation.getLongitude();

            Last_Location = new LatLng(latitude,longitude);

            lattitude_current = latitude;
            longitude_current = longitude;


            //Update to firebase
            geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    //Add marker
                    if(mCurrent !=null)
                        mCurrent.remove(); //Remove if already exists

                    mCurrent = mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi_location_pin))
                            .position(new LatLng(latitude,longitude))
                            .title("You"));

                    //Move camera to current location
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),15));

                }
            });
            //}
        }
        else
        {
            Log.d("ERROR","Cannot get your location");
        }
    }

    private void stopLocationUpdates(){

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);


    }

    private void setUpLocation(){

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            //Requesting runtime permission
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            },MY_PERMISSION_REQUEST_CODE);
        }
        else{

            if(checkPlayServices())

                buildGoogleApiClient();
            createLocationRequest();

            //if(location_switch.isChecked())
            displayLocation();

        }
    }

    private void createLocationRequest(){

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void buildGoogleApiClient(){

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    private boolean checkPlayServices(){

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if(resultCode != ConnectionResult.SUCCESS){

            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode))
                GooglePlayServicesUtil.getErrorDialog(resultCode,this,PLAY_SERVICE_RES_REQUEST).show();
            else{
                Toast.makeText(this,"This device is not supported",Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    //btn_Go Event
    private void goToLocation(){

       /* mMap.clear();

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
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
            mMap.addMarker(new MarkerOptions()
            .position(new LatLng(address.getLatitude(),address.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi_location_pin)));

            isSearched=true;
        }*/

    }

   /* private void getDirection(){

        //get direction
        currentPos = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

        String requestApi = null;

        try{

            requestApi = "https://maps.googleapis.com/maps/api/directions/json?"+
                    "mode=driving&"+
                    "transit_routing_preference=less_driving&"+
                    "origin="+currentPos.latitude+","+currentPos.longitude+"&"+
                    "destination="+destination+"&"+
                    "key="+getResources().getString(R.string.google_direction_api);
            Log.d("ERROR",requestApi);

            mService.getPath(requestApi)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                JSONArray jsonArray = jsonObject.getJSONArray("routes");

                                for(int i=0; i<jsonArray.length(); i++){
                                    JSONObject route = jsonArray.getJSONObject(i);
                                    JSONObject poly = route.getJSONObject("overview_polyline");
                                    String polyline = poly.getString("points");
                                    polyLineList = decodePoly(polyline);
                                }

                                //Adjusting bounds
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                for(LatLng latLng:polyLineList)
                                    builder.include(latLng);

                                LatLngBounds bounds = builder.build();
                                CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,2);
                                mMap.animateCamera(mCameraUpdate);

                                polyLineOptions = new PolylineOptions();
                                polyLineOptions.color(Color.GRAY);
                                polyLineOptions.width(5);
                                polyLineOptions.startCap(new SquareCap());
                                polyLineOptions.endCap(new SquareCap());
                                polyLineOptions.jointType(JointType.ROUND);
                                polyLineOptions.addAll(polyLineList);
                                greyPolyline = mMap.addPolyline(polyLineOptions);

                                blackPolyLineOptions = new PolylineOptions();
                                blackPolyLineOptions.color(Color.BLACK);
                                blackPolyLineOptions.width(5);
                                blackPolyLineOptions.startCap(new SquareCap());
                                blackPolyLineOptions.endCap(new SquareCap());
                                blackPolyLineOptions.jointType(JointType.ROUND);
                                blackPolyline = mMap.addPolyline(blackPolyLineOptions);

                                mMap.addMarker(new MarkerOptions()
                                        .position(polyLineList.get(polyLineList.size()-1))
                                        .title("Pickup Location"));

                                //Animation starts here
                                ValueAnimator polyLineAnimator = ValueAnimator.ofInt(0,100);
                                polyLineAnimator.setDuration(2000);
                                polyLineAnimator.setInterpolator(new LinearInterpolator());
                                polyLineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {

                                        List<LatLng> points = greyPolyline.getPoints();
                                        int percentValue = (int)valueAnimator.getAnimatedValue();
                                        int size = points.size();
                                        int newPoints = (int)(size* (percentValue/100.0f));
                                        List<LatLng> p = points.subList(0,newPoints);
                                        blackPolyline.setPoints(p);
                                    }
                                });
                                polyLineAnimator.start();

                                carMarker = mMap.addMarker(new MarkerOptions()
                                        .position(currentPos)
                                        .flat(true)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));

                                handler = new Handler();
                                index =-1;
                                next = 1;
                                handler.postDelayed(drawPathRunnable,3000);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });

        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

    /*private float getBearing(LatLng startPos, LatLng endPos){

        double lat = Math.abs(startPos.latitude - endPos.latitude);
        double lng = Math.abs(startPos.longitude - endPos.longitude);

        if(startPos.latitude < endPos.latitude && startPos.longitude < endPos.longitude)
            return (float) (Math.toDegrees(Math.atan(lng/lat)));

        else if(startPos.latitude >= endPos.latitude && startPos.longitude < endPos.longitude)
            return (float) (90-Math.toDegrees(Math.atan(lng/lat))+90);

        else if(startPos.latitude >= endPos.latitude && startPos.longitude >= endPos.longitude)
            return (float) (Math.toDegrees(Math.atan(lng/lat))+180);

        else if(startPos.latitude < endPos.latitude && startPos.longitude >= endPos.longitude)
            return (float) (90-Math.toDegrees(Math.atan(lng/lat))+270);

        return -1;
    }*/

   /* private List decodePoly(String encoded) {

        List poly = new ArrayList();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }*/

    private void backPressedEvent(){

        Intent i = new Intent(Bottom_Search_Activity.this, MapsActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();
    }

    @Override
    public void onRoutingFailure(RouteException e) {

        if(e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int i) {

        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (i = 0; i <route.size(); i++) {

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(R.color.primary_dark_material_light));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            address_sel = route.get(i).getEndAddressText();

            float dist = (float) route.get(i).getDistanceValue() / 1000;
            price_Info = (float) getPrice(dist);

            distance =  dist;

            duration = (float) route.get(i).getDurationValue() / 60;

        }

        btn_Go.setEnabled(false);
        btn_Go.setVisibility(View.INVISIBLE);

        btn_StartJourney.setEnabled(true);
        btn_StartJourney.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRoutingCancelled() {

    }

    private void clearPoly(){

        for(Polyline line : polylines)
        {
            line.remove();
        }

        polylines.clear();
    }

    public static double getPrice(double km){

        if((basePrice + km*perkm)< minPrice)
            return minPrice;
        else
            return basePrice + km*perkm;
    }

    private void infoDialogEvent(){

        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);

            LayoutInflater inflater = LayoutInflater.from(this);
            View info_layout = inflater.inflate(R.layout.bottom_info_sheet,null);

            final TextView tv_destination = info_layout.findViewById(R.id.txtDestination);
            final TextView tv_distance = info_layout.findViewById(R.id.txtDistance);
            final TextView tv_duration = info_layout.findViewById(R.id.txtDuration);
            final TextView tv_calculator = info_layout.findViewById(R.id.txtCalculator);

            tv_destination.setText(address_sel);
            tv_distance.setText(String.valueOf(distance)+" km");

            tv_duration.setText(String.valueOf(duration)+ " min");

            tv_calculator.setText(String.valueOf(price_Info)+" TL");

            dialog.setView(info_layout);

            //set button
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });

            dialog.show();
            }
    }

    private void requestForTaxi() {

            mMap.clear();
            mCurrent = mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi_location_pin))
                    .position(new LatLng(Selected_Location.latitude,Selected_Location.longitude))
                    .title("You"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Selected_Location.latitude,Selected_Location.longitude),11));


            AlertDialog.Builder dialog = new AlertDialog.Builder(Bottom_Search_Activity.this);
            dialog.setCancelable(true);

            LayoutInflater inflater = LayoutInflater.from(Bottom_Search_Activity.this);
            View request_taxi = inflater.inflate(R.layout.request_taxi,null);

             tv_RequestTaxi = (TextView)request_taxi.findViewById(R.id.txt_requestTaxi);
             tv_RequestTaxi.setText("Istenilen lokasyona ulaşıldı." +
                "\nÖdenecek Tutar : "+price_Info+ "TL");


        dialog.setView(request_taxi);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Bottom_Search_Activity.this,final_payment.class));
                    }
                }, 1000);

            }
        });

            dialog.show();

            clearPoly();
    }


}