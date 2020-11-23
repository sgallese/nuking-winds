package com.haystackreviews.nukingwinds

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.haystackreviews.nukingwinds.ui.main.WeatherFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, WeatherFragment())
                    .commitNow()
        }
    }
}