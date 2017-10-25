package me.jokey.threebears.gankio.ui.activity

import android.os.Bundle
import android.support.annotation.LayoutRes
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 * Created time 2017/10/23.
 * @author threeBears
 */
abstract class BaseActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        initData(savedInstanceState)
    }

    @LayoutRes
    protected abstract fun getLayout(): Int

    protected abstract fun initData(savedInstanceState: Bundle?)
}