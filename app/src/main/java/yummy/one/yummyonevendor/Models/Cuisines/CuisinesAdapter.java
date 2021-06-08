package yummy.one.yummyonevendor.Models.Cuisines;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.R;

public class CuisinesAdapter extends RecyclerView.Adapter<CuisinesAdapter.ViewHolder> implements Filterable {


    private ArrayList<Cuisines> invetories;
    private ArrayList<Cuisines> inventoriesfilterable;
    private ArrayList<Cuisines> selectedvalues;
    private Session session;

    public CuisinesAdapter(ArrayList<Cuisines> inventories) {
        this.inventoriesfilterable = inventories;
        this.invetories = inventories;
        selectedvalues = new ArrayList<Cuisines>();
    }


    @NonNull
    @Override
    public CuisinesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v=(View) LayoutInflater.from(parent.getContext()).inflate(R.layout.cuisine_row,parent,false);
        return new CuisinesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CuisinesAdapter.ViewHolder holder, final int position) {

        final Cuisines Cuisines = inventoriesfilterable.get(position);

        session = new Session(holder.view.getContext());


        holder.name.setText(Cuisines.Name);
        holder.pushid.setText(Cuisines.PushId);

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                                                   {
                                                       @Override
                                                       public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                            if(isChecked){
                                                                selectedvalues.add(Cuisines);
                                                            }
                                                            else{
                                                                selectedvalues.remove(Cuisines);
                                                            }
                                                       }
                                                   }

        );

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
                inventoriesfilterable = (ArrayList<Cuisines>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public ArrayList<Cuisines> listofselectedactivites(){
        return selectedvalues;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        public final View view;

        TextView name,pushid;
        CheckBox checkbox;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            name=view.findViewById(R.id.name);
            pushid=view.findViewById(R.id.pushid);
            checkbox=view.findViewById(R.id.checkbox);
        }


        @Override
        public void onClick(View v) {
        }
    }

}



