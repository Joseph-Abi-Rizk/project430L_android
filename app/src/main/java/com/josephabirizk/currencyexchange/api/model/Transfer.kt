package com.josephabirizk.currencyexchange.api.model

import com.google.gson.annotations.SerializedName

class Transfer {
    @SerializedName("credit_card")
    var creditCard: Int? = null

    @SerializedName("usd_amount")
    var usdAmount: Float? = null

    @SerializedName("lbp_amount")
    var lbpAmount: Float? = null

    @SerializedName("user")
    var user: String? = null

}