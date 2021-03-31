package yummy.one.yummyonevendor.SignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import yummy.one.yummyonevendor.Login.OtpActivity;
import yummy.one.yummyonevendor.R;

public class SignUp extends AppCompatActivity {

    Button btnLogin;
    EditText edtName,edtDob,edtEmail;
    TextView txtNumber;
    ProgressBar progressBar;
    String number="",status="",id="";

    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private int year,month,dayofmonth;

    LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnLogin = findViewById(R.id.btnLogin);
        edtName = findViewById(R.id.edtName);
        edtDob = findViewById(R.id.edtDob);
        edtEmail = findViewById(R.id.edtEmail);
        txtNumber = findViewById(R.id.txtNumber);
        progressBar = findViewById(R.id.progressBar);
        back = findViewById(R.id.back);


        progressBar.setVisibility(View.INVISIBLE);

        progressBar.setVisibility(View.GONE);
        LinearLayout imgBack;
        imgBack = findViewById(R.id.back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if(getIntent()!=null){
            number = getIntent().getStringExtra("mobilenumber");
            status = getIntent().getStringExtra("status");
            id = getIntent().getStringExtra("id");
            txtNumber.setText(number);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        edtName.setCursorVisible(false);

        edtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtName.setCursorVisible(true);
            }
        });

        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if(charSequence.length() == 1){
                    char ch = charSequence.charAt(charSequence.length()-1);
                    if (Character.isWhitespace(ch)) {
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            v.vibrate(500);
                        }
                        Animation shake = AnimationUtils.loadAnimation(SignUp.this, R.anim.shake);
                        edtName.startAnimation(shake);
                        edtName.setText(charSequence.toString().substring(0, charSequence.length() - 1));
                        edtName.setSelection(charSequence.length() - 1);
                    }
                }
                else if(charSequence.length()>1) {
                    char ch = charSequence.charAt(charSequence.length()-1);
                    char ch1 = charSequence.charAt(charSequence.length()-2);
                    if (Character.isWhitespace(ch)&&Character.isWhitespace(ch1)) {
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            v.vibrate(500);
                        }
                        Animation shake = AnimationUtils.loadAnimation(SignUp.this, R.anim.shake);
                        edtName.startAnimation(shake);
                        edtName.setText(charSequence.toString().substring(0, charSequence.length() - 1));
                        edtName.setSelection(charSequence.length() - 1);
                    }
                }

                if(charSequence.toString().length()>2){
                    edtName.setCompoundDrawablesWithIntrinsicBounds(0,0 ,R.drawable.tick,0);
                }
                else{
                    edtName.setCompoundDrawables(null, null, null, null);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        edtDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtName.setCursorVisible(false);
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayofmonth = calendar.get(Calendar.DAY_OF_MONTH);
                edtName.setCursorVisible(false);
                hideSoftKeyboard(SignUp.this);
                datePickerDialog = new DatePickerDialog(SignUp.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String day1, month1;
                                if (day <= 9)
                                    day1 = "0" + day;
                                else {
                                    day1 = Integer.toString(day);
                                }
                                month++;
                                if (month <= 9)
                                    month1 = "0" + month;
                                else
                                    month1 = Integer.toString(month);
                                edtDob.setText(day1 + "/" + (month1) + "/" + year);
                                if(getAge(edtDob.getText().toString())>=18){
                                    edtDob.setCompoundDrawablesWithIntrinsicBounds(0,0 ,R.drawable.tick,0);
                                }
                                else{
                                    edtDob.setCompoundDrawablesWithIntrinsicBounds(0,0 ,R.drawable.close1,0);
                                }
                            }
                        }, year, month, dayofmonth);

                datePickerDialog.setTitle("Please select date");
                // TODO Hide Future Date Here
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                // TODO Hide Past Date Here
                //  mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                if(!TextUtils.isEmpty(edtDob.getText().toString())) {
                    try {
                        String string = edtDob.getText().toString();
                        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                        Date date = format.parse(string);
                        calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH);
                        dayofmonth = calendar.get(Calendar.DAY_OF_MONTH);
                        datePickerDialog.updateDate(year, month, dayofmonth);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                datePickerDialog.show();
            }

        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(edtName.getText().toString())){
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                    Animation shake = AnimationUtils.loadAnimation(SignUp.this, R.anim.shake);
                    edtName.startAnimation(shake);
                    edtName.setError("Enter Full Name");
                    edtName.requestFocus();
                    return;
                }
                else{
                    edtName.setError(null);
                }

                if(edtName.getText().toString().length()<=2){
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                    Animation shake = AnimationUtils.loadAnimation(SignUp.this, R.anim.shake);
                    edtName.startAnimation(shake);
                    edtName.setError("Enter Proper Name");
                    edtName.requestFocus();
                    return;
                }
                else{
                    edtName.setError(null);
                }

                if(TextUtils.isEmpty(edtDob.getText().toString())){
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                    Animation shake = AnimationUtils.loadAnimation(SignUp.this, R.anim.shake);
                    edtDob.startAnimation(shake);
                    edtDob.setError("Select Date of Birth");
                    edtDob.requestFocus();
                    return;
                }
                else{
                    edtDob.setError(null);
                }


                if(getAge(edtDob.getText().toString())<18){
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                    Animation shake = AnimationUtils.loadAnimation(SignUp.this, R.anim.shake);
                    edtDob.startAnimation(shake);
                    Toast.makeText(getApplicationContext(),"Must be greater than 18 Yrs",Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(SignUp.this, OtpActivity.class);
                intent.putExtra("status",status);
                intent.putExtra("id",id);
                intent.putExtra("mobilenumber",number);
                intent.putExtra("name",edtName.getText().toString());
                intent.putExtra("dob",edtDob.getText().toString());
                startActivity(intent);

            }
        });
    }

    private int getAge(String dobString){

        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = sdf.parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date == null) return 0;

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month+1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        return age;
    }

    public void setupUI(View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(SignUp.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}