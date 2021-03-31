package yummy.one.yummyonevendor.Quantity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import yummy.one.yummyonevendor.MainActivity;
import yummy.one.yummyonevendor.Product.Product;
import yummy.one.yummyonevendor.R;


public class QuantityAdapter extends RecyclerView.Adapter<QuantityAdapter.ViewHolder> {

    private ArrayList<Quantity> slit;
    public QuantityAdapter(ArrayList<Quantity> cities) {
        this.slit = cities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=(View) LayoutInflater.from(parent.getContext()).inflate(R.layout.products_row3,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Quantity quantity=slit.get(position);

        holder.qty.setText(quantity.Qty);
        holder.mrp.setText(quantity.Mrp);
        holder.price.setText(quantity.Price);

        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(position);
                notifyDataSetChanged();
            }
        });


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) holder.view.getContext();

                final AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity, AlertDialog.THEME_HOLO_LIGHT);
                final EditText input = new EditText(mainActivity);
                final EditText input1 = new EditText(mainActivity);
                final EditText input2 = new EditText(mainActivity);
                input.setSingleLine();
                input1.setSingleLine();
                input.setHint("Enter  Quantity");
                input1.setHint("Enter Mrp Price");
                input2.setHint("Enter Offer Price");
                input.setText(holder.qty.getText().toString());
                input1.setText(holder.mrp.getText().toString().substring(1));
                input2.setText(holder.price.getText().toString().substring(1));
                input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                input1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                input2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                FrameLayout container1 = new FrameLayout(mainActivity);
                FrameLayout container2 = new FrameLayout(mainActivity);
                FrameLayout container3 = new FrameLayout(mainActivity);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin =mainActivity. getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.rightMargin =mainActivity. getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.topMargin = mainActivity.getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.bottomMargin = mainActivity.getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                input.setLayoutParams(params);
                input1.setLayoutParams(params);
                input2.setLayoutParams(params);
                container1.addView(input);
                container2.addView(input1);
                container3.addView(input2);

                LinearLayout layout = new LinearLayout(mainActivity);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(container1);
                layout.addView(container2);
                layout.addView(container3);

                alert.setTitle("Weights");
                alert.setView(layout);
                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (TextUtils.isEmpty(input.getText().toString())) {
                            input.setError("Enter Quantity");
                            input.requestFocus();
                            return;
                        }

                        if (TextUtils.isEmpty(input1.getText().toString())) {
                            input1.setError("Enter Mrp");
                            input1.requestFocus();
                            return;
                        }

                        if (TextUtils.isEmpty(input2.getText().toString())) {
                            input2.setError("Enter Offer Price");
                            input2.requestFocus();
                            return;
                        }

                            slit.set(position, new Quantity(input.getText().toString(), "\u20b9" + input1.getText().toString(),"\u20b9"+input2.getText().toString()));

                        notifyDataSetChanged();

                        input.setText("");
                        input1.setText("");
                        input2.setText("");

                        if (mainActivity != null) {
                            View v = mainActivity.getCurrentFocus();
                            if (v != null) {
                                InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                assert imm != null;
                                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            }
                        }

                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

                alert.show();

            }
        });


    }

    public void removeItem(int position) {
        slit.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        if(slit!=null){
            return slit.size();
        }
        else {
            return 0;
        }
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View view;
        public final TextView qty;
        public final TextView mrp;
        public final TextView price;
        public final LinearLayout linearLayout,close;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            qty = view.findViewById(R.id.qty);
            mrp = view.findViewById(R.id.mrp);
            price = view.findViewById(R.id.price);
            linearLayout=view.findViewById(R.id.linearLayout);
            close=view.findViewById(R.id.close);
        }

        @Override
        public void onClick(View v) {
        }

    }

}



