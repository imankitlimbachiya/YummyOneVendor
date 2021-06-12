package yummy.one.yummyonevendor.Functionality.VolleySupport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import yummy.one.yummyonevendor.Controller.Activities.Login.Login;
import yummy.one.yummyonevendor.Controller.Activities.Login.OtpActivity;
import yummy.one.yummyonevendor.Controller.Activities.SignUp.SignUp;

public class APIConstant {

    private static APIConstant apiConstant;

    public synchronized static APIConstant getInstance() {
        if (apiConstant == null)
            apiConstant = new APIConstant();
        return apiConstant;
    }


    private final String BASE_URL = "https://dev.yummyone.online/v1/api";

    public final String KEY = "AKIA6FKRFHSDVK6VYIOR";
    public final String SECRET = "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";

    //  Api POST
    public final String sendOtp = BASE_URL + "/auth/vendor/sendOtp";
    public final String verifyOtp = BASE_URL + "/auth/vendor/verifyOtp";
    public final String signInWithPhoneNumber = BASE_URL + "/auth/vendor/signInWithPhoneNumber";
    public final String signUp = BASE_URL + "/auth/vendor/signUp";
    public final String addRestaurantInfo = BASE_URL + "/vendor/addRestaurantInfo";
    public final String update = BASE_URL + "/vendor/update";
    public final String addBankDetails = BASE_URL + "/vendor/addBankDetails";
    public final String addPancardDocument = BASE_URL + "/vendor/addPancardDocument";
    public final String addFSSAIDocument = BASE_URL + "/vendor/addFSSAIDocument";
    public final String addGSTDocument = BASE_URL + "/vendor/addGSTDocument";
    public final String addRegistrationCertDocument = BASE_URL + "/vendor/addRegistrationCertDocument";
    public final String addRestaurantDocument = BASE_URL + "/vendor/addRestaurantDocument";
    public final String addCategory = BASE_URL + "/vendor/addCategory";
    public final String addFood = BASE_URL + "/vendor/addFood";
    public final String ADDRESTAURANTDETSILS = BASE_URL + "/vendor/addRestaurantDetails";
    public final String getRestaurantDetails = BASE_URL + "/vendor/getRestaurantDetails";
    public final String GETNOTIFICATION =  BASE_URL +"/vendor/getNotifications";
    public final String GETEARNING =  BASE_URL +"/vendor/getEarnings";
    public final String GETSETTLEMENTS =  BASE_URL +"/vendor/getSettlements";

    //APi GET
    public final String renewAccessToken = BASE_URL + "/auth/vendor/renewAuthToken";
    public final String cuisines =  BASE_URL +"/cuisines";
    public final String vendor =  BASE_URL +"/vendor";
    public final String checkPhoneNumber =  BASE_URL +"/auth/vendor/checkPhoneNumber/";
    public final String YUM16 =  BASE_URL +"/vendor/food/YUM6";
    public final String GETFAQ =  BASE_URL +"/vendor/getFAQs";
    public final String GETVENDORSTATUS =  BASE_URL +"/vendor/getVendorStatus";
    public final String GETAPPLICATIONSTATUS =  BASE_URL +"/vendor/getApplicationStatus";


    public void renewAccessTokenApi(Context mContext) {
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
                                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", mContext.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("ACCESSTOKEN", Jsondata.getString("accessToken"));
                                editor.putString("REFRESHTOKEN", Jsondata.getString("refreshToken"));
                                editor.apply();
                                editor.commit();
                                //Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                            }else {
                                // Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
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
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserData", mContext.MODE_PRIVATE);
                String keyname=sharedPreferences.getString("KEYNAME", "");//Refresh-Token
                String keyvalue=sharedPreferences.getString("KEYVALUE", "");
                String accesstoken=sharedPreferences.getString("refreshToken", "");
                params.put("Refresh-Token", accesstoken);
               // params.put("Connection", "keep-alive");
                   params.put(keyname, keyvalue);
                //params.put("ApiKey", "7164ea6006390c67a98172efb82fd007");
                Log.e("HEADER", "" + getInstance().renewAccessToken + params);
                return params;
            }
        };
        requstQueue.add(jsonobj);

    }

