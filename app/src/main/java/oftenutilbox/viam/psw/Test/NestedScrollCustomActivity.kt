package oftenutilbox.viam.psw.Test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.psw.oftenutilbox.R
import oftenutilbox.viam.psw.util.NewScrollView

class NestedScrollCustomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nested_scroll_custom)


        findViewById<NewScrollView>(R.id.scrMain)?.apply {
            header = findViewById(R.id.stick_header)
        }

    }
}