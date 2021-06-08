package yummy.one.yummyonevendor.Controller.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.R;

public class PaymentHistory extends Fragment {

    ImageView imgFilter;
    TextView bank, txtMonthly, txtWeekly, txtDaily;
    View v1, v2;
    LinearLayout t1, t2;
    LinearLayout imgBack;

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

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v1.setVisibility(View.VISIBLE);
                v2.setVisibility(View.GONE);
            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v2.setVisibility(View.VISIBLE);
                v1.setVisibility(View.GONE);
            }
        });

        Session session = new Session(getActivity());

//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        DocumentReference docRef = db.collection("Vendor").document(session.getusername());
//        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if (documentSnapshot.exists()) {
//                    if (documentSnapshot.contains("BranchName") && documentSnapshot.contains("AccountNumber")) {
//                        try {
//                            bank.setText(documentSnapshot.get("BranchName").toString() + ", ...XXX-" + documentSnapshot.get("AccountNumber").toString().substring(4));
//                        } catch (Exception e) {
//                            bank.setText(documentSnapshot.get("BranchName").toString() + ", ...XXX-" + documentSnapshot.get("AccountNumber").toString());
//                        }
//                    }
//                }
//            }
//        });

        return v;
    }
}