package com.example.rentalwheels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private TextView next;
    private EditText mobiledigit;
    private FirebaseAuth auth;

    //Constant keys for shared preferences
    public final String SHARED_PREFS = "shared_prefs";

    //storing phone
    public final String PHONE_KEY = "phone_key";

    SharedPreferences sharedPreferences;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() !=null) {
            startActivity(new Intent(RegisterActivity.this, MapsActivity.class));
        }
//         else {
//            Log.d(TAG, "onAuthStateChanged:signed_in");
//            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
//        }

        mobiledigit = findViewById(R.id.mobiledigit);

        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String mobile = mobiledigit.getText().toString().trim();

                if (mobile.isEmpty() || mobile.length() <10){
                    mobiledigit.setError("Kindly enter your mobile number");
                    mobiledigit.requestFocus();
                    return;
                }

                Intent intent = new Intent(RegisterActivity.this, Mobileverification.class);
                intent.putExtra("mobile", mobile);
                startActivity(intent);
            }
        });


    }
}