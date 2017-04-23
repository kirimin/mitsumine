package me.kirimin.mitsumine._common.network

import me.kirimin.mitsumine.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val defaultClient: OkHttpClient

    init {
        val httpClient = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
        }
        defaultClient = httpClient.build()
    }

    fun default(endPoint: EndPoint): Retrofit.Builder {
        return Retrofit.Builder()
                .baseUrl(endPoint.value)
                .client(defaultClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
    }

    enum class EndPoint(val value:String) {
        ENTRY_INFO("http://b.hatena.ne.jp")
    }
}
