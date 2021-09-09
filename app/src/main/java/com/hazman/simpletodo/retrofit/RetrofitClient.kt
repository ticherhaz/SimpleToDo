package com.hazman.simpletodo.retrofit

import com.google.gson.GsonBuilder
import com.hazman.simpletodo.utils.Constant
import com.hazman.simpletodo.utils.QuickSave
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val DOMAIN = Constant.BASE_URL

    private val retrofitClient: Retrofit.Builder by lazy {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor)
            .addNetworkInterceptor { chain ->
                val tokenService = QuickSave.instance.getString(Constant.TOKEN_SERVER, null)
                if (tokenService != null) {
                    val requestBuilder = chain.request().newBuilder().addHeader(
                        "X-Authorization",
                        tokenService
                    )
                    val request: Request = requestBuilder.build()
                    return@addNetworkInterceptor chain.proceed(request)
                }
                val requestBuilder = chain.request().newBuilder()
                val request: Request = requestBuilder.build()
                return@addNetworkInterceptor chain.proceed(request)
            }
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        val gSon = GsonBuilder()
            .setLenient()
            .create()
        Retrofit.Builder()
            .baseUrl(DOMAIN)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gSon))
    }

    val apiInterface: ApiInterface by lazy {
        retrofitClient.build()
            .create(ApiInterface::class.java)
    }
}