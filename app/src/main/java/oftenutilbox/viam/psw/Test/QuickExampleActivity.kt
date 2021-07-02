package oftenutilbox.viam.psw.Test

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class QuickExampleActivity : AppCompatActivity() {
    companion object{
        var fnSetup : ( (View)-> Unit )-> Unit = {}
        fun launch(ctx : Context, fnSetup : ( (View)-> Unit )-> Unit ){
            Intent(ctx, QuickExampleActivity::class.java).apply {
                QuickExampleActivity.fnSetup = fnSetup
                ctx.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fnSetup(::setContentView)
    }
}
