package yummy.one.yummyonevendor.Splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import java.security.Permissions;

import yummy.one.yummyonevendor.Functionality.Session;
import yummy.one.yummyonevendor.Login.Login;
import yummy.one.yummyonevendor.MainActivity;
import yummy.one.yummyonevendor.PermissionActivity;
import yummy.one.yummyonevendor.R;
import yummy.one.yummyonevendor.Signup.CategorySelection;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG ="LOGIN DATA" ;
    protected boolean _active = true;
    protected int _splashTime = 3000;
    private Session session;

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(getApplicationContext());
        setContentView(R.layout.activity_splash_screen);

        session = new Session(SplashScreen.this);
        final Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(100);
                        if (_active) {
                            waited += 100;
                        }
                    }
                } catch (Exception e) {

                } finally {

                    if (TextUtils.isEmpty(session.getisfirsttime())) {
                        FirebaseDynamicLinks.getInstance()
                                .getDynamicLink(getIntent())
                                .addOnSuccessListener(SplashScreen.this, new OnSuccessListener<PendingDynamicLinkData>() {
                                    @Override
                                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                                        // Get deep link from result (may be null if no link is found)
                                        Uri deepLink = null;
                                        if (pendingDynamicLinkData != null) {
                                            deepLink = pendingDynamicLinkData.getLink();
                                        }

                                        if (deepLink != null
                                                && deepLink.getBooleanQueryParameter("invitedby", false)) {
                                            String referrerUid = deepLink.getQueryParameter("invitedby");
                                            session.setreferral(referrerUid);

                                            startActivity(new Intent(SplashScreen.this,
                                                    Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                            finish();
                                        } else {
                                            startActivity(new Intent(SplashScreen.this,
                                                    Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                            finish();
                                        }


                                    }
                                });
                    } else {
                        if (session.getusername() != "") {
                            session.settemp("");
                            session.setaddress("");
                            session.setpincode("");
                            session.setcity("");
                            session.setstate("");
                            session.setdocument("");
                            startActivity(new Intent(SplashScreen.this,
                                    MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        } else {
                            startActivity(new Intent(SplashScreen.this,
                                    Login.class));
                            finish();
                        }
                    }
                }

                }
            };
        splashTread.start();
    }
}
