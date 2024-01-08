package com.tvchannels.sample.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tvchannels.sample.data.db.entity.ChannelDbEntity

@Dao
interface ChannelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: ChannelDbEntity)

    @Query("SELECT * FROM channel_entity")
    fun getAll(): List<ChannelDbEntity>

    @Query("SELECT * FROM channel_entity WHERE id=:id")
    fun getChannelData(id: Long): ChannelDbEntity?

    @Delete()
    suspend fun delete(user: ChannelDbEntity)

    @Query("DELETE FROM channel_entity WHERE id=:id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM channel_entity")
    suspend fun clear()

}