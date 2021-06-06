package oftenutilbox.viam.psw.Test

import android.content.Context
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

    }
}