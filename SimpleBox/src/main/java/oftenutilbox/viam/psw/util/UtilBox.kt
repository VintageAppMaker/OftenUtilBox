package oftenutilbox.viam.psw.util

import android.content.Context
import android.widget.Toast

object UtilBox {

}

fun Context?.toast(s : String){
    Toast.makeText(this, s, Toast.LENGTH_LONG).show()
}