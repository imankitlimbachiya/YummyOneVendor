package yummy.one.yummyonevendor.SignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.Location.LocationSearch;
import yummy.one.yummyonevendor.MainActivity;
import yummy.one.yummyonevendor.R;

public class RegisterDetails extends AppCompatActivity {

    Button btnLogin, btnNext2, btnNext1, btnNext;
    ImageView one, two, three;
    ImageView line1, line2, back;
    ImageView r1, r2, r3, r4, r5, r6, r7;
    LinearLayout stage1, stage2, stage3, imgBack;
    LinearLayout doc1, doc2, doc3, doc4, doc5, doc6, doc7, imagerow;
    TextView txtCategory, txtHeading, txtHeading1, txtContactSupportTeam;
    TextView comments1, comments2, comments3, comments4, comments5, comments6, comments7;
    ProgressBar progressBar;
    EditText edtRestaurantName, edtAddressLine1, edtAddressLine2, edtCity, edtState, edtPostcode, edtBankName, edtHolderName, edtAccountNumber, edtIfsc, edtOpentime, edtClosetime, edtEmail;
    private Session session;
    long id;
    private int selection = 0;
    private int currentselection = 1;
    private String path1 = "", path2 = "", path3 = "", path4 = "", path5 = "", path6 = "", path7 = "";
    private String open = "", close = "";

    private Uri imageUri;
    private Uri imageHoldUri = null;
    private static final int REQUEST_CAMERA = 3;
    private static final int REQUEST_CODE = 5;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int REQUEST_CAMERA_ACCESS_PERMISSION = 5674;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private static final int SELECT_FILE = 2;
    private final int RESULT_CROP = 400;
    private StorageReference mstorageReference;

