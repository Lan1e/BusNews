package com.example.busnews

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class App : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    init {
        context = this
    }
}