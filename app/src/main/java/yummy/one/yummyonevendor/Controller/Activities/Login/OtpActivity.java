package yummy.one.yummyonevendor.Controller.Activities.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.Controller.Activities.MainActivity;
import yummy.one.yummyonevendor.Functionality.VolleySupport.APIConstant;
import yummy.one.yummyonevendor.Functionality.VolleySupport.AppController;
import yummy.one.yummyonevendor.Models.Users;
import yummy.one.yummyonevendor.R;
import yummy.one.yummyonevendor.Controller.Activities.Register.CategorySelection;

import static yummy.one.yummyonevendor.Functionality.VolleySupport.APIConstant.getInstance;

public class OtpActivity extends AppCompatActivity {

    Context mContext;
    Button btnSubmit;
    TextView txtResend, txtTimer, txtAccess, txtNumber,txtChangeNumber;
    RelativeLayout backLayout;
    String mobileNumber = "", status = "", userId = "", name = "", dob = "", email = "",sessionId="";
    Users users;
    SmsVerifyCatcher smsVerifyCatcher;
    long id = 0;
    private static final String TAG = "PhoneAuthActivity";
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private FirebaseAuth mAuth;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private Session session;
    private ProgressBar progressBar;
    SharedPreferences sharedPreferences;

    LinearLayout loader;
    LottieAnimationView animation;
    EditText edtOtpFirstNumber, edtOtpSecondNumber, edtOtpThirdNumber, edtOtpFourthNumber, edtOtpFifthNumber, edtOtpSixthNumber;
    EditText[] editTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        mContext = this;

