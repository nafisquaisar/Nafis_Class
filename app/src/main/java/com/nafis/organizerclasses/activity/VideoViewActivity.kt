package com.nafis.organizerclasses.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.nafis.organizerclasses.databinding.ActivityVideoViewBinding


class VideoViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoViewBinding
    private var customView: View? = null
    private var customViewCallback: WebChromeClient.CustomViewCallback? = null
    private lateinit var fullscreenContainer: FrameLayout

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoViewBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.animationView.setFailureListener { throwable ->
            Log.e(
                "LottieError",
                "Error loading animation",
                throwable
            )
        }

        // Full-screen container for custom view
        fullscreenContainer = FrameLayout(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        binding.root.addView(fullscreenContainer)

        // Retrieve video URL and other data from Intent
        val url = intent.getStringExtra("VideoUrl") ?: ""
        val title = intent.getStringExtra("TitleName") ?: "Untitled"
        val des = intent.getStringExtra("Des") ?: title
        val chapterName = intent.getStringExtra("ChapterName") ?: "Unknown Chapter"
        val toolbar=binding.toolbarforActivity.toolbarForAll
        setSupportActionBar(toolbar)
        if(supportActionBar!=null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle(chapterName)
        }

        binding.videotitle.text=title
        binding.videodesc.text=des

        binding.apply   {
            // Enable JavaScript for WebView
            val webSettings: WebSettings = webView.settings
            webSettings.javaScriptEnabled = true
            webSettings.loadWithOverviewMode = true
            webSettings.useWideViewPort = true
            webSettings.domStorageEnabled = true
            webSettings.mediaPlaybackRequiresUserGesture = false // Allow auto-playing media

            // WebViewClient to handle opening URLs within WebView
            webView.webViewClient = WebViewClient()

            webView.webViewClient = object : WebViewClient() {

                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    // Check if the URL contains "youtube.com" or "youtu.be"
                    if (url.contains("youtube.com") || url.contains("youtu.be")) {
                        // Open the URL in the YouTube app if installed, or in a browser
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                        return true  // Indicate that we've handled this URL
                    }
                    // Otherwise, allow WebView to load the URL
                    return false
                }
            }

            // WebChromeClient to handle full-screen video support
            webView.webChromeClient = object : WebChromeClient() {
                override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                    // Enter full-screen mode
                    if (customView != null) {
                        callback?.onCustomViewHidden()
                        return
                    }
                    // Hide status bar and navigation bar (immersive mode)
                    hideSystemUI()
                    customView = view
                    customViewCallback = callback
                    fullscreenContainer.addView(view)
                    webView.visibility = View.GONE
                    toolbarforActivity.toolbarForAll.visibility=View.GONE
                    textLL.visibility=View.GONE
                    fullscreenContainer.visibility = View.VISIBLE

                    // Rotate to landscape when entering full-screen
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                }

                override fun onHideCustomView() {
                    // Exit full-screen mode
                    customView?.let {
                        fullscreenContainer.removeView(it)
                        customView = null
                    }
                    showSystemUI()
                    webView.visibility = View.VISIBLE
                    textLL.visibility=View.VISIBLE
                    toolbarforActivity.toolbarForAll.visibility=View.VISIBLE
                    fullscreenContainer.visibility = View.GONE
                    customViewCallback?.onCustomViewHidden()

                    // Rotate back to portrait when exiting full-screen
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                }
            }

            // Load the video URL
            val videoId = extractVideoIdFromUrl(url)
            if (videoId.isNotEmpty()) {
                val embedUrl = "https://www.youtube.com/embed/$videoId?modestbranding=1&rel=0"
                webView.loadUrl(embedUrl)
            } else {
                webView.loadData(
                    "<html><body><h2>Invalid Video URL</h2></body></html>",
                    "text/html",
                    "UTF-8"
                )
            }

        }
    }

    // Helper function to extract YouTube video ID from different types of URLs
    private fun extractVideoIdFromUrl(url: String): String {
        return when {
            url.contains("youtu.be/") -> {
                url.substringAfter("youtu.be/").substringBefore("?")
            }
            url.contains("youtube.com/watch?v=") -> {
                url.substringAfter("v=").substringBefore("&")
            }
            else -> {
                ""
            }
        }
    }

    // Handle back press to exit full-screen mode
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBackPressed() {
        if (customView != null) {
            binding.webView.webChromeClient?.onHideCustomView()
        } else {
            super.onBackPressed()
        }
    }


    // Hide system UI (status bar and navigation bar)
    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let {
                it.hide(android.view.WindowInsets.Type.statusBars() or android.view.WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    )
        }
    }

    // Show system UI (status bar and navigation bar)
    private fun showSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.show(android.view.WindowInsets.Type.statusBars() or android.view.WindowInsets.Type.navigationBars())
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id=item.itemId

        if(id== android.R.id.home)
        {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

}