    private int mHour;
    private int mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);

        Log.e("Activity", "RegisterDetails");

        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressbar);
        edtRestaurantName = findViewById(R.id.edtRestaurantName);
        edtEmail = findViewById(R.id.edtEmail);
        edtAddressLine1 = findViewById(R.id.edtAddressLine1);
        edtAddressLine2 = findViewById(R.id.edtAddressLine2);
        edtCity = findViewById(R.id.edtCity);
        edtState = findViewById(R.id.edtState);
        edtPostcode = findViewById(R.id.edtPostcode);
        edtOpentime = findViewById(R.id.edtOpentime);
        edtClosetime = findViewById(R.id.edtClosetime);
        edtBankName = findViewById(R.id.edtBankName);
        edtHolderName = findViewById(R.id.edtHolderName);
        edtAccountNumber = findViewById(R.id.edtAccountNumber);
        edtIfsc = findViewById(R.id.edtIfsc);
        btnNext = findViewById(R.id.btnNext);
        btnNext1 = findViewById(R.id.btnNext1);
        btnNext2 = findViewById(R.id.btnNext2);
        txtCategory = findViewById(R.id.txtCategory);
        txtHeading = findViewById(R.id.txtHeading);
        txtHeading1 = findViewById(R.id.txtHeading1);
        imgBack = findViewById(R.id.imgBack);
        stage1 = findViewById(R.id.stage1);
        stage2 = findViewById(R.id.stage2);
        stage3 = findViewById(R.id.stage3);
        doc1 = findViewById(R.id.doc1);
        doc2 = findViewById(R.id.doc2);
        doc3 = findViewById(R.id.doc3);
        doc4 = findViewById(R.id.doc4);
        doc5 = findViewById(R.id.doc5);
        doc6 = findViewById(R.id.doc6);
        doc7 = findViewById(R.id.doc7);
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);
        r1 = findViewById(R.id.r1);
        r2 = findViewById(R.id.r2);
        r3 = findViewById(R.id.r3);
        r4 = findViewById(R.id.r4);
        r5 = findViewById(R.id.r5);
        r6 = findViewById(R.id.r6);
        r7 = findViewById(R.id.r7);
        comments1 = findViewById(R.id.comments1);
        comments2 = findViewById(R.id.comments2);
        comments3 = findViewById(R.id.comments3);
        comments4 = findViewById(R.id.comments4);
        comments5 = findViewById(R.id.comments5);
        comments6 = findViewById(R.id.comments6);
        comments7 = findViewById(R.id.comments7);
        imagerow = findViewById(R.id.imagerow);
        txtContactSupportTeam = findViewById(R.id.txtContactSupportTeam);
        back = findViewById(R.id.back);
        session = new Session(getApplication());

        stage1.setVisibility(View.VISIBLE);
        stage2.setVisibility(View.GONE);
        stage3.setVisibility(View.GONE);
        imagerow.setVisibility(View.GONE);

        progressBar.setVisibility(View.GONE);

        edtRestaurantName.setCursorVisible(false);

        edtRestaurantName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtRestaurantName.setCursorVisible(true);
            }
        });

        txtCategory.setText(session.getcategory() + " Name");
        txtHeading.setText(session.getcategory() + " Details");
        txtHeading1.setText(session.getcategory() + " Details");

        if (RegisterDetails.this.getApplicationContext() != null) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }


        if (!TextUtils.isEmpty(session.gettemp())) {
            edtRestaurantName.setText(session.gettemp());
        }

        if (!TextUtils.isEmpty(session.getaddress())) {
            edtAddressLine1.setText(session.getaddress());
        }

        if (!TextUtils.isEmpty(session.getpincode())) {
            edtPostcode.setText(session.getpincode());
        }

        if (!TextUtils.isEmpty(session.getcity())) {
            edtCity.setText(session.getcity());
        }

        if (!TextUtils.isEmpty(session.getstate())) {
            edtState.setText(session.getstate());
        }

        comments5.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(session.getdocument())) {
            currentselection = 2;
            txtHeading.setText("Documents");
            txtHeading1.setText("Documents");
            session.setdocument("");
            stage1.setVisibility(View.GONE);
            stage2.setVisibility(View.VISIBLE);
            stage3.setVisibility(View.GONE);
            imagerow.setVisibility(View.GONE);
            one.setImageResource(R.drawable.sone);
            line1.setImageResource(R.drawable.sline);
            two.setImageResource(R.drawable.stwo);
            line2.setImageResource(R.drawable.line);
            three.setImageResource(R.drawable.three);
        }

        txtContactSupportTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterDetails.this);
                builder.setTitle("YummyOne Vendor");
                builder.setMessage("Contact our support team if any query.");
                builder.setPositiveButton("Contact Support", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtRestaurantName.setCursorVisible(false);
                if (currentselection == 1) {
                    Intent intent = new Intent(RegisterDetails.this, CategorySelection.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if (currentselection == 2) {
                    currentselection = 1;
                    stage1.setVisibility(View.VISIBLE);
                    stage2.setVisibility(View.GONE);
                    stage3.setVisibility(View.GONE);
                    imagerow.setVisibility(View.GONE);
                    one.setImageResource(R.drawable.sone);
                    line1.setImageResource(R.drawable.line);
                    two.setImageResource(R.drawable.two);
                    line2.setImageResource(R.drawable.line);
                    three.setImageResource(R.drawable.three);
                } else if (currentselection == 3) {
                    currentselection = 2;
                    stage1.setVisibility(View.GONE);
                    stage2.setVisibility(View.VISIBLE);
                    stage3.setVisibility(View.GONE);
                    imagerow.setVisibility(View.GONE);
                    one.setImageResource(R.drawable.sone);
                    line1.setImageResource(R.drawable.sline);
                    two.setImageResource(R.drawable.two);
                    line2.setImageResource(R.drawable.line);
                    three.setImageResource(R.drawable.three);
                }
            }
        });

        edtOpentime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtRestaurantName.setCursorVisible(false);
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(RegisterDetails.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                mHour = hourOfDay;
                                mMinute = minute;
                                String h = "", m = "";
                                if (mHour >= 0 && mHour <= 9)
                                    h = "0" + hourOfDay;
                                else
                                    h = "" + hourOfDay;

                                if (mMinute >= 0 && mMinute <= 9)
                                    m = "0" + minute;
                                else
                                    m = "" + minute;

                                Time tme = new Time(hourOfDay, minute, 0);//seconds by default set to zero
                                Format formatter;
                                formatter = new SimpleDateFormat("h:mm a");
                                edtOpentime.setText("" + formatter.format(tme));
                                open = h + ":" + m;
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.updateTime(06, 0);
                timePickerDialog.show();
            }
        });

        edtClosetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtRestaurantName.setCursorVisible(false);
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(RegisterDetails.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mHour = hourOfDay;
                                mMinute = minute;
                                String h = "", m = "";
                                if (mHour >= 0 && mHour <= 9)
                                    h = "0" + hourOfDay;
                                else
                                    h = "" + hourOfDay;

                                if (mMinute >= 0 && mMinute <= 9)
                                    m = "0" + minute;
                                else
                                    m = "" + minute;

                                Time tme = new Time(hourOfDay, minute, 0);//seconds by default set to zero
                                Format formatter;
                                formatter = new SimpleDateFormat("h:mm a");

                                edtClosetime.setText("" + formatter.format(tme));
                                close = h + ":" + m;
                            }
                        }, mHour, mMinute, false);

                timePickerDialog.updateTime(21, 0);
                timePickerDialog.show();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtRestaurantName.setCursorVisible(false);
                if (TextUtils.isEmpty(edtRestaurantName.getText().toString())) {
                    edtRestaurantName.setError("Enter Name");
                    edtRestaurantName.requestFocus();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                    Animation shake = AnimationUtils.loadAnimation(RegisterDetails.this, R.anim.shake);
                    edtRestaurantName.startAnimation(shake);
                    return;
                } else {
                    edtRestaurantName.setError(null);
                }

                if (TextUtils.isEmpty(edtAddressLine1.getText().toString())) {
                    edtAddressLine1.setError("Select Address");
                    edtAddressLine1.requestFocus();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                    Animation shake = AnimationUtils.loadAnimation(RegisterDetails.this, R.anim.shake);
                    edtAddressLine1.startAnimation(shake);
                    return;
                } else {
                    edtAddressLine1.setError(null);
                }

                if (TextUtils.isEmpty(session.getloc())) {
                    edtAddressLine1.setError("Select Address");
                    edtAddressLine1.requestFocus();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                    Animation shake = AnimationUtils.loadAnimation(RegisterDetails.this, R.anim.shake);
                    edtAddressLine1.startAnimation(shake);
                    return;
                } else {
                    edtAddressLine1.setError(null);
                }

                if (edtOpentime.getText().toString().equals("HH:MM")) {
                    edtOpentime.setError("Select Open Time ");
                    edtOpentime.requestFocus();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                    Animation shake = AnimationUtils.loadAnimation(RegisterDetails.this, R.anim.shake);
                    edtOpentime.startAnimation(shake);
                    return;
                }

                if (edtClosetime.getText().toString().equals("HH:MM")) {
                    edtClosetime.setError("Select Close Time ");
                    edtClosetime.requestFocus();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                    Animation shake = AnimationUtils.loadAnimation(RegisterDetails.this, R.anim.shake);
                    edtClosetime.startAnimation(shake);
                    return;
                }

                String l[] = session.getloc().split(",");


                FirebaseFirestore db = FirebaseFirestore.getInstance();
                session.setvendorname(edtRestaurantName.getText().toString());
                Map<String, Object> user = new HashMap<>();
                user.put("RestaurantName", edtRestaurantName.getText().toString());
                user.put("Address", edtAddressLine1.getText().toString());
                user.put("Email", edtEmail.getText().toString());
                user.put("City", edtCity.getText().toString());
                user.put("State", edtState.getText().toString());
                user.put("PostCode", edtPostcode.getText().toString());
                user.put("OpenTime", open);
                user.put("CloseTime", close);
                user.put("Location", new GeoPoint(Double.parseDouble(l[0]), Double.parseDouble(l[1])));
                db.collection("Vendor").document(session.getusername()).set(user, SetOptions.merge());
                stage1.setVisibility(View.GONE);
                stage2.setVisibility(View.VISIBLE);
                stage3.setVisibility(View.GONE);
                imagerow.setVisibility(View.GONE);
                one.setImageResource(R.drawable.sone);
                line1.setImageResource(R.drawable.sline);
                selection = 2;
                currentselection = 2;

            }
        });

