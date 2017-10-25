package me.jokey.threebears.gankio.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created time 2017/10/23.
 * @author threeBears
 */
class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private lateinit var titleList: List<String>
    private lateinit var fragmentList: List<Fragment>

    constructor(fm: FragmentManager, titleList: List<String>, fragmentList: List<Fragment>) : this(fm) {
        this.titleList = titleList
        this.fragmentList = fragmentList
    }

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getCount(): Int = fragmentList.size

    override fun getPageTitle(position: Int): CharSequence = titleList[position]

}