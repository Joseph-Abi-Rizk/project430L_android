package com.josephabirizk.currencyexchange.api.model

import com.google.gson.annotations.SerializedName

class Wallet {
    @SerializedName("id")
    var id: Int? = null

    @SerializedName("user_id")
    var userId: Int? = null

    @SerializedName("usd_balance")
    var usdBalance: Float? = null

    @SerializedName("lbp_balance")
    var lbpBalance: Float? = null
}