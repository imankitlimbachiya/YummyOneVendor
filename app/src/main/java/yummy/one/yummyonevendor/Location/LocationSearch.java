package yummy.one.yummyonevendor.Location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.internal.LatLngAdapter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.R;
import yummy.one.yummyonevendor.SignUp.RegisterDetails;

public class LocationSearch extends AppCompatActivity {

    private static final String TAG = "Location";

    private EditText txtSearch;
    private RecyclerView mRecyclerView;
//    private ImageView clear;
    private Handler handler = new Handler();
    private PlacePredictionAdapter adapter = new PlacePredictionAdapter();
    private Gson gson = new GsonBuilder().registerTypeAdapter(LatLng.class, new LatLngAdapter())
            .create();
    
    LinearLayout map;
    private RequestQueue queue;
    private PlacesClient placesClient;
    private AutocompleteSessionToken sessionToken;

    private ViewAnimator viewAnimator;
    private Button turnon;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mref;
    private Session sessions;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);

        LinearLayout back= findViewById(R.id.imgBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LocationSearch.this, RegisterDetails.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });


//        buildGoogleApiClient();

            Places.initialize(getApplicationContext(), "AIzaSyCPhxfpptoIc1yca5U8mXIigIajoERQCdE");

        txtSearch=findViewById(R.id.txtSearch);
        turnon=findViewById(R.id.turnon);
        mRecyclerView=findViewById(R.id.recyclerView);
//        clear=findViewById(R.id.clear);
//        if(getApplicationContext()!=null)
            placesClient = Places.createClient(getApplicationContext());

        viewAnimator = findViewById(R.id.view_animator);
        map = findViewById(R.id.map);


        viewAnimator.setVisibility(View.GONE);

        sessions= new Session(getApplication());


        txtSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (txtSearch.getRight() - txtSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        txtSearch.setText("");
                        txtSearch.clearFocus();
                        map.setVisibility(View.VISIBLE);
                        viewAnimator.setVisibility(View.GONE);
                        return true;
                    }
                }
                return false;
            }
        });


        turnon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(LocationSearch.this)
                        .withPermissions(
                                Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if(report.areAllPermissionsGranted()) {
                                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                    if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
                                        new AlertDialog.Builder(LocationSearch.this)
                                                .setTitle("GPS Not Enabled")  // GPS not found
                                                .setMessage("Are you sure u want enable gps") // Want to enable?
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                                    }
                                                })
                                                .setNegativeButton("No", null)
                                                .show();
                                    }
                                    else{
                                        Intent intent = new Intent(LocationSearch.this, LocationDetection.class);
                                        intent.putExtra("type", "location");
                                        startActivity(intent);
                                    }
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"e",Toast.LENGTH_LONG).show();
                                    final SweetAlertDialog sDialog = new SweetAlertDialog(LocationSearch.this, SweetAlertDialog.SUCCESS_TYPE);
                                    sDialog.setTitleText("Warning!");
                                    sDialog.setContentText("Some PermissionActivity have been denied permanently. Please go to setting to enable!");
                                    sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismiss();
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.fromParts("package", getPackageName(), null));
                                        }
                                    });
                                    sDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sDialog.dismiss();
                                        }
                                    });
                                    sDialog.setCancelable(false);
                                    sDialog.show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> PermissionActivity, PermissionToken token) {
                            }
                        })
                        .check();
            }
        });


        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                    LocationManager locationManager = (LocationManager)
