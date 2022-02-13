package com.example.rentalwheels.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "laundry.db";
    static final String TABLE_NAME = "laundryOrdersTable";
    static final String TEMP_TABLE_NAME = "Temp_Orders_Table";
    public static final String SIGNUP_TABLE_NAME = "SIGNUP_TABLE_LAUNDRY";
    static final String ADDRESS_TABLE_NAME = "ADDRESS_TABLE";
    static final String USER_ORDER = "USER_ORDER_TABLE";
    static final String USER_ORDER_PLACED = "USER_ORDER_TABLE_PLACED";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + "(column_id integer primary key, Hours integer,Days integer," +
                "Audi integer,BMW integer,LandCruiser integer, final_price integer,date text,time text,email_id text)");
        sqLiteDatabase.execSQL("create table " + TEMP_TABLE_NAME + "(column_id integer primary key, Hours integer,Days integer," +
                "Audi integer,BMW integer,LandCruiser integer, final_price integer,date text,time text,email_id text)");
        sqLiteDatabase.execSQL("create table " + USER_ORDER_PLACED + "(column_id integer primary key, Hours integer,Days integer," +
                "Audi integer,BMW integer,LandCruiser integer, final_price integer,date text,time text,email_id text)");
        sqLiteDatabase.execSQL("create table " + SIGNUP_TABLE_NAME + "(user_id integer primary key, first_name text,last_name text," +
                "email_id text,phone_number integer,password text)");
        sqLiteDatabase.execSQL("create table " + ADDRESS_TABLE_NAME + "(user_id integer primary key, full_name text," +
                "email_id text,phone_number integer,address text)");
        sqLiteDatabase.execSQL("create table " + USER_ORDER + "(id integer primary key," +
                " order_email text,phone_number text,address text,column_id integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public List<DBModel> getDataFromDbForHistory() {

        // Declaring a List object
        List<DBModel> modelList = new ArrayList<>();

//        String for query
        String query = "select * from " + TABLE_NAME;


        SQLiteDatabase db = this.getWritableDatabase();

//        creating cursor object to use query to get the data from table
        Cursor cursor = db.rawQuery(query, null);

//        Condition for set the data in pojo class
        if (cursor.moveToFirst()) {
            do {
//                creating DatabaseModel object to set the list using by Pojo
                DBModel model = new DBModel();
                model.setId((cursor).getInt(0));
                model.setHours((cursor).getInt(1));
                model.setDays((cursor).getInt(2));
                model.setAudi((cursor).getInt(3));
                model.setBMW((cursor).getInt(4));
                model.setLandCruiser((cursor).getInt(5));
                model.setFinal_price((cursor).getInt(6));
                model.setDate((cursor).getString(7));
                model.setTime((cursor).getString(8));
                modelList.add(model);
            } while (cursor.moveToNext());
        }


        Log.d("Cart data", modelList.toString());


        return modelList;
    }

    public List<DBModel> getDataTemp() {

        // Declaring a List object
        List<DBModel> modelList = new ArrayList<>();

//        String for query
        String query = "select * from " + TEMP_TABLE_NAME;


        SQLiteDatabase db = this.getWritableDatabase();

//        creating cursor object to use query to get the data from table
        Cursor cursor = db.rawQuery(query, null);

//        Condition for set the data in pojo class
        if (cursor.moveToFirst()) {
            do {
//                creating DatabaseModel object to set the list using by Pojo
                DBModel model = new DBModel();
                model.setId((cursor).getInt(0));
                model.setHours((cursor).getInt(1));
                model.setDays((cursor).getInt(2));
                model.setAudi((cursor).getInt(3));
                model.setBMW((cursor).getInt(4));
                model.setLandCruiser((cursor).getInt(5));
                model.setFinal_price((cursor).getInt(6));
                model.setDate((cursor).getString(6));
                model.setTime((cursor).getString(8));
                modelList.add(model);
            } while (cursor.moveToNext());
        }


        Log.d("Cart data", modelList.toString());


        return modelList;
    }

    public List<UserOrders> getUserOrder() {

        // Declaring a List object
        List<UserOrders> modelList = new ArrayList<>();

//        String for query
        String query = "select * from " + USER_ORDER_PLACED;


        SQLiteDatabase db = this.getWritableDatabase();

//        creating cursor object to use query to get the data from table
        Cursor cursor = db.rawQuery(query, null);

//        Condition for set the data in pojo class
        if (cursor.moveToFirst()) {
            do {
//                creating DatabaseModel object to set the list using by Pojo
                UserOrders model = new UserOrders();
                model.setHour((cursor).getInt(1));
                model.setDays((cursor).getInt(2));
                model.setAudi((cursor).getInt(3));
                model.setBMW((cursor).getInt(4));
                model.setLandCruiser((cursor).getInt(5));
                model.setCart_total((cursor).getInt(6));
                model.setDate((cursor).getInt(7));
                model.setTime((cursor).getString(8));
                modelList.add(model);
            } while (cursor.moveToNext());
        }


        Log.d("Cart data", modelList.toString());


        return modelList;
    }

    public void deleteEntry(String item_id) {
        SQLiteDatabase ourDatabase = this.getWritableDatabase();
        ourDatabase.delete(TABLE_NAME, "column_id" + " = '" + item_id + "'", null);
    }

    public void deleteTemp(String item_id) {
        SQLiteDatabase ourDatabase = this.getWritableDatabase();
        ourDatabase.delete(TEMP_TABLE_NAME, "column_id" + " = '" + item_id + "'", null);
    }

    public boolean insert(int Hours, int Days, int Audi, int BMW, int LandCruiser, int final_price, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("hours", Hours);
        contentValues.put("days", Days);
        contentValues.put("Audi", Audi);
        contentValues.put("BMW", BMW);
        contentValues.put("LandCruiser", LandCruiser);
        contentValues.put("final_price", final_price);
        contentValues.put("date", date);
        contentValues.put("time", time);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertTemp(int Hours, int Days, int Audi, int BMW, int LandCruiser, int final_price, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Hours", Hours);
        contentValues.put("Days", Days);
        contentValues.put("Audi", Audi);
        contentValues.put("BMW", BMW);
        contentValues.put("LandCruiser", LandCruiser);
        contentValues.put("final_price", final_price);
        contentValues.put("date", date);
        contentValues.put("time", time);
        db.insert(TEMP_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertUserOrder(String email_id, int Hours, int Days, int Audi, int BMW, int LandCruiser, int final_price, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("email_id", email_id);
        contentValues.put("Hours", Hours);
        contentValues.put("Days", Days);
        contentValues.put("Audi", Audi);
        contentValues.put("BMW", BMW);
        contentValues.put("LandCruiser", LandCruiser);
        contentValues.put("final_price", final_price);
        contentValues.put("date", date);
        contentValues.put("time", time);
        db.insert(USER_ORDER_PLACED, null, contentValues);
        return true;
    }
}
