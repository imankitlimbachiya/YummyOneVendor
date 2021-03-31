package yummy.one.yummyonevendor.Location;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.internal.LatLngAdapter;
import com.google.maps.model.GeocodingResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import yummy.one.yummyonevendor.R;

public class PlacePredictionAdapter extends RecyclerView.Adapter<PlacePredictionAdapter.PlacePredictionViewHolder> {

    private final List<AutocompletePrediction> predictions = new ArrayList<>();

//    private OnPlaceClickListener onPlaceClickListener;
    private Gson gson = new GsonBuilder().registerTypeAdapter(LatLng.class, new LatLngAdapter())
            .create();

//    private RequestQueue queue;


    @NonNull
    @Override
    public PlacePredictionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new PlacePredictionViewHolder(
                inflater.inflate(R.layout.search_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlacePredictionViewHolder holder, int position) {
        final AutocompletePrediction prediction = predictions.get(position);
        holder.setPrediction(prediction);


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pid=prediction.getPlaceId();

                LocationSearch mainActivity = (LocationSearch) holder.view;
                Intent intent = new Intent(mainActivity, LocationActivity.class);
                intent.putExtra("placeid", pid);
                mainActivity.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return predictions.size();
    }

    public void setPredictions(List<AutocompletePrediction> predictions) {
        this.predictions.clear();
        this.predictions.addAll(predictions);
        notifyDataSetChanged();
    }


    public static class PlacePredictionViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        public final Context view;
        private final TextView title;
        private final TextView address;
        private final LinearLayout linearLayout;

        public PlacePredictionViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView.getContext();
            title = itemView.findViewById(R.id.address);
            address = itemView.findViewById(R.id.description);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }

        public void setPrediction(AutocompletePrediction prediction) {
            title.setText(prediction.getPrimaryText(null));
            address.setText(prediction.getSecondaryText(null));
        }

        @Override
        public void onClick(View v) {
        }

    }

    private void geocodePlaceAndDisplay(final AutocompletePrediction placePrediction) {
        // Construct the request URL
        final String apiKey = "AIzaSyCPhxfpptoIc1yca5U8mXIigIajoERQCdE";
        final String url = "https://maps.googleapis.com/maps/api/geocode/json?place_id=%s&key=%s";
        final String requestURL = String.format(url, placePrediction.getPlaceId(), apiKey);

        // Use the HTTP request URL for Geocoding API to get geographic coordinates for the place
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Inspect the value of "results" and make sure it's not empty
                            JSONArray results = response.getJSONArray("results");
                            if (results.length() == 0) {
//                                Log.w(TAG, "No results from geocoding request.");
                                return;
                            }

                            // Use Gson to convert the response JSON object to a POJO
                            GeocodingResult result = gson.fromJson(
                                    results.getString(0), GeocodingResult.class);
                            FirebaseDatabase.getInstance().getReference().child("Test").setValue(result.formattedAddress);
                            FirebaseDatabase.getInstance().getReference().child("Test1").setValue(result.placeId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Request failed");
            }
        });

    }


}
