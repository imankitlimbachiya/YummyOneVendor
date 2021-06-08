package yummy.one.yummyonevendor.Controller.Activities.Location;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.R;
import yummy.one.yummyonevendor.Controller.Activities.Register.RegisterDetails;

public class LocationDetection extends AppCompatActivity
        implements
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        LocationListener {

    private GoogleMap mMap;
    ImageView pin;
    GoogleApiClient mGoogleApiClient;
    private Location mLastKnownLocation;
    LocationRequest mLocationRequest;
    int temp=0;
    String city="",type="",pincode="",state="";
    TextView coord,area;
    TextView location;
    private EditText flat,reach;
    Button save;
    Session session;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;
    private final float DEFAULT_ZOOM = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detection2);

        pin=findViewById(R.id.pin);
        session=new Session(LocationDetection.this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        save=findViewById(R.id.save);
        location=findViewById(R.id.location);
        coord=findViewById(R.id.coord);
        flat=findViewById(R.id.flat);
        reach=findViewById(R.id.reach);
        area=findViewById(R.id.area);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(LocationDetection.this);
        Places.initialize(LocationDetection.this, "AIzaSyCPhxfpptoIc1yca5U8mXIigIajoERQCdE");
        placesClient = Places.createClient(LocationDetection.this);
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();


        LinearLayout back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(location.getText().equals("Loading......"))
                {
                    Toast.makeText(LocationDetection.this,"Please Wait",Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(flat.getText().toString())){
                    flat.setError("Enter Flat/ Street");
                    flat.requestFocus();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    flat.startAnimation(shake);
                    return;
                }

//                if(TextUtils.isEmpty(reach.getText().toString())){
//                    reach.setError("Enter Landmark");
//                    reach.requestFocus();
//                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
//                    } else {
//                        //deprecated in API 26
//                        v.vibrate(500);
//                    }
//                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
//                    reach.startAnimation(shake);
//                    return;
//                }


                final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(LocationDetection.this, android.app.AlertDialog.THEME_HOLO_LIGHT);
                final EditText input = new EditText(LocationDetection.this);
                alert.setTitle("Are you sure!");
                alert.setMessage("The selected location is accurate and pointing to the exact location.");
//                    alert.setView(input);
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        session.setaddress(location.getText().toString());
                        session.setflat(flat.getText().toString());
                        session.setlandmark(reach.getText().toString());
                        session.setloc(coord.getText().toString());
                        Intent intent = new Intent(LocationDetection.this, RegisterDetails.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                alert.show();

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveListener(this);



        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

//        Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(LocationDetection.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
//
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker")
                .draggable(true));

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(LocationDetection.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(LocationDetection.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location)
    {
        if(temp==0) {
            mMap.clear();
            temp++;
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Marker"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            mMap.setMapType(mMap.MAP_TYPE_NORMAL);
        }


    }


    @Override
    public void onCameraMove() {

        mMap.clear();
        pin.setVisibility(View.VISIBLE);

    }


    @Override
    public void onCameraIdle() {

        pin.setVisibility(View.GONE);
        mMap.clear();



        String address="";
        String address1="";
        Geocoder gcd = new Geocoder(LocationDetection.this, Locale.getDefault());
        List<Address> addresses = new ArrayList<>();
        try {
            addresses = gcd.getFromLocation(mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            address=addresses.get(0).getAddressLine(0);
//            int i=address.indexOf(",");
//            if(i>-1){
//                flat.setText(address.substring(0,i));
//            }
            city=addresses.get(0).getLocality();
            pincode = addresses.get(0).getPostalCode();
            state = addresses.get(0).getAdminArea();
            location.setText(address);
//            area.setText(addresses.get(0).getSubLocality());
            coord.setText(""+mMap.getCameraPosition().target.latitude+","+mMap.getCameraPosition().target.longitude);

        }

//        ViewGroup infowindow=(ViewGroup)LocationDetection.this.getLayoutInflater().inflate(R.layout.map_info,null);
//        TextView add=infowindow.findViewById(R.id.address);
//        add.setText(address);

        mMap.addMarker(new MarkerOptions()
                .position(mMap.getCameraPosition().target)
                .title(address));

//        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(LocationDetection.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(LocationDetection.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(LocationDetection.this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(LocationDetection.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(LocationDetection.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }
}

