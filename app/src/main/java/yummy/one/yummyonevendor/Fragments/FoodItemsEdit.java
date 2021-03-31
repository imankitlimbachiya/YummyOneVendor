package yummy.one.yummyonevendor.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
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

import cn.pedant.SweetAlert.SweetAlertDialog;
import yummy.one.yummyonevendor.Cuisines.Cuisines;
import yummy.one.yummyonevendor.Cuisines.CuisinesAdapter;
import yummy.one.yummyonevendor.FoodCategory.Category1;
import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.Login.OtpActivity;
import yummy.one.yummyonevendor.Product.Product;
import yummy.one.yummyonevendor.Product.ProductsAdapter;
import yummy.one.yummyonevendor.R;

import static android.app.Activity.RESULT_OK;

public class FoodItemsEdit extends Fragment {

    private EditText name, details, mrp, price, cuisine;
    private TextView stime, etime, txtCategoryName;
    private Spinner itemcategory;
    private RecyclerView r1, r2, r3;
    private TextView t1, t2, t3;
    private Button submit, plus, cusinesSave,delete;
    private ImageView image, add;
    private ProductsAdapter productsAdapter;
    private ProductsAdapter productsAdapter1;
    private ProductsAdapter productsAdapter2;
    private ArrayList<Product> products = new ArrayList<Product>();
    private ArrayList<Product> products1 = new ArrayList<Product>();
    private ArrayList<Product> products2 = new ArrayList<Product>();
    private ArrayList<String> itemcategory1 = new ArrayList<String>();
    private String a;
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetDialog bottomSheetDialog1;
    private Session session;
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
    private ProgressBar progressBar2;
    private String path = "No", cuisines1 = "";
    private RadioGroup radioGroup;
    private RadioButton veg, nonveg, vegan, egg;

    private String pushid = "";

    private int mHour;
    private int mMinute;
    double gtot = 0;

    private ArrayList<Cuisines> cuisinename = new ArrayList<Cuisines>();
    private ArrayList<Cuisines> selectedcusinenames = new ArrayList<Cuisines>();
    CuisinesAdapter cuisinesAdapter;

    private Category1 category1000;


    public FoodItemsEdit() {
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
        View v = inflater.inflate(R.layout.fragment_food_items_edit, container, false);


        if (getActivity() != null) {
            LinearLayout bottomnavigation = getActivity().findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.GONE);
        }


        name = v.findViewById(R.id.name);
        details = v.findViewById(R.id.details);
        mrp = v.findViewById(R.id.mrp);
        price = v.findViewById(R.id.price);
        image = v.findViewById(R.id.doc1);
        stime = v.findViewById(R.id.stime);
        etime = v.findViewById(R.id.etime);
        plus = v.findViewById(R.id.plus);
        txtCategoryName = v.findViewById(R.id.txtCategoryName);
        itemcategory = v.findViewById(R.id.itemcategory);
        r1 = v.findViewById(R.id.r1);
        r2 = v.findViewById(R.id.r2);
        r3 = v.findViewById(R.id.r3);
        t1 = v.findViewById(R.id.t1);
        t2 = v.findViewById(R.id.t2);
        t3 = v.findViewById(R.id.t3);
        submit = v.findViewById(R.id.submit);
        delete = v.findViewById(R.id.delete);
        add = v.findViewById(R.id.add);
        radioGroup = v.findViewById(R.id.radioGroup);
        veg = v.findViewById(R.id.veg);
        nonveg = v.findViewById(R.id.nonveg);
        cuisine = v.findViewById(R.id.cuisine);
        vegan = v.findViewById(R.id.vegan);
        egg = v.findViewById(R.id.egg);

        session = new Session(getActivity());

