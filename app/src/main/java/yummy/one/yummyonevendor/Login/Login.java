package yummy.one.yummyonevendor.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.R;
import yummy.one.yummyonevendor.Signup.Signup;

public class Login extends AppCompatActivity {

    Context mContext;
    Button btnLogin;
    EditText edtPhoneNumber;
    ProgressBar progressBar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ConstraintLayout mainview;

    boolean isKeyboardShowing = false;
    boolean temp=false;
    LinearLayout loader;
    LottieAnimationView animation;
    String status="";
    String id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;

        btnLogin = findViewById(R.id.btnLogin);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        progressBar = findViewById(R.id.progressBar);
        mainview = findViewById(R.id.mainview);
        loader = findViewById(R.id.loader);
        animation = findViewById(R.id.animation);
        progressBar.setVisibility(View.GONE);
        loader.setVisibility(View.GONE);

        animation.pauseAnimation();

        // hideSoftKeyboard(Login.this);

        edtPhoneNumber.addTextChangedListener(new TextWatcher() {
            int count;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int i1, int i2) {
                count=i2;


                if(charSequence.length()>4) {
                    if (!TextUtils.isDigitsOnly(charSequence.subSequence(4, charSequence.length()))) {
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            v.vibrate(500);
                        }
                        Animation shake = AnimationUtils.loadAnimation(Login.this, R.anim.shake);
                        edtPhoneNumber.startAnimation(shake);
                        edtPhoneNumber.setText(charSequence.toString().substring(0, charSequence.length() - 1));
                        edtPhoneNumber.setSelection(charSequence.length() - 1);
                    }
                }



                if (charSequence.toString().length() == 14) {
                    edtPhoneNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tick, 0);
                } else {
                    edtPhoneNumber.setCompoundDrawables(null, null, null, null);
                }

//                String text_value = edtPhoneNumber.getText().toString().trim();
//                if(text_value.equalsIgnoreCase("+91"))
//                {
//                    edtPhoneNumber.setText("");
//                }else
//                {
//                    if(!text_value.startsWith("+91") && text_value.length()>0) {
//                        edtPhoneNumber.setText("+91" + edtPhoneNumber.toString());
//                    }
//                }

            }
            @Override
            public void afterTextChanged(Editable s) {

                if(!s.toString().startsWith("+91 ")){
                    edtPhoneNumber.setText("+91 ");
                    edtPhoneNumber.setSelection(edtPhoneNumber.getText().length());
                }


            }
        });

        edtPhoneNumber.setSelection(edtPhoneNumber.getText().length());


        Session session = new Session(getApplication());
        session.setisfirsttime("false");
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideSoftKeyboard(Login.this);

                if (TextUtils.isEmpty(edtPhoneNumber.getText().toString())) {
                    edtPhoneNumber.setError("Enter Mobile Number");
                    edtPhoneNumber.requestFocus();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                    Animation shake = AnimationUtils.loadAnimation(Login.this, R.anim.shake);
                    edtPhoneNumber.startAnimation(shake);
                    return;
                } else {
                    edtPhoneNumber.setError(null);
                }

                if (edtPhoneNumber.getText().toString().length() != 14) {
                    edtPhoneNumber.setError("Enter 10 digit Mobile Number");
                    edtPhoneNumber.requestFocus();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                    Animation shake = AnimationUtils.loadAnimation(Login.this, R.anim.shake);
                    edtPhoneNumber.startAnimation(shake);
                    return;
                } else {
                    edtPhoneNumber.setError(null);
                }

                btnLogin.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                loader.setVisibility(View.VISIBLE);
                animation.playAnimation();

                db.collection("Vendor")
                        .whereEqualTo("MobileNumber", edtPhoneNumber.getText().toString().substring(4))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()) {
                                        status = "notregistered";
                                    } else {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            id = document.getId();
                                            break;
                                        }
                                        status = "registered";
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "" + task.getException(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                animation.addAnimatorListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        progressBar.setVisibility(View.GONE);
                        btnLogin.setEnabled(true);
                            if(status.equals("notregistered")){
                                Intent intent = new Intent(Login.this, Signup.class);
                                intent.putExtra("status", "notregistered");
                                intent.putExtra("id", "");
                                intent.putExtra("mobilenumber", edtPhoneNumber.getText().toString().substring(4));
                                startActivity(intent);
                            }
                            else{
                                Intent intent = new Intent(Login.this, OtpActivity.class);
                                intent.putExtra("status", "registered");
                                intent.putExtra("id", id);
                                intent.putExtra("mobilenumber", edtPhoneNumber.getText().toString().substring(4));
                                intent.putExtra("name", "");
                                intent.putExtra("dob", "");
                                intent.putExtra("category", "");
                                startActivity(intent);
                            }
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });


            }
        });


        mainview.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        Rect r = new Rect();
                        mainview.getWindowVisibleDisplayFrame(r);
                        int screenHeight = mainview.getRootView().getHeight();

                        // r.bottom is the position above soft keypad or device button.
                        // if keypad is shown, the r.bottom is smaller than that before.
                        int keypadHeight = screenHeight - r.bottom;

                        Log.d("TAG", "keypadHeight = " + keypadHeight);

                        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                            // keyboard is opened
                            if (!isKeyboardShowing) {
                                isKeyboardShowing = true;
                                onKeyboardVisibilityChanged(true,r.bottom);
                            }
                        }
                        else {
                            // keyboard is closed
                            if (isKeyboardShowing) {
                                isKeyboardShowing = false;
                                onKeyboardVisibilityChanged(false,r.bottom);
                            }
                        }
                    }
                });
    }

    public void setupUI(View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(Login.this);
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

    void onKeyboardVisibilityChanged(boolean opened,int a) {
//        if(opened){
//            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) edtPhoneNumber.getLayoutParams();
//            final float scale = getCurrentFocus().getResources().getDisplayMetrics().density;
//            // convert the DP into pixel
//            int pixel =  (int)(20 * scale + 0.5f);
//            int pixel1 =  (int)(140 * scale + 0.5f);
//            for(int i=pixel1;i > pixel;i--) {
//                params.topMargin = i;
//                edtPhoneNumber.requestLayout();
//            }
//
//        }
//        else{
//            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) edtPhoneNumber.getLayoutParams();
//            final float scale = getCurrentFocus().getResources().getDisplayMetrics().density;
//            // convert the DP into pixel
//            int pixel =  (int)(20 * scale + 0.5f);
//            int pixel1 =  (int)(140 * scale + 0.5f);
//            for(int i=pixel;i <= pixel1;i++) {
//                params.topMargin = i;
//                edtPhoneNumber.requestLayout();
//            }
//        }
    }

    @Override
    public void onResume(){
        super.onResume();
        loader.setVisibility(View.GONE);
    }


}
