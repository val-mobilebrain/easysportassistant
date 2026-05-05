package com.easysport.assistantappnewmb.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bets")
data class Bet(
    @PrimaryKey val id: Long = System.currentTimeMillis(),
    val date: String,
    val name: String,
    val odds: String,
    val stake: Double,
    val result: String
)
