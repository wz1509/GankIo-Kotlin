package me.jokey.threebears.gankio.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import me.jokey.threebears.gankio.R
import me.jokey.threebears.gankio.model.entity.GankEntity
import me.jokey.threebears.gankio.ui.adapter.base.BaseRecyclerViewAdapter
import me.jokey.threebears.gankio.ui.adapter.base.BaseViewHolder

/**
 * Created time 2017/10/24.
 * @author threeBears
 */
class GankAdapter(context: Context) : BaseRecyclerViewAdapter<GankEntity>(context,R.layout.activity_main) {

    private val typeGankImage = 100
    private val typeGankNoImage = 101

    override fun getItemViewType(position: Int): Int {
        if (position + 1 == itemCount && mList.size > 0) {
            return typeFooter
        } else if (mList.isNotEmpty()) {
            val list = mList[position].images
            return if (list != null && list.isNotEmpty()) {
                typeGankImage
            } else {
                typeGankNoImage
            }
        }
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                typeGankImage -> BaseViewHolder.get(getView(parent, R.layout.rv_item_gank_image))
                typeGankNoImage -> BaseViewHolder.get(getView(parent, R.layout.rv_item_gank_no_image))
                else -> super.onCreateViewHolder(parent, viewType)
            }

    override fun onBindViewHolder(holder: BaseViewHolder, data: GankEntity, position: Int) {
        Log.d("wz", data.toString())

        val imageList = data.images
        if (imageList != null && imageList.isNotEmpty()) {
            val imageView = holder.getView<ImageView>(R.id.gank_item_image)
            Glide.with(context)
                    .load(data.images!![0])
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView)
        }
        holder.setTextView(R.id.gank_item_title, data.desc)
                .setTextView(R.id.gank_item_description, "来源：" + data.who)
                .setTextView(R.id.gank_item_datetime, data.publishedAt)

        holder.itemView.setOnClickListener { view ->
            if (mOnItemClickListener != null) {
                mOnItemClickListener!!.onItemClick(view, data)
            }
        }
    }
}