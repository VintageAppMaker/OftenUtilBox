package oftenutilbox.viam.psw.Test

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.UiThread
import com.test.psw.oftenutilbox.R
import kotlinx.coroutines.Job
import oftenutilbox.viam.psw.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testSystemOveray()
        testErrorHandler()
        testViewSize()
        testPref()
        testCustomSpinner()

        testCoroutine()

        testLayoutChange().apply { this() }
    }

    var job   : Job = Job()
    private fun testCoroutine() {
        val txtWatch = findViewById<TextView>(R.id.txtWatch)
        job = UiStopWatch(2, 30, { min, sec ->
            runOnUiThread {
                txtWatch.setText("${min}분${sec}초")
            }
        })
        txtWatch.setOnClickListener {
            job.cancel()
            toast("stop")
        }
    }

    private fun testCustomSpinner() {
        val spn = findViewById<Spinner>(R.id.spnCustom)
        spn.setCustomAdapter(this, mutableListOf("1", "2", "3", "4"), "선택안됨")
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


    // 변수관리 귀찮아서 closure
    private fun testLayoutChange() : ()->Unit{
        var bIsHidden = true

        return{
            fun updateLayout(lst : List<View>, b :Boolean) {
                val parentView = lst[0].parent
                if (parentView !is ViewGroup) return

                for (i in 0 until parentView.childCount) {
                    parentView
                            .getChildAt(i)
                            .takeIf { !( it in lst)  }
                            ?.visibility = if (b) View.GONE else View.VISIBLE
                }
            }

            fun doLayoutChanges() {
                val txtWatch = findViewById<TextView>(R.id.txtWatch)

                val mainLooper = Looper.getMainLooper()
                val isAlreadyMainLooper = Looper.myLooper() == mainLooper
                val btn = findViewById<Button>(R.id.btnChangeLayout)

                if (isAlreadyMainLooper) {
                    updateLayout(listOf(txtWatch, btn), bIsHidden)
                } else {
                    val handler = Handler(mainLooper)
                    handler.post( { updateLayout(listOf(txtWatch, btn), bIsHidden) } )
                }

                bIsHidden = !bIsHidden
            }

            findViewById<Button>(R.id.btnChangeLayout)?.apply {
                setOnClickListener {
                    doLayoutChanges()
                }
            }
        }

    }

}