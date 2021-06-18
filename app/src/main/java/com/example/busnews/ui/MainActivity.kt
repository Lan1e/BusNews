package com.example.busnews.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.busnews.BusDataManager
import com.example.busnews.R


class MainActivity : AppCompatActivity() {
    val viewModel by lazy { ViewModelProvider(this).get(LiveDataViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.filter_container, FilterFragment()).commitAllowingStateLoss()
        supportFragmentManager.beginTransaction().replace(R.id.result_container, ResultFragment()).commitAllowingStateLoss()

        // trigger this to update result
//        BusDataManager.updateResult()
    }
}