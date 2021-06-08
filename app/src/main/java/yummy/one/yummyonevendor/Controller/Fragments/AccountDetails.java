package yummy.one.yummyonevendor.Controller.Fragments;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import yummy.one.yummyonevendor.Controller.Activities.Login.Login;
import yummy.one.yummyonevendor.Controller.Activities.Login.OtpActivity;
import yummy.one.yummyonevendor.Controller.Activities.SignUp.SignUp;
import yummy.one.yummyonevendor.Functionality.Retrofit.APIClient;
import yummy.one.yummyonevendor.Functionality.Retrofit.APIInterface;
import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.Functionality.VolleySupport.APIConstant;
import yummy.one.yummyonevendor.R;

import static yummy.one.yummyonevendor.Functionality.VolleySupport.APIConstant.getInstance;


public class AccountDetails extends Fragment {

    LinearLayout imgBack;
    TextView txtRName;
    EditText edtRestaurantName,edtVendor,edtOpentime,edtClosetime,edtFullName,edtMobile,edtEmail;
    Button btnSave;
    Session session;
    ProgressBar progressBar;

    private int mHour;
    private int mMinute;
    private String open="",close="";

    SharedPreferences sharedPreferences;
    APIInterface apiInterface;
    public AccountDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_account_details, container, false);

        if(getActivity()!=null){
            LinearLayout bottomnavigation = getActivity().findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.GONE);
        }
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sharedPreferences = getContext().getSharedPreferences("UserData", getContext().MODE_PRIVATE);

        imgBack = v.findViewById(R.id.back);
        progressBar = v.findViewById(R.id.progressBar);
        edtRestaurantName = v.findViewById(R.id.edtRestaurantName);
        edtVendor = v.findViewById(R.id.edtVendor);
        edtOpentime = v.findViewById(R.id.edtOpentime);
        edtClosetime = v.findViewById(R.id.edtClosetime);
        btnSave = v.findViewById(R.id.btnSave);
        txtRName = v.findViewById(R.id.txtRName);
        edtFullName = v.findViewById(R.id.edtFullName);
        edtMobile = v.findViewById(R.id.edtMobile);
        edtEmail = v.findViewById(R.id.edtEmail);

        session = new Session(getActivity());

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null){
                    getActivity().onBackPressed();
                }
            }
        });
        APIConstant.getInstance().renewAccessTokenApi(getContext());
        txtRName.setText(session.getcategory() + " Name");
        edtRestaurantName.setText(session.getvendorname());
        edtVendor.setText(session.getusername());
        edtFullName.setText(session.getname());
        APIConstant.getInstance().renewAccessTokenApi(getContext());
        edtOpentime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                mHour = hourOfDay;
                                mMinute = minute;
                                String h="",m="";
                                if (mHour>=0 && mHour<=9)
                                    h = "0" + hourOfDay;
                                else
                                    h = ""+ hourOfDay;

                                if(mMinute>=0 && mMinute <=9)
                                    m = "0" +minute;
                                else
                                    m =""+ minute;

                                Time tme = new Time(hourOfDay,minute,0);//seconds by default set to zero
                                Format formatter;
                                formatter = new SimpleDateFormat("h:mm a");
                                edtOpentime.setText(""+formatter.format(tme));
                                open = h+":"+m;

                            }
                        }, mHour, mMinute, false);
                timePickerDialog.updateTime(06,0);
                timePickerDialog.show();
            }
        });

        edtClosetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mHour = hourOfDay;
                                mMinute = minute;
                                String h="",m="";
                                if (mHour>=0 && mHour<=9)
                                    h = "0" + hourOfDay;
                                else
                                    h = ""+ hourOfDay;

                                if(mMinute>=0 && mMinute <=9)
                                    m = "0" +minute;
                                else
                                    m =""+ minute;

                                Time tme = new Time(hourOfDay,minute,0);//seconds by default set to zero
                                Format formatter;
                                formatter = new SimpleDateFormat("h:mm a");

                                edtClosetime.setText(""+formatter.format(tme));
                                close = h+":"+m;
                            }
                        }, mHour, mMinute, false);

                timePickerDialog.updateTime(21,0);
                timePickerDialog.show();
            }
        });


