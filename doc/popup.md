### Popup window
> 간편하게 Popup window를 사용하기

![](popup.gif)


- 정의) SimpleBox->ViewExt.kt
~~~kotlin

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

~~~

- 사용예제) NestedScrollCustomActivity.kt

[NestedScrollCustomAcitivity](/app/src/main/java/oftenutilbox/viam/psw/example/NestedScrollCustomActivity.kt)
~~~kotlin
        toView.setOnClickListener {
            quickPopup(toView){
                fnDismiss ->
                val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val view = inflater.inflate(R.layout.popup_item, null)

                view.findViewById<TextView>(R.id.text_menu1)?.apply{
                    setOnClickListener {
                        toast("text1 click!")
                        fnDismiss()
                    }
                }

                // width, height가 0이면 wrap_contents
                return@quickPopup PopupInfo(view, 0, 40, 30, 0)
            }
        }
~~~

