package yummy.one.yummyonevendor.Controller.Activities.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.services.s3.AmazonS3Client;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import yummy.one.yummyonevendor.Functionality.Retrofit.APIClient;
import yummy.one.yummyonevendor.Functionality.Retrofit.APIInterface;
import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.Functionality.VolleySupport.APIConstant;
import yummy.one.yummyonevendor.R;

import static yummy.one.yummyonevendor.Functionality.VolleySupport.APIConstant.getInstance;

public class DocumentUploader1 extends AppCompatActivity {

    ImageView doc1, doc2, doc3, close1, close2, close3;
    ProgressBar progressBar1, progressBar2, progressBar3;
    TextView txtHeading, txtHeading1, txtApprovedHeading, txtDocName, txtComments, txtComments1, approvedComment;
    EditText edtDocumentNumber;
    Button btnUpload, btnOK;
    LinearLayout l1, l2, l3, imgBack, imgBack1, imgBack2;
    Session session;
    RelativeLayout r1, r2, r3;
    ScrollView pendinglayout, mainLayout, ApprovedLayout;
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
    int selection = 0;
    private String path1 = "", path2 = "", path3 = "";
    RequestOptions requestOptions;
    RadioButton fssai;
    RelativeLayout imageLayout;
    ImageView image, close, imgApproved;
    SharedPreferences sharedPreferences;
    APIInterface apiInterface;

    //S3 Bucket
    private AmazonS3Client s3Client;
    private BasicAWSCredentials credentials;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_uploader1);

