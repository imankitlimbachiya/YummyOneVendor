package yummy.one.yummyonevendor.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.Models.Orders.OrderAdapter;
import yummy.one.yummyonevendor.Models.Orders.OrdersData;
import yummy.one.yummyonevendor.R;


public class PastOrders extends Fragment {


    public PastOrders() {
        // Required empty public constructor
    }


    private ImageView imgBack;
    private ArrayList<OrdersData> orders=new ArrayList<OrdersData>();
    private OrderAdapter orderAdapter;
    private RecyclerView recyclerView;
    Session sessions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_past_orders, container, false);

        if(getActivity()!=null){
            LinearLayout bottomnavigation = getActivity().findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.GONE);
        }

        imgBack = v.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null){
                    getActivity().onBackPressed();
                }
            }
        });

        sessions = new Session(getActivity());
        recyclerView = v.findViewById(R.id.recyclerView);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference docRef = db.collection("Orders");
        docRef.whereEqualTo("Vendor",sessions.getusername()).whereIn("Status", Arrays.asList("4", "5","10","100"))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        if(value.isEmpty()){
                            recyclerView.setVisibility(View.GONE);
                        }
                        else {
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
                            }
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            orderAdapter = new OrderAdapter(orders);
                            recyclerView.setAdapter(orderAdapter);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                });

        return v;
    }
}
