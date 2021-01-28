package yummy.one.yummyonevendor.Signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.R;

public class BankDetails extends AppCompatActivity {


    EditText  edtBankName, edtHolderName, edtAccountNumber, edtIfsc;
    private String path7 = "";
    ImageView r7;
    LinearLayout doc7,back;
    Button btnNext;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details);

        edtAccountNumber = findViewById(R.id.edtAccountNumber);
        edtHolderName = findViewById(R.id.edtHolderName);
        edtBankName = findViewById(R.id.edtBankName);
        edtIfsc = findViewById(R.id.edtIfsc);
        back = findViewById(R.id.back);
        doc7 = findViewById(R.id.doc7);
        r7 = findViewById(R.id.r7);
        btnNext = findViewById(R.id.btnNext);

        session = new Session(getApplication());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BankDetails.this, RegisterDetails.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Vendor").document(session.getusername());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    try {
                        int count = 0;
                        if (documentSnapshot.contains("AccountNumber")) {
                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("AccountNumber")).toString())) {
                                edtAccountNumber.setText(Objects.requireNonNull(documentSnapshot.get("AccountNumber")).toString());
                                count++;
                            }
                        }

                        if (documentSnapshot.contains("BranchName")) {
                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("BranchName")).toString())) {
                                edtBankName.setText(Objects.requireNonNull(documentSnapshot.get("BranchName")).toString());
                                count++;
                            }
                        }


                        if (documentSnapshot.contains("IFSCCode")) {
                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("IFSCCode")).toString())) {
                                edtIfsc.setText(Objects.requireNonNull(documentSnapshot.get("IFSCCode")).toString());
                                count++;
                            }
                        }

                        if (documentSnapshot.contains("AccountName")) {
                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("AccountName")).toString())) {
                                edtHolderName.setText(Objects.requireNonNull(documentSnapshot.get("AccountName")).toString());
                                count++;
                            }
                        }

                        if (documentSnapshot.contains("PassbookImage1") && documentSnapshot.contains("PassbookImageApproval") && documentSnapshot.contains("PassbookImageComments")) {
                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("PassbookImage1")).toString())) {
                                path7 = Objects.requireNonNull(documentSnapshot.get("PassbookImage1")).toString();
                            }

                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("PassbookImageApproval")).toString())) {
                                if (documentSnapshot.get("PassbookImageApproval").toString().equalsIgnoreCase("Pending")) {
                                    doc7.setBackgroundResource(R.color.pending);
                                    r7.setImageResource(R.drawable.right_circle);
                                } else if (documentSnapshot.get("PassbookImageApproval").toString().equalsIgnoreCase("Approved")) {
                                    doc7.setBackgroundResource(R.color.success);
                                    r7.setImageResource(R.drawable.right_circle);
                                } else if (documentSnapshot.get("PassbookImageApproval").toString().equalsIgnoreCase("Rejected")) {
                                    doc7.setBackgroundResource(R.color.warning);
                                    r7.setImageResource(R.drawable.right_circle);
                                }
                            } else {
                                doc7.setBackgroundResource(R.color.initial);
                                r7.setImageResource(R.drawable.right_circle);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        doc7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> user = new HashMap<>();
                user.put("AccountNumber", edtAccountNumber.getText().toString());
                user.put("BranchName", edtBankName.getText().toString());
                user.put("IFSCCode", edtIfsc.getText().toString());
                user.put("AccountName", edtHolderName.getText().toString());
                db.collection("Vendor").document(session.getusername()).set(user, SetOptions.merge());

                session.setdocument("Yes");
                Intent intent = new Intent(BankDetails.this, DocumentUploader5.class);
                session.setdocumentdata("BankDetails");
                startActivity(intent);
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(edtBankName.getText().toString())) {
                    edtBankName.setError("Enter Bank Name");
                    edtBankName.requestFocus();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                    Animation shake = AnimationUtils.loadAnimation(BankDetails.this, R.anim.shake);
                    edtBankName.startAnimation(shake);
                    return;
                } else {
                    edtBankName.setError(null);
                }


                if (TextUtils.isEmpty(edtHolderName.getText().toString())) {
                    edtHolderName.setError("Enter Holder Name");
                    edtHolderName.requestFocus();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                    Animation shake = AnimationUtils.loadAnimation(BankDetails.this, R.anim.shake);
                    edtHolderName.startAnimation(shake);
                    return;
                } else {
                    edtHolderName.setError(null);
                }

                if (TextUtils.isEmpty(edtAccountNumber.getText().toString())) {
                    edtAccountNumber.setError("Enter Account Number");
                    edtAccountNumber.requestFocus();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                    Animation shake = AnimationUtils.loadAnimation(BankDetails.this, R.anim.shake);
                    edtAccountNumber.startAnimation(shake);
                    return;
                } else {
                    edtAccountNumber.setError(null);
                }

                if (TextUtils.isEmpty(edtIfsc.getText().toString())) {
                    edtIfsc.setError("Enter IFSC Code");
                    edtIfsc.requestFocus();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                    Animation shake = AnimationUtils.loadAnimation(BankDetails.this, R.anim.shake);
                    edtIfsc.startAnimation(shake);
                    return;
                } else {
                    edtIfsc.setError(null);
                }


                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> user = new HashMap<>();
                user.put("AccountNumber", edtAccountNumber.getText().toString());
                user.put("BranchName", edtBankName.getText().toString());
                user.put("IFSCCode", edtIfsc.getText().toString());
                user.put("AccountName", edtHolderName.getText().toString());
                db.collection("Vendor").document(session.getusername()).set(user, SetOptions.merge());

                Intent intent = new Intent(BankDetails.this, RegisterDetails.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

    }
}