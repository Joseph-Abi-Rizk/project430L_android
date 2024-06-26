package com.josephabirizk.currencyexchange.api

import com.josephabirizk.currencyexchange.api.model.ExchangeRates
import com.josephabirizk.currencyexchange.api.model.GraphLists
import com.josephabirizk.currencyexchange.api.model.Offer
import com.josephabirizk.currencyexchange.api.model.Transaction
import com.josephabirizk.currencyexchange.api.model.User
import com.josephabirizk.currencyexchange.api.model.Token
import com.josephabirizk.currencyexchange.api.model.Transfer
import com.josephabirizk.currencyexchange.api.model.Wallet
import com.josephabirizk.currencyexchange.api.model.Wallet_money
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Header

object ExchangeService {
    private const val API_URL: String = "http://10.0.2.2:5000"
    fun exchangeApi():Exchange {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        // Create an instance of our Exchange API interface.
        return retrofit.create(Exchange::class.java);
    }
    interface Exchange {
        @GET("/exchangeRate")
        fun getExchangeRates(): Call<ExchangeRates>

        @POST("/transaction")
        fun addTransaction(@Body transaction: Transaction,
                           @Header("Authorization") authorization: String?): Call<Any>
        @GET("/transaction")
        fun getTransactions(@Header("Authorization") authorization: String):
                Call<List<Transaction>>

        @POST("/user")
        fun addUser(@Body user: User): Call<User>

        @POST("/authentication")
        fun authenticate(@Body user:User): Call<Token>

        @GET("/wallet")
        fun handle_wallet(@Header("Authorization") authorization: String):
                Call<Wallet>

        @POST("/wallet")
        fun addMoney(@Body wallet_money: Wallet_money,
                     @Header("Authorization") authorization: String?): Call<Any>

        @GET("/getGraph")
        fun get_graphs():Call<GraphLists>

        @POST("/makeOffer")
        fun makeOffer(@Body offer: Offer,
                      @Header("Authorization") authorization: String?): Call<Any>

        @POST("/transfer")
        fun transfer(@Body transfer: Transfer,
                     @Header("Authorization") authorization: String?): Call<Any>
    }
}
