package com.tvchannels.sample.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "channel_entity")
data class ChannelDbEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Long ,
    @ColumnInfo("name")
    val name:String,
    @ColumnInfo("image_url")
    val imageUrl:String,
    @ColumnInfo("video_url")
    val videoUrl:String,
    @ColumnInfo("created_at")
    val createdAt: Long = System.currentTimeMillis()
)