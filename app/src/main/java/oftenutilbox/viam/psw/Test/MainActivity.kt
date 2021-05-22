package oftenutilbox.viam.psw.Test

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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

        testConstraint()
    }

    private fun testConstraint() {
        val button_1 = findViewById<Button>(R.id.button_1)
        val button_2 = findViewById<Button>(R.id.button_2)

        // app:layout_constraintBottom_toBottomOf="parent"
        //                    app:layout_constraintEnd_toEndOf="parent"
        //                    app:layout_constraintHorizontal_bias="0.052"
        //                    app:layout_constraintStart_toStartOf="parent"
        //                    app:layout_constraintTop_toTopOf="parent"
        //                    app:layout_constraintVertical_bias="1.0"

        // 기존 contraint가 남아있으면 영향을 받음. parent가 id 있어야 함
        button_2.setConstraint {
            params ->
            params.endToEnd     = (button_1.parent as ConstraintLayout).id
            params.startToStart = button_1.id
            params.topToTop = (button_1.parent as ConstraintLayout).id
        }
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
            val txtWatch = findViewById<TextView>(R.id.txtWatch)
            val btn = findViewById<Button>(R.id.btnChangeLayout)

            btn?.apply {
                setOnClickListener {
                    layoutToggle(listOf(txtWatch, btn), bIsHidden)
                    bIsHidden = !bIsHidden
                }
            }
        }

    }

}