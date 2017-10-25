package me.jokey.threebears.gankio.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.activity_girl_detail.*
import me.jokey.threebears.gankio.R

class GirlDetailActivity : BaseActivity() {

    companion object {

        private val keyTitle = "key_title"
        private val keyUrl = "key_url"

        fun newIntent(context: Context, title: String, url: String): Intent {
            val intent = Intent(context, GirlDetailActivity::class.java)
            intent.putExtra(keyTitle, title)
            intent.putExtra(keyUrl, url)
            return intent
        }
    }

    override fun getLayout(): Int = R.layout.activity_girl_detail

    override fun initData(savedInstanceState: Bundle?) {
        toolbar.title = intent.getStringExtra(keyTitle)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener({ onBackPressed() })

        Glide.with(this)
                .load(intent.getStringExtra(keyUrl))
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.glide_pic_failed)
                .into(photoView)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0及以上
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4到5.0
            val localLayoutParams = window.attributes
            localLayoutParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or
                    localLayoutParams.flags
        }
    }

}
