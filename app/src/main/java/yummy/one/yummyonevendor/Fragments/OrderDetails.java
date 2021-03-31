package yummy.one.yummyonevendor.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.ncorti.slidetoact.SlideToActView;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.Models.OrderDetails.OrderDetailsAdapter;
import yummy.one.yummyonevendor.R;

public class OrderDetails extends Fragment {

    public OrderDetails() {
        // Required empty public constructor
    }

    private MapView mMapView;
    private GoogleMap googleMap;
    private LatLng sydney;
    private HashMap<String, Marker> hashMapMarker;

    TextView orderid, status, name, subtotal, discount, commision, grandtotal, textView7,txtAddress;
    RecyclerView recyclerView;
    TextView reject;
    SlideToActView accept, ready, handover;
    String pushid = "";
    double gtot = 0, gst = 0, gst1 = 0;
    Session session;
    LinearLayout back;

    ArrayList<yummy.one.yummyonevendor.Models.OrderDetails.OrderDetails> orderDetails = new ArrayList<yummy.one.yummyonevendor.Models.OrderDetails.OrderDetails>();
    OrderDetailsAdapter orderAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_order_details, container, false);

        if (getActivity() != null) {
            LinearLayout bottomnavigation = getActivity().findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.GONE);
        }

        orderid = v.findViewById(R.id.orderid);
        status = v.findViewById(R.id.status);
        name = v.findViewById(R.id.name);
        subtotal = v.findViewById(R.id.subtotal);
        discount = v.findViewById(R.id.discount);
        commision = v.findViewById(R.id.commision);
        grandtotal = v.findViewById(R.id.grandtotal);
        textView7 = v.findViewById(R.id.textView7);
        recyclerView = v.findViewById(R.id.recyclerView);
        txtAddress = v.findViewById(R.id.txtAddress);

        /*supportMapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);*/

        accept = v.findViewById(R.id.accept);
        reject = v.findViewById(R.id.reject);
        ready = v.findViewById(R.id.ready);
        handover = v.findViewById(R.id.handover);
        back = v.findViewById(R.id.back);

        orderDetails.clear();
        orderAdapter = new OrderDetailsAdapter(orderDetails);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null)
                    getActivity().onBackPressed();
            }
        });

        mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        hashMapMarker = new HashMap<>();


        session = new Session(getActivity());

        accept.setVisibility(View.GONE);
        reject.setVisibility(View.GONE);
        ready.setVisibility(View.GONE);
        handover.setVisibility(View.GONE);

        pushid = getArguments().getString("pushid");
        final DecimalFormat form = new DecimalFormat("0.00");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Orders").document(pushid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    orderid.setText("OrderID : #" + documentSnapshot.get("OrderNo").toString().substring(5));
                    name.setText(documentSnapshot.get("CustomerName").toString());
                    subtotal.setText("\u20b9" + documentSnapshot.get("SubTotal").toString());
                    // String a = documentSnapshot.get("SubTotal").toString();
                    discount.setText("\u20b90.00");

                    double price = Double.parseDouble(subtotal.getText().toString().substring(1));
                    double del = Double.parseDouble(discount.getText().toString().substring(1));
                    double com = Double.parseDouble(documentSnapshot.get("VendorCommision").toString());

                    double tot = price * (com / 100.0);
                    gst = tot * 0.18;
                    gst1 = del * 0.18;
                    gtot = (price - tot - gst - del - gst1);

                    gtot = Double.parseDouble(form.format(gtot));

                    txtAddress.setText("Delivering To\n"+documentSnapshot.get("Address").toString());

                    discount.setText("( - ) \u20b90.00");
                    subtotal.setText("\u20b9" + form.format(Double.parseDouble(documentSnapshot.get("SubTotal").toString())));
                    commision.setText("( - ) \u20b9" + form.format(tot));
                    // taxes.setText("( - ) \u20b9" + form.format(gst));
                    grandtotal.setText("\u20b9" + form.format(gtot));

                    int statusvalue = Integer.parseInt(documentSnapshot.get("Status").toString());

                    if (statusvalue == 1) {
                        ready.setVisibility(View.GONE);
                        reject.setVisibility(View.VISIBLE);
                        accept.setVisibility(View.VISIBLE);
                        handover.setVisibility(View.GONE);
                        status.setText("Pending");
                        status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.preparing)));
                    } else if (statusvalue == 2) {
                        ready.setVisibility(View.VISIBLE);
                        reject.setVisibility(View.GONE);
                        accept.setVisibility(View.GONE);
                        handover.setVisibility(View.GONE);
                        status.setText("Preparing");
                        status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.preparing)));
                    } else if (statusvalue == 3) {
                        ready.setVisibility(View.GONE);
                        reject.setVisibility(View.GONE);
                        accept.setVisibility(View.GONE);
                        handover.setVisibility(View.VISIBLE);
                        status.setText("Ready");
                        status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.ready)));
                    } else if (statusvalue == 4) {
                        ready.setVisibility(View.GONE);
                        reject.setVisibility(View.GONE);
                        accept.setVisibility(View.GONE);
                        handover.setVisibility(View.GONE);
                        status.setText("On Going");
                        status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.preparing)));
                    } else if (statusvalue == 5) {
                        ready.setVisibility(View.GONE);
                        reject.setVisibility(View.GONE);
                        accept.setVisibility(View.GONE);
                        handover.setVisibility(View.GONE);
                        status.setText("Completed");
                        status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_green_color)));
                    } else if (statusvalue == 10) {
                        ready.setVisibility(View.GONE);
                        reject.setVisibility(View.GONE);
                        accept.setVisibility(View.GONE);
                        handover.setVisibility(View.GONE);
                        status.setText("Cancelled");
                        status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cancelled)));
                    }
                }
            }
        });

        final Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        final String date1 = df.format(currentDate);

        SimpleDateFormat df1 = new SimpleDateFormat("dd, MMM yyyy  HH:MM");
        final String date2 = df1.format(currentDate);

        accept.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NotNull SlideToActView slideToActView) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> data = new HashMap<>();
                data.put("Status", "2");
                data.put("VendorName", session.getstorename());
                data.put("VendorAddress", session.getaddress());
                data.put("VendorNumber", session.getnumber());
                db.collection("Orders").document(pushid).set(data, SetOptions.merge());

                if (getActivity() != null) {
                    Fragment fragment = new OrdersFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container, fragment).commit();
                }
            }
        });