//                           getSystemService(Context.LOCATION_SERVICE);
//                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                        displayLocationSettingsRequest(getApplicationContext());
//                        return;
//                    }
//
//                        Intent intent = new Intent(LocationSearch.this, LocationDetection.class);
//                        intent.putExtra("type", "location");
//                        startActivity(intent);

                Dexter.withActivity(LocationSearch.this)
                        .withPermissions(
                                Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if(report.areAllPermissionsGranted()) {
                                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                    if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
                                        new AlertDialog.Builder(LocationSearch.this)
                                                .setTitle("GPS Not Enabled")  // GPS not found
                                                .setMessage("Are you sure u want enable gps") // Want to enable?
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                                    }
                                                })
                                                .setNegativeButton("No", null)
                                                .show();
                                    }
                                    else{
                                        Intent intent = new Intent(LocationSearch.this, LocationDetection.class);
                                        intent.putExtra("type", "location");
                                        startActivity(intent);
                                    }
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"e",Toast.LENGTH_LONG).show();
                                    final SweetAlertDialog sDialog = new SweetAlertDialog(LocationSearch.this, SweetAlertDialog.SUCCESS_TYPE);
                                    sDialog.setTitleText("Warning!");
                                    sDialog.setContentText("Some PermissionActivity have been denied permanently. Please go to setting to enable!");
                                    sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismiss();
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.fromParts("package", getPackageName(), null));
                                        }
                                    });
                                    sDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sDialog.dismiss();
                                        }
                                    });
                                    sDialog.setCancelable(false);
                                    sDialog.show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> PermissionActivity, PermissionToken token) {
                            }
                        })
                        .check();
            }
        });


        placesClient = com.google.android.libraries.places.api.Places.createClient(getApplicationContext());
        queue = Volley.newRequestQueue(getApplicationContext());


        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), layoutManager.getOrientation()));


        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
                sessionToken = AutocompleteSessionToken.newInstance();
            }

            @Override
            public void onTextChanged(final CharSequence s, int i, int i1, int i2) {

                map.setVisibility(View.GONE);

                viewAnimator.setVisibility(View.VISIBLE);
//                clear.setVisibility(View.VISIBLE);

                handler.removeCallbacksAndMessages(null);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getPlacePredictions(s.toString());
                    }
                }, 300);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


//        clear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                txtSearch.setText("");
//
//                map.setVisibility(View.VISIBLE);
//
//                viewAnimator.setVisibility(View.GONE);
//                clear.setVisibility(View.GONE);
//
//            }
//        });

    }

    private void getPlacePredictions(String query) {


//        final LocationBias bias = RectangularBounds.newInstance(
//            new LatLng(13.590194, 77.535981), // NE lat, lng
//            new LatLng(13.622520, 77.503923) // SW lat, lng
//        );

        // Create a new programmatic Place Autocomplete request in Places SDK for Android
        final FindAutocompletePredictionsRequest newRequest = FindAutocompletePredictionsRequest
                .builder()
                .setSessionToken(sessionToken)
//            .setLocationBias(bias)
//                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setQuery(query)
                .setCountries("IN")
                .build();

        // Perform autocomplete predictions request
        placesClient.findAutocompletePredictions(newRequest).addOnSuccessListener(new OnSuccessListener<FindAutocompletePredictionsResponse>() {
            @Override
            public void onSuccess(FindAutocompletePredictionsResponse response) {
                List<AutocompletePrediction> predictions = response.getAutocompletePredictions();
                adapter.setPredictions(predictions);

                viewAnimator.setDisplayedChild(predictions.isEmpty() ? 0 : 1);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    Toast.makeText(getApplicationContext(),exception.getMessage(),Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                }
            }
        });



    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();

        txtSearch.setText("");

        map.setVisibility(View.VISIBLE);

        viewAnimator.setVisibility(View.GONE);


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if( locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            turnon.setVisibility(View.INVISIBLE);
        }


//        clear.setVisibility(View.GONE);

    }


    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Intent intent = new Intent(LocationSearch.this, LocationDetection.class);
                        intent.putExtra("type", "location");
                        startActivity(intent);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(getParent(), 0x1);
                            Intent intent1 = new Intent(LocationSearch.this, LocationDetection.class);
                            intent1.putExtra("type", "location");
                            startActivity(intent1);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }


}
