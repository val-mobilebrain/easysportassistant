package com.easysport.assistantappnewmb

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.easysport.assistantappnewmb.ui.BetCalculatorFragment
import com.easysport.assistantappnewmb.ui.OddsConverterFragment
import com.easysport.assistantappnewmb.ui.ParlayBuilderFragment
import com.easysport.assistantappnewmb.ui.ProfitTrackerFragment

class ScreenPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount() = 4

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> OddsConverterFragment()
        1 -> BetCalculatorFragment()
        2 -> ParlayBuilderFragment()
        3 -> ProfitTrackerFragment()
        else -> throw IllegalArgumentException("Invalid position $position")
    }
}
