package yummy.one.yummyonevendor.Controller.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import yummy.one.yummyonevendor.Functionality.Retrofit.APIClient;
import yummy.one.yummyonevendor.Functionality.Retrofit.APIInterface;
import yummy.one.yummyonevendor.Functionality.VolleySupport.APIConstant;
import yummy.one.yummyonevendor.R;

import static yummy.one.yummyonevendor.Functionality.VolleySupport.APIConstant.getInstance;

public class NotificationFragment extends Fragment {

    Context mContext;
    ImageView imgBack;
    ProgressBar progressBar;
    RecyclerView NotificationView;
    ArrayList<Notification> notificationList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    APIInterface apiInterface;

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

        apiInterface = APIClient.getClient().create(APIInterface.class);
        sharedPreferences = getContext().getSharedPreferences("UserData", getContext().MODE_PRIVATE);

        progressBar = v.findViewById(R.id.progressBar);
        imgBack = v.findViewById(R.id.imgBack);
        NotificationView = v.findViewById(R.id.NotificationView);

        if (getActivity() != null) {
            LinearLayout bottomnavigation = getActivity().findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.GONE);
        }
        GetnotificationApi();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null)
                    getActivity().onBackPressed();
            }
        });



        return v;
    }

    public void AddNotification() {
//        for (int i = 0; i < 20; i++) {
//            Notification notification = new Notification();
//            notificationList.add(notification);
//        }
        if (notificationList.size() > 0) {
            NotificationAdapter notificationAdapter = new NotificationAdapter(mContext, notificationList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            NotificationView.setLayoutManager(mLayoutManager);
            NotificationView.setItemAnimator(new DefaultItemAnimator());
            NotificationView.setAdapter(notificationAdapter);
            notificationAdapter.notifyDataSetChanged();
        }else {
            Toast.makeText(mContext, "No notification found.", Toast.LENGTH_SHORT).show();
        }
    }

    private void GetnotificationApi() {
        notificationList.clear();
        progressBar.setVisibility(View.VISIBLE);
        String vendorId=sharedPreferences.getString("VENDERID", "");
        JSONObject data = new JSONObject();
        try {
            data.put("vendorId",vendorId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("PARAMETER", "" + getInstance().GETNOTIFICATION + data);
        RequestQueue requstQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, getInstance().GETNOTIFICATION,data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject JsonMain) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GETNOTIFICATION + JsonMain);
                            String msg = JsonMain.getString("message");
                            if (msg.equalsIgnoreCase("SUCCESS")) {
                                JSONArray dataarray=JsonMain.getJSONArray("data");
                                for (int i=0;dataarray.length()>i;i++){
                                    JSONObject jsonObject=dataarray.getJSONObject(i);
                                    Notification notification=new Notification();
                                    notification.setDate(jsonObject.getString("date"));
                                    notification.set_id(jsonObject.getString("_id"));
                                    notification.setVendorId(jsonObject.getString("vendorId"));
                                    notification.setType(jsonObject.getString("type"));
                                    notification.setStatus(jsonObject.getString("status"));
                                    notification.setImage(jsonObject.getString("image"));
                                    notification.setName(jsonObject.getString("name"));
                                    notification.setCreatedOn(jsonObject.getString("createdOn"));
                                    notification.set__v(jsonObject.getString("__v"));
                                    notificationList.add(notification);
                                }

                                AddNotification();
                            }else {
                                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("ERROR", "" + getInstance().GETNOTIFICATION + error.toString());
                    }
                }
        ){
            //here I want to post data to sever
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String keyname=sharedPreferences.getString("KEYNAME", "");
                String keyvalue=sharedPreferences.getString("KEYVALUE", "");
                String accesstoken=sharedPreferences.getString("ACCESSTOKEN", "");
                params.put("Authorization", "Bearer "+ accesstoken);
                params.put("Content-Type", "application/json");
                params.put(keyname, keyvalue);
                Log.e("HEADER", "" + getInstance().GETNOTIFICATION + params);
                return params;
            }
        };
        requstQueue.add(jsonobj);

    }

    public static class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<Notification> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

             TextView txtNotification;
             ImageView img_Notificationimage;

            MyViewHolder(View view) {
                super(view);

                txtNotification = view.findViewById(R.id.txtNotification);
                img_Notificationimage = view.findViewById(R.id.img_Notificationimage);
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

             holder.txtNotification.setText(notification.getName());
            Glide.with(mContext).load(notification.getImage()).placeholder(R.drawable.add_image).into(holder.img_Notificationimage);
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    public static class Notification {

        String date;
        String _id;
        String vendorId;
        String type;
        String status;
        String image;
        String name;
        String createdOn;
        String __v;


        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getVendorId() {
            return vendorId;
        }

        public void setVendorId(String vendorId) {
            this.vendorId = vendorId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(String createdOn) {
            this.createdOn = createdOn;
        }

        public String get__v() {
            return __v;
        }

        public void set__v(String __v) {
            this.__v = __v;
        }



        public Notification() {

        }

    }
}