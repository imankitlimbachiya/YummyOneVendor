package yummy.one.yummyonevendor.Fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import yummy.one.yummyonevendor.R;

public class VideoViewFragment extends Fragment {


    public VideoViewFragment() {
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
        View v=inflater.inflate(R.layout.fragment_video_view, container, false);

        if(getActivity()!=null){
            LinearLayout bottomnavigation = getActivity().findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.GONE);
        }

        VideoView videoView = v.findViewById(R.id.Video_view);

        videoView.setVideoURI(Uri.parse(getArguments().getString("url")));
        videoView.start();
        MediaController mediaController = new MediaController(getContext());
        videoView.setMediaController(mediaController);


        return v;
    }
}