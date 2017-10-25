package me.jokey.threebears.gankio.model.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created time 2017/10/24.
 * @author threeBears
 */
class ServiceFactory {

    companion object {

        private val baseUrl = "http://gank.io/api/"

        fun <T> createRxRetrofitService(clazz: Class<T>): T {
            val httpClientBuilder = OkHttpClient.Builder()
            //设置超时时间
            httpClientBuilder.connectTimeout(15 * 1000, TimeUnit.MILLISECONDS)
            httpClientBuilder.writeTimeout(15 * 1000, TimeUnit.MILLISECONDS)
            httpClientBuilder.readTimeout(15 * 1000, TimeUnit.MILLISECONDS)
            return Retrofit
                    .Builder()
                    .client(httpClientBuilder.build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(baseUrl)
                    .build()
                    .create(clazz)
        }
    }

}