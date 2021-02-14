package com.ncfoundation.psw.oftenutilbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ncfoundation.psw.simplebox.toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toast("Test")
    }
}