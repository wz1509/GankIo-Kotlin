package me.jokey.threebears.gankio.listener

import android.view.View

/**
 * Created time 2017/10/25.
 * @author threeBears
 */
interface OnItemClickListener<in T> {

    fun onItemClick(view: View, data: T)

}