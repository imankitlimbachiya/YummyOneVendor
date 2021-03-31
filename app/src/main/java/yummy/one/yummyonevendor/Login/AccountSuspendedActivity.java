package yummy.one.yummyonevendor.Login;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import yummy.one.yummyonevendor.R;

public class AccountSuspendedActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ImageView imgBack;
    ProgressBar progressBar;
    Button btnContactSupportTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_suspended);

        mContext = this;

        ViewInitialization();

        ViewOnClick();
    }

    public void ViewInitialization() {
        progressBar = findViewById(R.id.progressBar);

        imgBack = findViewById(R.id.imgBack);

        btnContactSupportTeam = findViewById(R.id.btnContactSupportTeam);
    }

    public void ViewOnClick() {
        imgBack.setOnClickListener(this);
        btnContactSupportTeam.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnContactSupportTeam:
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
                break;
        }
    }
}