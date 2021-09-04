package oftenutilbox.viam.psw.Test

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.test.psw.oftenutilbox.R

class QuickExampleActivity : AppCompatActivity() {
    companion object{
        var fnSetup : ( (View)-> Unit )-> Unit = {}
        var bTrans  : Boolean = false
        fun launch(ctx : Context, fnSetup : ( (View)-> Unit )-> Unit, bTransparent : Boolean =false ){
            Intent(ctx, QuickExampleActivity::class.java).apply {
                QuickExampleActivity.fnSetup = fnSetup
                bTrans  = bTransparent
                ctx.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (bTrans){
            setTheme(R.style.Normal_transparent)
        }
        super.onCreate(savedInstanceState)
        fnSetup(::setContentView)

    }
}
