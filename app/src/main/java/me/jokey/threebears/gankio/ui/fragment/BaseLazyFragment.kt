package me.jokey.threebears.gankio.ui.fragment

import android.content.Context
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
abstract class BaseLazyFragment : RxFragment(), BaseView {

    private var mRootView: View? = null
    protected var mContext: Context ?= null
    private var isVisibleLazy: Boolean = false
    private var isPrepared: Boolean = false
    private var isFirst = true

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isPrepared = true
        initPrepare(savedInstanceState)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            isVisibleLazy = true
            lazyLoad()
        } else {
            isVisibleLazy = false
            onInvisible()
        }
    }

    override fun onResume() {
        super.onResume()
        if (userVisibleHint) {
            userVisibleHint = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (mRootView == null) {
            mRootView = inflater!!.inflate(getLayoutId(), container, false)
        }
        return mRootView
    }

    /**
     * 懒加载
     */
    private fun lazyLoad() {
        if (!isPrepared || !isVisible || !isFirst) return

        initData()
        isFirst = false
    }

    protected abstract fun getLayoutId(): Int

    /**
     * 在onActivityCreated中调用的方法，可以用来进行初始化操作。
     */
    protected abstract fun initPrepare(savedInstanceState: Bundle?)

    /**
     * fragment被设置为不可见时调用
     */
    protected abstract fun onInvisible()

    /**
     * 这里获取数据，刷新界面
     */
    protected abstract fun initData()

    override fun <T> bindToLife(): LifecycleTransformer<T> = bindToLifecycle()

}