//        accept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                if(getContext()!=null) {
////                    SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
////                            .setTitleText("Are you sure you want to accept the order?")
////                            .setConfirmText("Yes")
////                            .setCancelText("No")
////                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
////                                @Override
////                                public void onClick(SweetAlertDialog sweetAlertDialog) {
////                                    sweetAlertDialog.dismiss();
////
////                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
////                                    Map<String, Object> data = new HashMap<>();
////                                    data.put("Status", "2");
////                                    data.put("VendorName", session.getstorename());
////                                    data.put("VendorAddress", session.getaddress());
////                                    data.put("VendorNumber", session.getnumber());
////                                    db.collection("Orders").document(pushid).set(data, SetOptions.merge());
////
////                                    if(getActivity()!=null) {
////                                        Fragment fragment = new OrdersFragment();
////                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
////                                        fragmentManager.beginTransaction()
////                                                .replace(R.id.frame_container, fragment).commit();
////                                    }
////
////                                }
////                            })
////                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
////                                @Override
////                                public void onClick(SweetAlertDialog sweetAlertDialog) {
////                                    sweetAlertDialog.dismiss();
////                                }
////                            });
////
////                    sDialog.setCancelable(false);
////                    sDialog.show();
////                }
//
//
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                Map<String, Object> data = new HashMap<>();
//                data.put("Status", "2");
//                data.put("VendorName", session.getstorename());
//                data.put("VendorAddress", session.getaddress());
//                data.put("VendorNumber", session.getnumber());
//                db.collection("Orders").document(pushid).set(data, SetOptions.merge());
//
//                if(getActivity()!=null) {
//                    Fragment fragment = new OrdersFragment();
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.frame_container, fragment).commit();
//                }
//
//            }
//        });


        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getContext() != null) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                    final EditText input = new EditText(getActivity());
                    input.setSingleLine();
                    input.setHint("Enter Reason");
                    input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                    FrameLayout container1 = new FrameLayout(getActivity());
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                    params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                    params.topMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                    params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                    input.setLayoutParams(params);
                    container1.addView(input);
                    alert.setTitle("Please Mention the reason for rejection");
                    alert.setView(container1);
                    alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (TextUtils.isEmpty(input.getText().toString())) {
                                Toast.makeText(getContext(), "Enter Reason", Toast.LENGTH_LONG).show();
                                input.requestFocus();
                            } else {
                                input.setError(null);
                            }

                            if (!TextUtils.isEmpty(input.getText().toString())) {
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                Map<String, Object> data = new HashMap<>();
                                data.put("VendorStatus", "");
                                data.put("RejectionReason", input.getText().toString());
                                data.put("Status", "10");
                                db.collection("Orders").document(pushid).set(data, SetOptions.merge());
                                dialog.dismiss();
                                getActivity().onBackPressed();
                            }
                        }
                    });
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();
                }
            }
        });

        ready.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NotNull SlideToActView slideToActView) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> data = new HashMap<>();
                data.put("Status", "3");
                db.collection("Orders").document(pushid).set(data, SetOptions.merge());
                if (getActivity() != null)
                    getActivity().onBackPressed();
            }
        });

        handover.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NotNull SlideToActView slideToActView) {
                handover.setEnabled(false);
                if (getContext() != null) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> data = new HashMap<>();
                    data.put("VendorStatus", "");
                    db.collection("Orders").document(pushid).set(data, SetOptions.merge());
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                    }
                }
            }
        });

        getItemDetails();

        return v;
    }

    public void getItemDetails() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Orders")
                .document(pushid).collection("Cart")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (!value.isEmpty()) {
                            orderDetails.clear();
                            for (QueryDocumentSnapshot document : value) {
                                orderDetails.add(new yummy.one.yummyonevendor.Models.OrderDetails.OrderDetails(
                                        document.get("Name").toString(),
                                        document.get("Price").toString(),
                                        document.get("Type").toString(),
                                        document.get("Qty").toString(),
                                        document.get("Image").toString(),
                                        document.get("Details").toString()
                                ));
                            }
                            textView7.setText(orderDetails.size() + "Items");
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            orderAdapter = new OrderDetailsAdapter(orderDetails);
                            recyclerView.setAdapter(orderAdapter);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}
