package oftenutilbox.viam.psw.Test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.psw.oftenutilbox.R
import oftenutilbox.viam.psw.util.SafeHandler
import oftenutilbox.viam.psw.util.setOverSystemMenu
import oftenutilbox.viam.psw.util.toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setOverSystemMenu()

        testToast()
        testErrorHandler()
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

    private fun testToast() {
        toast("Test")
    }
}