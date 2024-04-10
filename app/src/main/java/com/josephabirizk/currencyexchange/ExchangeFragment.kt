package com.josephabirizk.currencyexchange

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.josephabirizk.currencyexchange.api.ExchangeService
import com.josephabirizk.currencyexchange.api.model.ExchangeRates
import android.view.LayoutInflater
import android.view.View
import android.widget.Button

import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import android.widget.EditText
import android.widget.RadioButton


class ExchangeFragment : Fragment() {
    private var buyUsdTextView: TextView? = null
    private var sellUsdTextView: TextView? = null
    private var calc_in: EditText?=null
    private var calc_out: TextView? = null
    private var calc_btn: Button?= null
    private var trans: RadioGroup?= null


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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_exchange,
            container, false)
        buyUsdTextView = view.findViewById(R.id.txtBuyUsdRate)
        sellUsdTextView = view.findViewById(R.id.txtSellUsdRate)

        calc_in = view?.findViewById(R.id.calculator_input)
        calc_out = view?.findViewById(R.id.calculator_res)
        trans =  view?.findViewById(R.id.calculator_trans_type)

        calc_btn =  view.findViewById(R.id.calculator_btn)


        calc_btn?.setOnClickListener { view->
            perform_caclulation()
        }


        return view
    }


}