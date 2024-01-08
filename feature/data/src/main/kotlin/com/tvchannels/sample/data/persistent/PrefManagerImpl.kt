package com.tvchannels.sample.data.persistent

import android.content.SharedPreferences
import com.tvchannels.sample.domain.persistant.IPrefManager
import com.tvchannels.sample.domain.persistant.ISharedPrefHolder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefManagerImpl @Inject constructor(
    preferences: SharedPreferences
) : IPrefManager, ISharedPrefHolder {

    override val preferences: SharedPreferences by lazy { preferences }


    override fun clearData() {}
}