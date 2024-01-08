package com.tvchannels.sample.data.di

import com.tvchannels.sample.data.delegate.FavoriteActionDelegate
import com.tvchannels.sample.domain.delegate.IFavoriteActionDelegate
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface IDelegateModule {
    @Binds
    fun bindFavoriteActionDelegate(impl: FavoriteActionDelegate): IFavoriteActionDelegate

}