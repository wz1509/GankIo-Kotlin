package me.jokey.threebears.gankio.model.network

import io.reactivex.Observable
import me.jokey.threebears.gankio.model.entity.GankEntity
import me.jokey.threebears.gankio.model.entity.HttpResult
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created time 2017/10/23.
 * @author threeBears
 */
interface GankService {


    @GET("data/{category}/{count}/{page}")
    fun getGankList(@Path("category") category: String,
                             @Path("count") count: Int,
                             @Path("page") page: Int): Observable<HttpResult<GankEntity>>

}