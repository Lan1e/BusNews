package com.example.busnews

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.net.URLEncoder
import java.security.SignatureException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {
    private val viewModel by lazy { ViewModelProvider(this).get(BusDataManager::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun bindObservers() {
        viewModel.downTown.observe(this,
            {

            })

        viewModel.route.observe(this,
            { current.text = it.owo() })

        viewModel.stop.observe(this,
            {
                current.text = viewModel.result.value.owo()
                result.text = "${viewModel.result.value ?: 0} ${it?.symbol ?: ""}"
            })
    }

}