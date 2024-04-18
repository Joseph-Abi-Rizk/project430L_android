package com.josephabirizk.currencyexchange.api.model
import com.google.gson.annotations.SerializedName
import java.util.Date
class Transaction() {
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("usd_amount")
    var usdAmount: Float? = null
    @SerializedName("lbp_amount")
    var lbpAmount: Float? = null
    @SerializedName("usd_to_lbp")
    var usdToLbp: Boolean? = null
    @SerializedName("added_date")
    var addedDate: String?= null

    //asked chatgpt added equals and hashcode
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Transaction
        if (id != other.id) return false

        return true
    }
    override fun hashCode(): Int {
        return id ?: 0
    }
}