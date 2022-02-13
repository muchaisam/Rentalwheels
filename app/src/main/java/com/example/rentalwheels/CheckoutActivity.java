package com.example.rentalwheels;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentalwheels.models.CustomCartAdapter;
import com.example.rentalwheels.models.DBHelper;
import com.example.rentalwheels.models.DBModel;
import com.example.rentalwheels.models.OnItemClick;
import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RaveUiManager;
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class CheckoutActivity extends AppCompatActivity implements OnItemClick {

    RecyclerView recyclerView;
    CustomCartAdapter customCartAdapter;
    DBHelper dbHelper;
    List<DBModel> dbModelList;
    List<DBModel> tempList;
    ImageView back_btn;
    int cart_total = 0;
    TextView cart_total_price, cartEmpty;
    RelativeLayout cart_value;
    Button checkout_page;
    List<Serializable> order_no = new ArrayList<Serializable>();

    public final String SHARED_PREFS = "shared_prefs";

    //storing phone
    public final String USER_NAME = "user_name";
    SharedPreferences sharedPreferences;
    String username;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
        setContentView(R.layout.activity_checkout);
        dbHelper = new DBHelper(this);
        dbModelList = dbHelper.getDataFromDbForHistory();
        tempList = dbHelper.getDataTemp();

        //initializing shared prefs
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        //getting data and storing it
        username = sharedPreferences.getString(USER_NAME, null);

        back_btn = findViewById(R.id.backbtn);
        cart_value = findViewById(R.id.cartsss);
        cart_total_price = findViewById(R.id.cart_total_tv);
        cartEmpty = findViewById(R.id.cart_empty);
        checkout_page = findViewById(R.id.checkout_page);
        recyclerView = findViewById(R.id.cart_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (dbModelList.size() > 0) {
            cart_value.setVisibility(View.VISIBLE);
            cartEmpty.setVisibility(View.GONE);

        } else {
            cart_value.setVisibility(View.GONE);
            cartEmpty.setVisibility(View.VISIBLE);

        }

        checkout_page.setOnClickListener(view -> {
            makePayment();
            int ordersss = 0;
            for (int i = 0; i < tempList.size(); i++) {

                order_no.add(tempList.get(i).getId());
                ordersss = tempList.get(i).getId();
            }
            Timber.e("onClick000: " + ordersss);
        });
        customCartAdapter = new CustomCartAdapter(this, dbModelList, this);
        recyclerView.setAdapter(customCartAdapter);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        for (int i = 0; i < dbModelList.size(); i++) {

            cart_total = cart_total + dbModelList.get(i).getFinal_price();
        }

        cart_total_price.setText("Ksh " + cart_total);

        Timber.e("onCreate: Ksh " + cart_total);

    }

    @Override
    public void onClick(List<DBModel> list,int cartvalue) {
        cart_total_price.setText("Ksh");
        cart_total = 0;
        if (list.size() == 0) {
            cart_value.setVisibility(View.GONE);
            cartEmpty.setVisibility(View.VISIBLE);
        } else {

            cart_value.setVisibility(View.VISIBLE);
            cartEmpty.setVisibility(View.GONE);
            for (int i = 0; i < list.size(); i++) {
                cart_total = cart_total + list.get(i).getFinal_price();
            }
            cart_total_price.setText("Ksh " + cart_total);
        }



    }


    private void makePayment() {
        new RaveUiManager(this)
                .setAmount(Double.parseDouble("500"))
                .setEmail("test@gmail.com")
                .setCountry("KE")
                .setCurrency("KES")
                .setfName(username)
                .setlName(username)
                .setNarration("Purchase Goods")
                .setPublicKey("FLWPUBK_TEST-4cef96f6425b9df294926d770d9f4975-X")
                .setEncryptionKey("FLWSECK_TEST193760794f17")
                .setTxRef(System.currentTimeMillis() + "Ref")
                .acceptAccountPayments(true)
                .acceptCardPayments(true)
                .acceptMpesaPayments(true)
                .onStagingEnv(true)
                .shouldDisplayFee(true)
                .showStagingLabel(true)
                .initialize();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                Toast.makeText(this, "SUCCESS " + message, Toast.LENGTH_LONG).show();
                new android.os.Handler().postDelayed(() -> {
                    Intent f = new Intent(CheckoutActivity.this, PayconfirmActivity.class);
                    startActivity(f);
                    finish();
                },100);
            }
            else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(this, "ERROR " + message, Toast.LENGTH_LONG).show();
            }
            else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(this, "CANCELLED " + message, Toast.LENGTH_LONG).show();
            }
        }
    }
}