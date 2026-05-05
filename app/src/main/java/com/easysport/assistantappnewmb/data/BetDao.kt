package com.easysport.assistantappnewmb.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BetDao {
    @Query("SELECT * FROM bets ORDER BY date DESC, id DESC")
    fun getAllBets(): Flow<List<Bet>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBet(bet: Bet)

    @Delete
    suspend fun deleteBet(bet: Bet)
}