//        stage1.setVisibility(View.VISIBLE);
//        stage2.setVisibility(View.GONE);
//        stage3.setVisibility(View.GONE);
//        imagerow.setVisibility(View.VISIBLE);
//        one.setImageResource(R.drawable.one);
//        line1.setImageResource(R.drawable.line);
//        two.setImageResource(R.drawable.two);
//        line2.setImageResource(R.drawable.line);
//        three.setImageResource(R.drawable.three);

        mstorageReference = FirebaseStorage.getInstance().getReference();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Vendor").document(session.getusername());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
//                    try {
                        int count = 0;
                        if (documentSnapshot.contains("RestaurantName")) {
                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("RestaurantName")).toString())) {
                                edtRestaurantName.setText(Objects.requireNonNull(documentSnapshot.get("RestaurantName")).toString());
                                session.settemp(edtRestaurantName.getText().toString());
                                count++;
                            }
                        }

                        if (documentSnapshot.contains("Address")) {
                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("Address")).toString())) {
                                String a = Objects.requireNonNull(documentSnapshot.get("Address")).toString();
                                edtAddressLine1.setText(a);
                                count++;
                                session.setaddress(edtAddressLine1.getText().toString());
                            }
                        }

                        if (documentSnapshot.contains("Email")) {
                            if (!TextUtils.isEmpty(documentSnapshot.get("Email").toString())) {
                                String a = Objects.requireNonNull(documentSnapshot.get("Email")).toString();
                                edtEmail.setText(a);
                                count++;
                                session.setemail(edtEmail.getText().toString());
                            }
                        }

                        if (documentSnapshot.contains("OpenTime")) {
                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("OpenTime")).toString())) {
                                open = Objects.requireNonNull(documentSnapshot.get("OpenTime")).toString();
                                String a[] = open.split(":");
                                Time tme = new Time(Integer.parseInt(a[0]), Integer.parseInt(a[1]), 0);//seconds by default set to zero
                                Format formatter;
                                formatter = new SimpleDateFormat("h:mm a");
                                edtOpentime.setText("" + formatter.format(tme));
                                count++;
                            }
                        }

                        if (documentSnapshot.contains("CloseTime")) {
                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("CloseTime")).toString())) {
                                close = Objects.requireNonNull(documentSnapshot.get("CloseTime")).toString();
                                String a[] = close.split(":");
                                Time tme = new Time(Integer.parseInt(a[0]), Integer.parseInt(a[1]), 0);//seconds by default set to zero
                                Format formatter;
                                formatter = new SimpleDateFormat("h:mm a");
                                edtClosetime.setText("" + formatter.format(tme));
                                count++;
                            }
                        }

                        if (documentSnapshot.contains("Location")) {
                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("Location")).toString())) {
                                GeoPoint geoPoint = documentSnapshot.getGeoPoint("Location");
                                session.setloc(geoPoint.getLatitude() + "," + geoPoint.getLongitude());
                            }
                        }

