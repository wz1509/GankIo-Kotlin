package me.jokey.threebears.gankio.di.module

import dagger.Module
import dagger.Provides
import me.jokey.threebears.gankio.contract.GankContract

/**
 * Created time 2017/10/24.
 * @author threeBears
 */
@Module
class GankModule(var view: GankContract.View) {

    @Provides
    fun provideView(): GankContract.View = view

}