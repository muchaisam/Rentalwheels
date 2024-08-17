package com.example.rentalwheels.adapters

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.rentalwheels.R
import com.example.rentalwheels.models.DBHelper
import com.example.rentalwheels.models.DBModel
import com.example.rentalwheels.models.OnItemClick

class CustomCartAdapter(
    private val context: Context,
    private var dbList: List<DBModel>,
    private val mCallback: OnItemClick
) : RecyclerView.Adapter<CustomCartAdapter.CartHolder>() {

    private lateinit var dbHelper: DBHelper
    private lateinit var topQty: Spinner
    private lateinit var lowerQty: Spinner
    private var Hour = 0
    private var Daily = 0

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): CartHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_listview, parent, false)
        dbHelper = DBHelper(context)
        return CartHolder(view)
    }

    override fun onBindViewHolder(@NonNull holder: CartHolder, position: Int) {
        holder.Hours.text = dbList[position].hours.toString()
        holder.Days.text = dbList[position].days.toString()
        if (dbList[position].landCruiser == 1) {
            holder.Hours.text = "Yes"
            holder.Days.text = "Yes"
        } else {
            if (dbList[position].audi == 0) {
                holder.Hours.text = "No"
            } else {
                holder.Days.text = "Yes"
            }
            if (dbList[position].bmw == 0) {
                holder.Hours.text = "No"
            } else {
                holder.Days.text = "Yes"
            }
        }

        holder.cart_price.text = "Ksh ${dbList[position].finalPrice}"
        holder.remove_from_cart.setOnClickListener {
            removeItem(position)
        }
    }

    private fun removeItem(position: Int) {
        val removeDialog = Dialog(context)
        removeDialog.setContentView(R.layout.delete_dialog)
        removeDialog.show()
        val remove_item_btn: Button = removeDialog.findViewById(R.id.yes_remove)
        val cancel_remove_btn: Button = removeDialog.findViewById(R.id.no_remove)
        remove_item_btn.setOnClickListener {
            dbHelper.deleteEntry(dbList[position].id.toString())
            dbHelper.deleteTemp(dbList[position].id.toString())
            dbList = dbList.toMutableList().apply { removeAt(position) }
            notifyDataSetChanged()
            removeDialog.dismiss()
            mCallback.onClick(dbList, 0)
        }
        cancel_remove_btn.setOnClickListener {
            removeDialog.dismiss()
        }
    }

    override fun getItemCount(): Int {
        return dbList.size
    }

    inner class CartHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cart_price: TextView = itemView.findViewById(R.id.cart_price)
        val Hours: TextView = itemView.findViewById(R.id.hourss)
        val Days: TextView = itemView.findViewById(R.id.dayss)
        val edit_cart: TextView = itemView.findViewById(R.id.edit_cart_items)
        val remove_from_cart: ImageView = itemView.findViewById(R.id.delete_item)
    }
}