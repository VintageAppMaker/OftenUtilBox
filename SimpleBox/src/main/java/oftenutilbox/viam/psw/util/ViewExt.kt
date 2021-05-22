package oftenutilbox.viam.psw.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.BaseAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.test.psw.simplebox.R


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

fun View.setHeight(context : Context, value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.height = dpToPx(value.toFloat())
        layoutParams = lp
    }
}
fun Spinner.setCustomAdapter(context : Context, lst : MutableList<String>, unselectedTitle : String = "" , defaultHeight: Int = 40){
    class CustomSpnAdapter : BaseAdapter {
        var lst : MutableList<String> = mutableListOf<String>()
        var context : Context
        var unselectedTitle : String

        constructor (context: Context, lst: MutableList<String>, unselectedTitle: String){
            this.context = context
            this.lst     = lst
            this.unselectedTitle = unselectedTitle
        }

        override fun getCount(): Int {
            return lst.size
        }

        override fun getItemId(p0: Int): Long {
            return 0
        }

        override fun getItem(n: Int): String {
            return lst[n]
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun getView(n: Int, p1: View?, p2: ViewGroup?): View {
            val v = LayoutInflater.from(context).inflate(R.layout.custom_spinner_item1, null)
            v.findViewById<TextView>(R.id.text_first)?.apply {
                text = lst[n]


                // ** 이 부분을 처리하지 않으면
                // ** spinner background가 커스텀 item으로 치환된다.
                // ** XML에서도 background를 같게 지정해주어야 한다.
                if(p2 is Spinner){

                    background = context.getDrawable(R.drawable.bg_spinner)
                    if(p2.selectedItemPosition < 0 ){
                        p2.setHeight(context, defaultHeight)
                        setTextColor(Color.parseColor("#626466"))
                        text = unselectedTitle
                    }
                }
            }
            return v
        }
    }

    adapter = CustomSpnAdapter(context, lst, unselectedTitle)
    setHeight(context, defaultHeight)
    this.setSelection(-1)
}

fun layoutToggle(unchangeList : List<View>, bToggle : Boolean) {
    fun changeLayout(b :Boolean) {
        val parentV = unchangeList[0].parent
        if (parentV !is ViewGroup) return

        for (i in 0 until parentV.childCount) {
            parentV
                .getChildAt(i)
                .takeIf { !( it in unchangeList)  }
                ?.visibility = if (b) View.GONE else View.VISIBLE
        }
    }

    val mainLooper   = Looper.getMainLooper()
    val isMainLooper = Looper.myLooper() == mainLooper

    if (isMainLooper) {
        changeLayout(bToggle)
    } else {
        val handler = Handler(mainLooper)
        handler.post( { changeLayout(bToggle) } )
    }
}

fun View.setConstraint(fnSet : (ConstraintLayout.LayoutParams) -> Unit) {
    if( layoutParams is ConstraintLayout.LayoutParams == false) return
    ( layoutParams as ConstraintLayout.LayoutParams).let{
        fnSet(it)
    }

    requestLayout()
}