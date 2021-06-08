package yummy.one.yummyonevendor.Controller.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
import yummy.one.yummyonevendor.Models.FoodCategory.Category;
import yummy.one.yummyonevendor.Models.FoodCategory.CategoryData;
import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.R;

import static yummy.one.yummyonevendor.Functionality.VolleySupport.APIConstant.getInstance;


public class InventoryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private Session sessions;
    private Button btnCategory;
    private String a = "";
    private Category category;
    EditText edtCategory;
    Button btnSave;
    BottomSheetDialog bottomSheetDialog;
    ArrayList<CategoryData> categoryData = new ArrayList<CategoryData>();

    SharedPreferences sharedPreferences;
    APIInterface apiInterface;
    ProgressBar progressBar;

    public InventoryFragment() {
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
        View v = inflater.inflate(R.layout.fragment_inventory, container, false);

        if (getActivity() != null) {
            LinearLayout bottomnavigation = getActivity().findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.VISIBLE);
        }
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sharedPreferences = getContext().getSharedPreferences("UserData", getContext().MODE_PRIVATE);

        mRecyclerView = v.findViewById(R.id.recyclerView);
        progressBar = v.findViewById(R.id.progressBar);
        btnCategory = v.findViewById(R.id.btnCategory);
        bottomSheetDialog = new BottomSheetDialog(getContext());
        sessions = new Session(getActivity());

       // loadData();
        categoryData.clear();


//        LayoutInflater factory = LayoutInflater.from(getActivity());
//        final View deleteDialogView = factory.inflate(R.layout.bottom_category, null);
//        final AlertDialog deleteDialog = new AlertDialog.Builder(getActivity()).create();
//        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        deleteDialog.setView(deleteDialogView);
//        final View bottomSheetDialogView1=getLayoutInflater().inflate(R.layout.bottom_category,null);
//        bottomSheetDialog.setContentView(bottomSheetDialogView1);
//        edtCategory = deleteDialogView.findViewById(R.id.edtCategory);
//        btnSave = deleteDialogView.findViewById(R.id.btnSave);
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(TextUtils.isEmpty(edtCategory.getText().toString())){
//                    edtCategory.setError("Enter Category Name");
//                    edtCategory.requestFocus();
//                    return;
//                }
//                else{
//                    edtCategory.setError(null);
//                }
//                a+=edtCategory.getText().toString()+",";
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                Map<String, Object> data = new HashMap<>();
//                data.put("ItemCategory", a);
//                data.put(edtCategory.getText().toString(),0);
//                db.collection("Vendor").document(sessions.getusername()).set(data, SetOptions.merge());
//                edtCategory.setText("");
//                deleteDialog.dismiss();
//            }
//        });


        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                final EditText input = new EditText(getActivity());
                input.setSingleLine();
                input.setHint("Enter Category Name");
                input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                FrameLayout container1 = new FrameLayout(getActivity());
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.topMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                input.setLayoutParams(params);
                container1.addView(input);
                alert.setTitle("Category");
                alert.setView(container1);
                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (TextUtils.isEmpty(input.getText().toString())) {
                            input.setError("Enter Category Name");
                            input.requestFocus();
                            return;
                        } else {
                            input.setError(null);
                        }

                        a += input.getText().toString() + ",";

                        AddCategoryApi(a);
                        input.setText("");
//                        FirebaseFirestore db = FirebaseFirestore.getInstance();
//                        Map<String, Object> data = new HashMap<>();
//                        data.put("ItemCategory", a);
//                        data.put(input.getText().toString(), 0);
//                        db.collection("Vendor").document(sessions.getusername()).set(data, SetOptions.merge());

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

                alert.show();
            }
        });


        return v;
    }

    public void loadData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("Vendor").document(sessions.getusername());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    String temp = snapshot.get("ItemCategory").toString();
                    a = temp;
                    categoryData.clear();
                    if (!TextUtils.isEmpty(a)) {
                        ArrayList<String> category1 = new ArrayList<String>(Arrays.asList(temp.split(",")));
                        for (String a : category1) {
                            if (snapshot.contains(a))
                                categoryData.add(new CategoryData(a, snapshot.get(a).toString()));
                            else
                                categoryData.add(new CategoryData(a, "0"));
                        }
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        category = new Category(categoryData);
                        mRecyclerView.setAdapter(category);
//                        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), mLayoutManager.getOrientation()));
                    }
                }
            }
        });


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
