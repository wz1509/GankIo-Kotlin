package me.jokey.threebears.gankio.ui.adapter.base

import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView



/**
 * Created time 2017/10/24.
 * @author threeBears
 */
class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var mViews: SparseArray<View> = SparseArray()

    companion object {

        fun get(view: View): BaseViewHolder = BaseViewHolder(view)
    }

    operator fun get(parent: ViewGroup, layoutId: Int): BaseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return get(itemView)
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    fun <T : View> getView(@IdRes viewId: Int): T {
        var view = mViews.get(viewId)
        if (view == null) {
            view = itemView.findViewById<View>(viewId)
            mViews.put(viewId, view)
        }
        return (view as T?)!!
    }

    fun setTextView(viewId: Int, text: String): BaseViewHolder {
        val view = getView<TextView>(viewId)
        view.text = text
        return this
    }

}