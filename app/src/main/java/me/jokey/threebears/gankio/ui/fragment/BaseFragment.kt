package me.jokey.threebears.gankio.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.components.support.RxFragment
import me.jokey.threebears.gankio.ui.view.BaseView

/**
 * Created time 2017/10/23.
 * @author threeBears
 */
abstract class BaseFragment : RxFragment(), BaseView {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater?.inflate(getLayoutId(), container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData(savedInstanceState)
    }

    protected abstract fun getLayoutId(): Int

    protected abstract fun initData(savedInstanceState: Bundle?)

    override fun <T> bindToLife(): LifecycleTransformer<T> = bindToLifecycle()
}