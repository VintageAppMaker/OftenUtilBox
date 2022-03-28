package oftenutilbox.viam.psw.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.test.psw.oftenutilbox.R
import oftenutilbox.viam.psw.util.NewScrollView
import oftenutilbox.viam.psw.util.attachFragments

class ViewPagerTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager_test)

        findViewById<NewScrollView>(R.id.scrMain)?.apply {
            header = findViewById(R.id.stick_header)
        }

        findViewById<ViewPager>(R.id.vPager)?.apply {
            this.attachFragments(
                this@ViewPagerTestActivity.supportFragmentManager,
                listOf(BlankFragment(), BlankFragment(), BlankFragment()),
                {n ->  showMessage("$n")})
        }
    }

    fun showMessage(s : String){
        Toast.makeText(this, "$s", Toast.LENGTH_LONG).show()
    }
}