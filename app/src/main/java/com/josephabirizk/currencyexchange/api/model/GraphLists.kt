package com.josephabirizk.currencyexchange.api.model

import com.google.gson.annotations.SerializedName



class GraphLists {

    @SerializedName("lbp_to_usd_x_axis")
    var lbp_to_usd_x_axis : List<String>?= null

    @SerializedName("lbp_to_usd_hist")
    var lbp_to_usd_y_axis : List<Float>?= null

    @SerializedName("usd_to_lbp_x_axis")
    var usd_to_lbp_x_axis : List<String>?= null

    @SerializedName("usd_to_lbp_hist")
    var usd_to_lbp_y_axis : List<Float>?= null


}