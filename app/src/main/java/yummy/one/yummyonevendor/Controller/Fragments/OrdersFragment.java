package yummy.one.yummyonevendor.Controller.Fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.zcw.togglebutton.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.Models.Orders.OrderAdapter;
import yummy.one.yummyonevendor.Models.Orders.OrdersData;
import yummy.one.yummyonevendor.R;

public class OrdersFragment extends Fragment {

    private TextView txtNeworders, txtPastorders, txtCount, txtStatus, txtCurrentDate;
    private LinearLayout linearBlock1, linearBlock2, linearNew;
    private Session sessions;
    private ToggleButton toggleStatus;

    private ArrayList<OrdersData> orders = new ArrayList<OrdersData>();
    private OrderAdapter orderAdapter;
    private RecyclerView recyclerView;
    int count = 0;

    public OrdersFragment() {
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
        View v = inflater.inflate(R.layout.fragment_orders, container, false);

        if (getActivity() != null) {
            LinearLayout bottomnavigation = getActivity().findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.VISIBLE);
        }

        txtNeworders = v.findViewById(R.id.txtNeworders);
        txtPastorders = v.findViewById(R.id.txtPastorders);
        linearBlock1 = v.findViewById(R.id.linearBlock1);
        linearBlock2 = v.findViewById(R.id.linearBlock2);
        recyclerView = v.findViewById(R.id.recyclerView);
        toggleStatus = v.findViewById(R.id.toggleStatus);
        txtCount = v.findViewById(R.id.txtCount);
        txtStatus = v.findViewById(R.id.txtStatus);
        linearNew = v.findViewById(R.id.linearNew);
        txtCurrentDate = v.findViewById(R.id.txtCurrentDate);

        sessions = new Session(getActivity());

        final Date currentDate = new Date(System.currentTimeMillis());

        SimpleDateFormat df1 = new SimpleDateFormat("MMM dd, yyyy  HH:MM a");
        final String date2 = df1.format(currentDate);

        txtCurrentDate.setText(date2);


        if (TextUtils.isEmpty(sessions.getselection())) {
            txtNeworders.setTextColor(getResources().getColor(R.color.white));
            txtNeworders.setBackgroundResource(R.drawable.button_border);
            txtNeworders.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            txtPastorders.setTextColor(getResources().getColor(R.color.colorDarkgrey));
            txtPastorders.setBackgroundResource(0);
            linearNew.setVisibility(View.VISIBLE);
            sessions.setselection("1");
            OnGoing();
        } else {
            if (sessions.getselection().equals("1")) {
                txtNeworders.setTextColor(getResources().getColor(R.color.white));
                txtNeworders.setBackgroundResource(R.drawable.button_border);
                txtNeworders.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                txtPastorders.setTextColor(getResources().getColor(R.color.colorDarkgrey));
                txtPastorders.setBackgroundResource(0);
                linearNew.setVisibility(View.VISIBLE);
                OnGoing();
            } else {
                txtPastorders.setTextColor(getResources().getColor(R.color.white));
                txtPastorders.setBackgroundResource(R.drawable.button_border);
                txtPastorders.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                txtNeworders.setTextColor(getResources().getColor(R.color.colorDarkgrey));
                txtNeworders.setBackgroundResource(0);
                linearNew.setVisibility(View.GONE);
                PastOrders();
            }
        }


        linearBlock1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtNeworders.setTextColor(getResources().getColor(R.color.white));
                txtNeworders.setBackgroundResource(R.drawable.button_border);
                txtNeworders.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                txtPastorders.setTextColor(getResources().getColor(R.color.colorDarkgrey));
                txtPastorders.setBackgroundResource(0);
                linearNew.setVisibility(View.VISIBLE);
                sessions.setselection("1");
                OnGoing();
            }
        });

        linearBlock2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPastorders.setTextColor(getResources().getColor(R.color.white));
                txtPastorders.setBackgroundResource(R.drawable.button_border);
                txtPastorders.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                txtNeworders.setTextColor(getResources().getColor(R.color.colorDarkgrey));
                txtNeworders.setBackgroundResource(0);
                linearNew.setVisibility(View.GONE);
                sessions.setselection("2");
                PastOrders();
            }
        });

//        toggleStatus.setToggleOn(true);
//
//        toggleStatus.setVisibility(View.GONE);
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                toggleStatus.setToggleOff(true);
//            }
//        }, 100);
//
//
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
        if (sessions.getstatus().equals("Active")) {
            toggleStatus.setToggleOn(true);
            txtStatus.setText("ONLINE");
            txtStatus.setTextColor(Color.parseColor("#6BD505"));
            toggleStatus.setVisibility(View.VISIBLE);
        } else {
            toggleStatus.setToggleOff(true);
            txtStatus.setText("OFFLINE");
            txtStatus.setTextColor(Color.parseColor("#FF0000"));
            toggleStatus.setVisibility(View.VISIBLE);
        }
