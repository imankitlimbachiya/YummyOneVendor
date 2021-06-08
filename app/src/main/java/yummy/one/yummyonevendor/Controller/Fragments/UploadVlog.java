package yummy.one.yummyonevendor.Controller.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import yummy.one.yummyonevendor.R;

public class UploadVlog extends Fragment {

    Context mContext;
    ImageView imgVlog;

    public UploadVlog() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upload_vlog, container, false);

        mContext = getActivity();

        imgVlog = view.findViewById(R.id.imgVlog);

        return view;
    }
}

