package me.jokey.threebears.gankio.presenter

import me.jokey.threebears.gankio.contract.GankContract
import me.jokey.threebears.gankio.model.network.GankService
import me.jokey.threebears.gankio.model.network.RxSchedulers
import javax.inject.Inject

/**
 * Created time 2017/10/24.
 * @author threeBears
 */

class GankPresenter : GankContract.Presenter {

    private var view: GankContract.View
    private var gankService: GankService

    @Inject
    constructor(view: GankContract.View, gankService: GankService) {
        this.view = view
        this.gankService = gankService
    }

    override fun getGankList(category: String, count: Int, page: Int) {
        gankService.getGankList(category, count, page)
                .compose(RxSchedulers.ioMain())
                .compose(view.bindToLife())
                .subscribe({ result ->
                    if (result.isSuccess()) {
                        view.onResultGankList(result.results)
                    } else {
                        view.onFailed("null")
                    }
                }, { throwable ->
                    view.onFailed(throwable.message!!)
                })
    }
}