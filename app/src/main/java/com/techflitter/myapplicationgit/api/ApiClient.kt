package com.techflitter.myapplicationgit.api


import android.util.Log
import com.google.gson.GsonBuilder
import com.techflitter.myapplicationgit.BuildConfig
import com.techflitter.myapplicationgit.utils.Constants
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiClient {

    lateinit var retrofit: Retrofit

    private var client: OkHttpClient? = null

    private val timeout = 30

    private var logging = HttpLoggingInterceptor()


    fun createRetrofit(): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        retrofit = Retrofit.Builder()
            .baseUrl(Constants.URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(getHttpLogClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit
    }

    private fun getHttpLogClient(): OkHttpClient {

        val httpClient = OkHttpClient().newBuilder()
            .connectTimeout(timeout.toLong(), TimeUnit.SECONDS)
            .readTimeout(timeout.toLong(), TimeUnit.SECONDS)
            .writeTimeout(timeout.toLong(), TimeUnit.SECONDS)
       /* // Create a Custom Interceptor to apply Headers application wide
        val headerInterceptor = object : Interceptor {

            override fun intercept(chain: Interceptor.Chain): Response {

                var request = chain.request()
                Log.e(
                    "AUTH_TOKEN_IN_HEADER",
                    "ApiClient -  Authorization = Bearer TOKEN = ${pref.TOKEN} - User_id= ${pref.USER_ID}"
                )

                var tokens = ""
                if (!pref.TOKEN.isNullOrEmpty()) {
                    tokens = pref.TOKEN
                }
                if (!tokens.isNullOrEmpty()) {
                    request = request.newBuilder()
                        .addHeader(
                            "Authorization",
                            "Token ${tokens}"
                        )
                        .build()
                }
                val response = chain.proceed(request)
                return response
            }
        }
        httpClient.addInterceptor(headerInterceptor)*/

        try {
            val logging = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            }
            httpClient.addInterceptor(logging)
        } catch (ex: Exception) {
            Log.e(
                "AUTH_TOKEN_IN_HEADER",
                "ApiClient - getHttpLogClient =Exception- ${ex.localizedMessage}"
            )
        }
        return httpClient.build()
    }
}
