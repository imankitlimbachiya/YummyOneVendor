package yummy.one.yummyonevendor.Controller.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import yummy.one.yummyonevendor.R;

public class NotificationFragment extends Fragment {

    Context mContext;
    ImageView imgBack;
    ProgressBar progressBar;
    RecyclerView NotificationView;
    ArrayList<Notification> notificationList = new ArrayList<>();

    public NotificationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_notification, container, false);

        progressBar = v.findViewById(R.id.progressBar);
        imgBack = v.findViewById(R.id.imgBack);
        NotificationView = v.findViewById(R.id.NotificationView);

        if (getActivity() != null) {
            LinearLayout bottomnavigation = getActivity().findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.GONE);
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null)
                    getActivity().onBackPressed();
            }
        });

        AddNotification();

        return v;
    }

    public void AddNotification() {
        notificationList.clear();
        for (int i = 0; i < 20; i++) {
            Notification notification = new Notification();
            notificationList.add(notification);
        }
        if (notificationList.size() > 0) {
            NotificationAdapter notificationAdapter = new NotificationAdapter(mContext, notificationList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            NotificationView.setLayoutManager(mLayoutManager);
            NotificationView.setItemAnimator(new DefaultItemAnimator());
            NotificationView.setAdapter(notificationAdapter);
            notificationAdapter.notifyDataSetChanged();
        }
    }

    public static class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<Notification> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            // TextView txtBusinessName;

            MyViewHolder(View view) {
                super(view);

                // txtBusinessName = view.findViewById(R.id.txtBusinessName);
            }
        }

        public NotificationAdapter(Context mContext, ArrayList<Notification> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
            final Notification notification = arrayList.get(position);

            // holder.txtBusinessName.setText(postHireHelperModel.getKeyword());
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    public static class Notification {

        int NotificationSenderImage;
        String NotificationName;

        public Notification() {

        }

        public Notification(String NotificationName, int NotificationSenderImage) {
            this.NotificationName = NotificationName;
            this.NotificationSenderImage = NotificationSenderImage;
        }
    }
}