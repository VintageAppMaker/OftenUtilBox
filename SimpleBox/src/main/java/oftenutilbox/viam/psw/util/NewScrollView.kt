package oftenutilbox.viam.psw.util

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ScrollView
import androidx.annotation.RequiresApi
import androidx.core.widget.NestedScrollView

// 오픈소스
// https://github.com/5seunghoon/Sticky_Scroll_View_Example
class NewScrollView : NestedScrollView, ViewTreeObserver.OnGlobalLayoutListener {

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)
    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attr,
            defStyleAttr
    ) {
        overScrollMode = OVER_SCROLL_NEVER
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    var header: View? = null
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        set(value) {
            field = value
            field?.let {
                it.translationZ = 1f
                it.setOnClickListener { _ ->
                    //클릭 시, 헤더뷰가 최상단으로 오게 스크롤 이동
                    moveTop(it)
                }
            }
        }

    // 2021.05.27 viam 추가: tab을 상단으로 이동
    // View가 최상위까지 스크룰한 적이 있어야 작동함.
    // 외부제공
    fun moveTop(it: View) {
        this.smoothScrollTo(scrollX, it.top)
        callStickListener()
    }

    var stickListener: (View) -> Unit = {}
    var freeListener: (View) -> Unit = {}

    private var mIsHeaderSticky = false

    private var mHeaderInitPosition = 0f

    override fun onGlobalLayout() {
        mHeaderInitPosition = header?.top?.toFloat() ?: 0f
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)

        val scrolly = t

        if (scrolly > mHeaderInitPosition) {
            stickHeader(scrolly - mHeaderInitPosition)
        } else {
            freeHeader()
        }
    }

    private fun stickHeader(position: Float) {
        header?.translationY = position
        callStickListener()
    }

    fun callStickListener() {
        if (!mIsHeaderSticky) {
            stickListener(header ?: return)
            mIsHeaderSticky = true
        }
    }

    private fun freeHeader() {
        header?.translationY = 0f
        callFreeListener()
    }

    private fun callFreeListener() {
        if (mIsHeaderSticky) {
            freeListener(header ?: return)
            mIsHeaderSticky = false
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

}