package me.jokey.threebears.gankio.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import kotlinx.android.synthetic.main.activity_webview.*
import me.jokey.threebears.gankio.R

class GankDetailActivity : BaseActivity() {

    companion object {
        private val keyTitle = "key_title"
        private val keyUrl = "key_url"

        fun startActivity(context: Context, title: String, url: String) {
            val intent = Intent(context, GankDetailActivity::class.java)
            intent.putExtra(keyTitle, title)
            intent.putExtra(keyUrl, url)
            context.startActivity(intent)
        }
    }

    override fun getLayout(): Int = R.layout.activity_webview

    override fun initData(savedInstanceState: Bundle?) {
        initToolbar()
        initWebSettings()
    }

    private fun initToolbar() {
        val title = intent.getStringExtra(keyTitle)
        toolbar.title = title

        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener({ finish() })
        toolbar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_share) {
                val url = intent.getStringExtra(keyUrl)
                val shareText = title + "\n" + url + "\n\t\t\t  - 来自GankIo的分享"
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享")
                intent.putExtra(Intent.EXTRA_TEXT, shareText)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(Intent.createChooser(intent, "分享"))
                return@OnMenuItemClickListener true
            }
            false
        })
    }

    private fun initWebSettings() {
        val url = intent.getStringExtra(keyUrl)
        val webSettings = webView.settings
        //设置自适应屏幕，两者合用
        //将图片调整到适合webview的大小
        webSettings.useWideViewPort = true
        // 缩放至屏幕的大小
        webSettings.loadWithOverviewMode = true
        //缩放操作
        //支持缩放，默认为true。是下面那个的前提。
        webSettings.setSupportZoom(true)
        //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.builtInZoomControls = true
        //隐藏原生的缩放控件
        webSettings.displayZoomControls = false
        //其他细节操作
        /*
          缓存模式如下：
          LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
          LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
          LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
          LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        //关闭webView中缓存
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        //设置可以访问文件
        webSettings.allowFileAccess = true
        //支持通过JS打开新窗口
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        //支持自动加载图片
        webSettings.loadsImagesAutomatically = true
        //设置编码格式
        webSettings.defaultTextEncodingName = "utf-8"
        webView.loadUrl(url)
        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.url.toString())
                    return true
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        progressBar.progress = 0
        progressBar.max = 100
        progressBar.visibility = View.VISIBLE
        webView.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                progressBar.progress = newProgress
                progressBar.visibility = if (newProgress == 100) View.GONE else View.VISIBLE
            }

            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                toolbar.title = title
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_share, menu)
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onResume() {
        super.onResume()
        if (webView != null) {
            webView.settings.javaScriptEnabled = true
            webView.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (webView != null) {
            webView.settings.javaScriptEnabled = false
            webView.onPause()
        }
    }

    override fun onDestroy() {
        // 在 Activity 销毁（ WebView ）的时候，
        // 先让 WebView 加载null内容，然后移除 WebView，再销毁 WebView，最后置空
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            webView.clearHistory()
            (webView.parent as ViewGroup).removeView(webView)
            webView.destroy()
        }
        super.onDestroy()
    }

}