//                        if (count == 4) {
//                            stage1.setVisibility(View.GONE);
//                            stage2.setVisibility(View.VISIBLE);
//                            stage3.setVisibility(View.GONE);
//                            imagerow.setVisibility(View.VISIBLE);
//                            one.setImageResource(R.drawable.sone);
//                            line1.setImageResource(R.drawable.sline);
//                            two.setImageResource(R.drawable.stwo);
//                            line2.setImageResource(R.drawable.line);
//                            three.setImageResource(R.drawable.three);
//                        }

                        count = 0;

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

                        int temp = 0;
                        if (count == 4) {
//                            r5.setImageResource(R.drawable.right_circle);
//                            doc5.setBackgroundResource(R.color.success);
//                            comments5.setVisibility(View.GONE);
                            temp++;
                        }

                        count = 0;


                        if (documentSnapshot.contains("PancardImage1") && documentSnapshot.contains("PancardImageApproval") && documentSnapshot.contains("PancardImageComments")) {
                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("PancardImage1")).toString())) {
                                path1 = Objects.requireNonNull(documentSnapshot.get("PancardImage1")).toString();
                            }

                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("PancardImageApproval")).toString())) {
                                if (documentSnapshot.get("PancardImageApproval").toString().equalsIgnoreCase("Pending")) {
                                    comments1.setVisibility(View.GONE);
                                    doc1.setBackgroundResource(R.color.pending);
                                    r1.setImageResource(R.drawable.right_circle);
                                    comments1.setText("Pending for Approval");
                                } else if (documentSnapshot.get("PancardImageApproval").toString().equalsIgnoreCase("Approved")) {
                                    comments1.setVisibility(View.GONE);
                                    doc1.setBackgroundResource(R.color.success);
                                    r1.setImageResource(R.drawable.right_green);
                                    count++;
                                } else if (documentSnapshot.get("PancardImageApproval").toString().equalsIgnoreCase("Rejected")) {
                                    comments1.setVisibility(View.GONE);
                                    doc1.setBackgroundResource(R.color.warning);
                                    r1.setImageResource(R.drawable.right_warning);
                                    if (documentSnapshot.contains("PancardImageComments")) {
                                        comments1.setText(documentSnapshot.get("PancardImageComments").toString());
                                    }
                                }
                            } else {
                                comments1.setVisibility(View.GONE);
                                doc1.setBackgroundResource(R.color.initial);
                                r1.setImageResource(R.drawable.right_circle);
                            }
                        }


                        if (documentSnapshot.contains("FSSAIImageApproval")) {

                            if(documentSnapshot.contains("FSSAIImage1")) {
                                if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("FSSAIImage1")).toString())) {
                                    path2 = Objects.requireNonNull(documentSnapshot.get("FSSAIImage1")).toString();
                                }
                            }

                            if (!TextUtils.isEmpty(documentSnapshot.get("FSSAIImageApproval").toString())) {
                                if (documentSnapshot.get("FSSAIImageApproval").toString().equalsIgnoreCase("Pending")) {
                                    comments2.setVisibility(View.GONE);
                                    doc2.setBackgroundResource(R.color.pending);
                                    r2.setImageResource(R.drawable.right_circle);
                                    comments2.setText("Pending for Approval");
                                } else if (documentSnapshot.get("FSSAIImageApproval").toString().equalsIgnoreCase("Approved")) {
                                    comments2.setVisibility(View.GONE);
                                    doc2.setBackgroundResource(R.color.success);
                                    r2.setImageResource(R.drawable.right_green);
                                } else if (documentSnapshot.get("FSSAIImageApproval").toString().equalsIgnoreCase("Rejected")) {
                                    comments2.setVisibility(View.GONE);
                                    doc2.setBackgroundResource(R.color.warning);
                                    r2.setImageResource(R.drawable.right_warning);
                                    if (documentSnapshot.contains("FSSAIImageComments")) {
                                        comments2.setText(documentSnapshot.get("FSSAIImageComments").toString());
                                    }
                                }
                            } else {
                                comments2.setVisibility(View.GONE);
                                doc2.setBackgroundResource(R.color.initial);
                                r2.setImageResource(R.drawable.right_circle);
                            }
                        }

                        if (documentSnapshot.contains("GSTImage1") && documentSnapshot.contains("GSTImageApproval") && documentSnapshot.contains("GSTImageComments")) {
                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("GSTImage1")).toString())) {
                                path3 = Objects.requireNonNull(documentSnapshot.get("GSTImage1")).toString();
                            }

                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("GSTImageApproval")).toString())) {
                                if (documentSnapshot.get("GSTImageApproval").toString().equalsIgnoreCase("Pending")) {
                                    comments3.setVisibility(View.GONE);
                                    doc3.setBackgroundResource(R.color.pending);
                                    r3.setImageResource(R.drawable.right_circle);
                                    comments3.setText("Pending for Approval");
                                } else if (documentSnapshot.get("GSTImageApproval").toString().equalsIgnoreCase("Approved")) {
                                    comments3.setVisibility(View.GONE);
                                    doc3.setBackgroundResource(R.color.success);
                                    r3.setImageResource(R.drawable.right_green);
                                    count++;
                                } else if (documentSnapshot.get("GSTImageApproval").toString().equalsIgnoreCase("Rejected")) {
                                    comments3.setVisibility(View.GONE);
                                    doc3.setBackgroundResource(R.color.warning);
                                    r3.setImageResource(R.drawable.right_warning);
                                    if (documentSnapshot.contains("GSTImageComments")) {
                                        comments3.setText(documentSnapshot.get("GSTImageComments").toString());
                                    }
                                }
                            } else {
                                comments3.setVisibility(View.GONE);
                                doc3.setBackgroundResource(R.color.initial);
                                r3.setImageResource(R.drawable.right_circle);
                            }
                        }

                        if (documentSnapshot.contains("LICImage1") && documentSnapshot.contains("LICImageApproval") && documentSnapshot.contains("LICImageComments")) {
                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("LICImage1")).toString())) {
                                path6 = Objects.requireNonNull(documentSnapshot.get("LICImage1")).toString();
                            }

                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("LICImageApproval")).toString())) {
                                if (documentSnapshot.get("LICImageApproval").toString().equalsIgnoreCase("Pending")) {
                                    comments6.setVisibility(View.GONE);
                                    doc6.setBackgroundResource(R.color.pending);
                                    r6.setImageResource(R.drawable.right_circle);
                                    comments6.setText("Pending for Approval");
                                } else if (documentSnapshot.get("LICImageApproval").toString().equalsIgnoreCase("Approved")) {
                                    comments6.setVisibility(View.GONE);
                                    doc6.setBackgroundResource(R.color.success);
                                    r6.setImageResource(R.drawable.right_green);
                                    count++;
                                } else if (documentSnapshot.get("LICImageApproval").toString().equalsIgnoreCase("Rejected")) {
                                    comments6.setVisibility(View.GONE);
                                    doc6.setBackgroundResource(R.color.warning);
                                    r6.setImageResource(R.drawable.right_warning);
                                    if (documentSnapshot.contains("LICImageComments")) {
                                        comments6.setText(documentSnapshot.get("LICImageComments").toString());
                                    }
                                }
                            } else {
                                comments6.setVisibility(View.GONE);
                                doc6.setBackgroundResource(R.color.initial);
                                r6.setImageResource(R.drawable.right_circle);
                            }
                        }

                        if (documentSnapshot.contains("RestaurantImage1") && documentSnapshot.contains("RestaurantImageApproval") && documentSnapshot.contains("RestaurantImageComments")) {
                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("RestaurantImage1")).toString())) {
                                path4 = Objects.requireNonNull(documentSnapshot.get("RestaurantImage1")).toString();
                            }

                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("RestaurantImageApproval")).toString())) {
                                if (documentSnapshot.get("RestaurantImageApproval").toString().equalsIgnoreCase("Pending")) {
                                    comments4.setVisibility(View.GONE);
                                    doc4.setBackgroundResource(R.color.pending);
                                    r4.setImageResource(R.drawable.right_circle);
                                    comments4.setText("Pending for Approval");
                                } else if (documentSnapshot.get("RestaurantImageApproval").toString().equalsIgnoreCase("Approved")) {
                                    comments4.setVisibility(View.GONE);
                                    doc4.setBackgroundResource(R.color.success);
                                    r4.setImageResource(R.drawable.right_green);
                                    count++;
                                } else if (documentSnapshot.get("RestaurantImageApproval").toString().equalsIgnoreCase("Rejected")) {
                                    comments4.setVisibility(View.GONE);
                                    doc4.setBackgroundResource(R.color.warning);
                                    r4.setImageResource(R.drawable.right_warning);
                                    if (documentSnapshot.contains("RestaurantImageComments")) {
                                        comments4.setText(documentSnapshot.get("RestaurantImageComments").toString());
                                    }
                                }
                            } else {
                                comments4.setVisibility(View.GONE);
                                doc4.setBackgroundResource(R.color.initial);
                                r4.setImageResource(R.drawable.right_circle);
                            }
                        }

                        if (documentSnapshot.contains("PassbookImage1") && documentSnapshot.contains("PassbookImageApproval") && documentSnapshot.contains("PassbookImageComments")) {
                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("PassbookImage1")).toString())) {
                                path7 = Objects.requireNonNull(documentSnapshot.get("PassbookImage1")).toString();
                            }

                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("PassbookImageApproval")).toString())) {
                                if (documentSnapshot.get("PassbookImageApproval").toString().equalsIgnoreCase("Pending")) {
                                    comments7.setVisibility(View.GONE);
                                    doc7.setBackgroundResource(R.color.pending);
                                    r7.setImageResource(R.drawable.right_circle);
                                    comments7.setText("Pending for Approval");
                                    r5.setImageResource(R.drawable.right_circle);
                                    doc5.setBackgroundResource(R.color.pending);
                                    comments5.setVisibility(View.GONE);
                                } else if (documentSnapshot.get("PassbookImageApproval").toString().equalsIgnoreCase("Approved")) {
                                    comments7.setVisibility(View.GONE);
                                    doc7.setBackgroundResource(R.color.success);
                                    r7.setImageResource(R.drawable.right_green);
                                    r5.setImageResource(R.drawable.right_green);
                                    doc5.setBackgroundResource(R.color.success);
                                    comments5.setVisibility(View.GONE);
                                    count++;
                                } else if (documentSnapshot.get("PassbookImageApproval").toString().equalsIgnoreCase("Rejected")) {
                                    comments7.setVisibility(View.GONE);
                                    doc7.setBackgroundResource(R.color.warning);
                                    r7.setImageResource(R.drawable.right_warning);
                                    r5.setImageResource(R.drawable.right_warning);
                                    doc5.setBackgroundResource(R.color.warning);
                                    comments5.setVisibility(View.GONE);
                                    if (documentSnapshot.contains("PassbookImageComments")) {
                                        comments7.setText(documentSnapshot.get("PassbookImageComments").toString());
                                    }
                                }
                            } else {
                                comments7.setVisibility(View.GONE);
                                doc7.setBackgroundResource(R.color.initial);
                                r7.setImageResource(R.drawable.right_circle);
                            }
                        }

                        if (count == 6 && temp == 1) {
                            btnNext1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#c8c8dc")));
                        }
                        else {
                            btnNext1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
                        }

                        //                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
