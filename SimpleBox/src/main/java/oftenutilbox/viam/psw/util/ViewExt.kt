package oftenutilbox.viam.psw.util

import android.app.Activity
import android.appwidget.AppWidgetHost
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.test.psw.simplebox.R
import androidx.viewpager.widget.ViewPager.OnPageChangeListener





// Toast 간소화
fun Context?.toast(s: String){
    Toast.makeText(this, s, Toast.LENGTH_LONG).show()
}

// 시스템 영역까지 침범(테마)
// 만약 위젯의 위치까지 침범하고자 한다면
// style 파일에 다음을 적용하면 된다.
//        <item name="android:windowTranslucentStatus">true</item>
//        <item name="android:windowTranslucentNavigation">false</item>
fun Activity.setOverSystemMenu(backCololor : Int = 0 ) {
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
        window.statusBarColor = if (backCololor == 0 ) Color.TRANSPARENT else backCololor
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

// 커스텀 스피너
fun Spinner.setCustomAdapter(context: Context, lst: MutableList<String>, unselectedTitle: String = "", defaultHeight: Int = 40){
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
                        p2.setHeightDp(defaultHeight)
                        setTextColor(Color.parseColor("#626466"))
                        text = unselectedTitle
                    }
                }
            }
            return v
        }
    }

    adapter = CustomSpnAdapter(context, lst, unselectedTitle)
    setHeightDp(defaultHeight)
    this.setSelection(-1)
}

// 특정뷰만 제외하고 GONE/VISIBLE
fun layoutToggle(unchangeList: List<View>, bToggle: Boolean) {
    fun changeLayout(b: Boolean) {
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
        handler.post({ changeLayout(bToggle) })
    }
}

// constrain layout 제어
fun View.setConstraint(fnSet: (View, ConstraintLayout.LayoutParams) -> Unit) {
    if( layoutParams is ConstraintLayout.LayoutParams == false) return
    ( layoutParams as ConstraintLayout.LayoutParams).let{
        fnSet(this, it)
    }

    requestLayout()
}

// 영역밖 그리기 enable
fun View.setAllParentsClip() {
    var p = parent
    while (p is ViewGroup) {
        p.clipChildren = false
        p.clipToPadding = false
        p = p.parent
    }
}

// 하단 메시지
fun Context.showBottomMessage(s: String, dimEnable : Boolean = true, height : Float = 50f){
    BottomSheetDialog(this).apply {
        val dialogView = layoutInflater.inflate(R.layout.simple_bottom_message, null)
        dialogView?.apply {
            findViewById<TextView>(R.id.txtMessage)?.apply {
                text = s
            }

            findViewById<ImageView>(R.id.close_button)?.apply {
                setOnClickListener {
                    dismiss()
                }
            }
        }

        // 배경에 dim 효과 (여러 개 호출 시, dim이 한 개라도 설정되어있으면 모두 dim으로 됨)
        if (dimEnable == false){
            val window = getWindow()
            window!!.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
            window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }

        setContentView(dialogView)
        dialogView.getLayoutParams().height = dpToPx(height)

        show()

    }
}

// pop을 관리하기 싫어서 closure로 생성함
data class PopupInfo(
    val v : View,
    val width  : Int,
    val height : Int,
    val x  : Int,
    val y : Int
 )

// makePopupClosure를 한 번에 실행
fun Context.quickPopup(toView: View, fnSetup : (()->Unit ) -> PopupInfo){
    fun makePopupClosure(toView: View, fnSetup : (()->Unit ) -> PopupInfo) : ()->Unit{
        var pop : PopupWindow? = null
        fun dismiss() = pop?.dismiss()
        return {

            val popInfo = fnSetup(::dismiss)

            val width  = if(popInfo.width == 0 ){
                ViewGroup.LayoutParams.WRAP_CONTENT
            } else dpToPx(popInfo.width.toFloat())

            val height = if(popInfo.height == 0 ){
                ViewGroup.LayoutParams.WRAP_CONTENT
            } else dpToPx(popInfo.height.toFloat())

            pop = PopupWindow(
                    popInfo.v,
                    width,
                    height,
                    true
            ).apply {
                showAsDropDown(toView, popInfo.x, popInfo.y)
            }
        }
    }

    makePopupClosure(toView){
        dismiss ->
        fnSetup(dismiss)
    }.apply { this() }
}

// 폰트설정
fun TextView.setUserFont(fontName: String) {
    setTypeface(Typeface.createFromAsset(context.assets, "fonts/$fontName.ttf"))
}

// 하단 시스템 바 색상변경
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Activity.setBottomSystemBarColor(c : Int){
    getWindow().setNavigationBarColor(c)
}

// 하단 시스템 바 색상변경
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun DialogFragment.setBottomSystemBarColor(c : Int){
    dialog?.getWindow()?.setNavigationBarColor(c)
}

fun View.showAndHide(time : Long = 1500){
    visibility = View.VISIBLE
    startAnimation(
        AlphaAnimation(1.0f, 0.0f).apply {
            duration = time
            fillAfter = true
        }
    )
}

// 글로벌 영역에 finish 코드를 보관한다.
var fnPlayerActivityClose : () -> Unit = {}
fun Activity.regiterOnlyOnePlayer(){
    // 매우 위험한 시도. 새로운 액티비티 실행 시,
    // 이전 Activity를 종료한다.
    fnPlayerActivityClose()
    fnPlayerActivityClose = {finish()}
}

// 레퍼런스 :
// https://stackoverflow.com/questions/26370289/snappy-scrolling-in-recyclerview/33774983
fun RecyclerView.getScrollDistanceOfColumnClosestToLeft(): Int {
    val manager = layoutManager as LinearLayoutManager?
    val firstVisibleColumnViewHolder = findViewHolderForAdapterPosition(
        manager!!.findFirstVisibleItemPosition()
    ) ?: return 0
    val columnWidth = firstVisibleColumnViewHolder.itemView.measuredWidth
    val left = firstVisibleColumnViewHolder.itemView.left
    val absoluteLeft = Math.abs(left)
    return if (absoluteLeft <= columnWidth / 2) left else columnWidth - absoluteLeft
}

fun RecyclerView.setMagneticMove(nStart : Int = 0 ){
    // 로그를 찍을 때, 확장함수도 class 외의 함수이므로
    // 패키지명 + 파일명으로 파라메터를 넘긴다.
    DUtil.d(me = "oftenutilbox.viam.psw.util.ViewExt", "setMagneticMove( $nStart )")

    addOnScrollListener(object:RecyclerView.OnScrollListener(){
        var oldMoveTo : Int = 0
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            val moveTo = getScrollDistanceOfColumnClosestToLeft() - nStart
            if(newState == RecyclerView.SCROLL_STATE_IDLE){
                // Viam 2021-10-10 오전 12:54 :
                // smoothScrollBy()를 사용했을 경우,
                // 양쪽 끝자리에서 움직이면 무한루핑이 됨.
                // 그래서 이전값과 비교함.
                if(moveTo !== oldMoveTo){
                    recyclerView.smoothScrollBy(moveTo, 0)
                    oldMoveTo = moveTo
                }
            }
        }
    })
}

fun ViewPager.attachFragments(fm : FragmentManager, lst : List<Fragment>, fnOnSelected : (Int)->Unit){
    var fragmentList : List<Fragment> = lst
    class FragmentAdapter (fm : FragmentManager): FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int = fragmentList.size

        override fun getPageTitle(position: Int): CharSequence? {
            return ""
        }
    }

    this.adapter = FragmentAdapter(fm)
    addOnPageChangeListener(object : OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {}
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            fnOnSelected(position)
        }
    })
}