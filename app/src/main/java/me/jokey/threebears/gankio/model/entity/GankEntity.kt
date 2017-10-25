package me.jokey.threebears.gankio.model.entity

import com.google.gson.annotations.SerializedName

/**
 * Created time 2017/10/23.
 * @author threeBears
 */
data class GankEntity(@SerializedName("_id")
                      var id: String,
                      var desc: String,
                      var url: String,
                      var who: String? = null,
                      var type: String,
                      var publishedAt: String,
                      var images: List<String>? = null) {

    override fun toString(): String {
        return "GankEntity(id='$id', desc='$desc', url='$url', who='$who', type='$type', " +
                "publishedAt='$publishedAt', images=$images)"
    }
}