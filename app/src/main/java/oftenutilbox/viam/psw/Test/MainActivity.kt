package oftenutilbox.viam.psw.Test

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.test.psw.oftenutilbox.R
import oftenutilbox.viam.psw.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testSystemOveray()
        testErrorHandler()
        testViewSize()
        testPref()

    }


    // pref 사용
    val ISFIRST_RUN = "ISFIRST_RUN"
    var SharedPreferences.isFirst: Boolean
        get() = getBoolean(ISFIRST_RUN, true)
        set(value) = edit { putBoolean(ISFIRST_RUN, value) }

    private fun testPref() {
        if ( pref.isFirst ){
            pref.isFirst = false
        } else{
            toast("not first")
        }

    }

    private fun testViewSize() {
        val txtHello = findViewById<TextView>(R.id.message)
        txtHello.setWidthDp(300)
        txtHello.setHeightDp(40)
        txtHello.setMarginDp(10F, 5F, 1F, 1F)
        txtHello.setPaddingDp(30F, 10F, 10F, 10F)
    }

    private fun testSystemOveray() {
        setOverSystemMenu()
    }

    private fun testErrorHandler() {
        var name : String? = null
        SafeHandler({
            toast(name!!)
        })

        SafeHandler({
            val sum = 1 / 0
        }, { e -> toast(e) })
    }

}