package me.kirimin.mitsumine._common.network

import me.kirimin.mitsumine.BuildConfig
import me.kirimin.mitsumine._common.domain.model.Account
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer
import se.akerfeldt.okhttp.signpost.SigningInterceptor


object RetrofitClient {

    private val defaultClient: OkHttpClient

    init {
        val clientBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) setLoggingInterceptor(clientBuilder)
        defaultClient = clientBuilder.build()
    }

    fun default(endPoint: EndPoint): Retrofit.Builder {
        return Retrofit.Builder()
                .baseUrl(endPoint.value)
                .client(defaultClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
    }

    fun authClient(endPoint: EndPoint, account: Account): Retrofit.Builder {
        val clientBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) setLoggingInterceptor(clientBuilder)
        val consumer = OkHttpOAuthConsumer(BuildConfig.OAUTH_KEY, BuildConfig.OAUTH_SECRET)
        consumer.setTokenWithSecret(account.token, account.tokenSecret)
        clientBuilder.addInterceptor(SigningInterceptor(consumer))
        val authClient = clientBuilder.build()
        return Retrofit.Builder()
                .baseUrl(endPoint.value)
                .client(authClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
    }

    private fun setLoggingInterceptor(clientBuilder: OkHttpClient.Builder) {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(logging)
    }

    enum class EndPoint(val value:String) {
        API("http://b.hatena.ne.jp"),
        REST_API("http://api.b.hatena.ne.jp"),
        STAR("http://s.hatena.com"),
        BOOKMARK_COUNT("http://api.b.st-hatena.com")
    }
}
