package oftenutilbox.viam.psw.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import java.lang.Exception

object UtilBox {
    fun dpToPx(context: Context, dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
    }
}

// Toast 간소화
fun Context?.toast(s : String){
    Toast.makeText(this, s, Toast.LENGTH_LONG).show()
}

// 시스템 영역까지 침범(테마)
// 만약 위젯의 위치까지 침범하고자 한다면
// style 파일에 다음을 적용하면 된다.
//        <item name="android:windowTranslucentStatus">true</item>
//        <item name="android:windowTranslucentNavigation">false</item>
fun Activity.setOverSystemMenu() {
    fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
    }
    if (Build.VERSION.SDK_INT >= 19) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
    if (Build.VERSION.SDK_INT >= 21) {
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = Color.TRANSPARENT
    }
}

// View의 Height 설정
fun View.setHeight(context : Context, value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.height = UtilBox.dpToPx(context, value.toFloat()).toInt()
        layoutParams = lp
    }
}

// View의 width 설정
fun View.setWidth(context : Context, value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.width = UtilBox.dpToPx(context, value.toFloat()).toInt()
        layoutParams = lp
    }
}

// try .. catch 간편화
fun SafeHandler( fnCode : () -> Unit, fnError : (String) -> Unit = {}  ){
    try{
        fnCode()
    } catch ( e: Exception){
        fnError(e.toString())
    }
}