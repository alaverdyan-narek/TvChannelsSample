package com.tvchannels.sample.data.di

import com.tvchannels.sample.domain.persistant.IPrefManager
import com.tvchannels.sample.data.persistent.PrefManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
@Module
@InstallIn(SingletonComponent::class)
interface IPersistentDataModule {
    @Binds
    fun bindPrefManager(impl: PrefManagerImpl): IPrefManager
}