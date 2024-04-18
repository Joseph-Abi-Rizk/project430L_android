package com.josephabirizk.currencyexchange

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.josephabirizk.currencyexchange.api.Authentication
import com.josephabirizk.currencyexchange.api.ExchangeService
import com.josephabirizk.currencyexchange.api.model.ExchangeRates
import com.josephabirizk.currencyexchange.api.model.Wallet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WalletFragment : Fragment() {

    private var usd_Ballance: TextView? = null
    private var lbp_Ballance: TextView? = null
    private var add_money_button: Button? = null

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

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_wallet, container, false)
         lbp_Ballance= view.findViewById(R.id.txtLbpBallance)
        usd_Ballance= view.findViewById(R.id.txtUsdBallance)



        return view
    }


}