//            }
//        }, 500);


        toggleStatus.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> data = new HashMap<>();
                    data.put("Status", "Active");
                    db.collection("Vendor").document(sessions.getusername()).set(data, SetOptions.merge());
                    sessions.setstatus("Active");
                    txtStatus.setText("ONLINE");
                    txtStatus.setTextColor(Color.parseColor("#6BD505"));
                } else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> data = new HashMap<>();
                    data.put("Status", "InActive");
                    db.collection("Vendor").document(sessions.getusername()).set(data, SetOptions.merge());
                    sessions.setstatus("InActive");
                    txtStatus.setText("OFFLINE");
                    txtStatus.setTextColor(Color.parseColor("#FF0000"));
                }
            }
        });

        linearNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    Fragment fragment = new NewOrder();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commit();
                }
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference docRef = db.collection("Orders");
        docRef.whereEqualTo("VendorStatus", sessions.getusername())
                .whereEqualTo("Status", "1")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        if (value.isEmpty()) {
                            count = 0;
                            txtCount.setText("0");
                        } else {
                            count = 0;
                            for (QueryDocumentSnapshot document : value) {
                                count++;
                            }
                            txtCount.setText("" + count);
                        }
                    }
                });


        return v;
    }

    public void OnGoing() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference docRef = db.collection("Orders");
        docRef.whereEqualTo("VendorStatus", sessions.getusername()).whereIn("Status", Arrays.asList("2", "3"))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        if (value.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            orders.clear();
                            for (QueryDocumentSnapshot document : value) {
                                orders.add(new OrdersData(
                                        document.get("OrderNo").toString(),
                                        document.get("ItemDetails").toString(),
                                        document.getId(),
                                        document.get("OrderDateTime").toString(),
                                        document.get("SubTotal").toString(),
                                        document.get("Status").toString(),
                                        document.get("VendorCommision").toString(),
                                        document.get("Vendor").toString(),
                                        document.get("OrderDate").toString(),
                                        document.get("DeliveryDate").toString(),
                                        document.get("Total").toString(),
                                        document.get("CustomerName").toString(),
                                        document.get("Qty").toString()
                                ));
                                count++;
                            }
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            orderAdapter = new OrderAdapter(orders);
                            recyclerView.setAdapter(orderAdapter);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                });


//        CollectionReference docRef2 = db.collection("Orders");
//        docRef2.whereEqualTo("VendorStatus",sessions.getusername()).whereIn("Status", Arrays.asList("2", "3")).get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            orders.clear();
//                            int count = 0 ;
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                orders.add(new OrdersData(
//                                        document.get("OrderNo").toString(),
//                                        document.get("ItemDetails").toString(),
//                                        document.getId(),
//                                        document.get("OrderDateTime").toString(),
//                                        document.get("SubTotal").toString(),
//                                        document.get("Status").toString(),
//                                        document.get("VendorCommision").toString(),
//                                        document.get("Vendor").toString(),
//                                        document.get("OrderDate").toString(),
//                                        document.get("DeliveryDate").toString(),
//                                        document.get("Total").toString(),
//                                        document.get("CustomerName").toString(),
//                                        document.get("Qty").toString()
//                                ));
//                                count++;
//                            }
//                            txtCount.setText(""+count);
//                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
//                            recyclerView.setLayoutManager(mLayoutManager);
//                            orderAdapter = new OrderAdapter(orders);
//                            recyclerView.setAdapter(orderAdapter);
//                        }
//                        else{
//                            txtCount.setText("0");
//                            recyclerView.setVisibility(View.GONE);
//                        }
//                    }
//                });
    }

    public void PastOrders() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference docRef = db.collection("Orders");
        docRef.whereEqualTo("Vendor", sessions.getusername()).whereIn("Status", Arrays.asList("4", "5", "10", "100"))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        if (value.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            orders.clear();
                            for (QueryDocumentSnapshot document : value) {
                                orders.add(new OrdersData(
                                        document.get("OrderNo").toString(),
                                        document.get("ItemDetails").toString(),
                                        document.getId(),
                                        document.get("OrderDateTime").toString(),
                                        document.get("SubTotal").toString(),
                                        document.get("Status").toString(),
                                        document.get("VendorCommision").toString(),
                                        document.get("Vendor").toString(),
                                        document.get("OrderDate").toString(),
                                        document.get("DeliveryDate").toString(),
                                        document.get("Total").toString(),
                                        document.get("CustomerName").toString(),
                                        document.get("Qty").toString()
                                ));
                                count++;
                            }
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            orderAdapter = new OrderAdapter(orders);
                            recyclerView.setAdapter(orderAdapter);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

}
