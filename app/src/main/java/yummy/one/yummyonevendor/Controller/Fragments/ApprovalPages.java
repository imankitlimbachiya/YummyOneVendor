package yummy.one.yummyonevendor.Controller.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import yummy.one.yummyonevendor.R;

public class ApprovalPages extends Fragment {


    public ApprovalPages() {
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
        View v=inflater.inflate(R.layout.fragment_approval_pages, container, false);

        if(getActivity()!=null){
            LinearLayout bottom = getActivity().findViewById(R.id.bottomnavigation);
            bottom.setVisibility(View.GONE);
        }

        return v;
    }
}
