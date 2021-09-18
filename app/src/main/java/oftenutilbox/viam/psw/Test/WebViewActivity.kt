package oftenutilbox.viam.psw.Test

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.*
import com.test.psw.oftenutilbox.R
import com.test.psw.oftenutilbox.databinding.ActivityWebViewBinding
import oftenutilbox.viam.psw.util.toast

class WebViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityWebViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWebViewBinding.inflate(layoutInflater)
        binding.apply {
            webView.apply {
                setBackgroundColor(Color.TRANSPARENT)
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                    }

                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                        // 주소파싱 후,
                        // 이동없음 -> true, 이동하기 -> false
                        return false
                    }
                }

                settings.apply {
                    javaScriptEnabled = true
                    builtInZoomControls = false
                    displayZoomControls = false
                    cacheMode = WebSettings.LOAD_DEFAULT
                    allowFileAccess = false
                    allowUniversalAccessFromFileURLs = false
                    allowFileAccessFromFileURLs = false
                    domStorageEnabled = true // Viam 2021-09-15 오후 3:29 : 매우중요함. JQuery를 위해서 반드시 필요함.
                }

                addJavascriptInterface(AndroidJavascriptInterface(), "android")
                setWebChromeClient(WebChromeClient())

                loadUrl("http://www.google.com")

            }
        }
        setContentView(binding.root)
    }

    inner class AndroidJavascriptInterface{
        @JavascriptInterface
        fun test(s:String){
            toast(s)
        }
    }
}