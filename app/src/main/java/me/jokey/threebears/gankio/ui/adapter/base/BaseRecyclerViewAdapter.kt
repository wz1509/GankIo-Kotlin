package me.jokey.threebears.gankio.ui.adapter.base

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import me.jokey.threebears.gankio.R
import me.jokey.threebears.gankio.listener.OnItemClickListener
import me.jokey.threebears.gankio.listener.OnReloadClickListener
import java.util.*

/**
 * Created time 2017/10/24.
 * @author threeBears
 */
abstract class BaseRecyclerViewAdapter<T>(val context: Context) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val typeItem = 0
    protected val typeFooter = 1

    private var mItemLayoutRes = -1
    protected var mList: MutableList<T> = ArrayList<T>()
    protected var mOnItemClickListener: OnItemClickListener<T>? = null
    protected var mOnReloadClickListener: OnReloadClickListener? = null

    private lateinit var mFooterViewHolder: FooterViewHolder

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<T>) {
        mOnItemClickListener = onItemClickListener
    }

    fun setOnReloadClickListener(onReloadClickListener: OnReloadClickListener) {
        this.mOnReloadClickListener = onReloadClickListener
    }

    constructor(context: Context, @LayoutRes itemLayoutRes: Int) : this(context) {
        this.mItemLayoutRes = itemLayoutRes
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == typeFooter) {
            val view = getView(parent, R.layout.rv_item_footer)
            mFooterViewHolder = FooterViewHolder(view)
            mFooterViewHolder
        } else {
            val view = getView(parent, mItemLayoutRes)
            val baseViewHolder = BaseViewHolder(view)
            view.setOnClickListener { v ->
                if (mOnItemClickListener != null) {
                    val position = baseViewHolder.layoutPosition
                    mOnItemClickListener!!.onItemClick(v, mList[position])
                }
            }
            baseViewHolder
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BaseViewHolder && mList.size != 0 && mList.size != position) {
            onBindViewHolder(holder, mList[position], position)
        } else {
            val layoutParams = holder.itemView.layoutParams
            if (layoutParams != null && layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                val params = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
                params.isFullSpan = true
            }
        }
    }

    /**
     * item
     *
     * @param holder   ViewHolder
     * @param data     实体数据
     * @param position 索引
     */
    protected abstract fun onBindViewHolder(holder: BaseViewHolder, data: T, position: Int)

    protected fun getView(parent: ViewGroup, layoutId: Int): View =
            LayoutInflater.from(parent.context).inflate(layoutId, parent, false)

    override fun getItemCount(): Int {
        val itemCount = mList.size
        return if (itemCount == 0) 0 else itemCount + 1
    }

    override fun getItemViewType(position: Int): Int =
            if (position + 1 == itemCount && mList.size > 0) typeFooter else typeItem


    fun setData(data: List<T>) {
        mList.clear()
        mList.addAll(data)
        notifyDataSetChanged()
    }

    fun addMoreData(data: List<T>) {
        mList.addAll(data)
        notifyDataSetChanged()
    }

    fun deleteData(position: Int) {
        mList.removeAt(position)
        notifyItemRemoved(position)
    }

    class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var progressBar: ProgressBar = itemView.findViewById(R.id.pb_loading)
        internal var prompt: TextView = itemView.findViewById(R.id.tv_prompt)
    }

    fun setLoading() {
        mFooterViewHolder.prompt.text = "正在加载更多"
        mFooterViewHolder.prompt.visibility = View.VISIBLE
        mFooterViewHolder.progressBar.visibility = View.VISIBLE
    }

    fun setNotMore() {
        mFooterViewHolder.prompt.text = "没有更多了"
        mFooterViewHolder.progressBar.visibility = View.GONE
    }

    fun setNetError() {
        mFooterViewHolder.prompt.text = "加载失败，点击重试"
        mFooterViewHolder.prompt.setOnClickListener {
            if (mOnReloadClickListener != null) {
                mOnReloadClickListener!!.onClick()
            }
        }
        mFooterViewHolder.progressBar.visibility = View.GONE
    }

}