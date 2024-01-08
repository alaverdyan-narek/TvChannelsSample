package com.tvchannels.sample.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException

class SessionInterceptor  : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder()
                .header(X_KEY, X_KEY_VALUE)
                .build()
        )
    }
    companion object{
        private const val X_KEY = "X-Key"
        private const val X_KEY_VALUE = "fh3487klskhjk2fh782kjhsdi72knjwfk7i2efdjbm"
    }
}