//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        DocumentReference docRef = db.collection("Vendor").document(session.getusername());
//        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if(documentSnapshot.exists()){
//                        int count = 0;
//                        if (documentSnapshot.contains("RestaurantName")) {
//                                edtRestaurantName.setText(documentSnapshot.get("RestaurantName").toString());
//                                session.setvendorname(documentSnapshot.get("RestaurantName").toString());
//                                count++;
//                        }
//
//                    if (documentSnapshot.contains("Name")) {
//                        edtFullName.setText(documentSnapshot.get("Name").toString());
//                        session.setname(documentSnapshot.get("Name").toString());
//                        count++;
//                    }
//
//                    if (documentSnapshot.contains("Email")) {
//                        edtEmail.setText(documentSnapshot.get("Email").toString());
//                        session.setemail(documentSnapshot.get("Email").toString());
//                        count++;
//                    }
//
//                    if (documentSnapshot.contains("MobileNumber")) {
//                        edtMobile.setText(documentSnapshot.get("MobileNumber").toString());
//                        session.setnumber(documentSnapshot.get("MobileNumber").toString());
//                        count++;
//                    }
//
//                        if (documentSnapshot.contains("OpenTime")) {
//                                open = documentSnapshot.get("OpenTime").toString();
//                                String a[]=open.split(":");
//                                Time tme = new Time(Integer.parseInt(a[0]),Integer.parseInt(a[1]),0);//seconds by default set to zero
//                                Format formatter;
//                                formatter = new SimpleDateFormat("h:mm a");
//                                edtOpentime.setText(""+formatter.format(tme));
//                                count++;
//                        }
//
//                        if (documentSnapshot.contains("CloseTime")) {
//                                close =documentSnapshot.get("CloseTime").toString();
//                                String a[]=close.split(":");
//                                Time tme = new Time(Integer.parseInt(a[0]),Integer.parseInt(a[1]),0);//seconds by default set to zero
//                                Format formatter;
//                                formatter = new SimpleDateFormat("h:mm a");
//                                edtClosetime.setText(""+formatter.format(tme));
//                                count++;
//                        }
//
//
//                }
//            }
//        });



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(edtRestaurantName.getText().toString())){
                    edtRestaurantName.setError("Enter Restaurant Name");
                    edtRestaurantName.requestFocus();
                    Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                    Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                    edtRestaurantName.startAnimation(shake);
                    return;
                }

                if(TextUtils.isEmpty(edtFullName.getText().toString())){
                    edtFullName.setError("Enter  Name");
                    edtFullName.requestFocus();
                    Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                    Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                    edtFullName.startAnimation(shake);
                    return;
                }

                AddRestaurantInfoApi("");

//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                Map<String, Object> user = new HashMap<>();
//                user.put("RestaurantName",edtRestaurantName.getText().toString());
//                user.put("Name",edtFullName.getText().toString());
//                user.put("OpenTime",open);
//                user.put("CloseTime",close);
//                user.put("Email",edtEmail.getText().toString());
//                db.collection("Vendor").document(session.getusername()).set(user, SetOptions.merge());
//                if(getActivity()!=null){
//                    getActivity().onBackPressed();
//                }
            }
        });

        return v;
    }


    private void AddRestaurantInfoApi(String category) {
        progressBar.setVisibility(View.VISIBLE);
        JSONObject data = new JSONObject();
        try {
            String mobilenumber=sharedPreferences.getString("MOBILENUMBER", "");
            String address=sharedPreferences.getString("ADDRESS", "");
            String latitude=sharedPreferences.getString("LATITUDE", "");
            String longitude=sharedPreferences.getString("LONGITUDE", "");
            data.put("mobilenumber",mobilenumber);
            data.put("restaurantName",edtRestaurantName.getText().toString());
            data.put("category",category);
            data.put("email",edtEmail.getText().toString());
            data.put("openTime",open);
            data.put("closeTime",close);
            data.put("address",address);
            data.put("latitude",latitude);
            data.put("longitude",longitude);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("PARAMETER", "" + getInstance().addRestaurantInfo + data);
        RequestQueue requstQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, getInstance().addRestaurantInfo,data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject JsonMain) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().addRestaurantInfo + JsonMain);
                            String msg = JsonMain.getString("message");
                            if (msg.equalsIgnoreCase("SUCCESS")) {
                                JSONObject dataobject=JsonMain.getJSONObject("data");
                                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                                if(getActivity()!=null){
                                    getActivity().onBackPressed();
                                }
                            }else {
                                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("ERROR", "" + getInstance().addRestaurantInfo + error.toString());
                    }
                }
        ){
            //here I want to post data to sever
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String keyname=sharedPreferences.getString("KEYNAME", "");
                String keyvalue=sharedPreferences.getString("KEYVALUE", "");
                String accesstoken=sharedPreferences.getString("ACCESSTOKEN", "");
                params.put("Authorization", "Bearer "+ accesstoken);
                params.put("Content-Type", "application/json");
                params.put(keyname, keyvalue);
                Log.e("HEADER", "" + getInstance().addRestaurantInfo + params);
                return params;
            }
        };
        requstQueue.add(jsonobj);

    }

    private void updateRestaurantNameApi(String mobilenumber,String restaurantName) {
        progressBar.setVisibility(View.VISIBLE);
        JSONObject data = new JSONObject();
        try {

            data.put("mobilenumber",mobilenumber);
            data.put("restaurantName",restaurantName);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("PARAMETER", "" + getInstance().update + data);
        RequestQueue requstQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, getInstance().update,data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject JsonMain) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().update + JsonMain);
                            String msg = JsonMain.getString("message");
                            if (msg.equalsIgnoreCase("SUCCESS")) {
                                JSONObject dataobject=JsonMain.getJSONObject("data");
                                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                                if(getActivity()!=null){
                                    getActivity().onBackPressed();
                                }
                            }else {
                                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("ERROR", "" + getInstance().update + error.toString());
                    }
                }
        ){
            //here I want to post data to sever
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String keyname=sharedPreferences.getString("KEYNAME", "");
                String keyvalue=sharedPreferences.getString("KEYVALUE", "");
                String accesstoken=sharedPreferences.getString("ACCESSTOKEN", "");
                params.put("Authorization", "Bearer "+ accesstoken);
                params.put("Content-Type", "application/json");
                params.put(keyname, keyvalue);
                Log.e("HEADER", "" + getInstance().update + params);
                return params;
            }
        };
        requstQueue.add(jsonobj);

    }
}