package me.jokey.threebears.gankio.contract

import me.jokey.threebears.gankio.model.entity.GankEntity
import me.jokey.threebears.gankio.ui.view.BaseView

/**
 * Created time 2017/10/24.
 * @author threeBears
 */
interface GankContract {

    interface View : BaseView {

        /**
         * 成功
         *
         * @param list 集合
         */
        fun onResultGankList(list: List<GankEntity>)

        /**
         * 失败
         *
         * @param errorMsg 错误描述
         */
        fun onFailed(errorMsg: String)
    }

    interface Presenter {

        /**
         * 请求 gank.io list
         *
         * @param category 类别
         * @param count    数量
         * @param page     页码
         */
        fun getGankList(category: String, count: Int, page: Int)

    }

}