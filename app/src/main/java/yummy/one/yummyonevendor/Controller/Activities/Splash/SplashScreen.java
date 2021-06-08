package yummy.one.yummyonevendor.Controller.Activities.Splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.grpc.Server;
import okhttp3.OkHttpClient;
import yummy.one.yummyonevendor.Controller.Activities.Login.OtpActivity;
import yummy.one.yummyonevendor.Controller.Activities.Register.DocumentUploader1;
import yummy.one.yummyonevendor.Controller.Activities.Register.RegisterDetails;
import yummy.one.yummyonevendor.Controller.Activities.SignUp.SignUp;
import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.Controller.Activities.Login.Login;
import yummy.one.yummyonevendor.Controller.Activities.MainActivity;
import yummy.one.yummyonevendor.Functionality.VolleySupport.APIConstant;
import yummy.one.yummyonevendor.Functionality.VolleySupport.AppController;
import yummy.one.yummyonevendor.R;

import static yummy.one.yummyonevendor.Functionality.VolleySupport.APIConstant.getInstance;

public class SplashScreen extends AppCompatActivity {

    Context mContext;
    protected boolean _active = true;
    protected int _splashTime = 3000;
    private Session session;
    String VenderID;
    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(getApplicationContext());
        setContentView(R.layout.activity_splash_screen);

        mContext = this;
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        VenderID = sharedPreferences.getString("VENDERID", "");

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("KEYNAME", "ApiKey");
        editor.putString("KEYVALUE", "7164ea6006390c67a98172efb82fd007");
        editor.apply();
        editor.commit();
        session = new Session(mContext);
        APIConstant.getInstance().renewAccessTokenApi(mContext);


