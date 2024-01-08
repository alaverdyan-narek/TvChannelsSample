package com.tvchannels.sample.domain.persistant

import android.content.SharedPreferences

/**
 * Holds only an instance of [SharedPreferences]
 * Do not put values that should be stored in shared preferences
 * Use [IPrefManager] for that instead
 */
interface ISharedPrefHolder {
    val preferences: SharedPreferences
}
