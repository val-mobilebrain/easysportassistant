package com.easysport.assistantappnewmb.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.easysport.assistantappnewmb.OddsUtils
import com.easysport.assistantappnewmb.data.Bet
import com.easysport.assistantappnewmb.data.BetDatabase
import kotlinx.coroutines.launch

class TrackerViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = BetDatabase.getInstance(application).betDao()
    val bets: LiveData<List<Bet>> = dao.getAllBets().asLiveData()

    fun addBet(bet: Bet) = viewModelScope.launch { dao.insertBet(bet) }

    fun deleteBet(bet: Bet) = viewModelScope.launch { dao.deleteBet(bet) }

    fun calculateNetProfit(bets: List<Bet>): Double {
        return bets.sumOf { bet ->
            when (bet.result) {
                "win" -> {
                    val decimal = OddsUtils.parseAutoToDecimal(bet.odds)
                    if (decimal != null && decimal > 1.0) (bet.stake * decimal) - bet.stake else 0.0
                }
                "loss" -> -bet.stake
                else -> 0.0
            }
        }
    }

    fun calculateProfit(bet: Bet): Double? {
        val decimal = OddsUtils.parseAutoToDecimal(bet.odds) ?: return null
        return if (decimal > 1.0) (bet.stake * decimal) - bet.stake else null
    }
}
