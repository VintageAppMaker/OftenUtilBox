package oftenutilbox.viam.psw.Test

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.test.psw.oftenutilbox.R

class TestExampleActivity : AppCompatActivity() {
    companion object{
        var fnSetup : ( (View)-> Unit )-> Unit = {}
        fun launch(ctx : Context, fnSetup : ( (View)-> Unit )-> Unit ){
            val intent = Intent(ctx, TestExampleActivity::class.java).apply {
                TestExampleActivity.fnSetup = fnSetup
                ctx.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fnSetup(::setContentView)
    }
}