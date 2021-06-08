package yummy.one.yummyonevendor.Controller.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import yummy.one.yummyonevendor.R;


public class PastOrderDetails extends Fragment {


    public PastOrderDetails() {
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
        View v=inflater.inflate(R.layout.fragment_past_order_details, container, false);


        if(getActivity()!=null){
            LinearLayout bottomnavigation = getActivity().findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.GONE);
        }

        return v;
    }
}
