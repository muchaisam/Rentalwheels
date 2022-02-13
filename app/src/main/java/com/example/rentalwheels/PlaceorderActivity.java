package com.example.rentalwheels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.rentalwheels.models.AppUser;
import com.example.rentalwheels.models.DBHelper;
import com.example.rentalwheels.models.DBModel;
import com.example.rentalwheels.models.UserOrders;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

public class PlaceorderActivity extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextView;
    private EditText doc_details;
    String docdetails;
    int Hours = 0;
    int Days = 0;
    int Audi = 0;
    int LandCruiser = 0;
    int BMW = 0;
    TextView topQty, lowerQty, timeview, topPrice, lowerPrice;
    public RadioGroup radioService;
    public RadioButton radiobtn;
    private DatePickerDialog datePickerDialog;
    private MaterialTimePicker picker;
    private Calendar calendar;
    Button checkout;
    int t2hour, t2minute;
    int month, date, year, hours, minutes;
    String time;
    int cart_total = 0;
    ProgressBar progressbar;
    DBHelper dbHelper;
    List<DBModel> dbModelList = new ArrayList<>();
    Button dateButton;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeorder);
        overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);

        dbHelper = new DBHelper(this);
        topQty = findViewById(R.id.topQty);
        lowerQty = findViewById(R.id.lowerQty);
        autoCompleteTextView = findViewById(R.id.autoComplete);
        radioService = findViewById(R.id.radioService);
        dateButton = findViewById(R.id.datebutton);
        timeview = findViewById(R.id.timeview);
        checkout = findViewById(R.id.checkout);
        topPrice = findViewById(R.id.topPrice);
        lowerPrice = findViewById(R.id.lowerPrice);
        dbModelList = dbHelper.getDataFromDbForHistory();
        doc_details = findViewById(R.id.docdetails);


        String [] option = {"Passport", "National ID" , "VISA"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.list_item, option);
        autoCompleteTextView.setText(arrayAdapter.getItem(0).toString(), false);
        autoCompleteTextView.setAdapter(arrayAdapter);

        //checkout
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int Hours = Integer.parseInt(topPrice.getText().toString());
                int Days = Integer.parseInt(lowerPrice.getText().toString());
                String docdetails = doc_details.getText().toString().trim();
                String day = date + "/" + month + "/" + year;
                if (docdetails.isEmpty() || docdetails.length() <6){
                    Toast.makeText(PlaceorderActivity.this, "Oops not yet, Kindly recheck if your details", Toast.LENGTH_LONG).show();
                    return;
                }
                if (hours == 0) {
                    hours = 12;
                    time = hours + ":" + minutes;

                } else {
                    time = hours + ":" + minutes;

                }
                Timber.e("onClick: " + Hours + "\n" + Days + "\n"  + day + "\n" + time);
                cart_total = Hours + Days ;
                if (Hours == 0 && Days == 0 ) {
                    Toast.makeText(PlaceorderActivity.this, "cart can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
               else {
                    dbHelper.insert(Hours, Days,
                            Audi, BMW, LandCruiser, cart_total, day, time);
                    dbHelper.insertTemp(Hours, Days,
                            Audi, BMW, LandCruiser, cart_total, day, time);
                    SharedPreferences sp = getApplicationContext().getSharedPreferences("RentalWheels", Context.MODE_PRIVATE);
                    dbHelper.insertUserOrder(sp.getString("email", ""), Hours, Days,
                            Audi, BMW, LandCruiser, cart_total, day, time);

                }
                startActivity(new Intent(PlaceorderActivity.this, CheckoutActivity.class));

            }
        });

        //timepicker
        timeview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showTimePicker();
            }
        });


        //datepicker
        initDatePicker();
        dateButton.setText(getTodayDate());
    }

    private void showTimePicker() {
        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Set Reminder Time")
                .build();

        picker.show(getSupportFragmentManager(), "RentalWheels");

        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (picker.getHour() > 12){
                    timeview.setText(
                            String.format("%02d",(picker.getHour()-12))+ ":" +String.format("%02d", picker.getMinute()) + "PM"
                    );
                }else {
                    timeview.setText(picker.getHour() + ":" + picker.getMinute() + "AM");
                }

                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
                calendar.set(Calendar.MINUTE, picker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
            }
        });
    }

    private String getTodayDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }
    //date
    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
//        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year){
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month){
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view){
        datePickerDialog.show();
    }

    public void clickradioButton(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.Audi:
                if (checked) {
                    displayHourPrice(Hours * 100);
                    displayDailyCharge(Days * 2000);
                    Audi = 1;
                    BMW = 0;
                    LandCruiser = 0;
                }
                break;
            case R.id.BMW:
                if (checked) {
                    displayHourPrice(Hours * 150);
                    displayDailyCharge(Days * 2500);
                    Audi = 0;
                    BMW = 1;
                    LandCruiser = 0;
                }
                break;
            case R.id.LandCruiser:
                if (checked) {

                    displayHourPrice(Hours * 200);
                    displayDailyCharge(Days * 3000);
                    Audi = 0;
                    BMW = 0;
                    LandCruiser = 1;
                }
                break;
        }
    }

    //adding
    public void topInc(View view) {
        if (Hours == 11) {
            Hours = 11;
            Toast.makeText(getApplicationContext(), "Can't be more than 11", Toast.LENGTH_SHORT).show();
        } else {
            Hours = Hours + 1;
            displayHour(Hours);

        }
        updatePrice("Hours", Hours);
    }

    public void topDec(View view) {
        if (Hours == 0) {
            Hours = 0;
            Toast.makeText(getApplicationContext(), "Can't be less than 0", Toast.LENGTH_SHORT).show();
        } else {
            Hours = Hours - 1;
            displayHour(Hours);
        }
        updatePrice("Hours", Hours);
    }

    public void lowerInc(View view) {
        if (Days == 10) {
            Days = 10;
            Toast.makeText(getApplicationContext(), "Can't be more than 10", Toast.LENGTH_SHORT).show();
        } else {
            Days = Days + 1;
            displayDaily(Days);
        }
        updatePrice("Days", Days);
    }

    public void lowerDec(View view) {
        if (Days == 0) {
            Days = 0;
            Toast.makeText(getApplicationContext(), "Can't be less than 0", Toast.LENGTH_SHORT).show();
        } else {
            Days = Days - 1;
            displayDaily(Days);
        }
        updatePrice("Daily", Days);
    }

    private void displayHour(int number) {

        topQty.setText("" + number);
    }

    private void displayDaily(int number) {
        lowerQty.setText("" + number);
    }

    public void updatePrice(String orderType, int count) {
        int selectedId = radioService.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        radiobtn = (RadioButton) findViewById(selectedId);
        if (radiobtn.getText().equals("Audi")) {
            if (orderType == "Hour") {
                displayHourPrice(count * 5);
            } else if (orderType == "Daily") {
                displayDailyCharge(count * 10);
            }
        } else if (radiobtn.getText().equals("BMW")) {
            if (orderType == "Hour") {
                displayHourPrice(count * 3);
            } else if (orderType == "Daily") {
                displayDailyCharge(count * 3);
            }
        } else if (radiobtn.getText().equals("LandCruiser")) {
            if (orderType == "Hour") {
                displayHourPrice(count * 8);
            } else if (orderType == "Daily") {
                displayDailyCharge(count * 13);
            }
        }
    }


    private void displayHourPrice(int number) {
        topPrice.setText("" + number);
    }

    private void displayDailyCharge(int number) {
        lowerPrice.setText("" + number);
    }

}