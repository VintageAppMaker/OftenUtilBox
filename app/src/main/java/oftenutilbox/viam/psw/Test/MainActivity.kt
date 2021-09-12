package oftenutilbox.viam.psw.Test

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.psw.oftenutilbox.R
import com.test.psw.oftenutilbox.databinding.*
import kotlinx.coroutines.Job
import oftenutilbox.viam.psw.Test.adapter.Box
import oftenutilbox.viam.psw.Test.adapter.MagneticAdapter
import oftenutilbox.viam.psw.Test.adapter.SimpleData
import oftenutilbox.viam.psw.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testSystemNavigation()

        setButtonAction(R.id.btnViewModelTimer, {testViewModelTimer()})
        setButtonAction(R.id.btnViewResize, {testViewSize()})
        setButtonAction(R.id.btnViewRotation, {testViewRotation()})
        setButtonAction(R.id.btnMagneticRecyclerView, {testMagneticRecyclerView()})
        setButtonAction(R.id.btnAppBarlayout2Lines, {testAppbarlayout2Lines()})
        setButtonAction(R.id.btnWeightChange, {testWeightChange()})
        setButtonAction(R.id.btnViewPager, {testViewPager()})


        testErrorHandler()
        testPref()
        testCustomSpinner()

        testCoroutine()

        testLayoutChange().apply { this() }

        testConstraint()
        testBottomMessage()
        
        testNestedScroll()
        testAlphaAnimation()
        testBottomDialog()

    }

    private fun testViewPager() {
        Intent(this, ViewPagerTestActivity::class.java).apply {
            startActivity(this)
        }
    }

    private fun setButtonAction(resID : Int, fnProcess : () ->Unit){
        findViewById<Button>(resID)?.apply {
            setOnClickListener {
                fnProcess()
            }
        }
    }

    private fun testWeightChange() {
        QuickExampleActivity.launch(this, { setContent ->
            val binding: Example5Binding
            binding = Example5Binding.inflate(layoutInflater)
            setContent(binding.root)

            binding.apply {
                // 부모가 Linearlayout이고 weightsum을 지정하고 있어야 한다.

                // 3:7이었던 것을 view2로 모두 채워버린다.
                (view1.layoutParams as LinearLayout.LayoutParams).weight = 0.0f
                (view2.layoutParams as LinearLayout.LayoutParams).weight = 1.0f
            }

        })
    }

    private fun testSystemNavigation() {
        // Systembar 침범
        setOverSystemMenu()

        // 하단 네비게이션 메뉴 색상변경
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setBottomSystemBarColor(Color.parseColor("#FF99AA"))
        }
    }

    private fun testViewModelTimer() {
        QuickExampleActivity.launch(this, { setContent ->
            val binding: Example6Binding
            binding = Example6Binding.inflate(layoutInflater)
            setContent(binding.root)

            var count = 0
            val viewModel = ViewModelProvider(this@MainActivity).get(TimerViewModel::class.java)
            val timerAction : () -> Unit = { binding.txtMessage.text = "${count++}" }
            viewModel.setInterval(500)
            viewModel.timerStart(timerAction)
            binding.txtMessage.setOnClickListener {
                viewModel.apply {
                    if ( isRun ) timerStop() else timerStart(timerAction)
                }
            }
        })
    }

    private fun testAppbarlayout2Lines() {
        QuickExampleActivity.launch(this, { setContent ->
            val binding: Example4Binding
            binding = Example4Binding.inflate(layoutInflater)
            setContent(binding.root)
        })
    }

    private fun testMagneticRecyclerView() {
        QuickExampleActivity.launch(this, { setContent ->
            val binding: Example3Binding
            binding = Example3Binding.inflate(layoutInflater)
            setContent(binding.root)

            binding.apply {
                val lst = mutableListOf<SimpleData>()
                val colortable = listOf(Color.RED, Color.GRAY, Color.BLUE, Color.GREEN, Color.WHITE)
                (0..30).forEach {
                    val item = Box(color = colortable.get( it % colortable.size), alpha = it * 0.1f % 1.0f )
                    lst.add( item as SimpleData )
                }

                // 좌측에서 이동
                //recycler.setMagneticMove(dpToPx(60f) * -1)
                recycler.setMagneticMove()

                val manager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
                recycler.layoutManager = manager
                val adt = MagneticAdapter(lst, applicationContext)
                recycler.adapter = adt
            }

        })
    }

    private fun testViewRotation() {
        QuickExampleActivity.launch(this, { setContent ->
            val binding: Example2Binding
            binding = Example2Binding.inflate(layoutInflater)
            setContent(binding.root)
        })
    }

    private fun testViewSize() {

        QuickExampleActivity.launch(this, { setContent ->
            val binding: Example1Binding
            binding = Example1Binding.inflate(layoutInflater)
            setContent(binding.root)

            binding.apply {
                message.setWidthDp(300)
                message.setHeightDp(40)
                message.setMarginDp(10F, 5F, 1F, 1F)
                message.setPaddingDp(30F, 10F, 10F, 10F)
            }

        })

    }


    private fun testBottomDialog() {
        val btnBottomDialog = findViewById<Button>(R.id.btnBottomDialog)
        btnBottomDialog.setOnClickListener {
            QuickBottomDialog().apply {
                QShow(this@MainActivity.supportFragmentManager, "", {

                    fnDismiss ->
                    val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val view = inflater.inflate(R.layout.dialog_quick, null)

                    return@QShow view

                })
            }
        }
    }

    private fun testAlphaAnimation() {
        val btnAlphaAni = findViewById<Button>(R.id.btnAlphaAni)
        btnAlphaAni.setOnClickListener {
            btnAlphaAni.showAndHide(3000)
        }
    }
    private fun testNestedScroll() {
        val btnNestedScroll = findViewById<Button>(R.id.btnNestedScroll)
        btnNestedScroll.setOnClickListener {
            Intent(this, NestedScrollCustomActivity::class.java)?.apply {
                startActivity(this)
            }
        }
    }

    private fun testBottomMessage() {
        val btnShowMessage = findViewById<Button>(R.id.btnShowMessage)
        btnShowMessage.setOnClickListener {
            showBottomMessage("1\n1\n1\n1\n1\n" +
                    "1\n" +
                    "1\n" +
                    "1\n", height = 300f)
            showBottomMessage("2")
            showBottomMessage("3 👨‍🎓")
        }
    }

    private fun testConstraint() {
        val button_1 = findViewById<Button>(R.id.button_1)
        val button_2 = findViewById<Button>(R.id.button_2)

        // app:layout_constraintBottom_toBottomOf="parent"
        //                    app:layout_constraintEnd_toEndOf="parent"
        //                    app:layout_constraintHorizontal_bias="0.052"
        //                    app:layout_constraintStart_toStartOf="parent"
        //                    app:layout_constraintTop_toTopOf="parent"
        //                    app:layout_constraintVertical_bias="1.0"

        // 기존 contraint가 남아있으면 영향을 받음. parent가 id 있어야 함
        button_2.setConstraint {
            v, params ->
            params.endToEnd     = (v.parent as ConstraintLayout).id
            params.startToStart = button_1.id
            params.horizontalBias = 1.0f
            params.topToBottom = button_1.id

            v.setHeightDp(60)
            v.setMarginDp(0f, 0f, 40f, 10f)

        }
    }

    var job   : Job = Job()
    private fun testCoroutine() {
        val txtWatch = findViewById<TextView>(R.id.txtWatch)
        job = UiStopWatch(2, 30, { min, sec ->
            runOnUiThread {
                txtWatch.setText("${min}분${sec}초")
            }
        })
        txtWatch.setOnClickListener {
            job.cancel()
            toast("stop")
        }
    }

    private fun testCustomSpinner() {
        val spn = findViewById<Spinner>(R.id.spnCustom)
        spn.setCustomAdapter(this, mutableListOf("1", "2", "3", "4"), "선택안됨")
    }


    // pref 사용
    val ISFIRST_RUN = "ISFIRST_RUN"
    var SharedPreferences.isFirst: Boolean
        get() = getBoolean(ISFIRST_RUN, true)
        set(value) = edit { putBoolean(ISFIRST_RUN, value) }

    private fun testPref() {
        if ( pref.isFirst ){
            pref.isFirst = false
        } else{
            toast("not first")
        }

    }



    private fun testErrorHandler() {
        var name : String? = null
        SafeHandler({
            toast(name!!)
        })

        SafeHandler({
            val sum = 1 / 0
        }, { e -> toast(e) })
    }


    // 변수관리 귀찮아서 closure
    private fun testLayoutChange() : ()->Unit{
        var bIsHidden = true

        return{
            val txtWatch = findViewById<TextView>(R.id.txtWatch)
            val btn = findViewById<Button>(R.id.btnChangeLayout)

            btn?.apply {
                setOnClickListener {
                    layoutToggle(listOf(txtWatch, btn), bIsHidden)
                    bIsHidden = !bIsHidden
                }
            }
        }

    }

}