        if (getActivity() != null) {
            LinearLayout bottomnavigation = (getActivity()).findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.GONE);
        }

        mstorageReference = FirebaseStorage.getInstance().getReference();

        submit.setVisibility(View.VISIBLE);

        String aitemcategory = getArguments().getString("category");

        txtCategoryName.setText("Edit items to " + aitemcategory);

        t1.setVisibility(View.GONE);
        t2.setVisibility(View.GONE);
        t3.setVisibility(View.GONE);
        products.clear();
        products1.clear();
        products2.clear();

        cuisinename.clear();

        cuisinesAdapter = new CuisinesAdapter(cuisinename);

        image.setOnClickListener(new View.OnClickListener() {
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

        LinearLayout back = v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null)
                    getActivity().onBackPressed();
            }
        });

        veg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    veg.setChecked(true);
                    nonveg.setChecked(false);
                    vegan.setChecked(false);
                    egg.setChecked(false);
                }
            }
        });

        nonveg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    nonveg.setChecked(true);
                    veg.setChecked(false);
                    vegan.setChecked(false);
                    egg.setChecked(false);
                }
            }
        });

        egg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    egg.setChecked(true);
                    nonveg.setChecked(false);
                    vegan.setChecked(false);
                    veg.setChecked(false);
                }
            }
        });

        vegan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    vegan.setChecked(true);
                    nonveg.setChecked(false);
                    veg.setChecked(false);
                    egg.setChecked(false);
                }
            }
        });

        stime.setOnClickListener(new View.OnClickListener() {
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
                                String h = "", m = "";
                                if (mHour >= 0 && mHour <= 9)
                                    h = "0" + hourOfDay;
                                else
                                    h = "" + hourOfDay;
                                if (mMinute >= 0 && mMinute <= 9)
                                    m = "0" + minute;
                                else
                                    m = "" + minute;
                                stime.setText(h + ":" + m);
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });

        etime.setOnClickListener(new View.OnClickListener() {
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
                                String h = "", m = "";
                                if (mHour >= 0 && mHour <= 9)
                                    h = "0" + hourOfDay;
                                else
                                    h = "" + hourOfDay;
                                if (mMinute >= 0 && mMinute <= 9)
                                    m = "0" + minute;
                                else
                                    m = "" + minute;
                                etime.setText(h + ":" + m);
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });

        if (!TextUtils.isEmpty(session.gettemp())) {
            name.setText(session.gettemp());
        }

        if (getArguments() != null) {
            pushid = getArguments().getString("pushid");
        }

        if (!TextUtils.isEmpty(pushid)) {

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("Vendor").document(session.getusername());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String temp = documentSnapshot.get("ItemCategory").toString();
                        a = temp;
                        itemcategory1 = new ArrayList<String>(Arrays.asList(temp.split(",")));
                        if (getContext() != null) {
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner, itemcategory1);
                            itemcategory.setAdapter(adapter);
                        }
                    } else {
                        itemcategory1.clear();
                        itemcategory1.add("Select");
                        if (getContext() != null) {
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner, itemcategory1);
                            itemcategory.setAdapter(adapter);
                        }
                    }

                    DocumentReference docRef1 = db.collection("Vendor").document(session.getusername()).collection("Products").document(pushid);
                    docRef1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                name.setText(documentSnapshot.get("ItemName").toString());
                                details.setText(documentSnapshot.get("ItemDescription").toString());
                                if (documentSnapshot.contains("SellingPrice"))
                                    price.setText(documentSnapshot.get("SellingPrice").toString());
                                price.setText(documentSnapshot.get("MarkettingPrice").toString());
                                stime.setText(documentSnapshot.get("STime").toString());
                                etime.setText(documentSnapshot.get("ETime").toString());
                                path = documentSnapshot.get("FoodImage").toString();
                                if (documentSnapshot.contains("Cuisines")) {
                                    String a[] = documentSnapshot.get("Cuisines").toString().split(",");
                                    try {
                                        cuisines1 = "";
                                        String b = "";
                                        for (int i = 0; i < a.length; i++) {
                                            b += "\u2022" + a[i] + " ";
                                            cuisines1 += a[i] + ",";
                                        }
                                        cuisine.setText(b);
                                        bottomSheetDialog1.dismiss();
                                    } catch (Exception e) {
                                        cuisine.setText("");
                                        bottomSheetDialog1.dismiss();
                                    }
                                }
                                RequestOptions requestOptions = new RequestOptions();
                                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(20));
                                if (!path.equalsIgnoreCase("No")) {
                                    Glide.with(getActivity())
                                            .load(path)
                                            .apply(requestOptions)
                                            .into(image);
                                }

                                ArrayList<String> category2 = new ArrayList<String>(Arrays.asList(a.split(",")));

                                if (category2.indexOf(documentSnapshot.get("ItemCategory").toString()) > -1)
                                    itemcategory.setSelection(category2.indexOf(documentSnapshot.get("ItemCategory").toString()));

                                if (documentSnapshot.get("FoodType").toString().equalsIgnoreCase("Veg")) {
                                    veg.setChecked(true);
                                } else if (documentSnapshot.get("FoodType").toString().equalsIgnoreCase("Non-Veg")) {
                                    nonveg.setChecked(true);
                                } else if (documentSnapshot.get("FoodType").toString().equalsIgnoreCase("Vegan")) {
                                    vegan.setChecked(true);
                                } else if (documentSnapshot.get("FoodType").toString().equalsIgnoreCase("Egg")) {
                                    egg.setChecked(true);
                                }


                                db.collection("Vendor").document(session.getusername()).collection("Products").document(pushid).collection("Addons").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                products2.add(new Product((document.get("Name").toString()), "\u20b9" + document.get("Price").toString()));
                                            }
                                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                            r3.setLayoutManager(mLayoutManager);
                                            productsAdapter2 = new ProductsAdapter(products2);
                                            r3.setAdapter(productsAdapter2);
                                            if (productsAdapter2.getItemCount() > 0) {
                                                t3.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }
                                });


                                db.collection("Vendor").document(session.getusername()).collection("Products").document(pushid).collection("Portions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                products1.add(new Product((document.get("Name").toString()), "\u20b9" + document.get("Price").toString()));
                                            }
                                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                            r2.setLayoutManager(mLayoutManager);
                                            productsAdapter1 = new ProductsAdapter(products1);
                                            r2.setAdapter(productsAdapter1);
                                            if (productsAdapter1.getItemCount() > 0) {
                                                t2.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }

        itemcategory1.add("Select");
        if (getContext() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner, itemcategory1);
            itemcategory.setAdapter(adapter);
        }

        bottomSheetDialog1 = new BottomSheetDialog(getContext());
        final View bottomSheetDialogView1 = getLayoutInflater().inflate(R.layout.bottom_cuisines, null);
        bottomSheetDialog1.setContentView(bottomSheetDialogView1);
        r1 = bottomSheetDialogView1.findViewById(R.id.r1);
        cusinesSave = bottomSheetDialogView1.findViewById(R.id.add);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference docRef1 = db.collection("Cuisines");
        docRef1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    cuisinename.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        cuisinename.add(new Cuisines(
                                document.get("Name").toString(),
                                document.getId()
                        ));
                    }
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    r1.setLayoutManager(mLayoutManager);
                    cuisinesAdapter = new CuisinesAdapter(cuisinename);
                    r1.setAdapter(cuisinesAdapter);
                }
            }
        });

        cuisine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog1.show();
            }
        });

        cusinesSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedcusinenames = cuisinesAdapter.listofselectedactivites();
                String a = "";
                cuisines1 = "";
                for (int i = 0; i < selectedcusinenames.size(); i++) {
                    Cuisines cuisines = selectedcusinenames.get(i);
                    a += "\u2022" + cuisines.Name + " ";
                    cuisines1 += cuisines.Name + ",";
                }
                cuisine.setText(a);
                bottomSheetDialog1.dismiss();
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog1;
                bottomSheetDialog1 = new BottomSheetDialog(getContext());
                final View bottomSheetDialogView = getActivity().getLayoutInflater().inflate(R.layout.bottom_subcategory, null);
                bottomSheetDialog1.setContentView(bottomSheetDialogView);
                RecyclerView recyclerView = bottomSheetDialogView.findViewById(R.id.recyclerview);
                ImageView submit = bottomSheetDialogView.findViewById(R.id.submit);
                EditText categoryname = bottomSheetDialogView.findViewById(R.id.category);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("Vendor").document(session.getusername());
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String temp = documentSnapshot.get("ItemCategory").toString();
                            a = temp;
                            ArrayList<String> category1 = new ArrayList<String>(Arrays.asList(temp.split(",")));
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            category1000 = new Category1(category1);
                            recyclerView.setAdapter(category1000);
                        }
                    }
                });


                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(categoryname.getText().toString())) {
                            categoryname.setError("Enter Category");
                            categoryname.requestFocus();
                            return;
                        }
                        a += categoryname.getText().toString() + ",";
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String, Object> data = new HashMap<>();
                        data.put("ItemCategory", a);
                        db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());
                        categoryname.setText("");
                        DocumentReference docRef = db.collection("Vendor").document(session.getusername());
                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    String temp = documentSnapshot.get("ItemCategory").toString();
                                    a = temp;
                                    a = temp;
                                    ArrayList<String> category1 = new ArrayList<String>(Arrays.asList(temp.split(",")));
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    category1000 = new Category1(category1);
                                    recyclerView.setAdapter(category1000);
                                }
                            }
                        });
                    }
                });
                bottomSheetDialog1.show();
            }
        });

        productsAdapter = new ProductsAdapter(products);
        productsAdapter1 = new ProductsAdapter(products1);
        productsAdapter2 = new ProductsAdapter(products2);
        bottomSheetDialog = new BottomSheetDialog(getContext());

        final View bottomSheetDialogView = getLayoutInflater().inflate(R.layout.bottom_portions, null);
        bottomSheetDialog.setContentView(bottomSheetDialogView);

        EditText addons = bottomSheetDialogView.findViewById(R.id.addons);
        TextView label = bottomSheetDialogView.findViewById(R.id.label);
        EditText payment = bottomSheetDialogView.findViewById(R.id.payment);
        Button add = bottomSheetDialogView.findViewById(R.id.add);
        Spinner type = bottomSheetDialogView.findViewById(R.id.type);
        ArrayList<String> type1 = new ArrayList<String>();
        type1.add("Portions");
        type1.add("Addons");

        if (getContext() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner, type1);
            type.setAdapter(adapter);
        }

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    label.setText("Portion");
                } else {
                    label.setText("Add On");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(addons.getText().toString())) {
                    addons.setError("Enter Quantity");
                    addons.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(payment.getText().toString())) {
                    payment.setError("Enter Price");
                    payment.requestFocus();
                    return;
                }

                if (label.getText().toString().equalsIgnoreCase("portion")) {
                    int temp = 0, position = 0;
                    for (int i = 0; i < productsAdapter1.getItemCount(); i++) {
                        Product product = products1.get(i);
                        if (product.Name.equals(addons.getText().toString())) {
                            temp++;
                            position = i;
                        }
                    }

                    if (temp == 0)
                        products1.add(new Product(addons.getText().toString(), "\u20b9" + payment.getText().toString()));
                    else
                        products1.set(position, new Product(addons.getText().toString(), "\u20b9" + payment.getText().toString()));


                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    r2.setLayoutManager(mLayoutManager);

                    productsAdapter1 = new ProductsAdapter(products1);

                    r2.setAdapter(productsAdapter1);
                    if (productsAdapter1.getItemCount() > 0) {
                        t2.setVisibility(View.VISIBLE);
                    }

                    addons.setText("");
                    payment.setText("");

                    if (getActivity() != null) {
                        View v = getActivity().getCurrentFocus();
                        if (v != null) {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            assert imm != null;
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }

                    bottomSheetDialog.dismiss();
                } else {
                    int temp = 0, position = 0;
                    for (int i = 0; i < productsAdapter2.getItemCount(); i++) {
                        Product product = products2.get(i);
                        if (product.Name.equals(addons.getText().toString())) {
                            temp++;
                            position = i;
                        }
                    }

                    if (temp == 0)
                        products2.add(new Product(addons.getText().toString(), "\u20b9" + payment.getText().toString()));
                    else
                        products2.set(position, new Product(addons.getText().toString(), "\u20b9" + payment.getText().toString()));


                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    r3.setLayoutManager(mLayoutManager);

                    productsAdapter2 = new ProductsAdapter(products2);

                    r3.setAdapter(productsAdapter2);

                    if (productsAdapter2.getItemCount() > 0) {
                        t3.setVisibility(View.VISIBLE);
                    }
                    addons.setText("");
                    payment.setText("");

                    if (getActivity() != null) {
                        View v = getActivity().getCurrentFocus();
                        if (v != null) {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            assert imm != null;
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }

                    bottomSheetDialog.dismiss();
                }
            }
        });


        final CharSequence[] items = {"Add Portions", "Add Addons",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("Add Item Customisation!");

        //SET ITEMS AND THERE LISTENERS
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Add Portions")) {
                    //Adding Portions
                    final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                    final EditText input = new EditText(getActivity());
                    final EditText input1 = new EditText(getActivity());
                    input.setSingleLine();
                    input1.setSingleLine();
                    input.setHint("Enter Portion Quantity");
                    input1.setHint("Enter Portion Price");
                    input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                    input1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    FrameLayout container1 = new FrameLayout(getActivity());
                    FrameLayout container2 = new FrameLayout(getActivity());
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                    params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                    params.topMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                    params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                    input.setLayoutParams(params);
                    input1.setLayoutParams(params);
                    container1.addView(input);
                    container2.addView(input1);

                    LinearLayout layout = new LinearLayout(getActivity());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.addView(container1);
                    layout.addView(container2);

                    alert.setTitle("Portions");
                    alert.setView(layout);
                    alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (TextUtils.isEmpty(input.getText().toString())) {
                                input.setError("Enter Quantity");
                                input.requestFocus();
                                return;
                            }

                            if (TextUtils.isEmpty(input1.getText().toString())) {
                                input1.setError("Enter Price");
                                input1.requestFocus();
                                return;
                            }

                            int temp = 0, position = 0;
                            for (int i = 0; i < productsAdapter1.getItemCount(); i++) {
                                Product product = products1.get(i);
                                if (product.Name.equals(input.getText().toString())) {
                                    temp++;
                                    position = i;
                                }
                            }

                            if (temp == 0)
                                products1.add(new Product(input.getText().toString(), "\u20b9" + input1.getText().toString()));
                            else
                                products1.set(position, new Product(input.getText().toString(), "\u20b9" + input1.getText().toString()));


                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            r2.setLayoutManager(mLayoutManager);

                            productsAdapter1 = new ProductsAdapter(products1);

                            r2.setAdapter(productsAdapter1);
                            if (productsAdapter1.getItemCount() > 0) {
                                t2.setVisibility(View.VISIBLE);
                            }

                            input.setText("");
                            input1.setText("");

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
                } else if (items[item].equals("Add Addons")) {
                    //Adding Addons
                    final AlertDialog.Builder alert1 = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                    final EditText input2 = new EditText(getActivity());
                    final EditText input3 = new EditText(getActivity());
                    input2.setSingleLine();
                    input3.setSingleLine();
                    input2.setHint("Enter Addons Quantity");
                    input3.setHint("Enter Addons Price");
                    input2.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                    input3.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    FrameLayout container3 = new FrameLayout(getActivity());
                    FrameLayout container4 = new FrameLayout(getActivity());
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                    params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                    params.topMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                    params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                    input2.setLayoutParams(params);
                    input3.setLayoutParams(params);
                    container3.addView(input2);
                    container4.addView(input3);
                    LinearLayout layout1 = new LinearLayout(getActivity());
                    layout1.setOrientation(LinearLayout.VERTICAL);
                    layout1.addView(container3);
                    layout1.addView(container4);
                    alert1.setTitle("Addons");
                    alert1.setView(layout1);
                    alert1.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (TextUtils.isEmpty(input2.getText().toString())) {
                                input2.setError("Enter Quantity");
                                input2.requestFocus();
                                return;
                            }

                            if (TextUtils.isEmpty(input3.getText().toString())) {
                                input3.setError("Enter Price");
                                input3.requestFocus();
                                return;
                            }

                            int temp = 0, position = 0;
                            for (int i = 0; i < productsAdapter2.getItemCount(); i++) {
                                Product product = products2.get(i);
                                if (product.Name.equals(input2.getText().toString())) {
                                    temp++;
                                    position = i;
                                }
                            }

                            if (temp == 0)
                                products2.add(new Product(input2.getText().toString(), "\u20b9" + input3.getText().toString()));
                            else
                                products2.set(position, new Product(input2.getText().toString(), "\u20b9" + input3.getText().toString()));

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            r3.setLayoutManager(mLayoutManager);
                            productsAdapter2 = new ProductsAdapter(products2);
                            r3.setAdapter(productsAdapter2);

                            if (productsAdapter2.getItemCount() > 0) {
                                t3.setVisibility(View.VISIBLE);
                            }
                            input2.setText("");
                            input3.setText("");

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
                    alert1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    alert1.show();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.show();
            }
        });

        payment.setFilters(new InputFilter[]{new AddFood.DecimalDigitsInputFilter(10, 2)});
        price.setFilters(new InputFilter[]{new AddFood.DecimalDigitsInputFilter(10, 2)});
        mrp.setFilters(new InputFilter[]{new AddFood.DecimalDigitsInputFilter(10, 2)});


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CharSequence[] items = {"Yes", "No"};
                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT);
                final EditText input = new EditText(getContext());
                alert.setTitle("Are you sure");
                alert.setMessage("You want to delte the food item");
                // alert.setView(input);
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        FirebaseFirestore.getInstance().collection("Vendor").document(session.getusername()).collection("Products").document(pushid).delete();
                        if(getActivity()!=null)
                            getActivity().onBackPressed();
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                alert.show();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(name.getText().toString())) {
                    name.setError("Enter Item Name");
                    name.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(price.getText().toString())) {
                    price.setError("Enter Price");
                    price.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(details.getText().toString())) {
                    details.setError("Enter Item Details");
                    details.requestFocus();
                    return;
                }

                int count = 0;
                String selection = "";
                if (veg.isChecked()) {
                    count++;
                    selection = "Veg";
                }
                if (nonveg.isChecked()) {
                    count++;
                    selection = "NonVeg";
                }
                if (egg.isChecked()) {
                    count++;
                    selection = "Egg";
                }
                if (vegan.isChecked()) {
                    count++;
                    selection = "Vegan";
                }

                if (stime.getText().toString().equals("HH:MM")) {
                    Toast.makeText(getContext(), "Select Start Time of Item Availablity", Toast.LENGTH_LONG).show();
                    return;
                }

                if (etime.getText().toString().equals("HH:MM")) {
                    Toast.makeText(getContext(), "Select End Time of Item Availablity", Toast.LENGTH_LONG).show();
                    return;
                }


                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference db1 = db.collection("Vendor").document(session.getusername()).collection("Products").document(pushid);
                Map<String, Object> user = new HashMap<>();
                user.put("PushId", pushid);
                user.put("ItemName", name.getText().toString());
                user.put("ItemDescription", details.getText().toString());
                user.put("FoodType", selection);
                user.put("ItemCategory", itemcategory.getSelectedItem().toString());
                user.put("SellingPrice", price.getText().toString());
                user.put("MarkettingPrice", price.getText().toString());
                user.put("Cuisines", cuisines1);
                user.put("STime", stime.getText().toString());
                user.put("ETime", etime.getText().toString());
                user.put("ApprovalStatus", "Pending");
                user.put("Status", "Active");
                user.put("FoodImage", path);

                CollectionReference docRef1 = db.collection("Vendor")
                        .document(session.getusername())
                        .collection("Products");
                docRef1.document(pushid).collection("Portions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("Vendor")
                                        .document(session.getusername())
                                        .collection("Products")
                                        .document(pushid)
                                        .collection("Portions")
                                        .document(document.getId()).delete();
                            }
                        }
                    }
                });

                docRef1.document(pushid).collection("Addons").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("Vendor")
                                        .document(session.getusername())
                                        .collection("Products")
                                        .document(pushid)
                                        .collection("Addons")
                                        .document(document.getId()).delete();
                            }
                        }

                        String portionaarray[] = new String[productsAdapter1.getItemCount()];
                        String addonsarray[] = new String[productsAdapter2.getItemCount()];

                        for (int i = 0; i < productsAdapter1.getItemCount(); i++) {
                            Product product = products1.get(i);
                            String a = db1.collection("Portions").document().getId();
                            Map<String, Object> user1 = new HashMap<>();
                            user1.put("PushId", a);
                            user1.put("Name", product.PushId);
                            user1.put("Price", product.Name.substring(1));
                            db1.collection("Portions").document(a).set(user1);
                            portionaarray[i] = product.PushId + "," + product.Name.substring(1);
                        }

                        for (int i = 0; i < productsAdapter2.getItemCount(); i++) {
                            Product product = products2.get(i);
                            String a = db1.collection("Addons").document().getId();
                            Map<String, Object> user1 = new HashMap<>();
                            user1.put("PushId", a);
                            user1.put("Name", product.PushId);
                            user1.put("Price", product.Name.substring(1));
                            db1.collection("Addons").document(a).set(user1);
                            addonsarray[i] = product.PushId + "," + product.Name.substring(1);
                        }

                        user.put("Addons", Arrays.asList(addonsarray));
                        user.put("Portions", Arrays.asList(portionaarray));
                        db1.set(user);

                        DocumentReference db2 = db.collection("FoodApprovals").document(session.getusername());
                        Map<String, Object> user1 = new HashMap<>();
                        user1.put("Category", itemcategory.getSelectedItem().toString());
                        user1.put("Email", session.getemail());
                        user1.put("Number", session.getnumber());
                        user1.put("RestarauntName", session.getname());
                        db2.set(user1);

                        if (getContext() != null) {
                            final CharSequence[] items = {"Contact Support", "Cancel"};
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

                        session.settemp("");
                        if (getActivity() != null)
                            getActivity().onBackPressed();
                    }
                });


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
                                            Glide.with(getContext()).load(path).apply(requestOptions).into(image);

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
                                                Glide.with(getContext()).load(path).apply(requestOptions).into(image);
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

    public class DecimalDigitsInputFilter implements InputFilter {

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
