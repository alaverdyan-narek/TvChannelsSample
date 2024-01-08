package com.tvchannels.sample.domain.utils

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class RealTimeCache<K, E> {

    private val cachedData = mutableMapOf<K, E>()
    private val cacheMutex = Mutex()

    suspend fun putSafeValue(key: K, element: E) {
        cacheMutex.withLock {
            cachedData.put(key, element)
        }
    }

    suspend fun getSafeValue(key: K) = cacheMutex.withLock {
        cachedData[key]
    }

    suspend fun removeSafe(key: K) = cacheMutex.withLock {
        cachedData.remove(key)
    }

    suspend fun clearSafe() = cacheMutex.withLock {
        cachedData.clear()
    }

    fun getNow(key: K)= if(cacheMutex.isLocked) null else cachedData[key]

}