        final Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(100);
                        if (_active) {
                            waited += 100;
                        }
                    }
                } catch (Exception ignored) {

                } finally {
                    if (VenderID.equals("")) {

                        startActivity(new Intent(mContext, Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }else {

                        startActivity(new Intent(mContext, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }


//                    if (TextUtils.isEmpty(session.getisfirsttime())) {
//                        FirebaseDynamicLinks.getInstance()
//                                .getDynamicLink(getIntent())
//                                .addOnSuccessListener(SplashScreen.this, new OnSuccessListener<PendingDynamicLinkData>() {
//                                    @Override
//                                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
//                                        // Get deep link from result (may be null if no link is found)
//                                        Uri deepLink = null;
//                                        if (pendingDynamicLinkData != null) {
//                                            deepLink = pendingDynamicLinkData.getLink();
//                                        }
//                                        if (deepLink != null && deepLink.getBooleanQueryParameter("invitedby", false)) {
//                                            String referrerUid = deepLink.getQueryParameter("invitedby");
//                                            session.setreferral(referrerUid);
//                                        }
//                                        startActivity(new Intent(mContext, Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                                        finish();
//                                    }
//                                });
//                    }
//                    else {
//                        if (!session.getusername().equals("")) {
//                            session.settemp("");
//                            session.setaddress("");
//                            session.setpincode("");
//                            session.setcity("");
//                            session.setstate("");
//                            session.setdocument("");
//                            startActivity(new Intent(mContext, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                        } else {
//                            startActivity(new Intent(mContext, Login.class));
//                        }
//                        finish();
//                    }
                }
            }
        };
        splashTread.start();

       /* final Thread splashTread = new Thread() {
            @Override
            public void run() {
                session.setdocument("Yes");
                Intent intent = new Intent(SplashScreen.this, DocumentUploader1.class);
                session.setdocumentdata("FoodLicense");
                startActivity(intent);
            }
        };
        splashTread.start();
*/

    }



    private void renewAccessTokenApi() {
        RequestQueue requstQueue = Volley.newRequestQueue(mContext);
        StringRequest jsonobj = new StringRequest(Request.Method.GET, getInstance().renewAccessToken,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("RESPONSE", "" + APIConstant.getInstance().renewAccessToken + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String msg = JsonMain.getString("message");
                            if (msg.equalsIgnoreCase("SUCCESS")) {
                                JSONObject Jsondata = JsonMain.getJSONObject("data").getJSONObject("tokenInfo");
                                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("ACCESSTOKEN", Jsondata.getString("accessToken"));
                                editor.putString("REFRESHTOKEN", Jsondata.getString("refreshToken"));
                                editor.apply();
                                editor.commit();
                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "" + getInstance().renewAccessToken + error.toString());
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
                params.put("Refresh-Token", accesstoken);
                params.put(keyname, keyvalue);
                params.put("accept-language", "en-US");
                Log.e("HEADER", "" + getInstance().renewAccessToken + params);
                return params;
            }
        };
        requstQueue.add(jsonobj);

    }

    private void cuisinesApi() {
        RequestQueue requstQueue = Volley.newRequestQueue(mContext);
        StringRequest jsonobj = new StringRequest(Request.Method.GET, getInstance().cuisines,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("RESPONSE", "" + APIConstant.getInstance().cuisines + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String msg = JsonMain.getString("message");
                            if (msg.equalsIgnoreCase("SUCCESS")) {
                                JSONArray Jsonarray = JsonMain.getJSONArray("data");
//                                    Jsonarray.getJSONObject(i).getString("_id");
//                                    Jsonarray.getJSONObject(i).getString("cuisines");
//                                    Jsonarray.getJSONObject(i).getString("createdOn");
//                                    Jsonarray.getJSONObject(i).getString("__v");


                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "" + getInstance().cuisines + error.toString());
                    }
                }
        ){
            //here I want to post data to sever
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String keyname=sharedPreferences.getString("KEYNAME", "");
                String keyvalue=sharedPreferences.getString("KEYVALUE", "");
                //String accesstoken=sharedPreferences.getString("ACCESSTOKEN", "");
                //params.put("Authorization", "Bearer "+ accesstoken);
                params.put("Content-Type", "application/json");
                params.put(keyname, keyvalue);
                Log.e("HEADER", "" + getInstance().cuisines + params);
                return params;
            }
        };
        requstQueue.add(jsonobj);

    }

    private void vendorApi() {
        RequestQueue requstQueue = Volley.newRequestQueue(mContext);
        StringRequest jsonobj = new StringRequest(Request.Method.GET, getInstance().vendor,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("RESPONSE", "" + APIConstant.getInstance().vendor + response);
//                            JSONObject JsonMain = new JSONObject(response);
//                            String msg = JsonMain.getString("message");
//                            if (msg.equalsIgnoreCase("SUCCESS")) {
//                                JSONObject Jsondata = JsonMain.getJSONObject("data").getJSONObject("tokenInfo");
//                                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
//                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                editor.putString("ACCESSTOKEN", Jsondata.getString("accessToken"));
//                                editor.putString("REFRESHTOKEN", Jsondata.getString("refreshToken"));
//                                editor.apply();
//                                editor.commit();
//                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
//                            }else {
//                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
//                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "" + getInstance().vendor + error.toString());
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
                Log.e("HEADER", "" + getInstance().vendor + params);
                return params;
            }
        };
        requstQueue.add(jsonobj);

    }

    private void checkPhoneNumberApi() {
        RequestQueue requstQueue = Volley.newRequestQueue(mContext);
        StringRequest jsonobj = new StringRequest(Request.Method.GET, getInstance().checkPhoneNumber,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("RESPONSE", "" + APIConstant.getInstance().checkPhoneNumber + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String msg = JsonMain.getString("message");
                            if (msg.equalsIgnoreCase("SUCCESS")) {
                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "" + getInstance().checkPhoneNumber + error.toString());
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
                Log.e("HEADER", "" + getInstance().checkPhoneNumber + params);
                return params;
            }
        };
        requstQueue.add(jsonobj);

    }

    private void YUM16Api() {
        RequestQueue requstQueue = Volley.newRequestQueue(mContext);
        StringRequest jsonobj = new StringRequest(Request.Method.GET, getInstance().YUM16,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("RESPONSE", "" + APIConstant.getInstance().YUM16 + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String msg = JsonMain.getString("message");
                            if (msg.equalsIgnoreCase("SUCCESS")) {
                                JSONArray jsonArray = JsonMain.getJSONArray("data");

                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "" + getInstance().YUM16 + error.toString());
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
                Log.e("HEADER", "" + getInstance().YUM16 + params);
                return params;
            }
        };
        requstQueue.add(jsonobj);

    }


    //SharedPreferences ALL KEY USE IN PROJECT

//    SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
//    SharedPreferences.Editor editor = sharedPreferences.edit();
//    editor.putString("KEYNAME", "ApiKey");
//    editor.putString("KEYVALUE", "7164ea6006390c67a98172efb82fd007");
//    editor.putString("NAME", dataobject.getString("name"));
//    editor.putString("MOBILENUMBER", dataobject.getString("name"));
//    editor.putString("VENDERID", dataobject.getString("vendorId"));
//    editor.putString("STORENAME", dataobject.getString("storeName"));
//    editor.putString("ADDRESS", dataobject.getString("address"));
//    editor.putString("LATITUDE", dataobject.getString("latitude"));
//    editor.putString("LONGITUDE", dataobject.getString("longitude"));
//    editor.putString("OPENTIME", dataobject.getString("openTime"));
//    editor.putString("CLOSETIME", dataobject.getString("closeTime"));
//    editor.putString("STATUS", dataobject.getString("status"));
//    editor.putString("APPROVALSTATUS", dataobject.getString("approvalStatus"));
//    editor.putString("ACCESSTOKEN", dataobject.getJSONObject("tokenInfo").getString("accessToken"));
//    editor.putString("REFRESHTOKEN", dataobject.getJSONObject("tokenInfo").getString("refreshToken"));
//    editor.apply();


}