//                    }
                }
            }
        });

        doc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                int color = ((ColorDrawable) doc1.getBackground()).getColor();
//
//                String hexColor = String.format("#%06X", (0xFFFFFF & color));
//
//                if (hexColor.equalsIgnoreCase("#FFF4E0") || hexColor.equalsIgnoreCase("#EDFEF6")) {
//                    return;
//                }

                session.setdocument("Yes");
                selection = 1;
                Intent intent = new Intent(RegisterDetails.this, DocumentUploader.class);
                session.setdocumentdata("IDProof");
                startActivity(intent);

//                selection=1;
//                final CharSequence[] items = {"Take Photo", "Choose from Library",
//                        "Cancel"};
//                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterDetails.this, AlertDialog.THEME_HOLO_LIGHT);
//                builder.setTitle("Add Photo!");
//
//                //SET ITEMS AND THERE LISTENERS
//                builder.setItems(items, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int item) {
//
//                        if (items[item].equals("Take Photo")) {
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
//                                    && ActivityCompat.checkSelfPermission(RegisterDetails.this, Manifest.permission.CAMERA)
//                                    != PackageManager.PERMISSION_GRANTED) {
//                                requestPermissions(new String[]{Manifest.permission.CAMERA},
//                                        REQUEST_CAMERA_ACCESS_PERMISSION);
//                            } else {
//                                cameraIntent();
//                            }
//                        } else if (items[item].equals("Choose from Library")) {
//                            galleryIntent();
//                        } else if (items[item].equals("Cancel")) {
//                            dialog.dismiss();
//                        }
//                    }
//                });
//                builder.show();
            }
        });

        doc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                int color = ((ColorDrawable) doc2.getBackground()).getColor();
//
//                String hexColor = String.format("#%06X", (0xFFFFFF & color));
//
//                if (hexColor.equalsIgnoreCase("#FFF4E0") || hexColor.equalsIgnoreCase("#EDFEF6")) {
//                    return;
//                }

                session.setdocument("Yes");
                selection = 2;
                Intent intent = new Intent(RegisterDetails.this, DocumentUploader1.class);
                session.setdocumentdata("FoodLicense");
                startActivity(intent);


//                final CharSequence[] items = {"Take Photo", "Choose from Library",
//                        "Cancel"};
//                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterDetails.this, AlertDialog.THEME_HOLO_LIGHT);
//                builder.setTitle("Add Photo!");
//
//                //SET ITEMS AND THERE LISTENERS
//                builder.setItems(items, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int item) {
//
//                        if (items[item].equals("Take Photo")) {
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
//                                    && ActivityCompat.checkSelfPermission(RegisterDetails.this, Manifest.permission.CAMERA)
//                                    != PackageManager.PERMISSION_GRANTED) {
//                                requestPermissions(new String[]{Manifest.permission.CAMERA},
//                                        REQUEST_CAMERA_ACCESS_PERMISSION);
//                            } else {
//                                cameraIntent();
//                            }
//                        } else if (items[item].equals("Choose from Library")) {
//                            galleryIntent();
//                        } else if (items[item].equals("Cancel")) {
//                            dialog.dismiss();
//                        }
//                    }
//                });
//                builder.show();
            }
        });

        doc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                int color = ((ColorDrawable) doc3.getBackground()).getColor();
