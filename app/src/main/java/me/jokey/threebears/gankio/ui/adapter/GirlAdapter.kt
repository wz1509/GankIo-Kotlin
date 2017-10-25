package me.jokey.threebears.gankio.ui.adapter

import android.content.Context
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
class GirlAdapter(context: Context) :
        BaseRecyclerViewAdapter<GankEntity>(context, R.layout.rv_item_gank_girl) {

    override fun onBindViewHolder(holder: BaseViewHolder, data: GankEntity, position: Int) {
        Glide.with(context)
                .load(data.url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.glide_pic_default)
                .error(R.drawable.glide_pic_failed)
                .into(holder.getView(R.id.photo_item_image))
    }

}