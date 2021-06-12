package yummy.one.yummyonevendor.Controller.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import yummy.one.yummyonevendor.Functionality.Retrofit.APIClient;
import yummy.one.yummyonevendor.Functionality.Retrofit.APIInterface;
import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.Functionality.VolleySupport.APIConstant;
import yummy.one.yummyonevendor.R;

import static yummy.one.yummyonevendor.Functionality.VolleySupport.APIConstant.getInstance;

public class PaymentHistory extends Fragment {

    ImageView imgFilter;
    TextView bank, txtMonthly, txtWeekly, txtDaily;
    View v1, v2;
    LinearLayout t1, t2;
    LinearLayout imgBack;
    SharedPreferences sharedPreferences;
    APIInterface apiInterface;
    ArrayList<Earnings> earningsList = new ArrayList<>();
    RecyclerView recyclerView;
    ProgressBar progressBar;

    public PaymentHistory() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_payment_history, container, false);

        if (getActivity() != null) {
            LinearLayout bottomnavigation = getActivity().findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.GONE);
        }
        progressBar = v.findViewById(R.id.progressBar);

        recyclerView = v.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sharedPreferences = getContext().getSharedPreferences("UserData", getContext().MODE_PRIVATE);

        imgBack = v.findViewById(R.id.back);
        imgFilter = v.findViewById(R.id.imgFilter);
        bank = v.findViewById(R.id.bank);
        txtMonthly = v.findViewById(R.id.txtMonthly);
        txtWeekly = v.findViewById(R.id.txtWeekly);
        txtDaily = v.findViewById(R.id.txtDaily);
        v1 = v.findViewById(R.id.v1);
        v2 = v.findViewById(R.id.v2);
        t1 = v.findViewById(R.id.t1);
        t2 = v.findViewById(R.id.t2);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtMonthly.getVisibility() == View.VISIBLE &&
                        txtWeekly.getVisibility() == View.VISIBLE &&
                        txtDaily.getVisibility() == View.VISIBLE) {
                    txtMonthly.setVisibility(View.GONE);
                    txtWeekly.setVisibility(View.GONE);
                    txtDaily.setVisibility(View.GONE);
                } else {
                    txtMonthly.setVisibility(View.VISIBLE);
                    txtWeekly.setVisibility(View.VISIBLE);
                    txtDaily.setVisibility(View.VISIBLE);
                }
            }
        });

        v1.setVisibility(View.VISIBLE);
        v2.setVisibility(View.GONE);

        GetEarningsApi();

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v1.setVisibility(View.VISIBLE);
                v2.setVisibility(View.GONE);
                GetEarningsApi();
            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v2.setVisibility(View.VISIBLE);
                v1.setVisibility(View.GONE);
                GetSettlementsApi();
            }
        });

        Session session = new Session(getActivity());


        return v;
    }

    private void GetEarningsApi() {
        earningsList.clear();
        progressBar.setVisibility(View.VISIBLE);
        String vendorId=sharedPreferences.getString("VENDERID", "");
        JSONObject data = new JSONObject();
        try {
            data.put("vendorId",vendorId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("PARAMETER", "" + getInstance().GETEARNING + data);
        RequestQueue requstQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, getInstance().GETEARNING,data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject JsonMain) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GETEARNING + JsonMain);
                            String msg = JsonMain.getString("message");
                            if (msg.equalsIgnoreCase("SUCCESS")) {
                                JSONArray dataarray=JsonMain.getJSONArray("data");
                                for (int i=0;dataarray.length()>i;i++){
                                    JSONObject jsonObject=dataarray.getJSONObject(i);
                                    Earnings earnings=new Earnings();
                                    earnings.setDate(jsonObject.getString("date"));
                                    earnings.setGenerated(jsonObject.getString("generated"));
                                    earnings.setTransactionId(jsonObject.getString("transactionId"));
                                    earnings.set_id(jsonObject.getString("_id"));
                                    earnings.setVendorId(jsonObject.getString("vendorId"));
                                    earnings.setUserBalance(jsonObject.getString("userBalance"));
                                    earnings.setAmount(jsonObject.getString("amount"));
                                    earnings.setTransactionType(jsonObject.getString("transactionType"));
                                    earnings.setTransactionNamestatus(jsonObject.getString("transactionName"));
                                    earnings.setStatus(jsonObject.getString("status"));
                                    earnings.setCreatedOn(jsonObject.getString("createdOn"));
                                    earnings.set__v(jsonObject.getString("__v"));
                                    earningsList.add(earnings);
                                }

                                if (earningsList.size() > 0) {
//                                    NotificationFragment.NotificationAdapter notificationAdapter = new NotificationFragment.NotificationAdapter(mContext, notificationList);
//                                    NotificationView.setAdapter(notificationAdapter);
//                                    notificationAdapter.notifyDataSetChanged();
                                }else {
                                    Toast.makeText(getContext(), "No Data found.", Toast.LENGTH_SHORT).show();
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
                        Log.e("ERROR", "" + getInstance().GETEARNING + error.toString());
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
                Log.e("HEADER", "" + getInstance().GETEARNING + params);
                return params;
            }
        };
        requstQueue.add(jsonobj);

    }

    private void GetSettlementsApi() {
        earningsList.clear();
        progressBar.setVisibility(View.VISIBLE);
        String vendorId=sharedPreferences.getString("VENDERID", "");
        JSONObject data = new JSONObject();
        try {
            data.put("vendorId",vendorId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("PARAMETER", "" + getInstance().GETSETTLEMENTS + data);
        RequestQueue requstQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, getInstance().GETSETTLEMENTS,data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject JsonMain) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GETSETTLEMENTS + JsonMain);
                            String msg = JsonMain.getString("message");
                            if (msg.equalsIgnoreCase("SUCCESS")) {
                                JSONArray dataarray=JsonMain.getJSONArray("data");
                                for (int i=0;dataarray.length()>i;i++){
                                    JSONObject jsonObject=dataarray.getJSONObject(i);
                                    Earnings earnings=new Earnings();
                                    earnings.setDate(jsonObject.getString("date"));
                                    earnings.setGenerated(jsonObject.getString("generated"));
                                    earnings.setTransactionId(jsonObject.getString("transactionId"));
                                    earnings.set_id(jsonObject.getString("_id"));
                                    earnings.setVendorId(jsonObject.getString("vendorId"));
                                    earnings.setUserBalance(jsonObject.getString("userBalance"));
                                    earnings.setAmount(jsonObject.getString("amount"));
                                    earnings.setTransactionType(jsonObject.getString("transactionType"));
                                    earnings.setTransactionNamestatus(jsonObject.getString("transactionName"));
                                    earnings.setStatus(jsonObject.getString("status"));
                                    earnings.setCreatedOn(jsonObject.getString("createdOn"));
                                    earnings.set__v(jsonObject.getString("__v"));
                                    earningsList.add(earnings);
                                }

                                if (earningsList.size() > 0) {
//                                    NotificationFragment.NotificationAdapter notificationAdapter = new NotificationFragment.NotificationAdapter(mContext, notificationList);
//                                    NotificationView.setAdapter(notificationAdapter);
//                                    notificationAdapter.notifyDataSetChanged();
                                }else {
                                    Toast.makeText(getContext(), "No Data found.", Toast.LENGTH_SHORT).show();
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
                        Log.e("ERROR", "" + getInstance().GETSETTLEMENTS + error.toString());
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
                Log.e("HEADER", "" + getInstance().GETSETTLEMENTS + params);
                return params;
            }
        };
        requstQueue.add(jsonobj);

    }

    public static class Earnings {

        String date;
        String generated;
        String transactionId;
        String _id;
        String vendorId;
        String userBalance;
        String amount;
        String transactionType;
        String transactionNamestatus;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        String status;
        String createdOn;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getGenerated() {
            return generated;
        }

        public void setGenerated(String generated) {
            this.generated = generated;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getVendorId() {
            return vendorId;
        }

        public void setVendorId(String vendorId) {
            this.vendorId = vendorId;
        }

        public String getUserBalance() {
            return userBalance;
        }

        public void setUserBalance(String userBalance) {
            this.userBalance = userBalance;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getTransactionType() {
            return transactionType;
        }

        public void setTransactionType(String transactionType) {
            this.transactionType = transactionType;
        }

        public String getTransactionNamestatus() {
            return transactionNamestatus;
        }

        public void setTransactionNamestatus(String transactionNamestatus) {
            this.transactionNamestatus = transactionNamestatus;
        }

        public String getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(String createdOn) {
            this.createdOn = createdOn;
        }

        public String get__v() {
            return __v;
        }

        public void set__v(String __v) {
            this.__v = __v;
        }

        String __v;

        public Earnings() {

        }

    }
}