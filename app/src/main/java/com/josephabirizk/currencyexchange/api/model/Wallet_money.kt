package com.josephabirizk.currencyexchange.api.model

import com.google.gson.annotations.SerializedName

class Wallet_money {

    @SerializedName("credit_card")
    var creditCard: Int? = null

    @SerializedName("usd_amount")
    var usdAmount: Float? = null

    @SerializedName("lbp_amount")
    var lbpAmount: Float? = null

    constructor( creditCard: Int? = null, usdAmount: Float? = null, lbpAmount: Float? = null) {
        this.creditCard=creditCard
        this.usdAmount = usdAmount
        this.lbpAmount = lbpAmount
    }
}
