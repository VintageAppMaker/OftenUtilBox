package oftenutilbox.viam.psw.example

import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.test.psw.oftenutilbox.R
import oftenutilbox.viam.psw.util.*

class NestedScrollCustomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nested_scroll_custom)

        // 하단 네비게이션 메뉴 색상변경
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setBottomSystemBarColor(Color.parseColor("#FFFF33"))
        }

        findViewById<NewScrollView>(R.id.scrMain)?.apply {
            header = findViewById(R.id.stick_header)
        }

        findViewById<TextView>(R.id.txtMoney)?.apply {
            text = convertMoneyComma(12334511)

            val txt = this
            setOnClickListener {
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
            }
        }

        findViewById<ImageView>(R.id.imageGreen)?.apply {
            setOnClickListener {
                QuickDialog().apply {
                    // XML(dialog_quick2)은 전체화면 디자인
                    // QShow 이전에 호출
                    setEnableFullMode()
                    QShow(this@NestedScrollCustomActivity.supportFragmentManager, "test2") {
                            fnDismiss ->
                        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                        val view = inflater.inflate(R.layout.dialog_quick2, null)

                        view?.apply{
                            setOnClickListener {
                                fnDismiss()
                            }
                        }
                        return@QShow view
                    }
                }
            }
        }

        val toView = findViewById<ImageView>(R.id.more)
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

        val txtLineCount = findViewById<TextView>(R.id.txtLineCount)
        txtLineCount.addOnLayoutChangeListener { view, i, i2, i3, i4, i5, i6, i7, i8 ->
            // lineCount는 레이아웃이 완료된 시점에서 재대로 동작한다.
            val cnt = txtLineCount.lineCount
            val LIMITLINE= 5
            if (cnt > LIMITLINE) {
                val s  = txtLineCount.text
                var s2 = ""
                s.split("\n").forEachIndexed{ index, data -> if (index < LIMITLINE) s2 += "${data}\n"  }
                txtLineCount.text = s2
            }
        }

    }
}