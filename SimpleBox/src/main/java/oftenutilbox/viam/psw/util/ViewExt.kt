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

fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)
fun Context.dpToPx(dp: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()

// View의 Height 설정
fun View.setHeightDp(value: Int) {
    val lp = layoutParams
    lp?.let {
        it.height = dpToPx(value.toFloat())
        layoutParams = it
    }
}

// View의 width 설정
fun View.setWidthDp(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.width = dpToPx(value.toFloat())
        layoutParams = lp
    }
}

fun View.setMarginDp(left: Float? = null, top: Float? = null, right: Float? = null, bottom: Float? = null) {
    layoutInfo<ViewGroup.MarginLayoutParams> {
        left?.let    { leftMargin   = dpToPx(it) }
        top?.let     { topMargin    = dpToPx(it) }
        right?.let   { rightMargin  = dpToPx(it) }
        bottom?.let  { bottomMargin = dpToPx(it) }
    }
}

fun View.setPaddingDp(left: Float = 0f, top: Float = 0f, right: Float = 0f, bottom: Float = 0f) {
    setPadding(dpToPx(left), dpToPx(top), dpToPx(right), dpToPx(bottom))
}

inline fun <reified T : ViewGroup.LayoutParams> View.layoutInfo(fnCode: T.() -> Unit) {
    if (layoutParams is T) fnCode(layoutParams as T)
}