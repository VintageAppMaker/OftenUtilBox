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

class QuickDialog:  DialogFragment(){

    private lateinit var fnSetup : ( ()-> Unit ) -> View?
    var fnInitUI : ( ()-> Unit ) -> View?
        get() = fnSetup
        set(value ){
            fnSetup = value
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