//    private void verifyOtpApi() {
//        String req = "req";
//        final StringRequest stringRequest = new StringRequest(Request.Method.POST, getInstance().verifyOtp, new Response.Listener<String>() {
//            @Override
//            public void onResponse(final String response) {
//                try {
//                    Log.e("RESPONSE", "" + APIConstant.getInstance().verifyOtp + response);
//                    JSONObject JsonMain = new JSONObject(response);
//                    String HAS_ERROR = JsonMain.getString("has_error");
//                    String Message = JsonMain.getString("msg");
//                    Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
//                    /*if (HAS_ERROR.equalsIgnoreCase("false")) {
//
//                    } else {
//
//                    }*/
//                } catch (Exception exception) {
//                    exception.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            public void onErrorResponse(VolleyError error) {
//            }
//        }) {
//
//            // Header data passing
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("ApiKey", "7164ea6006390c67a98172efb82fd007");
//                params.put("Content-Type", "application/json");
//                Log.e("HEADER", "" + APIConstant.getInstance().verifyOtp + params);
//                return params;
//            }
//
//            // Raw data passing
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                String params = "{\"otp\":\"" + "385390" + "\",\"sessionId\":\"" + "53616913-285d-4eae-9574-fd0838c2d479" + "\"}";
//                Log.e("PARAMETER", "" + APIConstant.getInstance().verifyOtp + params);
//                return params.getBytes();
//            }
//        };
//
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().verifyOtp);
//        AppController.getInstance().addToRequestQueue(stringRequest, req);
//    }
//
//    private void sendOtpApi() {
//        String req = "req";
//        final StringRequest stringRequest = new StringRequest(Request.Method.POST, getInstance().sendOtp, new Response.Listener<String>() {
//            @Override
//            public void onResponse(final String response) {
//                try {
//                    Log.e("RESPONSE", "" + APIConstant.getInstance().sendOtp + response);
//                    JSONObject JsonMain = new JSONObject(response);
//                    String HAS_ERROR = JsonMain.getString("has_error");
//                    String Message = JsonMain.getString("msg");
//                    Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
//                    /*if (HAS_ERROR.equalsIgnoreCase("false")) {
//
//                    } else {
//
//                    }*/
//                } catch (Exception exception) {
//                    exception.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            public void onErrorResponse(VolleyError error) {
//            }
//        }) {
//
//            // Header data passing
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("ApiKey", "7164ea6006390c67a98172efb82fd007");
//                params.put("Content-Type", "application/json");
//                Log.e("HEADER", "" + APIConstant.getInstance().sendOtp + params);
//                return params;
//            }
//
//            // Raw data passing
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                String params = "{\"mobilenumber\":\"" + "+919632125551" + "\"}";
//                Log.e("PARAMETER", "" + APIConstant.getInstance().sendOtp + params);
//                return params.getBytes();
//            }
//        };
//
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().sendOtp);
//        AppController.getInstance().addToRequestQueue(stringRequest, req);
//    }
//
//    private void signInWithPhoneNumberApi(String mobilenumber) {
//        String req = "req";
//        final StringRequest stringRequest = new StringRequest(Request.Method.POST, getInstance().signInWithPhoneNumber, new Response.Listener<String>() {
//            @Override
//            public void onResponse(final String response) {
//                try {
//                    Log.e("RESPONSE", "" + APIConstant.getInstance().signInWithPhoneNumber + response);
//                    JSONObject JsonMain = new JSONObject(response);
//                    String Message = JsonMain.getString("msg");
//                    if (Message.equalsIgnoreCase("SUCCESS")) {
//                        JSONObject dataobject=JsonMain.getJSONObject("data");
//                        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("NAME", dataobject.getString("name"));
//                        editor.putString("MOBILENUMBER", dataobject.getString("mobilenumber"));
//                        editor.putString("VENDERID", dataobject.getString("vendorId"));
//                        editor.putString("STORENAME", dataobject.getString("storeName"));
//                        editor.putString("ADDRESS", dataobject.getString("address"));
//                        editor.putString("LATITUDE", dataobject.getString("latitude"));
//                        editor.putString("LONGITUDE", dataobject.getString("longitude"));
//                        editor.putString("OPENTIME", dataobject.getString("openTime"));
//                        editor.putString("CLOSETIME", dataobject.getString("closeTime"));
//                        editor.putString("STATUS", dataobject.getString("status"));
//                        editor.putString("APPROVALSTATUS", dataobject.getString("approvalStatus"));
//                        editor.putString("ACCESSTOKEN", dataobject.getJSONObject("tokenInfo").getString("accessToken"));
//                        editor.putString("REFRESHTOKEN", dataobject.getJSONObject("tokenInfo").getString("refreshToken"));
//                        editor.apply();
//                        editor.commit();
//                        Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(Login.this, OtpActivity.class);
//                        intent.putExtra("status", "registered");
//                        intent.putExtra("id", id);
//                        intent.putExtra("mobilenumber", edtPhoneNumber.getText().toString());
//                        intent.putExtra("name", "");
//                        intent.putExtra("dob", "");
//                        intent.putExtra("category", "");
//                        startActivity(intent);
//                        finish();
//                    }else {
//                        Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(Login.this, SignUp.class);
//                        intent.putExtra("status", "notregistered");
//                        intent.putExtra("id", "");
//                        intent.putExtra("mobilenumber", edtPhoneNumber.getText().toString().substring(4));
//                        startActivity(intent);
//                        finish();
//                    }
//
//                } catch (Exception exception) {
//                    exception.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            public void onErrorResponse(VolleyError error) {
//            }
//        }) {
//
//            // Header data passing
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                String keyname=sharedPreferences.getString("KEYNAME", "");
//                String keyvalue=sharedPreferences.getString("KEYVALUE", "");
//                //params.put("ApiKey", "7164ea6006390c67a98172efb82fd007");
//                params.put(keyname, keyvalue);
//                params.put("Content-Type", "application/json");
//                Log.e("HEADER", "" + APIConstant.getInstance().signInWithPhoneNumber + params);
//                return params;
//            }
//
//            // Raw data passing
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                String params = "{\"mobilenumber\":\"" + "+917567513635" + "\"}";
//                Log.e("PARAMETER", "" + APIConstant.getInstance().signInWithPhoneNumber + params);
//                return params.getBytes();
//            }
//        };
//
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        AppController.getInstance().getRequestQueue().getCache().remove(APIConstant.getInstance().signInWithPhoneNumber);
//        AppController.getInstance().addToRequestQueue(stringRequest, req);
//    }

//S3 Bucket
//    private class UploadProfileAsyncTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... params) {
//            // TODO Auto-generated method stub
//            new UploadProThread().start();
//            return null;
//        }
//    }
//


   // call function like this
//            if (mPath != null) {
//        if (Consts.isNetworkAvailable(Edit_ProfileActivity.this)) {
//            //  progress.setVisibility(View.VISIBLE);
//            dialog.show();
//            new UploadProfileAsyncTask().execute();
//        } else {
//            Toast.makeText(Edit_ProfileActivity.this, R.string.networkcheck, Toast.LENGTH_SHORT).show();
//        }
//    }


//
//    accesskey secret key and bucket key tane api side thi malse and tare url bi nakhavi padase

}

