package me.jokey.threebears.gankio.di.module

import dagger.Module
import dagger.Provides
import me.jokey.threebears.gankio.model.network.GankService
import me.jokey.threebears.gankio.model.network.ServiceFactory
import javax.inject.Singleton

/**
 * Created time 2017/10/24.
 * @author threeBears
 */
@Module
class NetworkModule {

    @Provides
    fun provideGankService(): GankService = ServiceFactory.createRxRetrofitService(GankService::class.java)

}