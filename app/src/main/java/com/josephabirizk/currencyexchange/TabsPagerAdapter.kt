package com.josephabirizk.currencyexchange


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
class TabsPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                WalletFragment()
            }
            1 -> {
                ExchangeFragment()
            }

            2 -> {
                TransactionsFragment()
            }

            3-> {
                StatisticsFragment()
            }
            4->{
                OfferFragment()
            }

            else -> ExchangeFragment()
        }
    }
    override fun getItemCount(): Int {
        return 5
    }
}
