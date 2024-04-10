package com.josephabirizk.currencyexchange.api.model
import com.google.gson.annotations.SerializedName
class ExchangeRates {
    @SerializedName("usdtolbp")
    var usdToLbp: Float? = null
    @SerializedName("lbptousd")
    var lbpToUsd: Float? = null
}
