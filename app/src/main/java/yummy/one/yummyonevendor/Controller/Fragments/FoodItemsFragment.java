package yummy.one.yummyonevendor.Controller.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.Models.Inventory.Inventory;
import yummy.one.yummyonevendor.Models.Inventory.InventoryAdapter;
import yummy.one.yummyonevendor.R;

public class FoodItemsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private Session sessions;
    private Button btnFooditems, btnRemove;
    private ArrayList<Inventory> inventories = new ArrayList<Inventory>();
    private InventoryAdapter inventoryAdapter;
    private String category = "";
    private LinearLayout imgBack;
    private TextView txtCategoryName;
    int index = 0;
    ArrayList<String> categoryc;

    public FoodItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_food_items, container, false);

        if (getActivity() != null) {
            LinearLayout bottomnavigation = getActivity().findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.GONE);
        }

        imgBack = v.findViewById(R.id.back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        mRecyclerView = v.findViewById(R.id.recyclerView);
        btnFooditems = v.findViewById(R.id.btnFooditems);
        btnRemove = v.findViewById(R.id.btnRemove);
        txtCategoryName = v.findViewById(R.id.txtCategoryName);
        sessions = new Session(getActivity());

        inventories.clear();
        inventoryAdapter = new InventoryAdapter(inventories);

        category = getArguments().getString("category");
        index = Integer.parseInt(getArguments().getString("index"));

        txtCategoryName.setText(category);

        btnFooditems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    if (sessions.getcategory().equals("Grocery")) {
                        if (getActivity() != null) {
                            Fragment fragment = new AddGroceryItems();
                            Bundle bundle = new Bundle();
                            bundle.putString("category", category);
                            fragment.setArguments(bundle);
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .addToBackStack(null)
                                    .replace(R.id.frame_container, fragment).commit();
                        }
                    } else {
                        if (getActivity() != null) {
                            Fragment fragment = new AddFood();
                            Bundle bundle = new Bundle();
                            bundle.putString("category", category);
                            fragment.setArguments(bundle);
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .addToBackStack(null)
                                    .replace(R.id.frame_container, fragment).commit();
                        }
                    }
                }
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Vendor").document(sessions.getusername());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String temp = documentSnapshot.get("ItemCategory").toString();
                    String a = temp;
                    if (!TextUtils.isEmpty(a)) {
                        categoryc = new ArrayList<String>(Arrays.asList(temp.split(",")));
                    }
                }
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inventoryAdapter.getItemCount() <= 0) {
                    categoryc.remove(index);
                    String a = "";
                    for (int i = 0; i < categoryc.size(); i++) {
                        a += categoryc.get(i) + ",";
                    }
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> data = new HashMap<>();
                    data.put("ItemCategory", a);
                    db.collection("Vendor").document(sessions.getusername()).set(data, SetOptions.merge());
                    Toast.makeText(getContext(), "Category Deleted Successfully", Toast.LENGTH_LONG).show();
                    if (getActivity() != null)
                        getActivity().onBackPressed();
                } else {
                    Toast.makeText(getContext(), "Delete or move food items to delete the category", Toast.LENGTH_LONG).show();
                }
            }
        });

        CollectionReference docRef1 = db.collection("Vendor")
                .document(sessions.getusername())
                .collection("Products");
        docRef1.whereEqualTo("ItemCategory", category).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    inventories.clear();
                    mRecyclerView.setVisibility(View.VISIBLE);
                    if (sessions.getcategory().equals("Grocery")) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String foodtype = "";
                            foodtype = document.get("FoodImage").toString();
                            inventories.add(new Inventory(
                                    document.get("ItemName").toString(),
                                    document.getId(),
                                    document.get("Status").toString(),
                                    foodtype,
                                    document.get("ApprovalStatus").toString(),
                                    document.get("Mrp").toString(),
                                    category
                            ));
                        }
                    } else {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String foodtype = "";
                            foodtype = document.get("FoodImage").toString();
                            inventories.add(new Inventory(
                                    document.get("ItemName").toString(),
                                    document.getId(),
                                    document.get("Status").toString(),
                                    foodtype,
                                    document.get("ApprovalStatus").toString(),
                                    document.get("MarkettingPrice").toString(),
                                    category
                            ));
                        }
                    }
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    inventoryAdapter = new InventoryAdapter(inventories);
                    mRecyclerView.setAdapter(inventoryAdapter);
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                }
            }
        });

        return v;
    }
}