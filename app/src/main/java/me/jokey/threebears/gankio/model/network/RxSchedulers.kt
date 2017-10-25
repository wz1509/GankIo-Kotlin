package me.jokey.threebears.gankio.model.network

import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created time 2017/10/23.
 * @author threeBears
 */
class RxSchedulers {

    companion object {

        /**
         * 主线程切换到异步线程
         */
        fun <T> ioMain(): ObservableTransformer<T, T> {
            return ObservableTransformer { upstream ->
                upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            }
        }
    }

}