package yummy.one.yummyonevendor.Product;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import yummy.one.yummyonevendor.MainActivity;
import yummy.one.yummyonevendor.R;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private ArrayList<Product> slit;


    public ProductsAdapter(ArrayList<Product> cities) {
        this.slit = cities;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v=(View) LayoutInflater.from(parent.getContext()).inflate(R.layout.products_row2,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product=slit.get(position);

        holder.name.setText(product.PushId);
        holder.gender.setText(product.Name);


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
                input.setSingleLine();
                input1.setSingleLine();
                input.setHint("Enter Portion Quantity");
                input1.setHint("Enter Portion Price");
                input.setText(holder.name.getText().toString());
                input1.setText(holder.gender.getText().toString().substring(1));
                input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                input1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                FrameLayout container1 = new FrameLayout(mainActivity);
                FrameLayout container2 = new FrameLayout(mainActivity);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin =mainActivity.getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.rightMargin = mainActivity.getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.topMargin = mainActivity.getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.bottomMargin = mainActivity.getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                input.setLayoutParams(params);
                input1.setLayoutParams(params);
                container1.addView(input);
                container2.addView(input1);

                LinearLayout layout = new LinearLayout(mainActivity);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(container1);
                layout.addView(container2);

                alert.setTitle("Edit");
                alert.setView(layout);
                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (TextUtils.isEmpty(input.getText().toString())) {
                            input.setError("Enter Quantity");
                            input.requestFocus();
                            return;
                        }

                        if (TextUtils.isEmpty(input1.getText().toString())) {
                            input1.setError("Enter Price");
                            input1.requestFocus();
                            return;
                        }

                        holder.name.setText(input.getText().toString());
                        holder.gender.setText(input1.getText().toString());

                        slit.set(position,new Product(input.getText().toString(), "\u20b9" + input1.getText().toString()));

                        notifyDataSetChanged();
                        input.setText("");
                        input1.setText("");

                            View v = mainActivity.getCurrentFocus();
                            if (v != null) {
                                InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                assert imm != null;
                                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
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
        public final TextView name;
        public final TextView gender;
        public final LinearLayout linearLayout,close;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.name);
            gender = view.findViewById(R.id.gender);
            linearLayout=view.findViewById(R.id.linearLayout);
            close=view.findViewById(R.id.close);
        }

        @Override
        public void onClick(View v) {
        }

    }

}



