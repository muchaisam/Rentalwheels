package com.msdc.rentalwheels.modules

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.msdc.rentalwheels.R
import com.msdc.rentalwheels.adapters.CustomCartAdapter
import com.msdc.rentalwheels.models.DBHelper
import com.msdc.rentalwheels.models.DBModel
import com.msdc.rentalwheels.models.OnItemClick
import com.flutterwave.raveandroid.RavePayActivity
import com.flutterwave.raveandroid.RaveUiManager
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants
import timber.log.Timber
import java.io.Serializable

class CheckoutActivity : AppCompatActivity(), OnItemClick {

    private lateinit var recyclerView: RecyclerView
    private lateinit var customCartAdapter: CustomCartAdapter
    private lateinit var dbHelper: DBHelper
    private lateinit var dbModelList: List<DBModel>
    private lateinit var tempList: List<DBModel>
    private lateinit var backBtn: ImageView
    private var cartTotal = 0
    private lateinit var cartTotalPrice: TextView
    private lateinit var cartEmpty: TextView
    private lateinit var cartValue: RelativeLayout
    private lateinit var checkoutPage: Button
    private val orderNo: MutableList<Serializable> = ArrayList()

    private val sharedPrefs = "shared_prefs"
    private val userName = "user_name"
    private lateinit var sharedPreferences: SharedPreferences
    private var username: String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
        setContentView(R.layout.activity_checkout)

        dbHelper = DBHelper(this)
        dbModelList = dbHelper.getDataFromDbForHistory()
        tempList = dbHelper.getDataTemp()

        sharedPreferences = getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
        username = sharedPreferences.getString(userName, null)

        backBtn = findViewById(R.id.backbtn)
        cartValue = findViewById(R.id.cartsss)
        cartTotalPrice = findViewById(R.id.cart_total_tv)
        cartEmpty = findViewById(R.id.cart_empty)
        checkoutPage = findViewById(R.id.checkout_page)
        recyclerView = findViewById(R.id.cart_recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (dbModelList.isNotEmpty()) {
            cartValue.visibility = View.VISIBLE
            cartEmpty.visibility = View.GONE
        } else {
            cartValue.visibility = View.GONE
            cartEmpty.visibility = View.VISIBLE
        }

        checkoutPage.setOnClickListener {
            makePayment()
            var ordersss = 0
            for (item in tempList) {
                orderNo.add(item.id)
                ordersss = item.id
            }
            Timber.e("onClick000: $ordersss")
        }

        customCartAdapter = CustomCartAdapter(this, dbModelList, this)
        recyclerView.adapter = customCartAdapter

        backBtn.setOnClickListener { onBackPressed() }

        for (item in dbModelList) {
            cartTotal += item.finalPrice
        }

        cartTotalPrice.text = "Ksh $cartTotal"
        Timber.e("onCreate: Ksh $cartTotal")
    }

    override fun onClick(list: List<DBModel>, cartValue: Int) {
        cartTotalPrice.text = "Ksh"
        cartTotal = 0
        if (list.isEmpty()) {
            this.cartValue.visibility = View.GONE
            cartEmpty.visibility = View.VISIBLE
        } else {
            this.cartValue.visibility = View.VISIBLE
            cartEmpty.visibility = View.GONE
            for (item in list) {
                cartTotal += item.finalPrice
            }
            cartTotalPrice.text = "Ksh $cartTotal"
        }
    }

    private fun makePayment() {
        RaveUiManager(this)
            .setAmount(500.0)
            .setEmail("test@gmail.com")
            .setCountry("KE")
            .setCurrency("KES")
            .setfName(username)
            .setlName(username)
            .setNarration("Purchase Goods")
            .setPublicKey("FLWPUBK_TEST-4cef96f6425b9df294926d770d9f4975-X")
            .setEncryptionKey("FLWSECK_TEST193760794f17")
            .setTxRef(System.currentTimeMillis().toString() + "Ref")
            .acceptAccountPayments(true)
            .acceptCardPayments(true)
            .acceptMpesaPayments(true)
            .onStagingEnv(true)
            .shouldDisplayFee(true)
            .showStagingLabel(true)
            .initialize()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            val message = data.getStringExtra("response")
            when (resultCode) {
                RavePayActivity.RESULT_SUCCESS -> {
                    Toast.makeText(this, "SUCCESS $message", Toast.LENGTH_LONG).show()
                    android.os.Handler().postDelayed({
                        val intent = Intent(this@CheckoutActivity, PayconfirmActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 100)
                }
                RavePayActivity.RESULT_ERROR -> {
                    Toast.makeText(this, "ERROR $message", Toast.LENGTH_LONG).show()
                }
                RavePayActivity.RESULT_CANCELLED -> {
                    Toast.makeText(this, "CANCELLED $message", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}