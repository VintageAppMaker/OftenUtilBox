package oftenutilbox.viam.psw.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import java.lang.Exception

// try .. catch 간편화
fun SafeHandler( fnCode : () -> Unit, fnError : ( (String) -> Unit)? = null  ){
    try{
        fnCode()
    } catch ( e: Exception){
        if (fnError != null ) {
            fnError(e.toString())
        } else {
            // [TODO] 디폴트로 하고자 하는 기능
        }
    }
}