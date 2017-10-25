package me.jokey.threebears.gankio.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.layout_refresh_rv.*
import me.jokey.threebears.gankio.R
import me.jokey.threebears.gankio.contract.GankContract
import me.jokey.threebears.gankio.di.component.DaggerGankComponent
import me.jokey.threebears.gankio.di.component.DaggerNetworkComponent
import me.jokey.threebears.gankio.di.module.GankModule
import me.jokey.threebears.gankio.di.module.NetworkModule
import me.jokey.threebears.gankio.listener.OnItemClickListener
import me.jokey.threebears.gankio.listener.OnReloadClickListener
import me.jokey.threebears.gankio.model.entity.GankEntity
import me.jokey.threebears.gankio.presenter.GankPresenter
import me.jokey.threebears.gankio.ui.activity.GankDetailActivity
import me.jokey.threebears.gankio.ui.adapter.GankAdapter
import javax.inject.Inject

/**
 * Created time 2017/10/23.
 * @author threeBears
 */
class GankFragment : BaseLazyFragment(), GankContract.View, OnReloadClickListener,
        OnItemClickListener<GankEntity> {

    private val count = 15

    private var isRefresh: Boolean = true
    private var isLoadMore: Boolean = false
    private var page = 1
    lateinit var mAdapter: GankAdapter

    @Inject
    lateinit var presenter: GankPresenter

    companion object {
        private val keyCategory = "category"

        fun newGankFragment(category: String): GankFragment {
            val args = Bundle()
            args.putString(this.keyCategory, category)
            val gankFragment = GankFragment()
            gankFragment.arguments = args
            return gankFragment
        }
    }

    override fun getLayoutId(): Int = R.layout.layout_refresh_rv

    override fun initPrepare(savedInstanceState: Bundle?) {
        initPresenter()
        initRecyclerView()
    }

    override fun onInvisible() {

    }

    override fun initData() {
        presenter.getGankList(getCategory(), count, page)
    }

    private fun getCategory() = arguments.getString(keyCategory)

    private fun initPresenter() {
        DaggerGankComponent.builder()
                .networkComponent(DaggerNetworkComponent.builder().networkModule(NetworkModule())
                        .build())
                .gankModule(GankModule(this))
                .build()
                .inject(this)
    }

    private fun initRecyclerView() {
        swipeRefreshLayout.isRefreshing = true
//        swipeRefreshLayout.setColorSchemeColors(resources.getIntArray(R.array.swipeRefreshLayoutColor))
        // 下拉刷新事件
        swipeRefreshLayout.setOnRefreshListener({
            isRefresh = true
            page = 1
            presenter.getGankList(getCategory(), count, page)
        })
        recyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter = GankAdapter(context)
        mAdapter.setOnReloadClickListener(this)

        mAdapter.setOnItemClickListener(this)
        recyclerView.adapter = mAdapter
        // 滑到底部监听事件
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(rv: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(rv, newState)
                val lm = rv!!.layoutManager as LinearLayoutManager
                val totalItemCount = rv.adapter.itemCount
                val lastVisibleItemPosition = lm.findLastVisibleItemPosition()
                val visibleItemCount = rv.childCount
                if (!isRefresh && !isLoadMore && newState == RecyclerView.SCROLL_STATE_IDLE &&
                        lastVisibleItemPosition == totalItemCount - 1 && visibleItemCount > 0) {
                    isLoadMore = true
                    isRefresh = false
                    mAdapter.setLoading()
                    presenter.getGankList(getCategory(), count, page)
                }
            }
        })
    }

    /**
     * 加载更多 异常处理
     */
    override fun onClick() {
        mAdapter.setLoading()
        presenter.getGankList(getCategory(), count, page)
    }

    /**
     * item单击事件
     */
    override fun onItemClick(view: View, data: GankEntity) {
        GankDetailActivity.startActivity(activity, data.desc, data.url)
    }

    override fun onResultGankList(list: List<GankEntity>) {
        closeRefreshing()
        if (isRefresh) {
            if (list.isNotEmpty()) {
                mAdapter.setData(list)
                page++
            } else {
                Toast.makeText(context, "暂无数据", Toast.LENGTH_SHORT).show()
            }
        } else {
            if (list.isNotEmpty()) {
                mAdapter.addMoreData(list)
                page++
            } else {
                mAdapter.setNotMore()
            }
        }
        isRefresh = false
        isLoadMore = false
    }

    override fun onFailed(errorMsg: String) {
        closeRefreshing()
        if (mAdapter.itemCount == 0) {
            Toast.makeText(context, "加载出错：" + errorMsg, Toast.LENGTH_SHORT).show()
        } else {
            mAdapter.setNetError()
        }
    }

    /**
     * 关闭 SwipeRefreshLayout 下拉动画
     */
    private fun closeRefreshing() {
        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
        }
    }

}