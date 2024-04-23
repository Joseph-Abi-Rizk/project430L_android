package com.josephabirizk.currencyexchange

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.josephabirizk.currencyexchange.api.Authentication
import com.josephabirizk.currencyexchange.api.ExchangeService
import com.josephabirizk.currencyexchange.api.model.Offer
import com.josephabirizk.currencyexchange.api.model.Transaction
import com.josephabirizk.currencyexchange.api.model.Wallet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Calendar


class OfferFragment : Fragment() {


    private var deadline: String?=null

    private var isBuyingUsd: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_offer, container, false)

        val buyUsdSpinner = view.findViewById<Spinner>(R.id.spinner_buy_usd)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.buy_usd_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            buyUsdSpinner.adapter = adapter
        }

        buyUsdSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Check if the selected item is "Buy USD"
                isBuyingUsd = parent.getItemAtPosition(position).toString() == "Buy USD"
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optional: Handle the case where nothing is selected, if necessary
            }
        }

        val amountEditText = view.findViewById<EditText>(R.id.offer_amount)
        val rateEditText = view.findViewById<EditText>(R.id.offer_rate)
        val deadlineEditText = view.findViewById<EditText>(R.id.offer_deadline)
        deadlineEditText.setOnClickListener {
            showDatePickerDialog()
            deadlineEditText.setText(deadline)

        }
        val submitButton = view.findViewById<Button>(R.id.offer_submit_button)

        submitButton.setOnClickListener {


            val amount = amountEditText.text.toString().toFloat()
            val rate = rateEditText.text.toString().toFloat()

            val deadline = deadlineEditText.text.toString()
            val offer = Offer(amount, isBuyingUsd,  rate, deadline)

            postOffer(offer)
        }

        return view
    }




    //used chat for this functionality
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            // Set the date in the EditText
            val selectedDate = String.format("%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth)
            deadline=selectedDate
            val deadlineEditText = view?.findViewById<EditText>(R.id.offer_deadline)
            deadlineEditText?.setText(selectedDate)

        }, year, month, day)

        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        datePickerDialog.show()
    }



    private fun postOffer(offer: Offer) {

        val authorization = "Bearer ${Authentication.getToken()}"
        if (Authentication.getToken() != null) {
            ExchangeService.exchangeApi()
                .makeOffer(offer, "Bearer ${Authentication.getToken()}")
                .enqueue(object : Callback<Any> {

                    override fun onResponse(call: Call<Any>, response: Response<Any>) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Offer posted successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Failed to post offer: ${response.message()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }

    }
}