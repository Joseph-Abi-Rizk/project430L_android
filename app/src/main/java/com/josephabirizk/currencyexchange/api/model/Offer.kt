package com.josephabirizk.currencyexchange.api.model

import com.google.gson.annotations.SerializedName

class Offer {
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("amount")
    var Amount: Float? = null
    @SerializedName("buy_usd")
    var BuyUsd: Boolean? = null
    @SerializedName("rate")
    var Rate: Float? = null
    @SerializedName("deadline")
    var Deadline: String?= null

    constructor( amount: Float? = null, buyUsd: Boolean? = null, rate: Float? = null, deadline: String? = null) {
        this.id = null
        this.Amount = amount
        this.BuyUsd = buyUsd
        this.Rate = rate
        this.Deadline = deadline
    }
}