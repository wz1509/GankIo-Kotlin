package me.jokey.threebears.gankio.di.component

import dagger.Component
import me.jokey.threebears.gankio.di.module.GankModule
import me.jokey.threebears.gankio.ui.fragment.GankFragment
import me.jokey.threebears.gankio.ui.fragment.GirlFragment

/**
 * Created time 2017/10/24.
 * @author threeBears
 */
@Component(modules = arrayOf(GankModule::class), dependencies = arrayOf(NetworkComponent::class))
interface GankComponent {

    fun inject(gankFragment: GankFragment)

    fun inject(girlFragment: GirlFragment)
}