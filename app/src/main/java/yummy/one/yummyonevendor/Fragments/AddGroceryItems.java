package yummy.one.yummyonevendor.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.Product.Product;
import yummy.one.yummyonevendor.Product.ProductsAdapter;
import yummy.one.yummyonevendor.Quantity.Quantity;
import yummy.one.yummyonevendor.Quantity.QuantityAdapter;
import yummy.one.yummyonevendor.R;

import static android.app.Activity.RESULT_OK;

public class AddGroceryItems extends Fragment {

    public AddGroceryItems() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    LinearLayout back;
    TextView txtCategoryName,txtQty;
    EditText name,qty,mrp,price,details;
    RecyclerView r1;
    Button plus,submit;
    ImageView doc1;
    Session session;

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
    private String path = "No",itemcategory = "";

    private QuantityAdapter quantityAdapter;
    private ArrayList<Quantity> quantities = new ArrayList<Quantity>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_grocery_items, container, false);

        back = v.findViewById(R.id.back);
        txtCategoryName = v.findViewById(R.id.txtCategoryName);
        txtQty = v.findViewById(R.id.txtQty);
        name = v.findViewById(R.id.name);
        qty = v.findViewById(R.id.qty);
        mrp = v.findViewById(R.id.mrp);
        price = v.findViewById(R.id.price);
        details = v.findViewById(R.id.details);
        r1 = v.findViewById(R.id.r1);
        plus = v.findViewById(R.id.plus);
        submit = v.findViewById(R.id.submit);
        doc1 = v.findViewById(R.id.doc1);

        quantities.clear();
        quantityAdapter = new QuantityAdapter(quantities);

        if (getActivity() != null) {
            LinearLayout bottomnavigation = getActivity().findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.GONE);
        }

        session = new Session(getActivity());

        mstorageReference = FirebaseStorage.getInstance().getReference();

        submit.setVisibility(View.VISIBLE);
        itemcategory = getArguments().getString("category");

        txtCategoryName.setText("Add items to " + itemcategory);

        doc1.setOnClickListener(new View.OnClickListener() {
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null)
                    getActivity().onBackPressed();
            }
        });



        //Quantity Adding



        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                final EditText input = new EditText(getActivity());
                final EditText input1 = new EditText(getActivity());
                final EditText input2 = new EditText(getActivity());
                input.setSingleLine();
                input1.setSingleLine();
                input.setHint("Enter  Quantity");
                input1.setHint("Enter Mrp Price");
                input2.setHint("Enter Offer Price");
                input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                input1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                input2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                FrameLayout container1 = new FrameLayout(getActivity());
                FrameLayout container2 = new FrameLayout(getActivity());
                FrameLayout container3 = new FrameLayout(getActivity());
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.topMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                input.setLayoutParams(params);
                input1.setLayoutParams(params);
                input2.setLayoutParams(params);
                container1.addView(input);
                container2.addView(input1);
                container3.addView(input2);

                LinearLayout layout = new LinearLayout(getActivity());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(container1);
                layout.addView(container2);
                layout.addView(container3);

                alert.setTitle("Weights");
                alert.setView(layout);
                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (TextUtils.isEmpty(input.getText().toString())) {
                            input.setError("Enter Quantity");
                            input.requestFocus();
                            return;
                        }

                        if (TextUtils.isEmpty(input1.getText().toString())) {
                            input1.setError("Enter Mrp");
                            input1.requestFocus();
                            return;
                        }

                        if (TextUtils.isEmpty(input2.getText().toString())) {
                            input2.setError("Enter Offer Price");
                            input2.requestFocus();
                            return;
                        }

                        int temp = 0, position = 0;
                        for (int i = 0; i < quantityAdapter.getItemCount(); i++) {
                            Quantity quantity = quantities.get(i);
                            if (quantity.Qty.equals(input.getText().toString())) {
                                temp++;
                                position = i;
                            }
                        }

                        if (temp == 0)
                            quantities.add(new Quantity(input.getText().toString(), "\u20b9" + input1.getText().toString(),"\u20b9"+input2.getText().toString()));
                        else
                            quantities.set(position, new Quantity(input.getText().toString(), "\u20b9" + input1.getText().toString(),"\u20b9"+input2.getText().toString()));


                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                        r1.setLayoutManager(mLayoutManager);

                        quantityAdapter = new QuantityAdapter(quantities);

                        r1.setAdapter(quantityAdapter);
                        if (quantityAdapter.getItemCount() > 0) {
                            txtQty.setVisibility(View.VISIBLE);
                        }

                        input.setText("");
                        input1.setText("");
                        input2.setText("");

                        if (getActivity() != null) {
                            View v = getActivity().getCurrentFocus();
                            if (v != null) {
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                assert imm != null;
                                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            }
                        }

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

        price.setFilters(new InputFilter[]{new AddFood.DecimalDigitsInputFilter(10, 2)});
        mrp.setFilters(new InputFilter[]{new AddFood.DecimalDigitsInputFilter(10, 2)});


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(name.getText().toString())) {
                    name.setError("Enter Item Name");
                    name.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(qty.getText().toString())) {
                    qty.setError("Enter Quantity");
                    qty.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(mrp.getText().toString())) {
                    mrp.setError("Enter MRP");
                    mrp.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(price.getText().toString())) {
                    price.setError("Enter Offer Price");
                    price.requestFocus();
                    return;
                }



                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String id = db.collection("Vendor").document(session.getusername()).collection("Products").document().getId();
                DocumentReference db1 = db.collection("Vendor").document(session.getusername()).collection("Products").document(id);
                Map<String, Object> user = new HashMap<>();
                user.put("PushId", id);
                user.put("ItemName", name.getText().toString());
                user.put("ItemDescription", details.getText().toString());
                user.put("FoodImage", path);
                user.put("ItemCategory", itemcategory);
                user.put("Price", price.getText().toString());
                user.put("Mrp", mrp.getText().toString());
                user.put("Qty", qty.getText().toString());
                user.put("ApprovalStatus", "Pending");
                user.put("Status", "Active");


                String portionaarray[] = new String[quantityAdapter.getItemCount()];
                for (int i = 0; i < quantityAdapter.getItemCount(); i++) {
                    Quantity product = quantities.get(i);
                    String a = db1.collection("Addons").document().getId();
                    Map<String, Object> user1 = new HashMap<>();
                    user1.put("PushId", a);
                    user1.put("Qty", product.Qty);
                    user1.put("Mrp", product.Mrp.substring(1));
                    user1.put("OfferPrice", product.Price.substring(1));
                    db1.collection("Addons").document(a).set(user1);
                    portionaarray[i] = product.Qty + "," + product.Mrp.substring(1)+","+product.Price.substring(1);
                }


                user.put("Addons", Arrays.asList(portionaarray));
                db1.set(user);


                DocumentReference washingtonRef = db.collection("Vendor").document(session.getusername());
                washingtonRef.update(itemcategory, FieldValue.increment(1));

                if (getContext() != null) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT);
                    final EditText input = new EditText(getContext());
                    alert.setTitle("Congrats!");
                    alert.setMessage("Item Added Successfully!");
