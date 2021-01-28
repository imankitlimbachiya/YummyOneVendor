package yummy.one.yummyonevendor.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
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

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import yummy.one.yummyonevendor.FoodCategory.Sub;
import yummy.one.yummyonevendor.FoodCategory.SubCategory;
import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.Inventory.Inventory;
import yummy.one.yummyonevendor.Inventory.InventoryAdapter;
import yummy.one.yummyonevendor.Models.Orders.OrderAdapter;
import yummy.one.yummyonevendor.Models.Orders.OrdersData;
import yummy.one.yummyonevendor.Product.Product;
import yummy.one.yummyonevendor.R;

import static android.app.Activity.RESULT_OK;

public class AddVlog extends Fragment {


    Button btnUpload,btnSelect;

    private VideoView videoView;
    private Uri videoUri;
    MediaController mediaController;
    private StorageReference mStorageRef;
    private Session session;
    private LinearLayout back;
    private FrameLayout background;
    private LinearLayout linearSelect,linearUpload;
    private RecyclerView recyclerView;
    private ImageView image1,image2;

    private static final int PICK_VIDEO_REQUEST = 1;

    private ArrayList<Sub> subcategory = new ArrayList<Sub>();
    private SubCategory subCategoryAdapter;

    public AddVlog() {
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
        View v=inflater.inflate(R.layout.fragment_add_vlog, container, false);

        btnUpload = v.findViewById(R.id.btnUpload);
        btnSelect = v.findViewById(R.id.btnSelect);
        linearUpload = v.findViewById(R.id.linearUpload);
        linearSelect = v.findViewById(R.id.linearSelect);
        recyclerView = v.findViewById(R.id.recyclerView);
//        image1 = v.findViewById(R.id.image1);
//        image2 = v.findViewById(R.id.image2);

        back=v.findViewById(R.id.back);
        background=v.findViewById(R.id.background);
        videoView=v.findViewById(R.id.Video_view);

        session = new Session(getActivity());

        linearSelect.setVisibility(View.VISIBLE);
        linearUpload.setVisibility(View.GONE);
        loadcategories();

        if(getActivity()!=null){
            LinearLayout bottomnavigation = getActivity().findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.GONE);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null)
                    getActivity().onBackPressed();
            }
        });

        mediaController = new MediaController(getContext());

        mStorageRef = FirebaseStorage.getInstance().getReference("videos");

        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.start();

        btnUpload.setVisibility(View.GONE);
        btnSelect.setVisibility(View.VISIBLE);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseVideo();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadVideo();
            }
        });

        return v;
    }

    private  void ChooseVideo(){
        requestMultiplePermissions();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            videoUri = data.getData();

            videoView.setVideoURI(videoUri);
            videoView.pause();
            background.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);

            linearUpload.setVisibility(View.VISIBLE);
            btnUpload.setVisibility(View.VISIBLE);
            btnSelect.setVisibility(View.GONE);
            linearSelect.setVisibility(View.GONE);

            Cursor returnCursor = getActivity().getContentResolver().query(videoUri, null, null, null, null);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();
            long a=returnCursor.getLong(sizeIndex);

            if((a/1024)/1024>30){
                Toast.makeText(getContext(),"Video Size is greather than 30 MB",Toast.LENGTH_SHORT).show();
                background.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.GONE);
                btnUpload.setVisibility(View.GONE);
                btnSelect.setVisibility(View.VISIBLE);
            }

        }}

    private String getFileExtension(Uri videoUri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(videoUri));
    }

    private void UploadVideo() {

        if (videoUri != null){
            StorageReference reference = mStorageRef.child(session.getusername());


            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading....!");
            progressDialog.show();
            progressDialog.setCancelable(false);
            reference.putFile(videoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        private Object ServerTimestamp;

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();

                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    String id= db.collection("Videos").document().getId();
                                    DocumentReference db1 = db.collection("Videos").document(id);
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("Address", "Active");
                                    data.put("Category", "Active");
                                    data.put("City", "Active");
                                    data.put("Dp", session.getpp());
                                    data.put("Likes", 0);
                                    data.put("Location", new GeoPoint(12.0,13.0));
                                    data.put("Name", session.getname());
                                    data.put("StoreName", session.getstorename());
                                    data.put("Pushid",id);
                                    data.put("Status", "Pending");
                                    data.put("Url", url);
                                    data.put("Userid", session.getusername());
                                    data.put("UserNumber", session.getnumber());
                                    data.put("Type", "Vendor");
                                    data.put("Views", 0);
                                    data.put("Created", ServerTimestamp);
                                    db1.set(data);


                                    for (int i = 0; i < subCategoryAdapter.getItemCount(); i++) {
                                        Sub subCategory = subcategory.get(i);
                                        if(!subCategory.Image.equals("New")) {
                                            String a = db1.collection("FoodItems").document().getId();
                                            Map<String, Object> user1 = new HashMap<>();
                                            user1.put("PushId", a);
                                            user1.put("Name", subCategory.Name);
                                            user1.put("ProductId", subCategory.PushId);
                                            user1.put("Image", subCategory.Image);
                                            db1.collection("FoodItems").document(a).set(user1);
                                        }
                                    }

                                    db.collection("Vendor").document(session.getusername())
                                            .collection("VideoTemp")
                                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    db.collection("Vendor")
                                                            .document(session.getusername())
                                                            .collection("VideoTemp")
                                                            .document(document.getId()).delete();
                                                }
                                                if(getActivity()!=null)
                                                    getActivity().onBackPressed();
                                            }
                                            else {
                                                if(getActivity()!=null)
                                                    getActivity().onBackPressed();
                                            }
                                        }
                                    });


                                }
                            });



                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NotNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage((int) progress + "%Uploaded");
                        }
                    });


        }else {
            Toast.makeText(getContext(),"No file selected",Toast.LENGTH_SHORT).show();
        }

    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {  // check if all permissions are granted

                            Intent intent = new Intent();
                            intent.setType("video/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, PICK_VIDEO_REQUEST);
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) { // check for permanent denial of any permission
                            // show alert dialog navigating to Settings
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
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
                        Toast.makeText(getContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    public void loadcategories(){

        subcategory.clear();
        subCategoryAdapter=new SubCategory(subcategory);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Vendor")
                .document(session.getusername())
                .collection("VideoTemp")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        if(value.isEmpty()){
                            subcategory.clear();
                            subcategory.add(new Sub("New","New","New"));
                            GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(),3);
                            recyclerView.setLayoutManager(mLayoutManager);
                            subCategoryAdapter = new SubCategory(subcategory);
                            recyclerView.setAdapter(subCategoryAdapter);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        else {
                            subcategory.clear();
                            for (QueryDocumentSnapshot document : value) {
                                if(document.contains("ItemName")&&document.contains("FoodImage"))
                                    subcategory.add(new Sub(document.get("ItemName").toString(),
                                            document.getId(),
                                            document.get("FoodImage").toString()));
                            }
                            subcategory.add(new Sub("New","New","New"));
                            GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(),3);
                            recyclerView.setLayoutManager(mLayoutManager);
                            subCategoryAdapter = new SubCategory(subcategory);
                            recyclerView.setAdapter(subCategoryAdapter);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }
}

