package me.jokey.threebears.gankio.ui.fragment

import android.app.ActivityOptions
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.ImageView
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
import me.jokey.threebears.gankio.ui.activity.GirlDetailActivity
import me.jokey.threebears.gankio.ui.adapter.GirlAdapter
import javax.inject.Inject

/**
 * Created time 2017/10/24.
 * @author threeBears
 */
class GirlFragment : BaseFragment(), GankContract.View, OnReloadClickListener,
        OnItemClickListener<GankEntity> {

    private val category = "福利"
    private val count = 15

    private var isRefresh: Boolean = true
    private var isLoadMore: Boolean = false
    private var page = 1

    private lateinit var adapter: GirlAdapter

    @Inject
    lateinit var presenter: GankPresenter

    override fun getLayoutId(): Int = R.layout.layout_refresh_rv

    override fun initData(savedInstanceState: Bundle?) {
        initPresenter()
        initRecyclerView()
        presenter.getGankList(category, count, page)
    }

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
        swipeRefreshLayout.setOnRefreshListener({
            isRefresh = true
            page = 1
            presenter.getGankList(category, count, page)
        })
        adapter = GirlAdapter(activity)
        adapter.setOnReloadClickListener(this)
        adapter.setOnItemClickListener(this)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(rv: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(rv, newState)
                val lastVisibleItemPosition = (rv!!.layoutManager as StaggeredGridLayoutManager)
                        .findLastVisibleItemPositions(null)
                val visibleItemCount = rv.layoutManager.childCount
                val totalItemCount = rv.layoutManager.itemCount

                val isMore = !isRefresh && !isLoadMore && visibleItemCount > 0 &&
                        newState == RecyclerView.SCROLL_STATE_IDLE &&
                        (lastVisibleItemPosition[0] >= totalItemCount - 1 ||
                                lastVisibleItemPosition[1] >= totalItemCount - 1)
                if (isMore) {
                    isLoadMore = true
                    isRefresh = false
                    presenter.getGankList(category, count, page)
                    adapter.setLoading()
                }
            }
        })
    }

    override fun onClick() {
        adapter.setLoading()
        presenter.getGankList(category, count, page)
    }

    override fun onItemClick(view: View, data: GankEntity) {
        val imageView = view.findViewById<ImageView>(R.id.photo_item_image)
        val intent = GirlDetailActivity.newIntent(activity, data.desc, data.url)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val options = ActivityOptions.makeSceneTransitionAnimation(
                    activity, imageView, getString(R.string.transition_photos))
            ActivityCompat.startActivity(activity, intent, options.toBundle())
        } else {
            val options = ActivityOptionsCompat.makeScaleUpAnimation(
                    imageView, imageView.width / 2, imageView.height / 2, 0, 0)
            ActivityCompat.startActivity(activity, intent, options.toBundle())
        }
    }

    override fun onResultGankList(list: List<GankEntity>) {
        closeRefreshing()
        if (isRefresh) {
            if (list.isNotEmpty()) {
                adapter.setData(list)
                page++
            } else {
                Toast.makeText(context, "暂无数据", Toast.LENGTH_SHORT).show()
            }
        } else {
            if (list.isNotEmpty()) {
                adapter.addMoreData(list)
                page++
            } else {
                adapter.setNotMore()
            }
        }
        isRefresh = false
        isLoadMore = false
    }

    override fun onFailed(errorMsg: String) {
        closeRefreshing()
        if (adapter.itemCount == 0) {
            Toast.makeText(context, "加载出错：" + errorMsg, Toast.LENGTH_SHORT).show()
        } else {
            adapter.setNetError()
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