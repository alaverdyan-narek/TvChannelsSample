package com.tvchannels.sample.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tvchannels.sample.data.db.dao.ChannelDao
import com.tvchannels.sample.data.db.entity.ChannelDbEntity

@Database(
    entities = [ChannelDbEntity::class],
    version = 1,
    exportSchema = true
)
abstract class TvChannelDB : RoomDatabase() {
    companion object {
        private const val NAME = "TvChannelDB"

        @Volatile
        private var INSTANCE: TvChannelDB? = null

        fun getInstance(
            context: Context
        ): TvChannelDB = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        private fun buildDatabase(context: Context): TvChannelDB =
            Room.databaseBuilder(context, TvChannelDB::class.java, NAME)
                .fallbackToDestructiveMigration().setJournalMode(JournalMode.WRITE_AHEAD_LOGGING)
                .build()
    }


    abstract fun channelDao(): ChannelDao


}