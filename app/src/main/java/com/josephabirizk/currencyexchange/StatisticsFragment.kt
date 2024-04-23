package com.josephabirizk.currencyexchange

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

import com.github.mikephil.charting.formatter.ValueFormatter
import com.josephabirizk.currencyexchange.api.Authentication
import com.josephabirizk.currencyexchange.api.ExchangeService
import com.josephabirizk.currencyexchange.api.model.GraphLists
import com.josephabirizk.currencyexchange.api.model.Wallet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



class StatisticsFragment : Fragment() {

    private lateinit var chartBuy: LineChart
    private lateinit var chartSell: LineChart


    override fun onResume() {
        super.onResume()
        getGraphs()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    //from chat to convert pairs to entries
    //from chat to convert pairs to entries
    fun convertPairsToEntries(pairs: List<Pair<String, Float>>?): List<Entry> {
        // Handle the case where pairs might be null
        if (pairs == null) return emptyList()

        // Convert each pair to an Entry, using the index as the x value
        return pairs.mapIndexed { index, pair ->
            Entry(index.toFloat(), pair.second)
        }
    }




    private fun getGraphs(){
        ExchangeService.exchangeApi().get_graphs().enqueue(object :
            Callback<GraphLists> {
            override fun onResponse(call: Call<GraphLists>, response: Response<GraphLists>) {
                val responseBody: GraphLists? = response.body();
                //asked chat how to join lists
                val datesSell= responseBody?.usd_to_lbp_x_axis
                val datesBuy=responseBody?.lbp_to_usd_x_axis

                val sellRates = responseBody?.usd_to_lbp_x_axis?.zip(responseBody.usd_to_lbp_y_axis?: listOf())
                val buyRates = responseBody?.lbp_to_usd_x_axis?.zip(responseBody.lbp_to_usd_y_axis?: listOf())

                //setupChartWithTestData(chartBuy)
                //setupChartWithTestData(chartSell)

                setupChart(chartBuy,convertPairsToEntries(buyRates),datesBuy)
                setupChart(chartSell, convertPairsToEntries(sellRates),datesSell)


            }
            override fun onFailure(call: Call<GraphLists>, t: Throwable) {
                return;
            }
        })
    }


    private fun setupChart(chart: LineChart, data: List<Entry>,labels: List<String>?) {
        val lineDataSet = LineDataSet(data, "Exchange Rate")


        val lineData = LineData(lineDataSet)
        chart.data = lineData
        //from chat to convert pairs to entries
        val xAxis = chart.xAxis
        xAxis.granularity = 1f // Only integer values
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.labelRotationAngle = -90f
        xAxis.textSize = 9f


        chart.animateX(1500, Easing.EaseInExpo);






        chart.invalidate()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_statistics, container, false)
        chartBuy = view.findViewById(R.id.buy_usd_rate_chart)
        chartBuy.animateX(1500, Easing.EaseInExpo)
        chartSell = view.findViewById(R.id.sell_usd_rate_chart)
        chartSell.animateX(1500, Easing.EaseInExpo);
        getGraphs()


        return view

    }




}