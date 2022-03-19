package oftenutilbox.viam.psw.Test

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
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
import com.bumptech.glide.Glide

import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DUtil.d(this.javaClass.toString(), "onCreate")

        setContentView(R.layout.activity_main)

        testSystemNavigation()

        setButtonAction(R.id.btnViewModelTimer, {testViewModelTimer()})
        setButtonAction(R.id.btnViewResize, {testViewSize()})
        setButtonAction(R.id.btnViewRotation, {testViewRotation()})
        setButtonAction(R.id.btnMagneticRecyclerView, {testMagneticRecyclerView()})
        setButtonAction(R.id.btnAppBarlayout2Lines, {testAppbarlayout2Lines()})
        setButtonAction(R.id.btnWeightChange, {testWeightChange()})
        setButtonAction(R.id.btnViewPager, {testViewPager()})
        setButtonAction(R.id.btnWebView, {testWebView()})
        setButtonAction(R.id.btnFlexlayout, {testFlexlayout()})
        setButtonAction(R.id.btnRatiolayout, {testRatiolayout()})
        setButtonAction(R.id.btnServerAPI, {testServerAPITest()})

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

        // textView의 백그라운드를 Glide를 통해 설정함.
        ImageTool.getNetworkDrawable(this@MainActivity,
            "https://avatars.githubusercontent.com/u/32689599?s=200&v=4",
            {   img->
                runOnUiThread {
                    findViewById<TextView>(R.id.txtBackgroudText).apply {
                        background = img
                    }
                }
            }
        )

        // 일단 GlideTest
        val target: ImageView = findViewById<View>(R.id.imgTest) as ImageView
        val url = "https://www.google.com/logos/doodles/2021/chuseok-2021-6753651837109089.2-l.webp"
        Glide.with(this)
            .load(url)
            .into(target)

    }

    private fun testWebView() {
        Intent(this, WebViewActivity::class.java).apply {
            startActivity(this)
        }
    }
    private fun testViewPager() {
        DUtil.d(this.javaClass.toString(), "testViewPager")

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

    // Server API Test
    private fun testServerAPITest(){

        QuickExampleActivity.launch(this, { setContent ->
            val binding: ExampleServerapiBinding
            binding = ExampleServerapiBinding.inflate(layoutInflater)
            setContent(binding.root)

            binding.apply {
                btnApiget.setOnClickListener {

                }
            }

        })
    }

    // 핸드폰 비율에 맞추어 width/height 조절
    private fun testRatiolayout(){

        QuickExampleActivity.launch(this, { setContent ->
            val binding: Example8Binding
            binding = Example8Binding.inflate(layoutInflater)
            setContent(binding.root)

            var baseWidth    = 480 // 디자인가이드에서 기준한 디바이스 전체 width

            var designWidth  = 280 // 디자인가이드에서 정의한 width 값
            var designHeight = 200 // 디자인가이드 height 값

            var screenWidth = resources.displayMetrics.widthPixels

            // 디자인가이드전체 : 디자인가이드가로 = 실제디바이스전체 : 적용된가로
            var newImageWidth = (screenWidth) * designWidth / baseWidth
            var newImageHeight = newImageWidth * designHeight / designWidth

            binding.apply {
                // 가로를 전체로 채우면 아래주석으로 설정
                //imgLogo.setLayoutParams(LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, newImageHeight))
                imgLogo.setLayoutParams(LinearLayout.LayoutParams(newImageWidth, newImageHeight))
            }


        })
    }

    private fun testFlexlayout(){

        QuickExampleActivity.launch(this, { setContent ->
            val binding: Example7Binding
            binding = Example7Binding.inflate(layoutInflater)
            setContent(binding.root)

            val colorTable = listOf("#FF0000", "#00FF00", "#0000FF", "#FF3200", "#33FFA0", "#00AAFF", "#333333")
            binding.flexPanel.apply {
                (1..100).forEach {
                    val tv = TextView(this@MainActivity)

                    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18.toFloat())
                    tv.setPaddingDp(10f, 10f, 10f, 10f)

                    val textIndx = colorTable[it % colorTable.size]
                    tv.setTextColor(Color.parseColor("#ffffff"))
                    tv.setBackgroundColor(Color.parseColor(textIndx))
                    tv.setText("Test ${it}")
                    addView(tv)

                    if(it % colorTable.size == 0){
                        val img = ImageView(this@MainActivity)
                        img.setImageResource(R.mipmap.ic_launcher)
                        addView(img)
                    }
                }
            }

        })
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
            setBottomSystemBarColor(Color.parseColor("#8BC34A"))
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