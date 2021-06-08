package yummy.one.yummyonevendor.Controller.Activities.Register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import yummy.one.yummyonevendor.Functionality.Retrofit.APIClient;
import yummy.one.yummyonevendor.Functionality.Retrofit.APIInterface;
import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.Functionality.VolleySupport.APIConstant;
import yummy.one.yummyonevendor.R;
import yummy.one.yummyonevendor.Functionality.VolleySupport.AppController;

import static yummy.one.yummyonevendor.Functionality.VolleySupport.APIConstant.getInstance;

public class BankDetails extends AppCompatActivity {

    EditText  edtBankName, edtHolderName, edtAccountNumber, edtIfsc;
    TextView address;
    private String path7 = "";
    ImageView r7;
    LinearLayout doc7,back;
    Button btnNext;
    Session session;
    String Url = "https://ifsc.razorpay.com/";
    SharedPreferences sharedPreferences;
    APIInterface apiInterface;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        progressBar = findViewById(R.id.progressBar);
        edtAccountNumber = findViewById(R.id.edtAccountNumber);
        edtHolderName = findViewById(R.id.edtHolderName);
        edtBankName = findViewById(R.id.edtBankName);
        edtIfsc = findViewById(R.id.edtIfsc);
        back = findViewById(R.id.back);
        doc7 = findViewById(R.id.doc7);
        r7 = findViewById(R.id.r7);
        btnNext = findViewById(R.id.btnNext);
        address = findViewById(R.id.address);

        session = new Session(getApplication());

        APIConstant.getInstance().renewAccessTokenApi(getApplicationContext());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BankDetails.this, RegisterDetails.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        edtIfsc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 11) {
                    GetBranchNameAndAddress(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Vendor").document(session.getusername());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    try {
                        int count = 0;
                        if (documentSnapshot.contains("AccountNumber")) {
                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("AccountNumber")).toString())) {
                                edtAccountNumber.setText(Objects.requireNonNull(documentSnapshot.get("AccountNumber")).toString());
                                count++;
                            }
                        }

                        if (documentSnapshot.contains("BranchName")) {
                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("BranchName")).toString())) {
                                edtBankName.setText(Objects.requireNonNull(documentSnapshot.get("BranchName")).toString());
                                count++;
                            }
                        }


                        if (documentSnapshot.contains("IFSCCode")) {
                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("IFSCCode")).toString())) {
                                edtIfsc.setText(Objects.requireNonNull(documentSnapshot.get("IFSCCode")).toString());
                                count++;
                            }
                        }

                        if (documentSnapshot.contains("AccountName")) {
                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("AccountName")).toString())) {
                                edtHolderName.setText(Objects.requireNonNull(documentSnapshot.get("AccountName")).toString());
                                count++;
                            }
                        }

                        if (documentSnapshot.contains("PassbookImage1") && documentSnapshot.contains("PassbookImageApproval") && documentSnapshot.contains("PassbookImageComments")) {
                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("PassbookImage1")).toString())) {
                                path7 = Objects.requireNonNull(documentSnapshot.get("PassbookImage1")).toString();
                            }

                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("PassbookImageApproval")).toString())) {
                                if (documentSnapshot.get("PassbookImageApproval").toString().equalsIgnoreCase("Pending")) {
                                    doc7.setBackgroundResource(R.color.pending);
                                    r7.setImageResource(R.drawable.right_circle);
                                } else if (documentSnapshot.get("PassbookImageApproval").toString().equalsIgnoreCase("Approved")) {
                                    doc7.setBackgroundResource(R.color.success);
                                    r7.setImageResource(R.drawable.right_circle);
                                } else if (documentSnapshot.get("PassbookImageApproval").toString().equalsIgnoreCase("Rejected")) {
                                    doc7.setBackgroundResource(R.color.warning);
                                    r7.setImageResource(R.drawable.right_circle);
                                }
                            } else {
                                doc7.setBackgroundResource(R.color.initial);
                                r7.setImageResource(R.drawable.right_circle);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        doc7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddBankDetailsApi("s3 url","s3 url","s3 url");
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                Map<String, Object> user = new HashMap<>();
//                user.put("AccountNumber", edtAccountNumber.getText().toString());
//                user.put("BranchName", edtBankName.getText().toString());
//                user.put("IFSCCode", edtIfsc.getText().toString());
//                user.put("AccountName", edtHolderName.getText().toString());
//                db.collection("Vendor").document(session.getusername()).set(user, SetOptions.merge());
//
//                session.setdocument("Yes");
//                Intent intent = new Intent(BankDetails.this, DocumentUploader5.class);
//                session.setdocumentdata("BankDetails");
//                startActivity(intent);
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(edtBankName.getText().toString())) {
                    edtBankName.setError("Enter Bank Name");
                    edtBankName.requestFocus();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                    Animation shake = AnimationUtils.loadAnimation(BankDetails.this, R.anim.shake);
                    edtBankName.startAnimation(shake);
                    return;
                } else {
                    edtBankName.setError(null);
                }


                if (TextUtils.isEmpty(edtHolderName.getText().toString())) {
                    edtHolderName.setError("Enter Holder Name");
                    edtHolderName.requestFocus();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                    Animation shake = AnimationUtils.loadAnimation(BankDetails.this, R.anim.shake);
                    edtHolderName.startAnimation(shake);
                    return;
                } else {
                    edtHolderName.setError(null);
                }

                if (TextUtils.isEmpty(edtAccountNumber.getText().toString())) {
                    edtAccountNumber.setError("Enter Account Number");
                    edtAccountNumber.requestFocus();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                    Animation shake = AnimationUtils.loadAnimation(BankDetails.this, R.anim.shake);
                    edtAccountNumber.startAnimation(shake);
                    return;
                } else {
                    edtAccountNumber.setError(null);
                }

                if (TextUtils.isEmpty(edtIfsc.getText().toString())) {
                    edtIfsc.setError("Enter IFSC Code");
                    edtIfsc.requestFocus();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                    Animation shake = AnimationUtils.loadAnimation(BankDetails.this, R.anim.shake);
                    edtIfsc.startAnimation(shake);
                    return;
                } else {
                    edtIfsc.setError(null);
                }

                AddBankDetailsApi("s3 url","s3 url","s3 url");

