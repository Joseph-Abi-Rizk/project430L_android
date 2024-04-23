package com.josephabirizk.currencyexchange

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.josephabirizk.currencyexchange.api.ExchangeService
import com.josephabirizk.currencyexchange.api.model.ExchangeRates
import android.view.LayoutInflater
import android.view.View
import android.widget.Button

import android.view.ViewGroup
import android.webkit.WebView
import android.widget.RadioGroup
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.josephabirizk.currencyexchange.api.Authentication
import com.josephabirizk.currencyexchange.api.model.Transaction
import java.text.DecimalFormat


class ExchangeFragment : Fragment() {
    private var buyUsdTextView: TextView? = null
    private var sellUsdTextView: TextView? = null
    private var calc_in: EditText?=null
    private var calc_out: TextView? = null
    private var calc_btn: Button?= null
    private var trans: RadioGroup?= null

    private var fab: FloatingActionButton? = null
    private var transactionDialog: View? = null


    override fun onResume() {
        super.onResume()
        fetchRates()
    }


    private fun fetchRates(){
        ExchangeService.exchangeApi().getExchangeRates().enqueue(object :
            Callback<ExchangeRates> {
            override fun onResponse(call: Call<ExchangeRates>, response: Response<ExchangeRates>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val decimalFormat = DecimalFormat("#.###")
                    val formattedUSDTOLBP = decimalFormat.format(responseBody?.usdToLbp)
                    val formattedLBPTOUSD = decimalFormat.format(responseBody?.lbpToUsd)
                    buyUsdTextView?.text = formattedUSDTOLBP
                    sellUsdTextView?.text = formattedLBPTOUSD
                } else {
                    Toast.makeText(context, "Failed to load rates: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<ExchangeRates>, t: Throwable) {
                //no need
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchRates()

    }
    private fun perform_caclulation(){
        fetchRates()

        val trans_id = trans?.checkedRadioButtonId
        val transType = when (trans_id) {
            R.id.calculator_buy_usd-> true
            R.id.calculator_sell_usd -> false
            else->false
        }

        val result = calc_in?.getText().toString().toFloat()
        if (transType){
            calc_out?.text=(result*(buyUsdTextView?.text.toString().toFloat())).toString()
        }
        else{
            calc_out?.text=(result*(sellUsdTextView?.text.toString().toFloat())).toString()
        }



    }

    private fun addTransaction(transaction: Transaction) {

        ExchangeService.exchangeApi().addTransaction(transaction, "Bearer ${Authentication.getToken()}" ).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response:
            Response<Any>) {
                Snackbar.make(fab as View, "Transaction added!",
                    Snackbar.LENGTH_LONG)
                    .show()
                fetchRates()
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Snackbar.make(fab as View, "Could not add transaction.",
                    Snackbar.LENGTH_LONG)
                    .show()
            }
        })
    }


    private fun showDialog() {
        transactionDialog = LayoutInflater.from(requireActivity())
            .inflate(R.layout.dialog_transaction, null, false)
        MaterialAlertDialogBuilder(requireActivity()).setView(transactionDialog)
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



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_exchange,
            container, false)

        fab = view.findViewById(R.id.fab)
        fab?.setOnClickListener { view ->
            showDialog()
        }
        buyUsdTextView = view.findViewById(R.id.txtBuyUsdRate)
        sellUsdTextView = view.findViewById(R.id.txtSellUsdRate)

        calc_in = view?.findViewById(R.id.calculator_input)
        calc_out = view?.findViewById(R.id.calculator_res)
        trans =  view?.findViewById(R.id.calculator_trans_type)

        calc_btn =  view.findViewById(R.id.calculator_btn)


        calc_btn?.setOnClickListener {
            if (calc_in?.text.toString().isNotEmpty()) {
                perform_caclulation()
            } else {
                Toast.makeText(context, "Please enter an amount to calculate", Toast.LENGTH_SHORT).show()
            }
        }


        return view
    }


}