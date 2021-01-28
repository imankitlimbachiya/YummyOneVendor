package yummy.one.yummyonevendor.Fragments;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.R;
import yummy.one.yummyonevendor.Signup.RegisterDetails;


public class AccountDetails extends Fragment {

    LinearLayout imgBack;
    TextView txtRName;
    EditText edtRestaurantName,edtVendor,edtOpentime,edtClosetime,edtFullName,edtMobile,edtEmail;
    Button btnSave;
    Session session;

    private int mHour;
    private int mMinute;
    private String open="",close="";

    public AccountDetails() {
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
        View v=inflater.inflate(R.layout.fragment_account_details, container, false);

        if(getActivity()!=null){
            LinearLayout bottomnavigation = getActivity().findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.GONE);
        }

        imgBack = v.findViewById(R.id.back);
        edtRestaurantName = v.findViewById(R.id.edtRestaurantName);
        edtVendor = v.findViewById(R.id.edtVendor);
        edtOpentime = v.findViewById(R.id.edtOpentime);
        edtClosetime = v.findViewById(R.id.edtClosetime);
        btnSave = v.findViewById(R.id.btnSave);
        txtRName = v.findViewById(R.id.txtRName);
        edtFullName = v.findViewById(R.id.edtFullName);
        edtMobile = v.findViewById(R.id.edtMobile);
        edtEmail = v.findViewById(R.id.edtEmail);

        session = new Session(getActivity());

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null){
                    getActivity().onBackPressed();
                }
            }
        });

        txtRName.setText(session.getcategory() + " Name");
        edtRestaurantName.setText(session.getvendorname());
        edtVendor.setText(session.getusername());
        edtFullName.setText(session.getname());

        edtOpentime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                mHour = hourOfDay;
                                mMinute = minute;
                                String h="",m="";
                                if (mHour>=0 && mHour<=9)
                                    h = "0" + hourOfDay;
                                else
                                    h = ""+ hourOfDay;

                                if(mMinute>=0 && mMinute <=9)
                                    m = "0" +minute;
                                else
                                    m =""+ minute;

                                Time tme = new Time(hourOfDay,minute,0);//seconds by default set to zero
                                Format formatter;
                                formatter = new SimpleDateFormat("h:mm a");
                                edtOpentime.setText(""+formatter.format(tme));
                                open = h+":"+m;

                            }
                        }, mHour, mMinute, false);
                timePickerDialog.updateTime(06,0);
                timePickerDialog.show();
            }
        });

        edtClosetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mHour = hourOfDay;
                                mMinute = minute;
                                String h="",m="";
                                if (mHour>=0 && mHour<=9)
                                    h = "0" + hourOfDay;
                                else
                                    h = ""+ hourOfDay;

                                if(mMinute>=0 && mMinute <=9)
                                    m = "0" +minute;
                                else
                                    m =""+ minute;

                                Time tme = new Time(hourOfDay,minute,0);//seconds by default set to zero
                                Format formatter;
                                formatter = new SimpleDateFormat("h:mm a");

                                edtClosetime.setText(""+formatter.format(tme));
                                close = h+":"+m;
                            }
                        }, mHour, mMinute, false);

                timePickerDialog.updateTime(21,0);
                timePickerDialog.show();
            }
        });


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Vendor").document(session.getusername());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                        int count = 0;
                        if (documentSnapshot.contains("RestaurantName")) {
                                edtRestaurantName.setText(documentSnapshot.get("RestaurantName").toString());
                                session.setvendorname(documentSnapshot.get("RestaurantName").toString());
                                count++;
                        }

                    if (documentSnapshot.contains("Name")) {
                        edtFullName.setText(documentSnapshot.get("Name").toString());
                        session.setname(documentSnapshot.get("Name").toString());
                        count++;
                    }

                    if (documentSnapshot.contains("Email")) {
                        edtEmail.setText(documentSnapshot.get("Email").toString());
                        session.setemail(documentSnapshot.get("Email").toString());
                        count++;
                    }

                    if (documentSnapshot.contains("MobileNumber")) {
                        edtMobile.setText(documentSnapshot.get("MobileNumber").toString());
                        session.setnumber(documentSnapshot.get("MobileNumber").toString());
                        count++;
                    }

                        if (documentSnapshot.contains("OpenTime")) {
                                open = documentSnapshot.get("OpenTime").toString();
                                String a[]=open.split(":");
                                Time tme = new Time(Integer.parseInt(a[0]),Integer.parseInt(a[1]),0);//seconds by default set to zero
                                Format formatter;
                                formatter = new SimpleDateFormat("h:mm a");
                                edtOpentime.setText(""+formatter.format(tme));
                                count++;
                        }

                        if (documentSnapshot.contains("CloseTime")) {
                                close =documentSnapshot.get("CloseTime").toString();
                                String a[]=close.split(":");
                                Time tme = new Time(Integer.parseInt(a[0]),Integer.parseInt(a[1]),0);//seconds by default set to zero
                                Format formatter;
                                formatter = new SimpleDateFormat("h:mm a");
                                edtClosetime.setText(""+formatter.format(tme));
                                count++;
                        }


                }
            }
        });



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(edtRestaurantName.getText().toString())){
                    edtRestaurantName.setError("Enter Restaurant Name");
                    edtRestaurantName.requestFocus();
                    Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                    Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                    edtRestaurantName.startAnimation(shake);
                    return;
                }

                if(TextUtils.isEmpty(edtFullName.getText().toString())){
                    edtFullName.setError("Enter  Name");
                    edtFullName.requestFocus();
                    Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                    Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                    edtFullName.startAnimation(shake);
                    return;
                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> user = new HashMap<>();
                user.put("RestaurantName",edtRestaurantName.getText().toString());
                user.put("Name",edtFullName.getText().toString());
                user.put("OpenTime",open);
                user.put("CloseTime",close);
                user.put("Email",edtEmail.getText().toString());
                db.collection("Vendor").document(session.getusername()).set(user, SetOptions.merge());
                if(getActivity()!=null){
                    getActivity().onBackPressed();
                }
            }
        });

        return v;
    }
}