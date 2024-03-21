package com.josephabirizk.currencyexchange
import com.josephabirizk.currencyexchange.api.model.Transaction
import com.josephabirizk.currencyexchange.api.ExchangeService
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.josephabirizk.currencyexchange.api.Authentication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TransactionsFragment : Fragment() {
    private var listview: ListView? = null
    private var transactions: ArrayList<Transaction>? = ArrayList()
    private var adapter: TransactionAdapter? = null

    private fun fetchTransactions() {
        if (Authentication.getToken() != null) {
            ExchangeService.exchangeApi()
                .getTransactions("Bearer ${Authentication.getToken()}")
                .enqueue(object : Callback<List<Transaction>> {
                    override fun onFailure(call: Call<List<Transaction>>,
                                           t: Throwable) {
                        return
                    }
                    override fun onResponse(
                        call: Call<List<Transaction>>,
                        response: Response<List<Transaction>>
                    ) {
                        transactions?.addAll(response.body()!!)
                        adapter?.notifyDataSetChanged()
                    }
                })
        }
    }


    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_transactions,
            container, false)
        listview = view.findViewById(R.id.listview)
        adapter =
            TransactionAdapter(layoutInflater, transactions!!)
        listview?.adapter = adapter
        fetchTransactions()
        return view


    }





}

class TransactionAdapter(
    private val inflater: LayoutInflater,
    private val dataSource: List<Transaction>
) : BaseAdapter() {
    override fun getView(
        position: Int, convertView: View?, parent:
        ViewGroup?
    ): View {
        val view: View = inflater.inflate(
            R.layout.item_transaction,
            parent, false
        )
        view.findViewById<TextView>(R.id.txtView).text =
            dataSource[position].id.toString()
        return view
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return dataSource[position].id?.toLong() ?: 0
    }

    override fun getCount(): Int {
        return dataSource.size
    }

}




