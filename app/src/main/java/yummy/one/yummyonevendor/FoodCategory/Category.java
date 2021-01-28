package yummy.one.yummyonevendor.FoodCategory;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import yummy.one.yummyonevendor.Fragments.FoodItemsFragment;
import yummy.one.yummyonevendor.Fragments.OrdersFragment;
import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.MainActivity;
import yummy.one.yummyonevendor.R;


public class Category extends RecyclerView.Adapter<Category.ViewHolder> {

    ArrayList<CategoryData> category;
    Session session;

    public Category(ArrayList<CategoryData> inventories) {
        this.category = inventories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v=(View) LayoutInflater.from(parent.getContext()).inflate(R.layout.category_row,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final Category.ViewHolder holder, final int position) {

        CategoryData data=category.get(position);

        session = new Session(holder.view.getContext());

        holder.txtName.setText(data.Name);
        holder.txtNumber.setText(data.Count+" food items");

        holder.linearRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity=(MainActivity) holder.view.getContext();
                Fragment fragment = new FoodItemsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("category",holder.txtName.getText().toString());
                bundle.putString("index",""+position);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame_container, fragment).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return category.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        public final View view;

        TextView txtName,txtNumber;
        LinearLayout linearRow;

        public ViewHolder(View mView) {
            super(mView);
            this.view = mView;
            txtName=mView.findViewById(R.id.txtName);
            txtNumber=mView.findViewById(R.id.txtNumber);
            linearRow=mView.findViewById(R.id.linearRow);
        }


        @Override
        public void onClick(View v) {
        }
    }

}



