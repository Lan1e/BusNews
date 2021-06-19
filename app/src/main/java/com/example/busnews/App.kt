package com.example.busnews

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho

class App : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        // Stetho Init
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }


    init {
        context = this
    }
}