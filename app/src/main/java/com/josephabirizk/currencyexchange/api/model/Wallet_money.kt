package com.josephabirizk.currencyexchange.api.model

import com.google.gson.annotations.SerializedName

class Wallet_money {

    @SerializedName("credit_card")
    var creditCard: Float? = null

    @SerializedName("usd_amount")
    var usdAmount: Float? = null

    @SerializedName("lbp_amount")
    var lbpAmount: Float? = null
}
