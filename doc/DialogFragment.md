### DialogFragment
> 간편하게 DialogFragment를 사용하기

DialogFragment를 상속받아 화면구성하는 부분만 외부함수처리하면 편리하게 사용할 수 있다고 생각했다. 그래서 QuickDialog를 다음과 같이 구현했다.

![](dialog.gif)


- 정의) SimpleBox->QuickDialog.kt
~~~kotlin

package oftenutilbox.viam.psw.util

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.test.psw.simplebox.R

class QuickDialog:  DialogFragment(){

    private lateinit var fnSetup : ( ()-> Unit ) -> View?
    var fnInitUI : ( ()-> Unit ) -> View?
        get() = fnSetup
        set(value ){
            fnSetup = value
        }

    private var _data : Any? = null
    var paramData : Any?
        get() = _data
        set(value) {_data = value}

    // Layout에서 MATCH_PARENT로 설정되어 있어야 한다.
    // 그리고 생성자나 QShow를 호출하기 이전에 실행하면
    // 전체 Dialog가 된다.
    fun setEnableFullMode(){
        setStyle(STYLE_NO_TITLE, R.style.dialog_fullscreen)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // 이 코드를 실행하지 않으면
        // XML에서 round 처리를 했어도 적용되지 않는다.
        setBackTransparent()
        return fnInitUI(::dismiss)
    }

    private fun setBackTransparent() {
        if (getDialog() != null && getDialog()!!.getWindow() != null) {
            getDialog()!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            getDialog()!!.getWindow()!!.requestFeature(
                Window.FEATURE_NO_TITLE
            );
        }
    }

    fun QShow(fm : FragmentManager, title: String, f : ( ()-> Unit ) -> View? ){
        fnInitUI = f
        show(fm, title)
    }

}

~~~

- 사용예제) NestedScrollCustomAcitivity.kt
[NestedScrollCustomAcitivity](/app/src/main/java/oftenutilbox/viam/psw/example/activity/NestedScrollCustomActivity.kt)
~~~kotlin
QuickDialog().apply {
    QShow(this@NestedScrollCustomActivity.supportFragmentManager, "test") {
        fnDismiss ->
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.dialog_quick, null)

        view?.apply{
            setOnClickListener {
                txt.text = "clicked!!"
                fnDismiss()
            }
        }
        return@QShow view
    }
}
~~~

