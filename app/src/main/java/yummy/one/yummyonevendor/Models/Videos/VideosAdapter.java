package yummy.one.yummyonevendor.Models.Videos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import yummy.one.yummyonevendor.Controller.Fragments.VideoViewFragment;
import yummy.one.yummyonevendor.Controller.Activities.MainActivity;
import yummy.one.yummyonevendor.R;


public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {

    private ArrayList<Video> videos;


    public VideosAdapter(ArrayList<Video> cities) {
        this.videos = cities;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v=(View) LayoutInflater.from(parent.getContext()).inflate(R.layout.video_row,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Video video=videos.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(20));

        Glide.with(holder.view.getContext())
                .load(video.Url)
                .apply(requestOptions)
                .into(holder.image);
        holder.followers.setText("0K");
        holder.length.setText(video.Length);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new VideoViewFragment();
                MainActivity activity = (MainActivity) holder.view.getContext();
                Bundle bundle = new Bundle();
                bundle.putString("url",video.Url);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame_container, fragment).commit();
            }
        });



    }

    public void removeItem(int position) {
        videos.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        if(videos!=null){
            return videos.size();
        }
        else {
            return 0;
        }
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView followers,length;
        public ImageView image;
        public final View view;


        public ViewHolder(View view) {
            super(view);
            this.view = view;
            image = view.findViewById(R.id.image);
            length = view.findViewById(R.id.length);
            followers = view.findViewById(R.id.followers);
       }

    }

}



