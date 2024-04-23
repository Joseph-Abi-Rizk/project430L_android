package com.josephabirizk.currencyexchange


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.josephabirizk.currencyexchange.api.Authentication
import com.josephabirizk.currencyexchange.api.ExchangeService
import com.josephabirizk.currencyexchange.api.model.Transfer
import com.josephabirizk.currencyexchange.api.model.Wallet
import com.josephabirizk.currencyexchange.api.model.Wallet_money
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WalletFragment : Fragment() {

        private var DialogOne: AlertDialog? = null
        private var DialogTwo: AlertDialog? = null
        private var DialogThree: AlertDialog? = null

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

        private fun transferMoney( transfer:Transfer) {

            ExchangeService.exchangeApi().transfer(transfer, "Bearer ${Authentication.getToken()}" ).enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response:
                Response<Any>) {
                    Snackbar.make(fab_wallet as View, "Money Transferred!",
                        Snackbar.LENGTH_LONG)
                        .show()
                }
                override fun onFailure(call: Call<Any>, t: Throwable) {
                    Snackbar.make(fab_wallet as View, "Could not Transfer money.",
                        Snackbar.LENGTH_LONG)
                        .show()
                }
            })
        }

        private fun showDialog_wallet_one() {

            if (DialogTwo != null && DialogTwo!!.isShowing()) {
                DialogTwo!!.dismiss()
            }

            WalletDialog = LayoutInflater.from(requireActivity())
                .inflate(R.layout.dialog_wallet, null, false)

            val buttonOne= WalletDialog?.findViewById<Button>(R.id.btnTabOne)
            val buttonTwo= WalletDialog?.findViewById<Button>(R.id.btnTabTwo)
            val buttonThree= WalletDialog?.findViewById<Button>(R.id.btnTabThree)
            val usdAmount = WalletDialog?.findViewById<TextInputLayout>(R.id.txtInptUsdAmount)//?.editText?.text.toString().toFloat()
            val lbpAmount = WalletDialog?.findViewById<TextInputLayout>(R.id.txtInptLbpAmount)//?.editText?.text.toString().toFloat()
            val creditCard = WalletDialog?.findViewById<TextInputLayout>(R.id.txtInptCreditCard)//?.editText?.text.toString().toFloat()

            buttonTwo?.setOnClickListener {
                showDialog_wallet_two()
            }
            DialogOne=MaterialAlertDialogBuilder(requireActivity()).setView(WalletDialog)
                .setPositiveButton("Add") { dialog, _ ->
                    val usdAmountStr = usdAmount?.editText?.text.toString().trim()
                    val lbpAmountStr = lbpAmount?.editText?.text.toString().trim()
                    val creditCardStr = creditCard?.editText?.text.toString().trim()





                    var isValid = true
                    if (creditCardStr.isEmpty()) {

                        isValid = false
                    }

                    if (usdAmountStr.isEmpty()) {

                        isValid = false
                    }

                    if (lbpAmountStr.isEmpty()) {
                        isValid = false
                    }

                    // If all inputs are valid, proceed to add the money
                    if (isValid) {
                        val usd = usdAmountStr.toFloat() // Convert to Float
                        val lbp = lbpAmountStr.toFloat() // Convert to Float
                        val credit = creditCardStr.toInt() // Convert to Float

                        val money = Wallet_money(credit, usd, lbp)
                        addMoney(money)
                    } else {

                        Snackbar.make(fab_wallet as View, "Please add input.",
                            Snackbar.LENGTH_LONG)
                            .show()
                        // Prevent dialog from dismissing when data is invalid
                        dialog.dismiss()
                    }



                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }


        private fun showDialog_wallet_two() {
            if (DialogOne != null && DialogOne!!.isShowing()) {
                DialogOne!!.dismiss()
            }
            WalletDialog = LayoutInflater.from(requireActivity())
                .inflate(R.layout.dialog_wallet_two, null, false)

            val buttonOne= WalletDialog?.findViewById<Button>(R.id.btnTabOne)
            val buttonTwo= WalletDialog?.findViewById<Button>(R.id.btnTabTwo)
            val buttonThree= WalletDialog?.findViewById<Button>(R.id.btnTabThree)
            val usdAmount = WalletDialog?.findViewById<TextInputLayout>(R.id.txtInptUsdAmount)//?.editText?.text.toString().toFloat()
            val lbpAmount = WalletDialog?.findViewById<TextInputLayout>(R.id.txtInptLbpAmount)//?.editText?.text.toString().toFloat()
            val User = WalletDialog?.findViewById<TextInputLayout>(R.id.txtInptUser)//?.editText?.text.toString().toFloat()

            buttonOne?.setOnClickListener {
                showDialog_wallet_one()
            }
            DialogTwo=MaterialAlertDialogBuilder(requireActivity()).setView(WalletDialog)
                .setPositiveButton("Send") { dialog, _ ->

                    buttonOne?.setOnClickListener {
                        dialog.dismiss()
                        showDialog_wallet_one()
                    }


                    val usdAmountStr = usdAmount?.editText?.text.toString().trim()
                    val lbpAmountStr = lbpAmount?.editText?.text.toString().trim()
                    val user = User?.editText?.text.toString().trim()

                    var isValid = true
                    if (user.isEmpty()) {

                        isValid = false
                    }

                    if (usdAmountStr.isEmpty()) {

                        isValid = false
                    }

                    if (lbpAmountStr.isEmpty()) {
                        isValid = false
                    }

                    // If all inputs are valid, proceed to add the money
                    if (isValid) {
                        val usd = usdAmountStr.toFloat() // Convert to Float
                        val lbp = lbpAmountStr.toFloat() // Convert to Float
                        val usertoSend = user

                        val transfer = Transfer()
                        transfer.usdAmount=usd
                        transfer.lbpAmount=lbp
                        transfer.user=usertoSend

                        transferMoney(transfer)
                    } else {

                        Snackbar.make(fab_wallet as View, "Please add input.",
                            Snackbar.LENGTH_LONG)
                            .show()
                        // Prevent dialog from dismissing when data is invalid
                        dialog.dismiss()
                    }



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
                showDialog_wallet_one()
            }


            return view
        }


    }