//
//                String hexColor = String.format("#%06X", (0xFFFFFF & color));
//
//                if (hexColor.equalsIgnoreCase("#FFF4E0") || hexColor.equalsIgnoreCase("#EDFEF6")) {
//                    return;
//                }

                session.setdocument("Yes");
                selection = 3;
                Intent intent = new Intent(RegisterDetails.this, DocumentUploader2.class);
                session.setdocumentdata("BusinessDocuments");
                startActivity(intent);

//                selection=3;
//                final CharSequence[] items = {"Take Photo", "Choose from Library",
//                        "Cancel"};
//                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterDetails.this, AlertDialog.THEME_HOLO_LIGHT);
//                builder.setTitle("Add Photo!");
//
//                //SET ITEMS AND THERE LISTENERS
//                builder.setItems(items, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int item) {
//
//                        if (items[item].equals("Take Photo")) {
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
//                                    && ActivityCompat.checkSelfPermission(RegisterDetails.this, Manifest.permission.CAMERA)
//                                    != PackageManager.PERMISSION_GRANTED) {
//                                requestPermissions(new String[]{Manifest.permission.CAMERA},
//                                        REQUEST_CAMERA_ACCESS_PERMISSION);
//                            } else {
//                                cameraIntent();
//                            }
//                        } else if (items[item].equals("Choose from Library")) {
//                            galleryIntent();
//                        } else if (items[item].equals("Cancel")) {
//                            dialog.dismiss();
//                        }
//                    }
//                });
//                builder.show();
            }
        });

        doc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                int color = ((ColorDrawable) doc4.getBackground()).getColor();
//
//                String hexColor = String.format("#%06X", (0xFFFFFF & color));
//
//                if (hexColor.equalsIgnoreCase("#FFF4E0") || hexColor.equalsIgnoreCase("#EDFEF6")) {
//                    return;
//                }

                session.setdocument("Yes");
                selection = 4;
                Intent intent = new Intent(RegisterDetails.this, DocumentUploader3.class);
                session.setdocumentdata("StoreFront");
                startActivity(intent);

//                selection=4;
//                final CharSequence[] items = {"Take Photo", "Choose from Library",
//                        "Cancel"};
//                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterDetails.this, AlertDialog.THEME_HOLO_LIGHT);
//                builder.setTitle("Add Photo!");
//
//                //SET ITEMS AND THERE LISTENERS
//                builder.setItems(items, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int item) {
//
//                        if (items[item].equals("Take Photo")) {
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
//                                    && ActivityCompat.checkSelfPermission(RegisterDetails.this, Manifest.permission.CAMERA)
//                                    != PackageManager.PERMISSION_GRANTED) {
//                                requestPermissions(new String[]{Manifest.permission.CAMERA},
//                                        REQUEST_CAMERA_ACCESS_PERMISSION);
//                            } else {
//                                cameraIntent();
//                            }
//                        } else if (items[item].equals("Choose from Library")) {
//                            galleryIntent();
//                        } else if (items[item].equals("Cancel")) {
//                            dialog.dismiss();
//                        }
//                    }
//                });
//                builder.show();
            }
        });

        doc5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                int color = ((ColorDrawable) doc5.getBackground()).getColor();
//                String hexColor = String.format("#%06X", (0xFFFFFF & color));
//
//                if (hexColor.equalsIgnoreCase("#FFF4E0") || hexColor.equalsIgnoreCase("#EDFEF6")) {
//                    return;
//                }
//                session.setdocument("Yes");
//                currentselection = 3;
//                stage1.setVisibility(View.GONE);
//                stage2.setVisibility(View.GONE);
//                stage3.setVisibility(View.VISIBLE);
//                imagerow.setVisibility(View.GONE);
//                imgBack.setVisibility(View.GONE);

                session.setdocument("Yes");
                selection = 5;
                Intent intent = new Intent(RegisterDetails.this, BankDetails.class);
                session.setdocumentdata("BankDetails");
                startActivity(intent);

            }
        });

        doc6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                int color = ((ColorDrawable) doc6.getBackground()).getColor();
//
//                String hexColor = String.format("#%06X", (0xFFFFFF & color));
//
//                if (hexColor.equalsIgnoreCase("#FFF4E0") || hexColor.equalsIgnoreCase("#EDFEF6")) {
//                    return;
//                }

                session.setdocument("Yes");
                selection = 6;
                Intent intent = new Intent(RegisterDetails.this, DocumentUploader4.class);
                session.setdocumentdata("StoreFront");
                startActivity(intent);

//                selection=4;
//                final CharSequence[] items = {"Take Photo", "Choose from Library",
//                        "Cancel"};
//                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterDetails.this, AlertDialog.THEME_HOLO_LIGHT);
//                builder.setTitle("Add Photo!");
//
//                //SET ITEMS AND THERE LISTENERS
//                builder.setItems(items, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int item) {
//
//                        if (items[item].equals("Take Photo")) {
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
//                                    && ActivityCompat.checkSelfPermission(RegisterDetails.this, Manifest.permission.CAMERA)
//                                    != PackageManager.PERMISSION_GRANTED) {
//                                requestPermissions(new String[]{Manifest.permission.CAMERA},
//                                        REQUEST_CAMERA_ACCESS_PERMISSION);
//                            } else {
//                                cameraIntent();
//                            }
//                        } else if (items[item].equals("Choose from Library")) {
//                            galleryIntent();
//                        } else if (items[item].equals("Cancel")) {
//                            dialog.dismiss();
//                        }
//                    }
//                });
//                builder.show();
            }
        });

        doc7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                int color = ((ColorDrawable) doc7.getBackground()).getColor();
