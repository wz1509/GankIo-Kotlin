package me.jokey.threebears.gankio.ui.fragment

import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.fragment_tab.*
import me.jokey.threebears.gankio.R
import me.jokey.threebears.gankio.ui.adapter.ViewPagerAdapter
import java.util.*

/**
 * Created time 2017/10/23.
 * @author threeBears
 */
class GankTabFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_tab

    override fun initData(savedInstanceState: Bundle?) {
        val categoryList = Arrays.asList("Android", "iOS", "前端", "休息视频", "拓展资源")
        val fragmentList = categoryList.map { GankFragment.newGankFragment(it) }
        Log.d("wz", "fragmentList size = " + fragmentList.size)
        val adapter = ViewPagerAdapter(childFragmentManager, categoryList, fragmentList)
        viewPager.adapter = adapter
        viewPager.currentItem = 0
        viewPager.offscreenPageLimit = fragmentList.size - 1
        tabLayout.setupWithViewPager(viewPager)
    }
}