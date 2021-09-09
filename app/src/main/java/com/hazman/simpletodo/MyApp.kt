package com.hazman.simpletodo

import android.app.Application
import com.hazman.simpletodo.utils.QuickSave

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        QuickSave.init(applicationContext)
    }
}