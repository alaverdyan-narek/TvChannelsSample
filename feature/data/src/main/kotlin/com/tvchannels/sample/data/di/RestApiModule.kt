package com.tvchannels.sample.data.di

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.tvchannels.sample.data.network.api.ChannelApi
import com.tvchannels.sample.data.network.interceptor.SessionInterceptor
import com.tvchannels.sample.domain.persistant.IPrefManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.tvchannels.sample.data.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RestApiModule {

    private const val TIMEOUT_CONNECTION_SECONDS = 20L
    private const val TIMEOUT_READ_SECONDS = 20L
    private const val TIMEOUT_WRITE_SECONDS = 20L


    @Provides
    @Singleton
    fun provideRetrofit(
        jsonConfigs: Json
    ): Retrofit = createRetrofit(jsonConfigs)




    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        sessionInterceptor: SessionInterceptor,
        cache: Cache,
    ): OkHttpClient = OkHttpClient.Builder()
        .retryOnConnectionFailure(true)
        .followRedirects(true)
        .followSslRedirects(true)
        .connectTimeout(TIMEOUT_CONNECTION_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_READ_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_WRITE_SECONDS, TimeUnit.SECONDS)
        .cache(cache)
        .addInterceptor(sessionInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideJsonConfigurations(): Json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        explicitNulls = false
        encodeDefaults = true
    }

    private fun createRetrofit(
        json: Json
    ): Retrofit {

        val converterFactory = json.asConverterFactory("application/json".toMediaType())

        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(converterFactory)
            .build()
    }

    private inline fun <reified T> createApiService(
        retrofit: Retrofit,
        okHttpClient: OkHttpClient
    ): T {
        return retrofit.newBuilder().client(okHttpClient).build().create(T::class.java)
    }

    @Provides
    @Singleton
    fun provideSessionInterceptor() = SessionInterceptor()

    @Provides
    @Singleton
    fun provideNetworkIntercepter(@ApplicationContext context: Context): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val request = chain.request()
            val newRequest = if (hasNetworkConnection(context)) {
                request.newBuilder()
                    .header("Cache-Control", "public, max-age=" + 60)
                    .build()
            } else {
                request.newBuilder()
                    .header(
                        "Cache-Control",
                        "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                    )
                    .build()
            }
            chain.proceed(newRequest)
        }
    }

    @Singleton
    @Provides
    fun provideChannelsApi(retrofit: Retrofit, okHttpClient: OkHttpClient): ChannelApi =
        createApiService(retrofit, okHttpClient)




    @Singleton
    @Provides
    fun provideCache(@ApplicationContext context: Context): Cache {
        val cacheSize = 10 * 1024 * 1024.toLong() // 10 MB
        val httpCacheDirectory = File(context.cacheDir, "http-cache")
        return Cache(httpCacheDirectory, cacheSize)
    }

   @SuppressLint("MissingPermission")
   private fun hasNetworkConnection(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork);
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true;
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true;
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true;
                    }
                }
            }
        } else {
            try {
                val activeNetworkInfo = connectivityManager?.activeNetworkInfo;
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    return true;
                }
            } catch (e: Exception) {
            }
        }
        return false;
    }

}
