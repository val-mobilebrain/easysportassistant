package com.easysport.assistantappnewmb.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Bet::class], version = 1, exportSchema = false)
abstract class BetDatabase : RoomDatabase() {
    abstract fun betDao(): BetDao

    companion object {
        @Volatile
        private var INSTANCE: BetDatabase? = null

        fun getInstance(context: Context): BetDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    BetDatabase::class.java,
                    "bets.db"
                ).build().also { INSTANCE = it }
            }
    }
}
