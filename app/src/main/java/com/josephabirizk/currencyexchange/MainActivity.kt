package com.josephabirizk.currencyexchange
import android.widget.TextView;
import com.josephabirizk.currencyexchange.api.model.ExchangeRates
import com.josephabirizk.currencyexchange.api.model.Token
import com.josephabirizk.currencyexchange.api.model.User

import com.google.android.material.textfield.TextInputLayout
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import com.josephabirizk.currencyexchange.api.Authentication
import com.josephabirizk.currencyexchange.api.model.Transaction
import com.josephabirizk.currencyexchange.api.ExchangeService
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.widget.RadioGroup

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import android.os.Bundle
import android.widget.EditText
import android.view.View
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private var buyUsdTextView: TextView? = null
    private var sellUsdTextView: TextView? = null
    private var fab: FloatingActionButton? = null
    private var transactionDialog: View? = null
    private var menu: Menu? = null
    private var tabLayout: TabLayout? = null
    private var tabsViewPager: ViewPager2? = null

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.login) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.register) {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.logout) {
            Authentication.clearToken()
            setMenu()
        }
        return true
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        setMenu()
        return true
    }
    private fun setMenu() {
        menu?.clear()
        menuInflater.inflate(if(Authentication.getToken() == null)
            R.menu.menu_logged_out else R.menu.menu_logged_in, menu)
    }


    private fun showDialog() {
        transactionDialog = LayoutInflater.from(this)
            .inflate(R.layout.dialog_transaction, null, false)
        MaterialAlertDialogBuilder(this).setView(transactionDialog)
            .setTitle("Add Transaction")
            .setMessage("Enter transaction details")
            .setPositiveButton("Add") { dialog, _ ->
                val usdAmount = transactionDialog?.findViewById<TextInputLayout>(R.id.txtInptUsdAmount)?.editText?.text.toString().toFloat()
                val lbpAmount = transactionDialog?.findViewById<TextInputLayout>(R.id.txtInptLbpAmount)?.editText?.text.toString().toFloat()

                val trans = transactionDialog?.findViewById<RadioGroup>(R.id.rdGrpTransactionType)
                val trans_id = trans?.checkedRadioButtonId
                val transType = when (trans_id) {
                    R.id.rdBtnBuyUsd -> true
                    R.id.rdBtnSellUsd -> false
                    else->false
                }

                val transaction = Transaction()
                transaction.usdAmount=usdAmount
                transaction.lbpAmount=lbpAmount
                transaction.usdToLbp=transType
                addTransaction(transaction)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    private fun fetchRates(){
        ExchangeService.exchangeApi().getExchangeRates().enqueue(object :
            Callback<ExchangeRates> {
            override fun onResponse(call: Call<ExchangeRates>, response: Response<ExchangeRates>) {
                val responseBody: ExchangeRates? = response.body();
                buyUsdTextView?.text = responseBody?.usdToLbp.toString()
                sellUsdTextView?.text = responseBody?.lbpToUsd.toString()
            }
            override fun onFailure(call: Call<ExchangeRates>, t: Throwable) {
                return;
                //TODO("Not yet implemented")
            }
        })
    }

    private fun addTransaction(transaction: Transaction) {

        ExchangeService.exchangeApi().addTransaction(transaction, if (Authentication.getToken() != null) "Bearer${Authentication.getToken()}" else null).enqueue(object :
            Callback<Any> {
            override fun onResponse(call: Call<Any>, response:
            Response<Any>) {
                Snackbar.make(fab as View, "Transaction added!",
                    Snackbar.LENGTH_LONG)
                    .show()
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Snackbar.make(fab as View, "Could not add transaction.",
                    Snackbar.LENGTH_LONG)
                    .show()
            }
        })
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Authentication.initialize(this)
        setContentView(R.layout.activity_main)

        fab = findViewById(R.id.fab)
        fab?.setOnClickListener { view ->
            showDialog()
        }

        tabLayout = findViewById(R.id.tabLayout)
        tabsViewPager = findViewById(R.id.tabsViewPager)
        tabLayout?.tabMode = TabLayout.MODE_FIXED
        tabLayout?.isInlineLabel = true
        // Enable Swipe
        tabsViewPager?.isUserInputEnabled = true
        // Set the ViewPager Adapter
        val adapter = TabsPagerAdapter(supportFragmentManager, lifecycle)
        tabsViewPager?.adapter = adapter
        TabLayoutMediator(tabLayout!!, tabsViewPager!!) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Exchange"
                }
                1 -> {
                    tab.text = "Transactions"
                }
            }
        }.attach()




    }
}