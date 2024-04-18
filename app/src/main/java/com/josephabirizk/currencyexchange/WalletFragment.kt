package com.josephabirizk.currencyexchange

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.josephabirizk.currencyexchange.api.Authentication
import com.josephabirizk.currencyexchange.api.ExchangeService
import com.josephabirizk.currencyexchange.api.model.ExchangeRates
import com.josephabirizk.currencyexchange.api.model.Transaction
import com.josephabirizk.currencyexchange.api.model.Wallet
import com.josephabirizk.currencyexchange.api.model.Wallet_money
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

    class WalletFragment : Fragment() {

        private var fab_wallet: FloatingActionButton? = null
        private var WalletDialog: View? = null
        private var usd_Ballance: TextView? = null
        private var lbp_Ballance: TextView? = null


        //asked chat
        override fun onResume() {
            super.onResume()
            fetchWallet()  // Call your data refreshing method here
        }

        private fun fetchWallet(){
            ExchangeService.exchangeApi().handle_wallet("Bearer ${Authentication.getToken()}").enqueue(object :
                Callback<Wallet> {
                override fun onResponse(call: Call<Wallet>, response: Response<Wallet>) {
                    val responseBody: Wallet? = response.body();
                    usd_Ballance?.text = responseBody?.usdBalance.toString()
                    lbp_Ballance?.text = responseBody?.lbpBalance.toString()
                }
                override fun onFailure(call: Call<Wallet>, t: Throwable) {
                    return;
                }
            })
        }

        // not useful after using onResume but left in case might use later
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            fetchWallet()

        }


        private fun addMoney( money: Wallet_money) {

            ExchangeService.exchangeApi().addMoney(money, "Bearer ${Authentication.getToken()}" ).enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response:
                Response<Any>) {
                    Snackbar.make(fab_wallet as View, "Money added!",
                        Snackbar.LENGTH_LONG)
                        .show()
                    fetchWallet()
                }
                override fun onFailure(call: Call<Any>, t: Throwable) {
                    Snackbar.make(fab_wallet as View, "Could not add Money.",
                        Snackbar.LENGTH_LONG)
                        .show()
                }
            })
        }

        private fun showDialog_wallet() {
            WalletDialog = LayoutInflater.from(requireActivity())
                .inflate(R.layout.dialog_wallet, null, false)
            MaterialAlertDialogBuilder(requireActivity()).setView(WalletDialog)
                .setTitle("Add Money")
                .setMessage("Enter details")
                .setPositiveButton("Add") { dialog, _ ->
                    val usdAmount = WalletDialog?.findViewById<TextInputLayout>(R.id.txtInptUsdAmount)?.editText?.text.toString().toFloat()
                    val lbpAmount = WalletDialog?.findViewById<TextInputLayout>(R.id.txtInptLbpAmount)?.editText?.text.toString().toFloat()
                    val creditCard = WalletDialog?.findViewById<TextInputLayout>(R.id.txtInptCreditCard)?.editText?.text.toString().toFloat()

                    val money =Wallet_money()
                    val transaction = Transaction()
                    money.creditCard=creditCard
                    money.usdAmount=usdAmount
                    money.lbpAmount=lbpAmount
                    addMoney(money)
                    dialog.dismiss()

                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }


        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view: View = inflater.inflate(R.layout.fragment_wallet, container, false)
            lbp_Ballance= view.findViewById(R.id.txtLbpBallance)
            usd_Ballance= view.findViewById(R.id.txtUsdBallance)
            fab_wallet= view.findViewById(R.id.fab)
            fab_wallet?.setOnClickListener { view ->
                showDialog_wallet()
            }


            return view
        }


    }