        ViewInitialization();
        
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txtChangeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext,Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("number",mobileNumber);
                startActivity(i);
            }
        });

        txtAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_LIGHT);
                alert.setTitle("Recover your Account");
                alert.setMessage("Our support team can help you regain access to your account");
                // alert.setView(input);
                alert.setPositiveButton("Contact Support", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
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

        txtResend.setVisibility(View.INVISIBLE);

        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                txtTimer.setText(millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                txtTimer.setVisibility(View.INVISIBLE);
                txtResend.setVisibility(View.VISIBLE);
            }
        }.start();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);
                Log.d(TAG, "token:" + token);
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // ...
            }
        };

        if (getIntent().getStringExtra("mobilenumber") != null) {
            mobileNumber = getIntent().getStringExtra("mobilenumber");
            txtNumber.setText(mobileNumber);
            SendOtpApi(txtNumber.getText().toString());
        }

        if (getIntent().getStringExtra("status") != null) {
            status = getIntent().getStringExtra("status");
            name = getIntent().getStringExtra("name");
            dob = getIntent().getStringExtra("dob");
            email = getIntent().getStringExtra("email");
        }

        if (!TextUtils.isEmpty(mobileNumber)) {
            startPhoneNumberVerification(mobileNumber);
        } else {
            Toast.makeText(getApplicationContext(), "Technical Error #2300", Toast.LENGTH_LONG).show();
            return;
        }

        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);//Parse verification code
                try {
                    String[] Code = code.split("");
                    edtOtpFirstNumber.setText(Code[1]);
                    edtOtpSecondNumber.setText(Code[2]);
                    edtOtpThirdNumber.setText(Code[3]);
                    edtOtpFourthNumber.setText(Code[4]);
                    edtOtpFifthNumber.setText(Code[5]);
                    edtOtpSixthNumber.setText(Code[6]);
                } catch (Exception e) {
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        if (status.equals("registered")) {
            if (getIntent().getStringExtra("id") != null) {
                userId = getIntent().getStringExtra("id");
            } else {
                Toast.makeText(mContext, "Technical Error", Toast.LENGTH_LONG).show();
                return;
            }

//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            db.collection("Vendor").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                @Override
//                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                    users = documentSnapshot.toObject(Users.class);
//                }
//            });
        }

        txtResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(mobileNumber, mResendToken);
                txtTimer.setVisibility(View.INVISIBLE);
                txtResend.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "OTP Resent", Toast.LENGTH_LONG).show();
                new CountDownTimer(30000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        txtTimer.setText("Resend Code in " + millisUntilFinished / 1000 + "s");
                    }

                    public void onFinish() {
                        txtTimer.setVisibility(View.INVISIBLE);
                        txtResend.setVisibility(View.VISIBLE);
                    }
                }.start();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String OtpFirstNumber = edtOtpFirstNumber.getText().toString();
                String OtpSecondNumber = edtOtpSecondNumber.getText().toString();
                String OtpThirdNumber = edtOtpThirdNumber.getText().toString();
                String OtpFourthNumber = edtOtpFourthNumber.getText().toString();
                String OtpFifthNumber = edtOtpFifthNumber.getText().toString();
                String OtpSixthNumber = edtOtpSixthNumber.getText().toString();

                String Otp = OtpFirstNumber + OtpSecondNumber + OtpThirdNumber + OtpFourthNumber + OtpFifthNumber + OtpSixthNumber;

                if (Otp.equals("")) {
                    Toast.makeText(getApplicationContext(), "Cannot be empty", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                    return;
                }
                progressBar.setVisibility(View.GONE);

                verifyPhoneNumberWithCode(mVerificationId, Otp);
                if (OtpActivity.this.getApplicationContext() != null) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                }
            }
        });
    }

    
    public void ViewInitialization(){

        btnSubmit = findViewById(R.id.btnSubmit);
        edtOtpFirstNumber = findViewById(R.id.edtOtpFirstNumber);
        edtOtpSecondNumber = findViewById(R.id.edtOtpSecondNumber);
        edtOtpThirdNumber = findViewById(R.id.edtOtpThirdNumber);
        edtOtpFourthNumber = findViewById(R.id.edtOtpFourthNumber);
        edtOtpFifthNumber = findViewById(R.id.edtOtpFifthNumber);
        edtOtpSixthNumber = findViewById(R.id.edtOtpSixthNumber);
        edtOtpFirstNumber.addTextChangedListener(new GenericTextWatcher(edtOtpFirstNumber));
        edtOtpSecondNumber.addTextChangedListener(new GenericTextWatcher(edtOtpSecondNumber));
        edtOtpThirdNumber.addTextChangedListener(new GenericTextWatcher(edtOtpThirdNumber));
        edtOtpFourthNumber.addTextChangedListener(new GenericTextWatcher(edtOtpFourthNumber));
        edtOtpFifthNumber.addTextChangedListener(new GenericTextWatcher(edtOtpFifthNumber));
        edtOtpSixthNumber.addTextChangedListener(new GenericTextWatcher(edtOtpSixthNumber));
        editTexts = new EditText[]{edtOtpFirstNumber, edtOtpSecondNumber, edtOtpThirdNumber, edtOtpFourthNumber,
                edtOtpFifthNumber, edtOtpSixthNumber};
        edtOtpFirstNumber.addTextChangedListener(new PinTextWatcher(0));
        edtOtpSecondNumber.addTextChangedListener(new PinTextWatcher(1));
        edtOtpThirdNumber.addTextChangedListener(new PinTextWatcher(2));
        edtOtpFourthNumber.addTextChangedListener(new PinTextWatcher(3));
        edtOtpFifthNumber.addTextChangedListener(new PinTextWatcher(4));
        edtOtpSixthNumber.addTextChangedListener(new PinTextWatcher(5));
        edtOtpFirstNumber.setOnKeyListener(new PinOnKeyListener(0));
        edtOtpSecondNumber.setOnKeyListener(new PinOnKeyListener(1));
        edtOtpThirdNumber.setOnKeyListener(new PinOnKeyListener(2));
        edtOtpFourthNumber.setOnKeyListener(new PinOnKeyListener(3));
        edtOtpFifthNumber.setOnKeyListener(new PinOnKeyListener(4));
        edtOtpSixthNumber.setOnKeyListener(new PinOnKeyListener(5));
        txtResend = findViewById(R.id.txtResend);
        txtNumber = findViewById(R.id.txtNumber);
        progressBar = findViewById(R.id.progressBar);
        txtTimer = findViewById(R.id.txtTimer);
        txtAccess = findViewById(R.id.txtAccess);
        loader = findViewById(R.id.loader);
        animation = findViewById(R.id.animation);
        txtChangeNumber = findViewById(R.id.txtChangeNumber);
        progressBar.setVisibility(View.GONE);
        loader.setVisibility(View.GONE);
        animation.pauseAnimation();
        backLayout = findViewById(R.id.BackLayout);
        mAuth = FirebaseAuth.getInstance();
        session = new Session(mContext);
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        APIConstant.getInstance().renewAccessTokenApi(mContext);

    }
    
    @Override
    public void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
        btnSubmit.setVisibility(View.VISIBLE);
    }

    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{6}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        try {
            //PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            // [END verify_with_code]
            //signInWithPhoneAuthCredential(credential,code);
            VerifyOtpApi(code,sessionId);
        } catch (Exception e) {
            btnSubmit.setVisibility(View.VISIBLE);
            Toast toast = Toast.makeText(mContext, "Verification Code is wrong", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                v.vibrate(500);
            }
            toast.show();
        }
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential,String code) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(OtpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success");

                    if (TextUtils.isEmpty(mobileNumber)) {
                        Toast.makeText(mContext, "Technical Error.Error Code #1200. Try after sometime or contact admin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    btnSubmit.setVisibility(View.GONE);
                   // animation.playAnimation();
                  //  loader.setVisibility(View.VISIBLE);
                    VerifyOtpApi(code,sessionId);

                   /* animation.addAnimatorListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (status.equals("notregistered")) {
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                final DocumentReference sfDocRef = db.collection("CategoryIdentity").document("Vendors");

                                db.runTransaction(new Transaction.Function<Void>() {
                                    @Override
                                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                        DocumentSnapshot snapshot = transaction.get(sfDocRef);

                                        // Note: this could be done without a transaction
                                        //       by updating the population using FieldValue.increment()
                                        long newValue = snapshot.getLong("entity") + 1;
                                        id = newValue;
                                        transaction.update(sfDocRef, "entity", newValue);
                                        // Success
                                        return null;
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        DocumentReference db1 = db.collection("Vendor").document("YO" + id);

                                        Map<String, Object> user = new HashMap<>();
                                        user.put("ApprovalStatus", "Pending");
                                        user.put("AccountNumber", "");
                                        user.put("AccountName", "");
                                        user.put("Address", "");
                                        user.put("BranchAddress", "");
                                        user.put("BranchName", "");
                                        user.put("VendorName", "");
                                        user.put("Category", "");
                                        user.put("City", "");
                                        user.put("CityPushId", "");
                                        user.put("CloseTime", "");
                                        user.put("OpenTime", "");
                                        user.put("Commission", "");
                                        user.put("Cuisines", "");
                                        user.put("DiscountType", "");
                                        user.put("Email", "");
                                        user.put("FSSAIAddress", "");
                                        user.put("FSSAIExpiryDate", "");
                                        user.put("FSSAIImage", "");
                                        user.put("FSSAINumber", "");
                                        user.put("GSTImage", "");
                                        user.put("GSTNumber", "");
                                        user.put("IFSCCode", "");
                                        user.put("ItemCategory", "");
                                        user.put("Location", "");
                                        user.put("LICImage", "");
                                        user.put("MobileNumber", mobileNumber);
                                        user.put("Name", name);
                                        user.put("OfferAmount", "");
                                        user.put("POC", "");
                                        user.put("Location", "");
                                        user.put("PackingCharges", "");
                                        user.put("PancardImage", "");
                                        user.put("PassbookImage", "");
                                        user.put("Password", "");
                                        user.put("PreperationTime", "");
                                        user.put("Status", "Active");
                                        user.put("VendorImage", "");
                                        user.put("Zone", "");
                                        user.put("ZonePushId", "");
                                        user.put("UserId", "YO" + id);
                                        user.put("Dob", dob);
                                        db1.set(user);

                                        session.setusername("YO" + id);
                                        session.setname(name);
                                        session.setnumber(mobileNumber);
                                        session.setemail("");
                                        session.setcategory("");
                                        startActivity(new Intent(mContext, CategorySelection.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Technical Error, Try Once again after some time", Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                session.setusername(users.UserId);
                                session.setname(users.Name);
                                session.setnumber(users.MobileNumber);
                                session.setemail(users.Email);
                                session.setpp(users.VendorImage);
                                session.setcategory(users.Category);
                                session.setvendorname(users.RestaurantName);
                                Intent intent = new Intent(mContext, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });*/

                    // [END_EXCLUDE]
                } else {
                    progressBar.setVisibility(View.GONE);
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast toast = Toast.makeText(mContext, "Dail", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            v.vibrate(500);
                        }
                        toast.show();
                    }
                }
            }
        }).addOnFailureListener(OtpActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast toast = Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    v.vibrate(500);
                }
                Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
                toast.show();
            }
        });
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mobileNumber;
        return true;
    }

    public void setupUI(View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(OtpActivity.this);
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
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    /**
     * need for Android 6 real time permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public class GenericTextWatcher implements TextWatcher {

        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch (view.getId()) {
                case R.id.edtOtpFirstNumber:
                    if (text.length() == 1) {
                        edtOtpSecondNumber.requestFocus();
                        setRequestFocus(edtOtpSecondNumber);
                        // removeRequestFocus(edtOtpFirstNumber);
                    } else {
                        edtOtpFirstNumber.requestFocus();
                        setRequestFocus(edtOtpFirstNumber);
                    }
                    break;
                case R.id.edtOtpSecondNumber:
                    if (text.length() == 1) {
                        edtOtpThirdNumber.requestFocus();
                        setRequestFocus(edtOtpThirdNumber);
                        // removeRequestFocus(edtOtpSecondNumber);
                    } else if (text.length() == 0) {
                        edtOtpSecondNumber.requestFocus();
                        setRequestFocus(edtOtpSecondNumber);
                    }
                    break;
                case R.id.edtOtpThirdNumber:
                    if (text.length() == 1) {
                        edtOtpFourthNumber.requestFocus();
                        setRequestFocus(edtOtpFourthNumber);
                        // removeRequestFocus(edtOtpThirdNumber);
                    } else if (text.length() == 0) {
                        edtOtpThirdNumber.requestFocus();
                        setRequestFocus(edtOtpThirdNumber);
                    }
                    break;
                case R.id.edtOtpFourthNumber:
                    if (text.length() == 1) {
                        edtOtpFifthNumber.requestFocus();
                        setRequestFocus(edtOtpFifthNumber);
                        // removeRequestFocus(edtOtpFourthNumber);
                    } else if (text.length() == 0) {
                        edtOtpFourthNumber.requestFocus();
                        setRequestFocus(edtOtpFourthNumber);
                    }
                    break;
                case R.id.edtOtpFifthNumber:
                    if (text.length() == 1) {
                        edtOtpSixthNumber.requestFocus();
                        setRequestFocus(edtOtpSixthNumber);
                        // removeRequestFocus(edtOtpFifthNumber);
                    } else if (text.length() == 0) {
                        edtOtpFifthNumber.requestFocus();
                        setRequestFocus(edtOtpFifthNumber);
                    }
                    break;
                case R.id.edtOtpSixthNumber:
                    if (text.length() == 1) {
                        hideSoftKeyboard(edtOtpSixthNumber);
                        // removeRequestFocus(edtOtpSixthNumber);
                    } else {
                        edtOtpSixthNumber.requestFocus();
                        setRequestFocus(edtOtpSixthNumber);
                    }
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }

    protected void setRequestFocus(EditText editText) {
        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);
        editText.requestFocus();
    }

    protected void hideSoftKeyboard(EditText editText) {
        InputMethodManager lInputMethodManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        lInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);
        editText.requestFocus();
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    public class PinTextWatcher implements TextWatcher {

        private int currentIndex;
        private boolean isFirst = false, isLast = false;
        private String newTypedString = "";

        PinTextWatcher(int currentIndex) {
            this.currentIndex = currentIndex;

            if (currentIndex == 0)
                this.isFirst = true;
            else if (currentIndex == editTexts.length - 1)
                this.isLast = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            newTypedString = s.subSequence(start, start + count).toString().trim();
        }

        @Override
        public void afterTextChanged(Editable s) {

            String text = newTypedString;

            /* Detect paste event and set first char */
            if (text.length() > 1)
                text = String.valueOf(text.charAt(0)); // TODO: We can fill out other EditTexts

            editTexts[currentIndex].removeTextChangedListener(this);
            editTexts[currentIndex].setText(text);
            editTexts[currentIndex].setSelection(text.length());
            editTexts[currentIndex].addTextChangedListener(this);

            if (text.length() == 1)
                moveToNext();
            else if (text.length() == 0)
                moveToPrevious();
        }

        private void moveToNext() {
            if (!isLast)
                editTexts[currentIndex + 1].requestFocus();

            if (isAllEditTextsFilled() && isLast) { // isLast is optional
                editTexts[currentIndex].clearFocus();
                hideKeyboard();
            }
        }

        private void moveToPrevious() {
            if (!isFirst)
                // editTexts[currentIndex - 1].requestFocus();
                editTexts[currentIndex].requestFocus();
        }

        private boolean isAllEditTextsFilled() {
            for (EditText editText : editTexts)
                if (editText.getText().toString().trim().length() == 0)
                    return false;
            return true;
        }

        private void hideKeyboard() {
            if (getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public class PinOnKeyListener implements View.OnKeyListener {

        private final int currentIndex;

        PinOnKeyListener(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (editTexts[currentIndex].getText().toString().isEmpty() && currentIndex != 0)
                    editTexts[currentIndex - 1].requestFocus();
            }
            return false;
        }
    }

    private void SendOtpApi(String mobilenumber) {
        progressBar.setVisibility(View.VISIBLE);
        loader.setVisibility(View.VISIBLE);
        JSONObject data = new JSONObject();
        try {
            data.put("mobilenumber",mobilenumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("PARAMETER", "" + getInstance().sendOtp + data);
        RequestQueue requstQueue = Volley.newRequestQueue(mContext);
        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, getInstance().sendOtp,data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject JsonMain) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            loader.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().sendOtp + JsonMain);
                            String msg = JsonMain.getString("message");
                            if (msg.equalsIgnoreCase("SUCCESS")) {
                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                                 sessionId=JsonMain.getJSONObject("data").getString("sessionId");
                            }else {
                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            loader.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        loader.setVisibility(View.GONE);
                        Log.e("ERROR", "" + getInstance().sendOtp + error.toString());
                    }
                }
        ){
            //here I want to post data to sever
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String keyname=sharedPreferences.getString("KEYNAME", "");
                String keyvalue=sharedPreferences.getString("KEYVALUE", "");
                // params.put("Content-Type", "application/json;charset=utf-8");
                params.put(keyname, keyvalue);
                //params.put("Accept", "application/json");
                Log.e("HEADER", "" + getInstance().sendOtp + params);
                return params;
            }
        };
        requstQueue.add(jsonobj);

    }

    private void VerifyOtpApi(String otp,String sessionId) {
        progressBar.setVisibility(View.VISIBLE);
        loader.setVisibility(View.VISIBLE);
        JSONObject data = new JSONObject();
        try {
            data.put("otp",otp);
            data.put("sessionId",sessionId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("PARAMETER", "" + getInstance().verifyOtp + data);
        RequestQueue requstQueue = Volley.newRequestQueue(mContext);
        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, getInstance().verifyOtp,data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject JsonMain) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            loader.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().verifyOtp + JsonMain);
                            String msg = JsonMain.getString("message");
                            if (msg.equalsIgnoreCase("SUCCESS")) {
                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(mContext, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            loader.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        loader.setVisibility(View.GONE);
                        Log.e("ERROR", "" + getInstance().verifyOtp + error.toString());
                    }
                }
        ){
            //here I want to post data to sever
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String keyname=sharedPreferences.getString("KEYNAME", "");
                String keyvalue=sharedPreferences.getString("KEYVALUE", "");
                // params.put("Content-Type", "application/json;charset=utf-8");
                params.put(keyname, keyvalue);
                //params.put("Accept", "application/json");
                Log.e("HEADER", "" + getInstance().sendOtp + params);
                return params;
            }
        };
        requstQueue.add(jsonobj);

    }


}