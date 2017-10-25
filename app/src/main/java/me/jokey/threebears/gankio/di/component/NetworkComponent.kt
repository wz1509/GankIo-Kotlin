package me.jokey.threebears.gankio.di.component

import dagger.Component
import me.jokey.threebears.gankio.di.module.NetworkModule
import me.jokey.threebears.gankio.model.network.GankService

/**
 * Created time 2017/10/24.
 * @author threeBears
 */
@Component(modules = arrayOf(NetworkModule::class))
interface NetworkComponent {

    fun getGankService(): GankService

}