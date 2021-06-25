package oftenutilbox.viam.psw.util

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// 하단 Dialog
open class QuickBottomDialog:  BottomSheetDialogFragment(){

    private lateinit var fnSetup : ( ()-> Unit ) -> View?
    private var _data : Any? = null
    var paramData : Any?
        get() = _data
        set(value) {_data = value}

    // 실행 시, expanded하게 보이기 위해서 보관한다.
    private lateinit var dlg : BottomSheetDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // 이 코드를 실행하지 않으면
        // XML에서 round 처리를 했어도 적용되지 않는다.
        dlg = ( super.onCreateDialog(savedInstanceState).apply {
            // window?.setDimAmount(0.2f) // Set dim amount here
            setOnShowListener {
                val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                bottomSheet.setBackgroundResource(android.R.color.transparent)
            }
        } ) as BottomSheetDialog
        return dlg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return fnSetup(::dismiss)
    }

    // 단지 이름을 통일해서 외부에서
    // 사용하기 위한 메소드
    open fun launch(fm : FragmentManager, title : String, fnCallBack : (Any)-> Unit = {} ){

    }

    fun QShow(fm : FragmentManager, title: String, fnInitView : (()-> Unit ) -> View? ){
        this.fnSetup = fnInitView
        show(fm, title)
    }

    override fun onStart() {
        super.onStart()
        dlg.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}