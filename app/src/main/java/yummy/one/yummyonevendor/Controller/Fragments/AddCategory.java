package yummy.one.yummyonevendor.Controller.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import yummy.one.yummyonevendor.Functionality.Retrofit.APIClient;
import yummy.one.yummyonevendor.Functionality.Retrofit.APIInterface;
import yummy.one.yummyonevendor.Functionality.VolleySupport.APIConstant;
import yummy.one.yummyonevendor.Models.FoodCategory.Category1;
import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.R;

import static yummy.one.yummyonevendor.Functionality.VolleySupport.APIConstant.getInstance;


public class AddCategory extends Fragment {


    private RecyclerView recyclerview;
    private ImageView submit;
    private EditText category1;
    private Category1 category;
    private String a="";

    ProgressBar progressBar;

    SharedPreferences sharedPreferences;
    APIInterface apiInterface;

    public AddCategory() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_add_category, container, false);

        LinearLayout bottomnavigation=(getActivity()).findViewById(R.id.bottomnavigation);
        bottomnavigation.setVisibility(View.GONE);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sharedPreferences = getContext().getSharedPreferences("UserData", getContext().MODE_PRIVATE);
        ImageView back = v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        submit=v.findViewById(R.id.submit);
        progressBar=v.findViewById(R.id.progressBar);
        category1=v.findViewById(R.id.category);
        recyclerview=v.findViewById(R.id.recyclerview);
        Session session=new Session(getContext());
        APIConstant.getInstance().renewAccessTokenApi(getContext());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Vendor").document(session.getusername());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String temp=documentSnapshot.get("ItemCategory").toString();
                    a=temp;
                    ArrayList<String> category1 = new ArrayList<String>(Arrays.asList(temp.split(",")));
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    recyclerview.setLayoutManager(mLayoutManager);
                    category = new Category1(category1);
                    recyclerview.setAdapter(category);
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(category1.getText().toString())){
                    category1.setError("Enter Category");
                    category1.requestFocus();
                    return;
                }

                a+=category1.getText().toString()+",";
                AddCategoryApi(a);
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                Map<String, Object> data = new HashMap<>();
//                data.put("ItemCategory", a);
//                db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());


                category1.setText("");


                DocumentReference docRef = db.collection("Vendor").document(session.getusername());
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String temp=documentSnapshot.get("ItemCategory").toString();
                            a=temp;
                            ArrayList<String> category1 = new ArrayList<String>(Arrays.asList(temp.split(",")));
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerview.setLayoutManager(mLayoutManager);
                            category = new Category1(category1);
                            recyclerview.setAdapter(category);
                        }
                    }
                });




            }
        });



        return v;
    }

    private void AddCategoryApi(String itemCategory) {
        progressBar.setVisibility(View.VISIBLE);
        JSONObject data = new JSONObject();
        try {
            String mobilenumber=sharedPreferences.getString("MOBILENUMBER", "");

            data.put("mobilenumber",mobilenumber);
            data.put("itemCategory",itemCategory);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("PARAMETER", "" + getInstance().addCategory + data);
        RequestQueue requstQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, getInstance().addCategory,data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject JsonMain) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().addCategory + JsonMain);
                            String msg = JsonMain.getString("message");
                            if (msg.equalsIgnoreCase("SUCCESS")) {
                                JSONObject dataobject=JsonMain.getJSONObject("data");
                                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                                category1.setText("");
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
                        Log.e("ERROR", "" + getInstance().addCategory + error.toString());
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
                Log.e("HEADER", "" + getInstance().addCategory + params);
                return params;
            }
        };
        requstQueue.add(jsonobj);

    }
}
