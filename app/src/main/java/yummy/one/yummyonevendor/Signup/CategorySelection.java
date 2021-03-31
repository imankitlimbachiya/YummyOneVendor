package yummy.one.yummyonevendor.SignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.R;

public class CategorySelection extends AppCompatActivity {

    Button imgC1, imgC2, imgC3, imgC4;
    Session session;
    String mobileNumber = "", status = "", userid = "", name = "", dob = "", category = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);

        imgC1 = findViewById(R.id.imgC1);
        imgC2 = findViewById(R.id.imgC2);
        imgC3 = findViewById(R.id.imgC3);
        imgC4 = findViewById(R.id.imgC4);
        session = new Session(getApplicationContext());

        if (getIntent().getStringExtra("mobilenumber") != null) {
            mobileNumber = getIntent().getStringExtra("mobilenumber");
        }

        if (getIntent().getStringExtra("status") != null) {
            status = getIntent().getStringExtra("status");
            name = getIntent().getStringExtra("name");
            dob = getIntent().getStringExtra("dob");
        }

        imgC1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> data = new HashMap<>();
                data.put("Category", "Restaurant");
                db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());
                session.setcategory("Restaurant");
                Intent intent = new Intent(CategorySelection.this, RegisterDetails.class);
                startActivity(intent);
            }
        });

        imgC2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> data = new HashMap<>();
                data.put("Category", "Homemade");
                db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());
                session.setcategory("Homemade");
                Intent intent = new Intent(CategorySelection.this, RegisterDetails.class);
                startActivity(intent);
            }
        });

        imgC3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> data = new HashMap<>();
                data.put("Category", "Fastfood");
                db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());
                session.setcategory("Fastfood");
                Intent intent = new Intent(CategorySelection.this, RegisterDetails.class);
                startActivity(intent);
//                Intent intent = new Intent(CategorySelection.this, OtpActivity.class);
//                intent.putExtra("status","notregistered");
//                intent.putExtra("id","");
//                intent.putExtra("mobilenumber",mobileNumber);
//                intent.putExtra("name",name);
//                intent.putExtra("dob",dob);
//                intent.putExtra("category","FastFood");
//                startActivity(intent);
            }
        });

        imgC4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> data = new HashMap<>();
                data.put("Category", "Grocery");
                db.collection("Vendor").document(session.getusername()).set(data, SetOptions.merge());
                session.setcategory("Grocery");
                Intent intent = new Intent(CategorySelection.this, RegisterDetails.class);
                startActivity(intent);
            }
        });

    }

    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}