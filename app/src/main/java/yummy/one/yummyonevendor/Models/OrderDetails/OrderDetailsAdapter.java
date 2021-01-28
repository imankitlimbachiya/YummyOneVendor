package yummy.one.yummyonevendor.Models.OrderDetails;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;

import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.MainActivity;
import yummy.one.yummyonevendor.R;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> implements Filterable {


    private ArrayList<OrderDetails> invetories;
    private ArrayList<OrderDetails> inventoriesfilterable;
    private Session session;

    public OrderDetailsAdapter(ArrayList<OrderDetails> inventories) {
        this.inventoriesfilterable = inventories;
        this.invetories = inventories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=(View) LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_details_row,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final OrderDetails orderDetails = inventoriesfilterable.get(position);
        session = new Session(holder.view.getContext());

        holder.name.setText(orderDetails.Name);
        holder.details.setText(orderDetails.Details);
        holder.price.setText("\u20b9"+orderDetails.Price);
        holder.qty.setText("X"+orderDetails.Qty);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(10));

        Glide.with(holder.view.getContext())
                .load(orderDetails.Image)
                .apply(requestOptions)
                .into(holder.indicator);
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
                inventoriesfilterable = invetories;
                FilterResults filterResults = new FilterResults();
                filterResults.values = inventoriesfilterable;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                inventoriesfilterable = (ArrayList<OrderDetails>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        public final View view;

        ImageView indicator;
        TextView name,details,price,pushid,qty;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            indicator = view.findViewById(R.id.indicator);
            name = view.findViewById(R.id.name);
            details = view.findViewById(R.id.details);
            price = view.findViewById(R.id.price);
            pushid = view.findViewById(R.id.pushid);
            qty = view.findViewById(R.id.qty);
        }
        @Override
        public void onClick(View v) {
        }
    }

}