//        try {
//            //S3 Bucket init
//            AWSMobileClient.getInstance().initialize(this).execute();
//            credentials = new BasicAWSCredentials(getInstance().KEY, getInstance().SECRET);
//            s3Client = new AmazonS3Client(credentials);
//        }catch (Exception e){
//            Log.e("EXCEPTION","ERROE=="+e);
//        }


        apiInterface = APIClient.getClient().create(APIInterface.class);
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        APIConstant.getInstance().renewAccessTokenApi(getApplicationContext());
        imgBack = findViewById(R.id.imgBack);
        imgBack1 = findViewById(R.id.imgBack1);
        imgBack2 = findViewById(R.id.imgBack2);
        txtHeading = findViewById(R.id.txtHeading);
        txtHeading1 = findViewById(R.id.txtHeading1);
        txtApprovedHeading = findViewById(R.id.txtApprovedHeading);
        txtDocName = findViewById(R.id.txtDocName);
        txtComments = findViewById(R.id.comments);
        txtComments1 = findViewById(R.id.comments1);
        approvedComment = findViewById(R.id.approvedComment);
        edtDocumentNumber = findViewById(R.id.edtDocumentNumber);
        btnUpload = findViewById(R.id.btnUpload);
        doc1 = findViewById(R.id.doc1);
        doc2 = findViewById(R.id.doc2);
        doc3 = findViewById(R.id.doc3);
        close1 = findViewById(R.id.close1);
        close2 = findViewById(R.id.close2);
        close3 = findViewById(R.id.close3);
        l1 = findViewById(R.id.l1);
        l2 = findViewById(R.id.l2);
        l3 = findViewById(R.id.l3);
        r1 = findViewById(R.id.r1);
        r2 = findViewById(R.id.r2);
        r3 = findViewById(R.id.r3);
        btnOK = findViewById(R.id.btnOK);
        imgApproved = findViewById(R.id.imgApproved);
        progressBar1 = findViewById(R.id.progressBar1);
        progressBar2 = findViewById(R.id.progressBar2);
        progressBar3 = findViewById(R.id.progressBar3);
        image = findViewById(R.id.image);
        close = findViewById(R.id.close);
        imageLayout = findViewById(R.id.imageLayout);
        mainLayout = findViewById(R.id.mainLayout);
        ApprovedLayout = findViewById(R.id.ApprovedLayout);
        pendinglayout = findViewById(R.id.pendinglayout);
        fssai = findViewById(R.id.fssai);
        pendinglayout.setVisibility(View.GONE);
        l1.setVisibility(View.VISIBLE);
        l2.setVisibility(View.GONE);
        l3.setVisibility(View.GONE);

        close1.setVisibility(View.GONE);
        close2.setVisibility(View.GONE);
        close3.setVisibility(View.GONE);

        progressBar1.setVisibility(View.GONE);
        progressBar2.setVisibility(View.GONE);
        progressBar3.setVisibility(View.GONE);

        doc1.setVisibility(View.VISIBLE);
        doc2.setVisibility(View.GONE);
        doc3.setVisibility(View.GONE);

        r1.setBackgroundResource(0);
        r2.setBackgroundResource(0);
        r3.setBackgroundResource(0);

        session = new Session(DocumentUploader1.this);

        mstorageReference = FirebaseStorage.getInstance().getReference();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DocumentUploader1.this, RegisterDetails.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        imgBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DocumentUploader1.this, RegisterDetails.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        imgBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DocumentUploader1.this, RegisterDetails.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DocumentUploader1.this, RegisterDetails.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        requestOptions = new RequestOptions();

        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(20));

        fssai.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(DocumentUploader1.this, AlertDialog.THEME_HOLO_LIGHT);
                    final EditText input = new EditText(DocumentUploader1.this);
                    alert.setTitle("Are you sure!");
                    alert.setMessage("You don't have FSSAI?");
                    // alert.setView(input);
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                            AddFSSAIDocumentApi("123456","s3 url","s3 url","s3 url");
//                            FirebaseFirestore db = FirebaseFirestore.getInstance();
//                            Map<String, Object> data = new HashMap<>();
//                            data.put("FSSAI", "No");
//                            data.put("FSSAIImageApproval", "Pending");
//                            db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());

                            final AlertDialog.Builder alert = new AlertDialog.Builder(DocumentUploader1.this, AlertDialog.THEME_HOLO_LIGHT);
                            alert.setTitle("Information");
                            alert.setMessage("Our agent will get in touch with you shortly!");
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(DocumentUploader1.this, RegisterDetails.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            });

                            alert.show();
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();
                }
            }
        });

        txtHeading.setText("FSSAI License");
        txtHeading1.setText("FSSAI License");
        txtApprovedHeading.setText("FSSAI License");
        txtDocName.setText("FSSAI Number");
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        DocumentReference docRef = db.collection("Vendor").document(session.getusername());
//        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if (documentSnapshot.exists()) {
//                    try {
//
//                        if (documentSnapshot.contains("FSSAIImage1")) {
//                            path1 = documentSnapshot.get("FSSAIImage1").toString();
//                            if (!TextUtils.isEmpty(path1)) {
//                                if (path1.contains(".pdf")) {
//                                    doc1.setImageResource(R.drawable.pdf);
//                                    doc1.setBackgroundResource(R.drawable.dotted_border);
//                                } else {
//                                    Glide.with(getApplicationContext()).load(path1).apply(requestOptions).into(doc1);
//                                    doc1.setBackgroundResource(R.drawable.dotted_border);
//                                }
//                                l1.setVisibility(View.VISIBLE);
//                                l2.setVisibility(View.VISIBLE);
//                                l3.setVisibility(View.VISIBLE);
//                                doc1.setVisibility(View.VISIBLE);
//                                doc2.setVisibility(View.VISIBLE);
//                                close1.setVisibility(View.VISIBLE);
//                            }
//                        }
//                        if (documentSnapshot.contains("FSSAIImage2")) {
//                            path2 = documentSnapshot.get("FSSAIImage2").toString();
//                            if (!TextUtils.isEmpty(path2)) {
//                                if (path2.contains(".pdf")) {
//                                    doc2.setImageResource(R.drawable.pdf);
//                                    doc2.setBackgroundResource(R.drawable.dotted_border);
//                                } else {
//                                    Glide.with(getApplicationContext()).load(path2).apply(requestOptions).into(doc2);
//                                    doc2.setBackgroundResource(R.drawable.dotted_border);
//                                }
//                                l1.setVisibility(View.VISIBLE);
//                                l2.setVisibility(View.VISIBLE);
//                                l3.setVisibility(View.VISIBLE);
//                                doc1.setVisibility(View.VISIBLE);
//                                doc2.setVisibility(View.VISIBLE);
//                                doc3.setVisibility(View.VISIBLE);
//                                close2.setVisibility(View.VISIBLE);
//                            }
//                        }
//                        if (documentSnapshot.contains("FSSAIImage3")) {
//                            path3 = documentSnapshot.get("FSSAIImage3").toString();
//                            if (!TextUtils.isEmpty(path3)) {
//                                if (path3.contains(".pdf")) {
//                                    doc3.setImageResource(R.drawable.pdf);
//                                    doc3.setBackgroundResource(R.drawable.dotted_border);
//                                } else {
//                                    Glide.with(getApplicationContext()).load(path3).apply(requestOptions).into(doc3);
//                                    doc3.setBackgroundResource(R.drawable.dotted_border);
//                                }
//                                l1.setVisibility(View.VISIBLE);
//                                l2.setVisibility(View.VISIBLE);
//                                l3.setVisibility(View.VISIBLE);
//                                doc1.setVisibility(View.VISIBLE);
//                                doc2.setVisibility(View.VISIBLE);
//                                doc3.setVisibility(View.VISIBLE);
//                                close3.setVisibility(View.VISIBLE);
//                            }
//                        }
//                        if (documentSnapshot.contains("FSSAINumber")) {
//                            edtDocumentNumber.setText(documentSnapshot.get("FSSAINumber").toString());
//                            if (btnUpload.getVisibility() != View.VISIBLE) {
//                                edtDocumentNumber.setEnabled(false);
//                            }
//                        }
//
//                        if (documentSnapshot.contains("FSSAIImageComments")) {
//                            if (!TextUtils.isEmpty(documentSnapshot.get("FSSAIImageComments").toString()))
//                                txtComments.setText("" + documentSnapshot.get("FSSAIImageComments").toString());
//                            else
//                                txtComments.setText("Pending for approval");
//                            txtComments.setVisibility(View.GONE);
//                            txtComments1.setVisibility(View.GONE);
//                        }
//
//                        if (documentSnapshot.contains("FSSAIImageApproval")) {
//                            if (!TextUtils.isEmpty(Objects.requireNonNull(documentSnapshot.get("FSSAIImageApproval")).toString())) {
//                                if (documentSnapshot.get("FSSAIImageApproval").toString().equalsIgnoreCase("Pending")) {
//                                    btnUpload.setVisibility(View.GONE);
//                                    close1.setVisibility(View.GONE);
//                                    close2.setVisibility(View.GONE);
//                                    close3.setVisibility(View.GONE);
//                                    doc1.setEnabled(false);
//                                    doc2.setEnabled(false);
//                                    doc3.setEnabled(false);
//                                    edtDocumentNumber.setEnabled(false);
//                                    txtComments.setText("Pending for Approval");
//                                    txtComments.setVisibility(View.VISIBLE);
//                                    txtComments1.setVisibility(View.GONE);
//                                    doc1.setVisibility(View.INVISIBLE);
//                                    doc2.setVisibility(View.INVISIBLE);
//                                    doc3.setVisibility(View.INVISIBLE);
//                                    if (!TextUtils.isEmpty(path1))
//                                        doc1.setVisibility(View.VISIBLE);
//                                    if (!TextUtils.isEmpty(path2))
//                                        doc2.setVisibility(View.VISIBLE);
//                                    if (!TextUtils.isEmpty(path3))
//                                        doc3.setVisibility(View.VISIBLE);
//                                    pendinglayout.setVisibility(View.VISIBLE);
//                                } else if (documentSnapshot.get("FSSAIImageApproval").toString().equalsIgnoreCase("Approved")) {
//                                    /*btnUpload.setVisibility(View.GONE);
//                                    btnUpload.setVisibility(View.GONE);
//                                    close1.setVisibility(View.GONE);
//                                    close2.setVisibility(View.GONE);
//                                    close3.setVisibility(View.GONE);
//                                    doc1.setEnabled(false);
//                                    doc2.setEnabled(false);
//                                    doc3.setEnabled(false);
//                                    edtDocumentNumber.setEnabled(false);
//                                    txtComments.setText("Approved");
//                                    txtComments.setTextColor(Color.parseColor("#119326"));
//                                    txtComments1.setVisibility(View.INVISIBLE);
//                                    doc1.setVisibility(View.INVISIBLE);
//                                    doc2.setVisibility(View.INVISIBLE);
//                                    doc3.setVisibility(View.INVISIBLE);
//                                    if (!TextUtils.isEmpty(path1))
//                                        doc1.setVisibility(View.VISIBLE);
//                                    if (!TextUtils.isEmpty(path2))
//                                        doc2.setVisibility(View.VISIBLE);
//                                    if (!TextUtils.isEmpty(path3))
//                                        doc3.setVisibility(View.VISIBLE);*/
//
//                                    mainLayout.setVisibility(View.GONE);
//                                    imageLayout.setVisibility(View.GONE);
//                                    pendinglayout.setVisibility(View.GONE);
//                                    ApprovedLayout.setVisibility(View.VISIBLE);
//                                    approvedComment.setText("This document has been approved. Rest assured while we review the other documents and set up the account for you. We are thrilled to have you onboard");
//                                    // Glide.with(DocumentUploader1.this).asGif().load(R.raw.document_approved).into(imgApproved);
//                                    Glide.with(DocumentUploader1.this).load(R.drawable.success).into(imgApproved);
//
//                                } else if (documentSnapshot.get("FSSAIImageApproval").toString().equalsIgnoreCase("Rejected")) {
//                                    btnUpload.setVisibility(View.VISIBLE);
//                                    txtComments.setVisibility(View.VISIBLE);
//                                    txtComments.setText("Rejected : " + documentSnapshot.get("FSSAIImageComments"));
//                                    txtComments.setTextColor(Color.parseColor("#FF0000"));
//                                    txtComments1.setVisibility(View.GONE);
//                                } else {
//                                    txtComments.setVisibility(View.GONE);
//                                    txtComments1.setVisibility(View.GONE);
//                                }
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
////                        txtComments.setVisibility(View.GONE);
////                        txtComments1.setVisibility(View.GONE);
//            }
//        });

        close1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(path3)) {
                    if (TextUtils.isEmpty(path2)) {
                        path1 = "";
                        doc1.setImageResource(R.drawable.add_image);
                        doc1.setBackgroundResource(0);
                        close1.setVisibility(View.GONE);
                        btnUpload.setVisibility(View.VISIBLE);
                        l2.setVisibility(View.GONE);
                        l3.setVisibility(View.GONE);
                        AddFSSAIDocumentApi("123456","s3 url","s3 url","s3 url");
//                        FirebaseFirestore db = FirebaseFirestore.getInstance();
//                        Map<String, Object> data = new HashMap<>();
//                        data.put("FSSAIImage1", FieldValue.delete());
//                        data.put("FSSAIImage2", FieldValue.delete());
//                        data.put("FSSAIImage3", FieldValue.delete());
//                        db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());
                    } else {
                        path1 = path2;
                        path2 = "";
                        Glide.with(getApplicationContext())
                                .load(path1)
                                .apply(requestOptions)
                                .into(doc1);
                        close2.setVisibility(View.GONE);
                        doc2.setImageResource(R.drawable.add_image);
                        doc2.setBackgroundResource(0);
                        doc3.setVisibility(View.GONE);
                        close2.setVisibility(View.GONE);
                        AddFSSAIDocumentApi("123456","s3 url","s3 url","s3 url");
//                        FirebaseFirestore db = FirebaseFirestore.getInstance();
//                        Map<String, Object> data = new HashMap<>();
//                        data.put("FSSAIImage1", path1);
//                        data.put("FSSAIImage2", FieldValue.delete());
//                        data.put("FSSAIImage3", FieldValue.delete());
//                        db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());
                    }
                } else {
                    path1 = path2;
                    path2 = path3;
                    path3 = "";
                    Glide.with(getApplicationContext())
                            .load(path1)
                            .apply(requestOptions)
                            .into(doc1);
                    Glide.with(getApplicationContext())
                            .load(path2)
                            .apply(requestOptions)
                            .into(doc2);
                    doc3.setImageResource(R.drawable.add_image);
                    doc3.setBackgroundResource(0);
                    close3.setVisibility(View.GONE);
                    AddFSSAIDocumentApi("123456","s3 url","s3 url","s3 url");
//                    FirebaseFirestore db = FirebaseFirestore.getInstance();
//                    Map<String, Object> data = new HashMap<>();
//                    data.put("FSSAIImage1", path1);
//                    data.put("FSSAIImage2", path2);
//                    data.put("FSSAIImage3", FieldValue.delete());
//                    db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());
                }
            }
        });

        close2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(path3)) {
                    path2 = "";
                    doc2.setImageResource(R.drawable.add_image);
                    doc2.setBackgroundResource(0);
                    doc3.setVisibility(View.GONE);
                    close2.setVisibility(View.GONE);
                    btnUpload.setVisibility(View.VISIBLE);
                    AddFSSAIDocumentApi("123456","s3 url","s3 url","s3 url");
//                    FirebaseFirestore db = FirebaseFirestore.getInstance();
//                    Map<String, Object> data = new HashMap<>();
//                    data.put("FSSAIImage1", path1);
//                    data.put("FSSAIImage2", FieldValue.delete());
//                    data.put("FSSAIImage3", FieldValue.delete());
//                    db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());
                } else {
                    path2 = path3;
                    path3 = "";
                    Glide.with(getApplicationContext())
                            .load(path2)
                            .apply(requestOptions)
                            .into(doc2);
                    doc3.setImageResource(R.drawable.add_image);
                    doc3.setBackgroundResource(0);
                    close3.setVisibility(View.GONE);
                    AddFSSAIDocumentApi("123456","s3 url","s3 url","s3 url");
//                    FirebaseFirestore db = FirebaseFirestore.getInstance();
//                    Map<String, Object> data = new HashMap<>();
//                    data.put("FSSAIImage1", path1);
//                    data.put("FSSAIImage2", path2);
//                    data.put("FSSAIImage3", FieldValue.delete());
//                    db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());
                }
            }
        });

        close3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                path3 = "";
                doc3.setImageResource(R.drawable.add_image);
                doc3.setBackgroundResource(0);
                close3.setVisibility(View.GONE);
                btnUpload.setVisibility(View.VISIBLE);
                AddFSSAIDocumentApi("123456","s3 url","s3 url","s3 url");
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                Map<String, Object> data = new HashMap<>();
//                data.put("FSSAIImage1", path1);
//                data.put("FSSAIImage2", path2);
//                data.put("FSSAIImage3", FieldValue.delete());
//                db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());
            }
        });

        doc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selection = 1;
                if (TextUtils.isEmpty(path1) || path1.contains(".pdf")) {
                    final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(DocumentUploader1.this, AlertDialog.THEME_HOLO_LIGHT);
                    builder.setTitle("Add Photo!");

                    //SET ITEMS AND THERE LISTENERS
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (items[item].equals("Take Photo")) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                        && ActivityCompat.checkSelfPermission(DocumentUploader1.this, Manifest.permission.CAMERA)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                                            REQUEST_CAMERA_ACCESS_PERMISSION);
                                } else {
                                    cameraIntent();
                                }
                            } else if (items[item].equals("Choose from Library")) {
                                galleryIntent();
                            } else if (items[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                } else {
                    final CharSequence[] items = {"View Photo", "Take Photo", "Choose from Library", "Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(DocumentUploader1.this, AlertDialog.THEME_HOLO_LIGHT);
                    builder.setTitle("Add Photo!");
                    //SET ITEMS AND THERE LISTENERS
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (items[item].equals("Take Photo")) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                        && ActivityCompat.checkSelfPermission(DocumentUploader1.this, Manifest.permission.CAMERA)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                                            REQUEST_CAMERA_ACCESS_PERMISSION);
                                } else {
                                    cameraIntent();
                                }
                            } else if (items[item].equals("Choose from Library")) {
                                galleryIntent();
                            } else if (items[item].equals("Cancel")) {
                                dialog.dismiss();
                            } else if (items[item].equals("View Photo")) {
                                Glide.with(getApplicationContext())
                                        .load(path1)
                                        .into(image);
                                imageLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    builder.show();
                }
            }
        });

        doc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selection = 2;
                if (TextUtils.isEmpty(path2) || path2.contains(".pdf")) {
                    final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(DocumentUploader1.this, AlertDialog.THEME_HOLO_LIGHT);
                    builder.setTitle("Add Photo!");
                    //SET ITEMS AND THERE LISTENERS
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (items[item].equals("Take Photo")) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                        && ActivityCompat.checkSelfPermission(DocumentUploader1.this, Manifest.permission.CAMERA)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                                            REQUEST_CAMERA_ACCESS_PERMISSION);
                                } else {
                                    cameraIntent();
                                }
                            } else if (items[item].equals("Choose from Library")) {
                                galleryIntent();
                            } else if (items[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                } else {
                    final CharSequence[] items = {"View Photo", "Take Photo", "Choose from Library", "Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(DocumentUploader1.this, AlertDialog.THEME_HOLO_LIGHT);
                    builder.setTitle("Add Photo!");
                    //SET ITEMS AND THERE LISTENERS
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (items[item].equals("Take Photo")) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                        && ActivityCompat.checkSelfPermission(DocumentUploader1.this, Manifest.permission.CAMERA)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                                            REQUEST_CAMERA_ACCESS_PERMISSION);
                                } else {
                                    cameraIntent();
                                }
                            } else if (items[item].equals("Choose from Library")) {
                                galleryIntent();
                            } else if (items[item].equals("Cancel")) {
                                dialog.dismiss();
                            } else if (items[item].equals("View Photo")) {
                                Glide.with(getApplicationContext())
                                        .load(path2)
                                        .into(image);
                                imageLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    builder.show();
                }
            }
        });

        doc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selection = 3;
                if (TextUtils.isEmpty(path3) || path3.contains(".pdf")) {
                    final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(DocumentUploader1.this, AlertDialog.THEME_HOLO_LIGHT);
                    builder.setTitle("Add Photo!");
                    //SET ITEMS AND THERE LISTENERS
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (items[item].equals("Take Photo")) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                        && ActivityCompat.checkSelfPermission(DocumentUploader1.this, Manifest.permission.CAMERA)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                                            REQUEST_CAMERA_ACCESS_PERMISSION);
                                } else {
                                    cameraIntent();
                                }
                            } else if (items[item].equals("Choose from Library")) {
                                galleryIntent();
                            } else if (items[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                } else {
                    final CharSequence[] items = {"View Photo", "Take Photo", "Choose from Library", "Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(DocumentUploader1.this, AlertDialog.THEME_HOLO_LIGHT);
                    builder.setTitle("Add Photo!");
                    //SET ITEMS AND THERE LISTENERS
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (items[item].equals("Take Photo")) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                        && ActivityCompat.checkSelfPermission(DocumentUploader1.this, Manifest.permission.CAMERA)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                                            REQUEST_CAMERA_ACCESS_PERMISSION);
                                } else {
                                    cameraIntent();
                                }
                            } else if (items[item].equals("Choose from Library")) {
                                galleryIntent();
                            } else if (items[item].equals("Cancel")) {
                                dialog.dismiss();
                            } else if (items[item].equals("View Photo")) {
                                Glide.with(getApplicationContext())
                                        .load(path3)
                                        .into(image);
                                imageLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    builder.show();
                }
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtDocumentNumber.getText().toString())) {
                    edtDocumentNumber.setError("Enter the FSSAI Number");
                    edtDocumentNumber.requestFocus();
                    return;
                } else {
                    edtDocumentNumber.setError(null);
                }

                if (TextUtils.isEmpty(path1)) {
                    Toast.makeText(getApplicationContext(), "Upload One Image", Toast.LENGTH_LONG).show();
                    return;
                }
                AddFSSAIDocumentApi("123456","s3 url","s3 url","s3 url");
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                Map<String, Object> data = new HashMap<>();
//                data.put("FSSAIImageApproval", "Pending");
//                data.put("FSSAINumber", edtDocumentNumber.getText().toString());
//                db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());

                Intent intent = new Intent(DocumentUploader1.this, RegisterDetails.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void galleryIntent() {
        //CHOOSE IMAGE FROM GALLERY
//        Log.d("gola", "entered here");
        final String[] ACCEPT_MIME_TYPES = {"application/pdf", "image/*"};
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_MIME_TYPES, ACCEPT_MIME_TYPES);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void cameraIntent() {
        requestMultiplePermissions();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //SAVE URI FROM GALLERY
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imageHoldUri = data.getData();

            if (imageHoldUri != null) {
                final Date c = Calendar.getInstance().getTime();

                ContentResolver cR = getApplication().getContentResolver();
                String type = cR.getType(imageHoldUri);

                File f = new File(String.valueOf(imageHoldUri));
                String imageName = f.getName();

                new UploadProfileAsyncTask().execute();

                if (selection == 1) {
                    doc1.setImageResource(0);
                    doc2.setVisibility(View.VISIBLE);
                    l1.setVisibility(View.VISIBLE);
                    l2.setVisibility(View.VISIBLE);
                    l3.setVisibility(View.VISIBLE);
                    close1.setVisibility(View.VISIBLE);
                } else if (selection == 2) {
                    doc2.setImageResource(0);
                    doc3.setVisibility(View.VISIBLE);
                    l1.setVisibility(View.VISIBLE);
                    l2.setVisibility(View.VISIBLE);
                    l3.setVisibility(View.VISIBLE);
                    close2.setVisibility(View.VISIBLE);
                } else if (selection == 3) {
                    doc3.setImageResource(0);
                    l1.setVisibility(View.VISIBLE);
                    l2.setVisibility(View.VISIBLE);
                    l3.setVisibility(View.VISIBLE);
                    close3.setVisibility(View.VISIBLE);
                }

//                StorageReference riversRef;
//                if(type.equals("application/pdf"))
//                    riversRef = mstorageReference.child("Documents/" + c + ".pdf");
//                else
//                    riversRef = mstorageReference.child("Documents/" + c + ".jpg");
//                riversRef.putFile(imageHoldUri)
//                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                // Get a URL to the uploaded content
//                                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//                                final String[] u = new String[1];
//                                String path ="";
//                                if(type.equals("application/pdf"))
//                                    path ="Documents/" + c + ".pdf";
//                                else
//                                    path = "Documents/" + c + ".jpg";
//
//                                storageRef.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                    @Override
//                                    public void onSuccess(Uri uri) {
//                                        u[0] = uri.toString();
//                                        if(selection==1) {
//                                            path1 = u[0];
//                                            RequestOptions requestOptions = new RequestOptions();
//                                            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(20));
//                                            if(type.equals("application/pdf"))
//                                                doc1.setImageResource(R.drawable.pdf);
//                                            else
//                                                Glide.with(getApplicationContext()).load(path1).apply(requestOptions).into(doc1);
//                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
//                                            Map<String, Object> data = new HashMap<>();
//                                            data.put("FSSAIImage1", path1);
//                                            data.put("FSSAIImageApproval", "Pending");
//                                            data.put("FSSAIImageComments", "");
//                                            data.put("FSSAINumber", edtDocumentNumber.getText().toString());
//                                            db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());
//                                            doc2.setVisibility(View.VISIBLE);
//                                        }
//                                        else  if(selection==2) {
//                                            path2 = u[0];
//                                            RequestOptions requestOptions = new RequestOptions();
//                                            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(20));
//                                            if(type.equals("application/pdf"))
//                                                doc2.setImageResource(R.drawable.pdf);
//                                            else
//                                                Glide.with(getApplicationContext()).load(path2).apply(requestOptions).into(doc2);
//                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
//                                            Map<String, Object> data = new HashMap<>();
//                                            data.put("FSSAIImage2", path2);
//                                            data.put("FSSAIImageApproval", "Pending");
//                                            data.put("FSSAIImageComments", "");
//                                            data.put("FSSAINumber", edtDocumentNumber.getText().toString());
//                                            db.collection("Vendor").document(session.getusername()).set(data,SetOptions.merge());
//                                            doc3.setVisibility(View.VISIBLE);
//                                        }
//                                        else  if(selection==3) {
//                                            path3 = u[0];
//                                            RequestOptions requestOptions = new RequestOptions();
//                                            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(20));
//                                            if(type.equals("application/pdf"))
//                                                doc3.setImageResource(R.drawable.pdf);
//                                            else
//                                                Glide.with(getApplicationContext()).load(path3).apply(requestOptions).into(doc3);
//                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
//                                            Map<String, Object> data = new HashMap<>();
//                                            data.put("FSSAIImage3", path3);
//                                            data.put("FSSAIImageApproval", "Pending");
//                                            data.put("FSSAIImageComments", "");
//                                            data.put("FSSAINumber", edtDocumentNumber.getText().toString());
//                                            db.collection("Vendor").document(session.getusername()).set(data,SetOptions.merge());
//                                        }
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception exception) {
//                                        // Handle any errors
//                                    }
//                                });
//                                if(selection==1){
//                                    progressBar1.setVisibility(View.GONE);
//                                }
//                                else if(selection == 2){
//                                    progressBar2.setVisibility(View.GONE);
//                                }
//                                else if(selection ==3){
//                                    progressBar3.setVisibility(View.GONE);
//                                }
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception exception) {
//                                // Handle unsuccessful uploads
//                                Toast.makeText(DocumentUploader1.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                                if(selection==1){
//                                    progressBar1.setVisibility(View.VISIBLE);
//                                }
//                                else if(selection == 2){
//                                    progressBar2.setVisibility(View.VISIBLE);
//                                }
//                                else if(selection ==3){
//                                    progressBar3.setVisibility(View.VISIBLE);
//                                }
//                            }
//                        });
            } else {
                Toast.makeText(DocumentUploader1.this, "File Path Null", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            imageHoldUri = imageUri;
            if (imageHoldUri != null) {
                final Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                File f = new File(String.valueOf(imageHoldUri));
                String imageName = f.getName();

                new UploadProfileAsyncTask().execute();


                if (selection == 1) {
                    doc1.setImageResource(0);
                    doc2.setVisibility(View.VISIBLE);
                    l1.setVisibility(View.VISIBLE);
                    l2.setVisibility(View.VISIBLE);
                    l3.setVisibility(View.VISIBLE);
                    close1.setVisibility(View.VISIBLE);
                } else if (selection == 2) {
                    doc2.setImageResource(0);
                    doc3.setVisibility(View.VISIBLE);
                    l1.setVisibility(View.VISIBLE);
                    l2.setVisibility(View.VISIBLE);
                    l3.setVisibility(View.VISIBLE);
                    close2.setVisibility(View.VISIBLE);
                } else if (selection == 3) {
                    doc3.setImageResource(0);
                    l1.setVisibility(View.VISIBLE);
                    l2.setVisibility(View.VISIBLE);
                    l3.setVisibility(View.VISIBLE);
                    close3.setVisibility(View.VISIBLE);
                }

//                StorageReference riversRef = mstorageReference.child("Documents/" + c + ".jpg");
//                riversRef.putFile(imageHoldUri)
//                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                // Get a URL to the uploaded content
//                                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//                                final String[] u = new String[1];
//                                storageRef.child("Documents/" +c + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                    @Override
//                                    public void onSuccess(Uri uri) {
//
//                                        u[0] = uri.toString();
//                                        if(selection==1) {
//                                            path1 = u[0];
//                                            RequestOptions requestOptions = new RequestOptions();
//                                            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(20));
//                                            Glide.with(getApplicationContext()).load(path1).apply(requestOptions).into(doc1);
//                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
//                                            Map<String, Object> data = new HashMap<>();
//                                            data.put("FSSAIImage1", path1);
//                                            data.put("FSSAIImageApproval", "Pending");
//                                            data.put("FSSAIImageComments", "");
//                                            data.put("FSSAINumber", edtDocumentNumber.getText().toString());
//                                            db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());
//                                            doc2.setVisibility(View.VISIBLE);
//                                        }
//                                        else  if(selection==2) {
//                                            path2 = u[0];
//                                           RequestOptions requestOptions = new RequestOptions();
//                                            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(20));
//                                            Glide.with(getApplicationContext()).load(path2).apply(requestOptions).into(doc2);
//                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
//                                            Map<String, Object> data = new HashMap<>();
//                                            data.put("FSSAIImage2", path2);
//                                            data.put("FSSAIImageApproval", "Pending");
//                                            data.put("FSSAIImageComments", "");
//                                            data.put("FSSAINumber", edtDocumentNumber.getText().toString());
//                                            db.collection("Vendor").document(session.getusername()).set(data,SetOptions.merge());
//                                            doc3.setVisibility(View.VISIBLE);
//                                        }
//                                        else  if(selection==3) {
//                                            path3 = u[0];
//                                              RequestOptions requestOptions = new RequestOptions();
//                                            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(20));
//                                            Glide.with(getApplicationContext()).load(path3).apply(requestOptions).into(doc3);
//                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
//                                            Map<String, Object> data = new HashMap<>();
//                                            data.put("FSSAIImage3", path3);
//                                            data.put("FSSAIImageApproval", "Pending");
//                                            data.put("FSSAIImageComments", "");
//                                            data.put("FSSAINumber", edtDocumentNumber.getText().toString());
//                                            db.collection("Vendor").document(session.getusername()).set(data,SetOptions.merge());
//                                        }
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception exception) {
//                                        // Handle any errors
//                                    }
//                                });
//                                if(selection==1){
//                                    progressBar1.setVisibility(View.GONE);
//                                }
//                                else if(selection == 2){
//                                    progressBar2.setVisibility(View.GONE);
//                                }
//                                else if(selection ==3){
//                                    progressBar3.setVisibility(View.GONE);
//                                }
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception exception) {
//                                // Handle unsuccessful uploads
//                                Toast.makeText(DocumentUploader1.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                                if(selection==1){
//                                    progressBar1.setVisibility(View.VISIBLE);
//                                }
//                                else if(selection == 2){
//                                    progressBar2.setVisibility(View.VISIBLE);
//                                }
//                                else if(selection ==3){
//                                    progressBar3.setVisibility(View.VISIBLE);
//                                }
//                            }
//                        });

            } else {
                Toast.makeText(DocumentUploader1.this, "File Path Null", Toast.LENGTH_SHORT).show();
            }


        }

    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(DocumentUploader1.this)
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
                        Toast.makeText(DocumentUploader1.this, "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    public void setupUI(View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(DocumentUploader1.this);
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
//        InputMethodManager inputMethodManager =
//                (InputMethodManager) activity.getSystemService(
//                        Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(
//                activity.getCurrentFocus().getWindowToken(), 0);
    }

    private class UploadProfileAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            new UploadProThread().start();
            return null;
        }
    }

    private class UploadProThread extends Thread {
        @SuppressWarnings("deprecation")
        @Override
        public void run() {
            // TODO Auto-generated method stub
            // public static final String BUCKET_NAME = "alldercare";
            // using YOUR_S3_ACCESS_KEY, and  YOUR_S3_SECRET
            int select = selection;
            String c = "" + System.currentTimeMillis();

            ContentResolver cR = getApplication().getContentResolver();
            String type = cR.getType(imageHoldUri);

            StorageReference riversRef;
            if (type.equals("application/pdf"))
                riversRef = mstorageReference.child("Documents/" + c + ".pdf");
            else
                riversRef = mstorageReference.child("Documents/" + c + ".jpg");

            riversRef.putFile(imageHoldUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                            final String[] u = new String[1];
                            String path = "";
                            if (type.equals("application/pdf"))
                                path = "Documents/" + c + ".pdf";
                            else
                                path = "Documents/" + c + ".jpg";
                            storageRef.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    u[0] = uri.toString();

                                    if (select == 1) {
                                        path1 = u[0];
                                        RequestOptions requestOptions = new RequestOptions();
                                        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(20));
                                        if (type.equals("application/pdf"))
                                            doc1.setImageResource(R.drawable.pdf);
                                        else
                                            Glide.with(getApplicationContext()).load(path1).apply(requestOptions).into(doc1);
                                        doc1.setBackgroundResource(R.drawable.dotted_border);
                                        AddFSSAIDocumentApi("123456","s3 url","s3 url","s3 url");
//                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
//                                        Map<String, Object> data = new HashMap<>();
//                                        data.put("FSSAIImage1", path1);
//                                        data.put("FSSAIImageComments", "");
//                                        data.put("FSSAINumber", edtDocumentNumber.getText().toString());
//                                        db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());
                                        doc2.setVisibility(View.VISIBLE);
                                        l1.setVisibility(View.VISIBLE);
                                        l2.setVisibility(View.VISIBLE);
                                        l3.setVisibility(View.VISIBLE);
                                        progressBar1.setVisibility(View.GONE);
                                    } else if (select == 2) {
                                        path2 = u[0];
                                        RequestOptions requestOptions = new RequestOptions();
                                        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(20));
                                        if (type.equals("application/pdf"))
                                            doc2.setImageResource(R.drawable.pdf);
                                        else
                                            Glide.with(getApplicationContext()).load(path2).apply(requestOptions).into(doc2);
                                        doc2.setBackgroundResource(R.drawable.dotted_border);
                                        AddFSSAIDocumentApi("123456","s3 url","s3 url","s3 url");
//                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
//                                        Map<String, Object> data = new HashMap<>();
//                                        data.put("FSSAIImage2", path2);
//                                        data.put("FSSAIImageComments", "");
//                                        data.put("FSSAINumber", edtDocumentNumber.getText().toString());
//                                        db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());
                                        doc3.setVisibility(View.VISIBLE);
                                        l1.setVisibility(View.VISIBLE);
                                        l2.setVisibility(View.VISIBLE);
                                        l3.setVisibility(View.VISIBLE);
                                        progressBar2.setVisibility(View.GONE);
                                    } else if (select == 3) {
                                        path3 = u[0];
                                        RequestOptions requestOptions = new RequestOptions();
                                        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(20));
                                        if (type.equals("application/pdf"))
                                            doc3.setImageResource(R.drawable.pdf);
                                        else
                                            Glide.with(getApplicationContext()).load(path3).apply(requestOptions).into(doc3);
                                        doc3.setBackgroundResource(R.drawable.dotted_border);
                                     AddFSSAIDocumentApi("123456","s3 url","s3 url","s3 url");
//                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
//                                        Map<String, Object> data = new HashMap<>();
//                                        data.put("FSSAIImage3", path3);
//                                        data.put("FSSAIImageComments", "");
//                                        data.put("FSSAINumber", edtDocumentNumber.getText().toString());
//                                        db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());
                                        l1.setVisibility(View.VISIBLE);
                                        l2.setVisibility(View.VISIBLE);
                                        l3.setVisibility(View.VISIBLE);
                                        progressBar3.setVisibility(View.GONE);
                                    }


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
                            if (select == 1) {
                                progressBar1.setVisibility(View.GONE);
                            } else if (select == 2) {
                                progressBar2.setVisibility(View.GONE);
                            } else if (select == 3) {
                                progressBar3.setVisibility(View.GONE);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Toast.makeText(DocumentUploader1.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            if (select == 1) {
                                progressBar1.setVisibility(View.VISIBLE);
                            } else if (select == 2) {
                                progressBar2.setVisibility(View.VISIBLE);
                            } else if (select == 3) {
                                progressBar3.setVisibility(View.VISIBLE);
                            }
                        }
                    });

        }
    }

    private void AddFSSAIDocumentApi(String fssaiNo,String fssaiImage1,String fssaiImage2,String fssaiImage3) {
       // progressBar.setVisibility(View.VISIBLE);
        JSONObject data = new JSONObject();
        try {
            String mobilenumber=sharedPreferences.getString("MOBILENUMBER", "");
            data.put("mobilenumber",mobilenumber);
            data.put("fssaiNo",fssaiNo);
            data.put("fssaiImage1",fssaiImage1);
            data.put("fssaiImage2",fssaiImage2);
            data.put("fssaiImage3",fssaiImage3);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("PARAMETER", "" + getInstance().addFSSAIDocument + data);
        RequestQueue requstQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, getInstance().addFSSAIDocument,data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject JsonMain) {
                        try {
                           // progressBar.setVisibility(View.GONE);
                            Log.e("RESPONSE", "" + APIConstant.getInstance().addFSSAIDocument + JsonMain);
                            String msg = JsonMain.getString("message");
                            if (msg.equalsIgnoreCase("SUCCESS")) {
                                JSONObject dataobject=JsonMain.getJSONObject("data");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                            }else {
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            //progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // progressBar.setVisibility(View.GONE);
                        Log.e("ERROR", "" + getInstance().addFSSAIDocument + error.toString());
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
                Log.e("HEADER", "" + getInstance().addFSSAIDocument + params);
                return params;
            }
        };
        requstQueue.add(jsonobj);

    }
}
