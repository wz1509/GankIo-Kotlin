package me.jokey.threebears.gankio.ui.activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import me.jokey.threebears.gankio.R
import me.jokey.threebears.gankio.ui.fragment.GankTabFragment
import me.jokey.threebears.gankio.ui.fragment.GirlFragment


/**
 * Created time 2017/10/23.
 * @author threeBears
 */
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var mGankTabFragment: Fragment? = null
    private var mGirlFragment: Fragment? = null

    override fun getLayout(): Int = R.layout.activity_main

    override fun initData(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)
        setDefaultFragment(0)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        when (item.itemId) {
            R.id.nav_gank_io_category -> {
                setDefaultFragment(0)
            }
            R.id.nav_gank_io_girl -> {
                setDefaultFragment(1)
            }
            R.id.nav_about -> {

            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (mGankTabFragment == null && fragment is GankTabFragment) {
            mGankTabFragment = fragment
        }
        if (mGirlFragment == null && fragment is GirlFragment) {
            mGirlFragment = fragment
        }
    }

    /**
     * 设置默认的Fragment
     *
     * @param index 选项卡的标号：id
     */
    private fun setDefaultFragment(index: Int) {
        //开启事务
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        hideFragments(fragmentTransaction)
        when (index) {
            0 -> if (mGankTabFragment == null) {
                mGankTabFragment = GankTabFragment()
                fragmentTransaction.add(R.id.frameLayout, mGankTabFragment)
            } else {
                fragmentTransaction.show(mGankTabFragment)
            }
            1 -> if (mGirlFragment == null) {
                mGirlFragment = GirlFragment()
                fragmentTransaction.add(R.id.frameLayout, mGirlFragment)
            } else {
                fragmentTransaction.show(mGirlFragment)
            }
            else -> {
            }
        }
        fragmentTransaction.commit()   // 事务提交
    }

    /**
     * 隐藏Fragment
     *
     * @param fragmentTransaction 事务
     */
    private fun hideFragments(fragmentTransaction: FragmentTransaction) {
        if (mGankTabFragment != null) fragmentTransaction.hide(mGankTabFragment)

        if (mGirlFragment != null) fragmentTransaction.hide(mGirlFragment)
    }

}