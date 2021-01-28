package yummy.one.yummyonevendor.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
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

import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
import com.zcw.togglebutton.ToggleButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yummy.one.yummyonevendor.FoodCategory.Sub;
import yummy.one.yummyonevendor.FoodCategory.SubCategory;
import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.Login.Login;
import yummy.one.yummyonevendor.MainActivity;
import yummy.one.yummyonevendor.R;
import yummy.one.yummyonevendor.Signup.CategorySelection;
import yummy.one.yummyonevendor.Signup.RegisterDetails;
import yummy.one.yummyonevendor.Signup.Signup;
import yummy.one.yummyonevendor.Videos.Video;
import yummy.one.yummyonevendor.Videos.VideosAdapter;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private TextView txtVendorname, txtName;
    private ImageView imgProfilepic;
    private Session session;
    private LinearLayout linearRow1, linearRow2, linearRow3, linearRow4, linearRow5, linearRow7, linearBlock1, linearBlock2, linearBlock3, linearBlock4, linearLive, linearUpload, linearadd;
    private TextView txtVlogs, txtAccountdetails;
    private View viewLine1, viewLine2;
    private Button btnAdd;
    private RecyclerView recyclerView;
    private ArrayList<Video> video = new ArrayList<Video>();
    private VideosAdapter videosAdapter;

    Uri imageUri;
    Uri imageHoldUri = null;
    private static final int REQUEST_CAMERA = 3;
    private static final int REQUEST_CODE = 5;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int REQUEST_CAMERA_ACCESS_PERMISSION = 5674;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private static final int SELECT_FILE = 2;
    private final int RESULT_CROP = 400;
    private StorageReference mstorageReference;
    private ProgressBar progressBar2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        session = new Session(getActivity());
        txtVendorname = v.findViewById(R.id.txtVendorname);
        txtName = v.findViewById(R.id.txtName);
        imgProfilepic = v.findViewById(R.id.imgProfilepic);
        linearRow1 = v.findViewById(R.id.linearRow1);
        linearRow2 = v.findViewById(R.id.linearRow2);
        linearRow3 = v.findViewById(R.id.linearRow3);
        linearRow4 = v.findViewById(R.id.linearRow4);
        linearRow5 = v.findViewById(R.id.linearRow5);
        linearRow7 = v.findViewById(R.id.linearRow7);
        linearBlock1 = v.findViewById(R.id.linearBlock1);
        linearBlock2 = v.findViewById(R.id.linearBlock2);
        linearBlock3 = v.findViewById(R.id.linearBlock3);
        linearBlock4 = v.findViewById(R.id.linearBlock4);
        txtVlogs = v.findViewById(R.id.txtVlogs);
        txtAccountdetails = v.findViewById(R.id.txtAccountdetails);
        viewLine1 = v.findViewById(R.id.viewLine1);
        viewLine2 = v.findViewById(R.id.viewLine2);
        btnAdd = v.findViewById(R.id.btnAdd);
        linearLive = v.findViewById(R.id.linearLive);
        linearUpload = v.findViewById(R.id.linearUpload);
        linearadd = v.findViewById(R.id.linearadd);

        //Video Recylcer View
        recyclerView = v.findViewById(R.id.recyclerView);
        video.clear();
        videosAdapter = new VideosAdapter(video);

        getVideos();

        if (getActivity() != null) {
            LinearLayout bottomnavigation = getActivity().findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.VISIBLE);
        }

        if (session.getcategory().equals("Homemade")) {
            linearadd.setVisibility(View.VISIBLE);
        } else {
            linearadd.setVisibility(View.GONE);
        }

        txtAccountdetails.setTextColor(getResources().getColor(R.color.colorPrimary));
        txtVlogs.setTextColor(getResources().getColor(R.color.colorDarkgrey));
        viewLine1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        viewLine1.setVisibility(View.VISIBLE);
        viewLine2.setVisibility(View.INVISIBLE);
        linearBlock3.setVisibility(View.VISIBLE);
        linearBlock4.setVisibility(View.GONE);

        linearBlock1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtAccountdetails.setTextColor(getResources().getColor(R.color.colorPrimary));
                txtVlogs.setTextColor(getResources().getColor(R.color.colorDarkgrey));
                viewLine1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                viewLine1.setVisibility(View.VISIBLE);
                viewLine2.setVisibility(View.INVISIBLE);
                linearBlock3.setVisibility(View.VISIBLE);
                linearBlock4.setVisibility(View.GONE);
            }
        });

        linearBlock2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtVlogs.setTextColor(getResources().getColor(R.color.colorPrimary));
                txtAccountdetails.setTextColor(getResources().getColor(R.color.colorDarkgrey));
                viewLine2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                viewLine1.setVisibility(View.INVISIBLE);
                viewLine2.setVisibility(View.VISIBLE);
                linearBlock3.setVisibility(View.GONE);
                linearBlock4.setVisibility(View.VISIBLE);
            }
        });

        mstorageReference = FirebaseStorage.getInstance().getReference();

        linearUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    Fragment fragment = new AddVlog();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commit();
                }
            }
        });

        linearLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        if (TextUtils.isEmpty(session.getpp())) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("Vendor").document(session.getusername());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        if (documentSnapshot.contains("PP")) {
                            if (!TextUtils.isEmpty(documentSnapshot.get("PP").toString())) {
                                session.setpp(documentSnapshot.get("PP").toString());
                                Glide.with(getActivity()).load(session.getpp()).into(imgProfilepic);
                            }
                        }
                    }
                }
            });
        } else {
            Glide.with(getActivity()).load(session.getpp()).into(imgProfilepic);
        }


        imgProfilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = {"Take Photo", "Choose from Library",
                        "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT);
                builder.setTitle("Add Photo!");

                //SET ITEMS AND THERE LISTENERS
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Take Photo")) {
                            if (getContext() != null) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                        && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                                            REQUEST_CAMERA_ACCESS_PERMISSION);
                                } else {
                                    cameraIntent();
                                }
                            }
                        } else if (items[item].equals("Choose from Library")) {
                            galleryIntent();
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        txtVendorname.setText(session.getname());
        txtName.setText(session.getvendorname());

        linearRow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    Fragment fragment = new AccountDetails();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commit();
                }
            }
        });

        linearRow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    Fragment fragment = new PaymentHistory();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commit();
                }
            }
        });

        linearRow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    Fragment fragment = new PastOrders();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commit();
                }
            }
        });

        linearRow4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    Fragment fragment = new ReviewRatingsFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commit();
                }
            }
        });

        linearRow5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    Fragment fragment = new CustomerSupport();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commit();
                }
            }
        });

        linearRow7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.setvendorname("");
                session.setname("");
                session.setapprovalstatus("");
                session.setnumber("");
                session.setcategory("");
                session.setusername("");
                session.setaddress("");
                session.setstate("");
                session.setcity("");
                session.setpincode("");
                session.setloc("");
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });

        return v;
    }

    public void getVideos() {
        video.clear();
        videosAdapter = new VideosAdapter(video);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Videos")
                .whereEqualTo("Userid", session.getusername())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        if (value.isEmpty()) {

                        } else {
                            video.clear();
                            for (QueryDocumentSnapshot document : value) {
                                if (document.contains("Url") && document.contains("Name") && document.contains("StoreName") && document.contains("Userid"))
                                    video.add(new Video(document.get("Url").toString(),
                                            document.get("Name").toString(),
                                            document.get("StoreName").toString(),
                                            document.get("Userid").toString(),
                                            ""));
                                Toast.makeText(getContext(), "Entered", Toast.LENGTH_LONG);
                            }
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                            videosAdapter = new VideosAdapter(video);
                            recyclerView.setAdapter(videosAdapter);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void cameraIntent() {
        requestMultiplePermissions();
    }


    private void galleryIntent() {
        //CHOOSE IMAGE FROM GALLERY
//        Log.d("gola", "entered here");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //SAVE URI FROM GALLERY
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imageHoldUri = data.getData();
            if (imageHoldUri != null) {
                final Date c = Calendar.getInstance().getTime();

                StorageReference riversRef = mstorageReference.child("Profile/" + session.getusername() + ".jpg");
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
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
                                storageRef.child("Profile/" + session.getusername() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        u[0] = uri.toString();
                                        if (getActivity() != null)
                                            Glide.with(getActivity()).load(u[0]).into(imgProfilepic);
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        Map<String, Object> data = new HashMap<>();
                                        data.put("PP", u[0]);
                                        db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());
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
                                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NotNull UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                progressDialog.setMessage((int) progress + "%Uploaded");
                            }
                        });

            } else {
                Toast.makeText(getContext(), "File Path Null", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            imageHoldUri = imageUri;
            Toast.makeText(getContext(), "" + imageHoldUri, Toast.LENGTH_LONG).show();
            if (imageHoldUri != null) {
                final Date c = Calendar.getInstance().getTime();
                StorageReference riversRef = mstorageReference.child("Profile/" + session.getusername() + ".jpg");
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
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

                                storageRef.child("Profile/" + session.getusername() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        u[0] = uri.toString();
                                        if (getActivity() != null)
                                            Glide.with(getActivity()).load(u[0]).into(imgProfilepic);
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        Map<String, Object> data = new HashMap<>();
                                        data.put("PP", u[0]);
                                        db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());


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
                                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NotNull UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                progressDialog.setMessage((int) progress + "%Uploaded");
                            }
                        });
            }
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

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "MyPicture");
                            values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
                            imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, CAMERA_REQUEST_CODE);
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

}
