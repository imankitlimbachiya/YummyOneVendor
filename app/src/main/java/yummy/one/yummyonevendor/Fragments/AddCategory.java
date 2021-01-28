package yummy.one.yummyonevendor.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import yummy.one.yummyonevendor.FoodCategory.Category;
import yummy.one.yummyonevendor.FoodCategory.Category1;
import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.R;


public class AddCategory extends Fragment {


    private RecyclerView recyclerview;
    private ImageView submit;
    private EditText category1;
    private Category1 category;
    private String a="";



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

        ImageView back = v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        submit=v.findViewById(R.id.submit);
        category1=v.findViewById(R.id.category);
        recyclerview=v.findViewById(R.id.recyclerview);
        Session session=new Session(getContext());


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

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> data = new HashMap<>();
                data.put("ItemCategory", a);
                db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());


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
}