//
//                String hexColor = String.format("#%06X", (0xFFFFFF & color));
//
//                if (hexColor.equalsIgnoreCase("#FFF4E0") || hexColor.equalsIgnoreCase("#EDFEF6")) {
//                    return;
//                }

                session.setdocument("Yes");
                selection = 7;
                Intent intent = new Intent(RegisterDetails.this, DocumentUploader5.class);
                session.setdocumentdata("StoreFront");
                startActivity(intent);

//                selection=4;
//                final CharSequence[] items = {"Take Photo", "Choose from Library",
//                        "Cancel"};
//                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterDetails.this, AlertDialog.THEME_HOLO_LIGHT);
//                builder.setTitle("Add Photo!");
//
//                //SET ITEMS AND THERE LISTENERS
//                builder.setItems(items, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int item) {
//
//                        if (items[item].equals("Take Photo")) {
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
//                                    && ActivityCompat.checkSelfPermission(RegisterDetails.this, Manifest.permission.CAMERA)
//                                    != PackageManager.PERMISSION_GRANTED) {
//                                requestPermissions(new String[]{Manifest.permission.CAMERA},
//                                        REQUEST_CAMERA_ACCESS_PERMISSION);
//                            } else {
//                                cameraIntent();
//                            }
//                        } else if (items[item].equals("Choose from Library")) {
//                            galleryIntent();
//                        } else if (items[item].equals("Cancel")) {
//                            dialog.dismiss();
//                        }
//                    }
//                });
//                builder.show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentselection = 2;
                stage1.setVisibility(View.GONE);
                stage2.setVisibility(View.VISIBLE);
                stage3.setVisibility(View.GONE);
                imagerow.setVisibility(View.VISIBLE);
                one.setImageResource(R.drawable.sone);
                line1.setImageResource(R.drawable.sline);
                two.setImageResource(R.drawable.two);
                line2.setImageResource(R.drawable.line);
                three.setImageResource(R.drawable.three);
            }
        });

        btnNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnNext1.getAlpha() == 0.5f) {
                    Toast.makeText(getApplicationContext(), "Upload all documents and wait for the approval process", Toast.LENGTH_LONG).show();
                } else {
                    session.setapprovalstatus("Approved");
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> user = new HashMap<>();
                    user.put("ApprovalStatus", "Approved");
                    db.collection("Vendor").document(session.getusername()).set(user, SetOptions.merge());
                    Intent intent = new Intent(RegisterDetails.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        edtAddressLine1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Dexter.withActivity(RegisterDetails.this)
//                        .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
//                        .withListener(new MultiplePermissionsListener() {
//                            @Override
//                            public void onPermissionsChecked(MultiplePermissionsReport report) {
//                                if(report.areAllPermissionsGranted()) {
                session.setdocument("");
                session.settemp(edtRestaurantName.getText().toString());
                Intent intent = new Intent(RegisterDetails.this, LocationSearch.class);
                startActivity(intent);
//                                }
//                                else{
//                                    Toast.makeText(getApplicationContext(),"Please enable permission from settings",Toast.LENGTH_LONG).show();
//                                    return;
//                                }
//                            }
//                            @Override
//                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                                token.continuePermissionRequest();
//                            }
//                        }).check();
            }
        });

        btnNext2.setOnClickListener(new View.OnClickListener() {
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
                    Animation shake = AnimationUtils.loadAnimation(RegisterDetails.this, R.anim.shake);
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
                    Animation shake = AnimationUtils.loadAnimation(RegisterDetails.this, R.anim.shake);
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
                    Animation shake = AnimationUtils.loadAnimation(RegisterDetails.this, R.anim.shake);
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
                    Animation shake = AnimationUtils.loadAnimation(RegisterDetails.this, R.anim.shake);
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

                stage1.setVisibility(View.GONE);
                stage2.setVisibility(View.VISIBLE);
                stage3.setVisibility(View.GONE);
                imagerow.setVisibility(View.GONE);
                doc5.setBackgroundResource(R.color.success);
                r5.setImageResource(R.drawable.right_circle);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(session.gettemp())) {
            edtRestaurantName.setText(session.gettemp());
        }

        if (!TextUtils.isEmpty(session.getaddress())) {
            edtAddressLine1.setText(session.getaddress());
        }

        if (!TextUtils.isEmpty(session.getpincode())) {
            edtPostcode.setText(session.getpincode());
        }

        if (!TextUtils.isEmpty(session.getcity())) {
            edtCity.setText(session.getcity());
        }

        if (!TextUtils.isEmpty(session.getstate())) {
            edtState.setText(session.getstate());
        }
    }

    private void galleryIntent() {
        // CHOOSE IMAGE FROM GALLERY
        // Log.d("gola", "entered here");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void cameraIntent() {
        requestMultiplePermissions();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        stage1.setVisibility(View.GONE);
        stage2.setVisibility(View.VISIBLE);
        stage3.setVisibility(View.GONE);
        //SAVE URI FROM GALLERY
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imageHoldUri = data.getData();
            if (imageHoldUri != null) {
                final Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);
                File f = new File(String.valueOf(imageHoldUri));
                String imageName = f.getName();
                StorageReference riversRef = mstorageReference.child("Documents/" + c + ".jpg");
                final ProgressDialog progressDialog = new ProgressDialog(RegisterDetails.this);
                progressDialog.setTitle("Updating....!");
                progressDialog.show();
                progressDialog.setCancelable(false);
                riversRef.putFile(imageHoldUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                final String[] u = new String[1];
                                storageRef.child("Documents/" + c + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        u[0] = uri.toString();
                                        if (selection == 1) {
                                            path1 = u[0];
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            Map<String, Object> data = new HashMap<>();
                                            data.put("PancardImage", path1);
                                            data.put("PancardImageApproval", "Pending");
                                            data.put("PancardImageComments", "");
                                            db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());
                                            doc1.setBackgroundResource(R.color.pending);
                                            r1.setImageResource(R.drawable.right_circle);
                                        } else if (selection == 2) {
                                            path2 = u[0];
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            Map<String, Object> data = new HashMap<>();
                                            data.put("FSSAIImage", path2);
                                            data.put("FSSAIImageApproval", "Pending");
                                            data.put("FSSAIImageComments", "");
                                            db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());
                                            doc2.setBackgroundResource(R.color.pending);
                                            r2.setImageResource(R.drawable.right_circle);
                                        } else if (selection == 3) {
                                            path3 = u[0];
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            Map<String, Object> data = new HashMap<>();
                                            data.put("GSTImage", path3);
                                            data.put("GSTImageApproval", "Pending");
                                            data.put("GSTImageComments", "");
                                            db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());
                                            doc3.setBackgroundResource(R.color.pending);
                                            r3.setImageResource(R.drawable.right_circle);
                                        } else if (selection == 4) {
                                            path4 = u[0];
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            Map<String, Object> data = new HashMap<>();
                                            data.put("RestaurantImage", path4);
                                            data.put("RestaurantImageApproval", "Pending");
                                            data.put("RestaurantImageComments", "");
                                            db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());
                                            doc4.setBackgroundResource(R.color.pending);
                                            r4.setImageResource(R.drawable.right_circle);
                                        }
                                        stage1.setVisibility(View.GONE);
                                        stage2.setVisibility(View.VISIBLE);
                                        stage3.setVisibility(View.GONE);
                                        imagerow.setVisibility(View.GONE);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                    }
                                });
                                progressDialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                Toast.makeText(RegisterDetails.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                progressDialog.setMessage((int) progress + "%Uploaded");
                            }
                        });
            } else {
                Toast.makeText(RegisterDetails.this, "File Path Null", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            stage1.setVisibility(View.GONE);
            stage2.setVisibility(View.VISIBLE);
            stage3.setVisibility(View.GONE);
            imagerow.setVisibility(View.GONE);

            imageHoldUri = imageUri;

            if (imageHoldUri != null) {
                final Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                File f = new File(String.valueOf(imageHoldUri));
                String imageName = f.getName();

                StorageReference riversRef = mstorageReference.child("Documents/" + c + ".jpg");
                final ProgressDialog progressDialog = new ProgressDialog(RegisterDetails.this);
                progressDialog.setTitle("Updating....!");
                progressDialog.show();
                progressDialog.setCancelable(false);
                riversRef.putFile(imageHoldUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                final String[] u = new String[1];
                                storageRef.child("Documents/" + c + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        stage1.setVisibility(View.GONE);
                                        stage2.setVisibility(View.VISIBLE);
                                        stage3.setVisibility(View.GONE);
                                        imagerow.setVisibility(View.GONE);

                                        u[0] = uri.toString();
                                        if (selection == 1) {
                                            path1 = u[0];
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            Map<String, Object> data = new HashMap<>();
                                            data.put("PancardImage", path1);
                                            data.put("PancardImageApproval", "Pending");
                                            data.put("PancardImageComments", "");
                                            db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());
                                            doc1.setBackgroundResource(R.color.pending);
                                            r1.setImageResource(R.drawable.right_circle);
                                        } else if (selection == 2) {
                                            path2 = u[0];
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            Map<String, Object> data = new HashMap<>();
                                            data.put("FSSAIImage", path2);
                                            data.put("FSSAIImageApproval", "Pending");
                                            data.put("FSSAIImageComments", "");
                                            db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());
                                            doc2.setBackgroundResource(R.color.pending);
                                            r2.setImageResource(R.drawable.right_circle);
                                        } else if (selection == 3) {
                                            path3 = u[0];
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            Map<String, Object> data = new HashMap<>();
                                            data.put("GSTImage", path3);
                                            data.put("GSTImageApproval", "Pending");
                                            data.put("GSTImageComments", "");
                                            db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());
                                            doc3.setBackgroundResource(R.color.pending);
                                            r3.setImageResource(R.drawable.right_circle);
                                        } else if (selection == 4) {
                                            path4 = u[0];
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            Map<String, Object> data = new HashMap<>();
                                            data.put("RestaurantImage", path4);
                                            data.put("RestaurantImageApproval", "Pending");
                                            data.put("RestaurantImageComments", "");
                                            db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());
                                            doc4.setBackgroundResource(R.color.pending);
                                            r4.setImageResource(R.drawable.right_circle);
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                    }
                                });
                                progressDialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                Toast.makeText(RegisterDetails.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                progressDialog.setMessage((int) progress + "%Uploaded");
                            }
                        });
            } else {
                Toast.makeText(RegisterDetails.this, "File Path Null", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onBackPressed() {
        if (currentselection == 1) {
            Intent intent = new Intent(RegisterDetails.this, CategorySelection.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (currentselection == 2) {
            currentselection = 1;
            stage1.setVisibility(View.VISIBLE);
            stage2.setVisibility(View.GONE);
            stage3.setVisibility(View.GONE);
            imagerow.setVisibility(View.GONE);
            one.setImageResource(R.drawable.sone);
            line1.setImageResource(R.drawable.line);
            two.setImageResource(R.drawable.two);
            line2.setImageResource(R.drawable.line);
            three.setImageResource(R.drawable.three);
        } else if (currentselection == 3) {
            currentselection = 2;
            stage1.setVisibility(View.GONE);
            stage2.setVisibility(View.VISIBLE);
            stage3.setVisibility(View.GONE);
            imagerow.setVisibility(View.GONE);
            one.setImageResource(R.drawable.sone);
            line1.setImageResource(R.drawable.sline);
            two.setImageResource(R.drawable.two);
            line2.setImageResource(R.drawable.line);
            three.setImageResource(R.drawable.three);
        } else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(RegisterDetails.this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {  // check if all permissions are granted
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "MyPicture");
                            values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
                            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, CAMERA_REQUEST_CODE);
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) { // check for permanent denial of any permission
                            // show alert dialog navigating to Settings
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(RegisterDetails.this, "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }
}
