package yummy.one.yummyonevendor.Controller.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import yummy.one.yummyonevendor.Functionality.Retrofit.APIClient;
import yummy.one.yummyonevendor.Functionality.Retrofit.APIInterface;
import yummy.one.yummyonevendor.Functionality.VolleySupport.APIConstant;
import yummy.one.yummyonevendor.R;

import static yummy.one.yummyonevendor.Functionality.VolleySupport.APIConstant.getInstance;

public class CustomerSupport extends Fragment {

    ImageView imgBack;
    SharedPreferences sharedPreferences;
    APIInterface apiInterface;
    RecyclerView rv_Faq;
    ArrayList<FaqSupport> faqsupportList = new ArrayList<>();
    ProgressBar progressBar;

    public CustomerSupport() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_customer_support, container, false);

        if(getActivity()!=null){
            LinearLayout bottomnavigation = getActivity().findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.GONE);
        }
        progressBar = v.findViewById(R.id.progressBar);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sharedPreferences = getContext().getSharedPreferences("UserData", getContext().MODE_PRIVATE);
        rv_Faq = v.findViewById(R.id.rv_Faq);
        getFAQsApi();

        imgBack = v.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null){
                    getActivity().onBackPressed();
                }
            }
        });
        return v;
    }

    private void getFAQsApi() {
        faqsupportList.clear();
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requstQueue = Volley.newRequestQueue(getContext());
        StringRequest jsonobj = new StringRequest(Request.Method.GET, getInstance().GETFAQ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().GETFAQ + response);
                            JSONObject JsonMain = new JSONObject(response);
                            String msg = JsonMain.getString("message");
                            if (msg.equalsIgnoreCase("SUCCESS")) {
                                JSONArray jsonArray = JsonMain.getJSONArray("data");
                                for (int i=0;jsonArray.length()>i;i++){
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    String id=jsonObject.getString("_id");
                                    String question=jsonObject.getString("question");
                                    String answer=jsonObject.getString("answer");
                                    String createdOn=jsonObject.getString("createdOn");
                                    String v=jsonObject.getString("__v");

                                    FaqSupport faqSupport=new FaqSupport();
                                    faqSupport.set_id(id);
                                    faqSupport.setQuestion(question);
                                    faqSupport.setAnswer(answer);
                                    faqSupport.setCreatedOn(createdOn);
                                    faqSupport.set__v(v);

                                    faqsupportList.add(faqSupport);
                                }
                                if (faqsupportList.size() > 0) {
                                    FaqSupportAdapter faqSupportAdapter = new FaqSupportAdapter(getContext(), faqsupportList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                                    rv_Faq.setLayoutManager(mLayoutManager);
                                    rv_Faq.setItemAnimator(new DefaultItemAnimator());
                                    rv_Faq.setAdapter(faqSupportAdapter);
                                    faqSupportAdapter.notifyDataSetChanged();
                                }

                                //Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("ERROR", "" + getInstance().GETFAQ + error.toString());
                    }
                }
        ){
            //here I want to post data to sever
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String keyname=sharedPreferences.getString("KEYNAME", "");
                String keyvalue=sharedPreferences.getString("KEYVALUE", "");
                String accesstoken=sharedPreferences.getString("ACCESSTOKEN", "");
                params.put("Authorization", "Bearer "+ accesstoken);
                params.put("Content-Type", "application/json");
                params.put(keyname, keyvalue);
                Log.e("HEADER", "" + getInstance().GETFAQ + params);
                return params;
            }
        };
        requstQueue.add(jsonobj);

    }

    public static class FaqSupportAdapter extends RecyclerView.Adapter<FaqSupportAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<FaqSupport> arrayList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txt_question,txt_answer;

            MyViewHolder(View view) {
                super(view);

                txt_question = view.findViewById(R.id.txt_question);
                txt_answer = view.findViewById(R.id.txt_answer);
            }
        }

        public FaqSupportAdapter(Context mContext, ArrayList<FaqSupport> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NotNull
        @Override
        public FaqSupportAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.customersupport_adapter, parent, false);
            return new FaqSupportAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull FaqSupportAdapter.MyViewHolder holder, int position) {
            final FaqSupport faqSupport = arrayList.get(position);

            holder.txt_question.setText(faqSupport.getQuestion());
            holder.txt_answer.setText(faqSupport.getAnswer());
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    public static class FaqSupport {
        String _id;
        String question;
        String answer;
        String createdOn;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(String createdOn) {
            this.createdOn = createdOn;
        }

        public String get__v() {
            return __v;
        }

        public void set__v(String __v) {
            this.__v = __v;
        }

        String __v;
        public FaqSupport() {

        }

    }
}
