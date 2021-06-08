package yummy.one.yummyonevendor.Models.Inventory;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.zcw.togglebutton.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import yummy.one.yummyonevendor.Controller.Fragments.AddGroceryItemsEdit;
import yummy.one.yummyonevendor.Controller.Fragments.FoodItemsEdit;
import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.Controller.Activities.MainActivity;
import yummy.one.yummyonevendor.R;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> implements Filterable {

    private ArrayList<Inventory> invetories;
    private ArrayList<Inventory> inventoriesfilterable;
    private Session session;

    public InventoryAdapter(ArrayList<Inventory> inventories) {
        this.inventoriesfilterable = inventories;
        this.invetories = inventories;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v=(View) LayoutInflater.from(parent.getContext()).inflate(R.layout.food_row,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final InventoryAdapter.ViewHolder holder, final int position) {

        final Inventory inventory = inventoriesfilterable.get(position);

        session = new Session(holder.view.getContext());


        holder.name.setText(inventory.ItemName);
        holder.pushid.setText(inventory.PushId);
//        if(!TextUtils.isEmpty(inventory.FoodType)) {
//            if (inventory.FoodType.equals("Veg")) {
//                holder.indicator.setImageResource(R.drawable.veg);
//            } else if(inventory.FoodType.equals("NonVeg")){
//                holder.indicator.setImageResource(R.drawable.nonveg);
//            }
//            else{
//                holder.indicator.setImageResource(R.drawable.egg);
//            }
//        }
//        else{
//            holder.indicator.setVisibility(View.GONE);
//        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(20));

        if(inventory.ApprovalStatus.equals("Approved")){
            holder.linear.setAlpha(1.0f);
        }
        else{
            holder.linear.setAlpha(0.5f);
            holder.toggle.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(inventory.FoodType)) {
            if(!inventory.FoodType.equals("No"))
                Glide.with(holder.view.getContext())
                        .load(inventory.FoodType)
                        .apply(requestOptions)
                        .into(holder.indicator);
        }


        if(inventory.Status.equals("Active")){
            holder.toggle.setToggleOn(true);
            holder.status.setText("In Stock");
            holder.status.setTextColor(Color.parseColor("#00B246"));
        }
        else{
            holder.toggle.setToggleOff(true);
            holder.status.setText("Out of Stock");
            holder.status.setTextColor(Color.parseColor("#FF0000"));
        }

        holder.price.setText("\u20b9"+inventory.SellingPrice);




        holder.toggle.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {

                if(holder.linear.getAlpha()==1.0f) {
                    if (on) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String, Object> data = new HashMap<>();
                        data.put("Status", "Active");
                        db.collection("Vendor").document(session.getusername()).collection("Products").document(holder.pushid.getText().toString()).set(data, SetOptions.merge());
                    } else {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String, Object> data = new HashMap<>();
                        data.put("Status", "InActive");
                        db.collection("Vendor").document(session.getusername()).collection("Products").document(holder.pushid.getText().toString()).set(data, SetOptions.merge());
                    }
                }
                else{
                    Toast.makeText(holder.view.getContext(),"Waiting for approval", Toast.LENGTH_LONG).show();
                }

            }
        });


        holder.indicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(session.getcategory().equals("Grocery")){
                    Fragment fragment = new AddGroceryItemsEdit();
                    Bundle bundle=new Bundle();
                    bundle.putString("pushid",holder.pushid.getText().toString());
                    bundle.putString("category",inventory.Category);
                    fragment.setArguments(bundle);
                    MainActivity mainActivity=(MainActivity) holder.view.getContext();
                    FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                }
                else{
                    Fragment fragment = new FoodItemsEdit();
                    Bundle bundle=new Bundle();
                    bundle.putString("pushid",holder.pushid.getText().toString());
                    bundle.putString("category",inventory.Category);
                    fragment.setArguments(bundle);
                    MainActivity mainActivity=(MainActivity) holder.view.getContext();
                    FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                }
            }
        });


//        holder.setIsRecyclable(false);

    }


    @Override
    public int getItemCount() {
        if(inventoriesfilterable!=null){
            return inventoriesfilterable.size();
        }
        else {
            return 0;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    inventoriesfilterable = invetories;
                } else {
                    ArrayList<Inventory> filteredList = new ArrayList<>();
                    for (Inventory row : invetories) {
                        if (row.ItemName.toLowerCase().contains(charString.toLowerCase()) || row.FoodType.toLowerCase().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    inventoriesfilterable = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = inventoriesfilterable;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                inventoriesfilterable = (ArrayList<Inventory>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        public final View view;

        TextView name,pushid,price,status;
        ImageView indicator;
        ToggleButton toggle;
        LinearLayout linear;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            name=view.findViewById(R.id.name);
            pushid=view.findViewById(R.id.pushid);
            price=view.findViewById(R.id.price);
            indicator=view.findViewById(R.id.indicator);
            toggle=view.findViewById(R.id.toggle);
            status=view.findViewById(R.id.status);
            linear=view.findViewById(R.id.linear);
        }


        @Override
        public void onClick(View v) {
        }
    }

}



