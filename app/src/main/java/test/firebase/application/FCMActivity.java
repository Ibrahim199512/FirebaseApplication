package test.firebase.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.FirebaseApp;

import test.firebase.application.authentication.GoogleSigninActivity;
import test.firebase.application.real_time_database.RealTimeDatabaseActivity;
import test.firebase.application.storage.UploadFileAndImagesActivity;

public class FCMActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcm);

        FirebaseApp.initializeApp(this);


        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getString("type") != null) {
                Log.e("type", getIntent().getExtras().getString("type") + "");
                openWithNotification(getIntent().getExtras().getString("type"));
            } else {
                openWithoutNotification();
            }
        } else {
            openWithoutNotification();
        }


    }

    private void openWithoutNotification() {
    }

    private void openWithNotification(String type) {
        Intent intent = null;
        switch (Integer.parseInt(type)) {
            case 0:
                intent = new Intent(this, GoogleSigninActivity.class);
                break;
            case 1:
                intent = new Intent(this, RealTimeDatabaseActivity.class);
                break;
            default:
                intent = new Intent(this, UploadFileAndImagesActivity.class);
        }
        startActivity(intent);
    }
}
