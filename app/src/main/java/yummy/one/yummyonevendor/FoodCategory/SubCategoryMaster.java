package yummy.one.yummyonevendor.FoodCategory;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.R;


public class SubCategoryMaster extends RecyclerView.Adapter<SubCategoryMaster.ViewHolder> {

    ArrayList<Sub> category;
    Session session;

    public SubCategoryMaster(ArrayList<Sub> inventories) {
        this.category = inventories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=(View) LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategory_row,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubCategoryMaster.ViewHolder holder, final int position) {

        Sub sub=category.get(position);
        session = new Session(holder.view.getContext());

        if(!sub.Name.equals("New")) {
            holder.name.setText(sub.Name);
            holder.pushid.setText(sub.PushId);
            Glide.with(holder.view.getContext())
                    .load(sub.Image)
                    .into(holder.image);
        }
        else{
            holder.name.setText("New");
            holder.name.setVisibility(View.INVISIBLE);
        }


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> data = new HashMap<>();
                data.put("ItemName", holder.name.getText().toString());
                data.put("PushId",holder.pushid.getText().toString());
                data.put("FoodImage", sub.Image);
                db.collection("Vendor").document(session.getusername()).collection("VideoTemp").document(holder.pushid.getText().toString()).set(data, SetOptions.merge());
            }
        });


    }

    @Override
    public int getItemCount() {
        return category.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        public final View view;

        TextView name,pushid;
        CircleImageView image;
        LinearLayout linearLayout;
        public ViewHolder(View mView) {
            super(mView);
            this.view = mView;
            name=mView.findViewById(R.id.name);
            pushid=mView.findViewById(R.id.pushid);
            image=mView.findViewById(R.id.image);
            linearLayout=mView.findViewById(R.id.linearLayout);
        }

        @Override
        public void onClick(View v) {
        }
    }

}
