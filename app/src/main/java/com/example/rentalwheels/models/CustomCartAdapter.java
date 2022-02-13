package com.example.rentalwheels.models;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalwheels.R;

import java.util.ArrayList;
import java.util.List;

public class CustomCartAdapter extends RecyclerView.Adapter<CustomCartAdapter.CartHolder> {
    Context context;
    List<DBModel> dbList = new ArrayList<>();
    DBHelper dbHelper;
    OnItemClick mCallback;
    Spinner topQty;
    Spinner lowerQty;
    int Hour = 0;
    int Daily = 0;

    public CustomCartAdapter(Context context, List<DBModel> dbList, OnItemClick mCallback) {
        this.context = context;
        this.dbList = dbList;
        this.mCallback = mCallback;
    }

    @NonNull
    @Override
    public CustomCartAdapter.CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_listview, parent, false);
        dbHelper = new DBHelper(context);


        return new CartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomCartAdapter.CartHolder holder, final int position) {

        holder.Hours.setText(String.valueOf(dbList.get(position).getHours()));
        holder.Days.setText(String.valueOf(dbList.get(position).getDays()));
        if (dbList.get(position).getLandCruiser() == 1) {
            holder.Hours.setText("Yes");
            holder.Days.setText("Yes");
        } else {
            if (dbList.get(position).getAudi() == 0) {
                holder.Hours.setText("No");
            } else {
                holder.Days.setText("Yes");

            }
            if (dbList.get(position).getBMW() == 0) {

                holder.Hours.setText("No");
            } else {
                holder.Days.setText("Yes");
            }
        }


        holder.cart_price.setText("Ksh " + dbList.get(position).getFinal_price());
        holder.remove_from_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(position);
            }
        });

    }

    void removeItem(final int position) {
        final Dialog removeDialog = new Dialog(context);
        removeDialog.setContentView(R.layout.delete_dialog);
        removeDialog.show();
        Button remove_item_btn = removeDialog.findViewById(R.id.yes_remove);
        Button cancel_remove_btn = removeDialog.findViewById(R.id.no_remove);
        remove_item_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.deleteEntry(String.valueOf(dbList.get(position).getId()));
                dbHelper.deleteTemp(String.valueOf(dbList.get(position).getId()));
                dbList.remove(position);
                notifyDataSetChanged();
                removeDialog.dismiss();
                mCallback.onClick(dbList, 0);

            }
        });
        cancel_remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeDialog.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return dbList.size();
    }

    class CartHolder extends RecyclerView.ViewHolder {

        TextView cart_price, Hours, Days, Audi, BMW, edit_cart;
        ImageView remove_from_cart;

        public CartHolder(@NonNull View itemView) {
            super(itemView);
            cart_price = itemView.findViewById(R.id.cart_price);
            Hours = itemView.findViewById(R.id.hourss);
            Days = itemView.findViewById(R.id.dayss);
            edit_cart = itemView.findViewById(R.id.edit_cart_items);
            remove_from_cart = itemView.findViewById(R.id.delete_item);

        }
    }


}
