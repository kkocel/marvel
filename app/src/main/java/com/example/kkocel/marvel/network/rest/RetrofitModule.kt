package com.example.kkocel.marvel.network.rest

import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit

class RetrofitModule {

    private val PUBLIC_KEY = "6a7ed890b4b941a925202a5630d5b162"
    private val PRIVATE_KEY = "0f1d0fdf46a0bf32f962b0b9997233c0395cdf8e"

    companion object {
        var baseUrl: String? = null
    }

    fun provideBaseUrl(): String {
        return baseUrl ?: "http://gateway.marvel.com/v1/public/"
    }

    fun provideRetrofit(): Retrofit = makeRetrofit(addRequiredQuery())

    fun makeRetrofit(vararg interceptors: Interceptor): Retrofit = Retrofit.Builder()
            .baseUrl(provideBaseUrl())
            .client(makeHttpClient(interceptors))
            .addConverters()
            .build()

    fun Retrofit.Builder.addConverters(): Retrofit.Builder = this
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

    private fun makeHttpClient(interceptors: Array<out Interceptor>) = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(headersInterceptor())
            .apply { interceptors().addAll(interceptors) }
            .addInterceptor(loggingInterceptor())
            .build()

    fun addRequiredQuery() = Interceptor { chain ->
        val originalRequest = chain.request()
        val timestamp = System.currentTimeMillis().toString()
        chain.proceed(originalRequest
                .newBuilder()
                .url(originalRequest.url().newBuilder()
                                        .addQueryParameter("apikey", PUBLIC_KEY)
                                        .addQueryParameter("hash", calculatedMd5AuthParameter(timestamp))
                                        .addQueryParameter("ts", timestamp)
                                        .build())
                .build())
    }

    fun loggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    fun headersInterceptor() = Interceptor { chain ->
        chain.proceed(chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Accept-Language", "en")
                .addHeader("Content-Type", "application/json")
                .build())
    }

    // Actually this code could be simplified to static hash of md5(constant ts + private key + public key)
    fun calculatedMd5AuthParameter(timeStamp: String): String {
        val messageDigest = getMd5Digest(timeStamp + PRIVATE_KEY + PUBLIC_KEY)
        val md5 = BigInteger(1, messageDigest).toString(16)
        return "0" * (32 - md5.length) + md5
    }

    private fun getMd5Digest(str: String): ByteArray = try {
        MessageDigest.getInstance("MD5").digest(str.toByteArray())
    } catch (e: NoSuchAlgorithmException) {
        // TODO: Log to Crashlytics?
        byteArrayOf()
    }

    private operator fun String.times(i: Int) = (1..i).fold("", { acc, _ -> acc + this })
}