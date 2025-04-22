package ru.practicum.android.diploma.data.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig

object RetrofitClient {
    private const val BASE_URL = "https://api.hh.ru/"

    fun create(): IApiService {
        val authInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder().apply {
                addHeader("Authorization", "Bearer ${BuildConfig.HH_ACCESS_TOKEN}")
                addHeader("HH-User-Agent", "Application Vakio")
            }.build()

            val response = chain.proceed(request)
            response
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IApiService::class.java)
    }
}
