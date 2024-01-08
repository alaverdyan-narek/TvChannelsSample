package com.tvchannels.sample.data.di

import com.tvchannels.sample.data.repository.ChannelsRepositoryImpl
import com.tvchannels.sample.domain.repository.IChannelsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindChannelsRepository(impl: ChannelsRepositoryImpl): IChannelsRepository
}