//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                Map<String, Object> user = new HashMap<>();
//                user.put("AccountNumber", edtAccountNumber.getText().toString());
//                user.put("BranchName", edtBankName.getText().toString());
//                user.put("IFSCCode", edtIfsc.getText().toString());
//                user.put("AccountName", edtHolderName.getText().toString());
//                db.collection("Vendor").document(session.getusername()).set(user, SetOptions.merge());

//                Intent intent = new Intent(BankDetails.this, RegisterDetails.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
            }
        });
    }

    private void GetBranchNameAndAddress(final String IFSCode) {
        String req = "req";
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, Url + IFSCode,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            Log.e("RESPONSE", "" + Url  + IFSCode + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String ADDRESS = JsonMain.getString("ADDRESS");
                            address.setText(ADDRESS);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        edtIfsc.setError("Enter valid IFSC Code.");
                        edtIfsc.requestFocus();
                    }
                }) {
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().remove(Url + IFSCode);
        AppController.getInstance().addToRequestQueue(stringRequest, req);
    }

    private void AddBankDetailsApi(String passbookImage1,String passbookImage2,  String passbookImage3) {
        progressBar.setVisibility(View.VISIBLE);
        JSONObject data = new JSONObject();
        try {
            String mobilenumber=sharedPreferences.getString("MOBILENUMBER", "");
            data.put("mobilenumber",mobilenumber);
            data.put("accountname", edtHolderName.getText().toString());
            data.put("accountnumber",edtAccountNumber.getText().toString());
            data.put("ifscCode", edtIfsc.getText().toString());
            data.put("branchAddress",edtBankName.getText().toString());
            data.put("passbookImage1",passbookImage1);
            data.put("passbookImage2",passbookImage2);
            data.put("passbookImage3",passbookImage3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("PARAMETER", "" + getInstance().addBankDetails + data);
        RequestQueue requstQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, getInstance().addBankDetails,data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject JsonMain) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().addBankDetails + JsonMain);
                            String msg = JsonMain.getString("message");
                            if (msg.equalsIgnoreCase("SUCCESS")) {
                                JSONObject dataobject=JsonMain.getJSONObject("data");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(BankDetails.this, RegisterDetails.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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
                        Log.e("ERROR", "" + getInstance().addBankDetails + error.toString());
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
                Log.e("HEADER", "" + getInstance().addBankDetails + params);
                return params;
            }
        };
        requstQueue.add(jsonobj);

    }
}