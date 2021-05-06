package oftenutilbox.viam.psw.Test

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

    }

    private fun testViewSize() {
        val txtHello = findViewById<TextView>(R.id.message)
        txtHello.setWidth(this, 100)
        txtHello.setHeight(this, 100)
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