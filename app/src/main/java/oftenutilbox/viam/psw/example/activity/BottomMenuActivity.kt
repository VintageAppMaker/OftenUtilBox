package oftenutilbox.viam.psw.example.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.test.psw.oftenutilbox.R
import com.test.psw.oftenutilbox.databinding.ActivityBottomMenuBinding
import oftenutilbox.viam.psw.example.fragment.*
import java.lang.Exception

class BottomMenuActivity : AppCompatActivity() {
    lateinit var binding: ActivityBottomMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragmentWithAnimation(HomeFragment.newInstance("", ""))
        setUpBottomMenu()
    }

    fun MenuIndex(s: String) : Int {
        val TABMENU = mapOf(
            "HOME"     to 0,
            "SECOND"   to 1,
            "THIRD"    to 2,
            "FOUR"     to 3
        )
        return TABMENU[s] ?: 0;
    }

    private fun setUpBottomMenu() {

        binding.bottomNav.apply {
            itemIconTintList = null

            menu.getItem(MenuIndex("HOME")).icon = getDrawable(android.R.drawable.bottom_bar)
            menu.getItem(MenuIndex("SECOND")).icon = getDrawable(android.R.drawable.sym_action_call)
            menu.getItem(MenuIndex("THIRD")).icon = getDrawable(android.R.drawable.btn_star_big_on)
            menu.getItem(MenuIndex("FOUR")).icon = getDrawable(android.R.drawable.ic_menu_camera)

            setOnNavigationItemSelectedListener { item ->
                when(item.itemId){
                    R.id.menu_home     -> {

                        setFragmentWithAnimation(HomeFragment.newInstance("", ""))
                        true
                    }
                    R.id.menu_second -> {
                        setFragmentWithAnimation(SecondFragment.newInstance("", "")); true}
                    R.id.menu_third   -> {
                        setFragmentWithAnimation(ThirdFragment.newInstance("", "")); true}
                    R.id.menu_four    -> {
                        setFragmentWithAnimation(FourFragment.newInstance("", "")); true}

                    else -> true
                }
            }
        }
    }

    fun setFragmentWithAnimation(fragment: Fragment) {

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            //.addToBackStack( if(bRegister) "wantFragment" else null)
            .addToBackStack( null)
            .commit()
    }

    override fun onBackPressed() {
        backKeyManager()
    }

    private fun backKeyManager() {

        // fragment내에서 Onbackpressed 구현
        val fragmentList = supportFragmentManager.fragments
        if (fragmentList != null) {
            for (fragment in fragmentList) {
                if (fragment is OnBackPressedListener) {
                    // fragment에서 onBack을 처리하겠다고 하면 리턴
                    if ( (fragment as OnBackPressedListener).onBackPressed() )
                        return
                }
            }
        }

        supportFragmentManager.fragments.size?.let { nCount ->
            if (nCount == 0)
                // 메모리에서 삭제
                finishAndRemoveTask()
        }
        supportFragmentManager.popBackStack()
        supportFragmentManager.executePendingTransactions()

        // 종료처리
        if (supportFragmentManager.backStackEntryCount == 0) finish()
        val nIndx = supportFragmentManager.fragments.size
        if (nIndx < 1) finish()

        // 잠시 setOnNavigationItemSelectedListener를 비활성화
        binding.bottomNav.setOnNavigationItemSelectedListener { true }

        // Sync에 문제가 발생하여 index가 안맞을 경우 종료시킴
        try {
            // menu click 기능구현
            ActivateBackFragment()
        } catch (e: Exception) {
            e.printStackTrace()
            finish()
        }
    }

    private fun ActivateBackFragment() {
        val n = if ( supportFragmentManager.fragments.size > 1) 1 else 0

        val f = supportFragmentManager.fragments[n]
        when (f) {
            is HomeFragment -> {
                binding.bottomNav.selectedItemId =
                    binding.bottomNav.menu.getItem(MenuIndex("HOME")).itemId
            }
            is SecondFragment -> {
                binding.bottomNav.selectedItemId =
                    binding.bottomNav.menu.getItem(MenuIndex("SECOND")).itemId
            }
            is ThirdFragment -> {
                binding.bottomNav.selectedItemId =
                    binding.bottomNav.menu.getItem(MenuIndex("THIRD")).itemId
            }
            is FourFragment -> {
                binding.bottomNav.selectedItemId =
                    binding.bottomNav.menu.getItem(MenuIndex("FOUR")).itemId
            }

        }

        setUpBottomMenu()
    }
}