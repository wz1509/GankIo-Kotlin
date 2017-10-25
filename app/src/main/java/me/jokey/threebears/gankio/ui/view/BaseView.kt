package me.jokey.threebears.gankio.ui.view

import com.trello.rxlifecycle2.LifecycleTransformer

/**
 * Created time 2017/10/23.
 * @author threeBears
 */
interface BaseView {

    /**
     * 绑定生命周期
     */
    fun <T> bindToLife(): LifecycleTransformer<T>
}