package yummy.one.yummyonevendor.Models.Orders;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import yummy.one.yummyonevendor.Controller.Fragments.OrderDetails;
import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.Controller.Activities.MainActivity;
import yummy.one.yummyonevendor.R;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> implements Filterable {


    private ArrayList<OrdersData> invetories;
    private ArrayList<OrdersData> inventoriesfilterable;
    private Session session;

    public OrderAdapter(ArrayList<OrdersData> inventories) {
        this.inventoriesfilterable = inventories;
        this.invetories = inventories;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v=(View) LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_row,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final OrdersData ordersData = inventoriesfilterable.get(position);
        session = new Session(holder.view.getContext());
        holder.name.setText(ordersData.CustomerName);
        holder.orderid.setText("OrderID: #"+ordersData.OrderNo.substring(0,5));
        holder.date.setText(ordersData.OrderDateTime);
        holder.items.setText(ordersData.Qty+" Items");
        holder.amount.setText("\u20b9"+ordersData.Total);
        holder.time.setText(ordersData.OrderDateTime);
        holder.pushid.setText(ordersData.PushId);


        DecimalFormat form=new DecimalFormat("0.00");

        double price = Double.parseDouble(ordersData.SubTotal);
        double del = Double.parseDouble("0.00");
        double com = Double.parseDouble(ordersData.VendorCommision);

        double tot = price * (com/100.0);
        double  gst = tot*0.18;
        double gst1 = del*0.18;
        double gtot = (price - tot  - gst - del - gst1);

        gtot = Double.parseDouble(form.format(gtot));

        holder.amount.setText("\u20b9"+form.format(gtot));

         if(ordersData.Status.equals("1")){
            holder.status.setText("Pending");
             holder.status.setBackgroundTintList(ColorStateList.valueOf(holder.view.getResources().getColor(R.color.preparing)));
        }
         if(ordersData.Status.equals("2")){
            holder.status.setText("Preparing");
             holder.status.setBackgroundTintList(ColorStateList.valueOf(holder.view.getResources().getColor(R.color.preparing)));
        }
        else  if(ordersData.Status.equals("3")){
            holder.status.setText("Ready");
             holder.status.setBackgroundTintList(ColorStateList.valueOf(holder.view.getResources().getColor(R.color.ready)));
        }
        else  if(ordersData.Status.equals("4")){
            holder.status.setText("On Going");
             holder.status.setBackgroundTintList(ColorStateList.valueOf(holder.view.getResources().getColor(R.color.preparing)));
        }
        else  if(ordersData.Status.equals("5")){
            holder.status.setText("Completed");
             holder.status.setBackgroundTintList(ColorStateList.valueOf(holder.view.getResources().getColor(R.color.main_green_color)));
        }
        else  if(ordersData.Status.equals("10")){
            holder.status.setText("Cancelled");
             holder.status.setBackgroundTintList(ColorStateList.valueOf(holder.view.getResources().getColor(R.color.cancelled)));
        }
        else  if(ordersData.Status.equals("100")){
            holder.status.setText("Refunded");
             holder.status.setBackgroundTintList(ColorStateList.valueOf(holder.view.getResources().getColor(R.color.cancelled)));
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new OrderDetails();
                Bundle bundle=new Bundle();
                bundle.putString("pushid",holder.pushid.getText().toString());
                fragment.setArguments(bundle);
                MainActivity mainActivity=(MainActivity) holder.view.getContext();
                FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
            }
        });

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
                inventoriesfilterable = (ArrayList<OrdersData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        public final View view;

        TextView orderid,name,date,items,amount,status,time,customertype,pushid;
        ConstraintLayout layout;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            name=view.findViewById(R.id.name);
            orderid=view.findViewById(R.id.orderid);
            date=view.findViewById(R.id.date);
            items=view.findViewById(R.id.items);
            amount=view.findViewById(R.id.amount);
            status=view.findViewById(R.id.status);
            time=view.findViewById(R.id.time);
            customertype=view.findViewById(R.id.customertype);
            pushid=view.findViewById(R.id.pushid);
            layout=view.findViewById(R.id.layout);
        }
        @Override
        public void onClick(View v) {
        }
    }

}



