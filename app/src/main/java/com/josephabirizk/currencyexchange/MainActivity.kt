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








    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Authentication.initialize(this)
        setContentView(R.layout.activity_main)


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
                    tab.text = "Wallet"
                }
                1 -> {
                    tab.text = "Exchange"
                }
                2 -> {
                    tab.text = "Transactions"
                }
                3 -> {
                    tab.text = "Statistics"
                }
            }
        }.attach()




    }
}