//                    alert.setView(input);
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                            if (getActivity() != null)
                                getActivity().onBackPressed();
                        }
                    });
                    alert.show();
                }

            }
        });




        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "MyPicture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
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
                System.out.println("Current time => " + c);
                StorageReference riversRef = mstorageReference.child("FoodItems/" + c + ".jpg");
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
                                storageRef.child("FoodItems/" + c + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        u[0] = uri.toString();
                                        path = u[0];
                                        RequestOptions requestOptions = new RequestOptions();
                                        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(20));
                                        if (getContext() != null)
                                            Glide.with(getContext()).load(path).apply(requestOptions).into(doc1);

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
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                progressDialog.setMessage((int) progress + "%Uploaded");
                            }
                        });

            } else {
                Toast.makeText(getContext(), "File Path Null", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {


            imageHoldUri = imageUri;


            if (imageHoldUri != null) {
                if (imageHoldUri != null) {
                    final Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);

                    StorageReference riversRef = mstorageReference.child("FoodItems/" + c + ".jpg");
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
                                    storageRef.child("FoodItems/" + c + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            u[0] = uri.toString();
                                            path = u[0];
                                            RequestOptions requestOptions = new RequestOptions();
                                            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(20));
                                            if (getContext() != null)
                                                Glide.with(getContext()).load(path).apply(requestOptions).into(doc1);
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
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                    progressDialog.setMessage((int) progress + "%Uploaded");
                                }
                            });
                }
            }
        }
    }

    public static class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }

    }

}