package com.example.rentalwheels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class PayconfirmActivity extends AppCompatActivity {
    Button okayBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payconfirm);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        okayBtn=findViewById(R.id.okay_button);
        okayBtn.setOnClickListener(v -> {
            Intent intent=new Intent(PayconfirmActivity.this, MapsActivity.class);
            startActivity(intent);
            finish();
        });
    }
}