package me.jokey.threebears.gankio.model.entity

/**
 * Created time 2017/10/24.
 * @author threeBears
 */
data class HttpResult<T>(private var error: Boolean, var results: List<T>){

    fun isSuccess(): Boolean = !error

}