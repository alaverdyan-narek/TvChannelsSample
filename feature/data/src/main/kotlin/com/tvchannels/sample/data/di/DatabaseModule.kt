package com.tvchannels.sample.data.di

import android.app.Application
import com.tvchannels.sample.data.db.TvChannelDB
import com.tvchannels.sample.data.db.dao.ChannelDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideTvChannelDB(application: Application): TvChannelDB {
        return TvChannelDB.getInstance(application)
    }

    @Provides
    fun provideChannelsDao(db: TvChannelDB): ChannelDao {
        return db.channelDao()
    }

}