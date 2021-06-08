package yummy.one.yummyonevendor.Models.FoodCategory;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.Controller.Activities.MainActivity;
import yummy.one.yummyonevendor.R;


public class SubCategory extends RecyclerView.Adapter<SubCategory.ViewHolder> {

    private ArrayList<Sub> category;
    private Session session;
    private BottomSheetDialog bottomSheetDialog;
    String a="";
    Category category1000;
    public SubCategory(ArrayList<Sub> inventories) {
        this.category = inventories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=(View) LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategory_row,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubCategory.ViewHolder holder, final int position) {

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
                if(holder.name.getText().toString().equals("New")){
                    MainActivity activity=(MainActivity) holder.view.getContext();
                    bottomSheetDialog=new BottomSheetDialog(holder.view.getContext());
                    final View bottomSheetDialogView=activity.getLayoutInflater().inflate(R.layout.bottom_fooditems,null);
                    bottomSheetDialog.setContentView(bottomSheetDialogView);
                    RecyclerView recyclerView=bottomSheetDialogView.findViewById(R.id.recyclerView);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference docRef1 = db.collection("Vendor")
                            .document(session.getusername())
                            .collection("Products");
                    docRef1.get() .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                ArrayList<Sub> subcategory = new ArrayList<Sub>();
                                SubCategoryMaster subCategoryAdapter;
                                subcategory.clear();

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    subcategory.add(new Sub(document.get("ItemName").toString(),
                                            document.getId(),
                                            document.get("FoodImage").toString()));
                                }

                                GridLayoutManager mLayoutManager = new GridLayoutManager(holder.view.getContext(),3);
                                recyclerView.setLayoutManager(mLayoutManager);
                                subCategoryAdapter = new SubCategoryMaster(subcategory);
                                recyclerView.setAdapter(subCategoryAdapter);


                            }
                        }
                    });

                    bottomSheetDialog.show();

                }
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
        ImageView image;
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
