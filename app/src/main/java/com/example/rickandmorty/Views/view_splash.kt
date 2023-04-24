package com.example.rickandmorty.Views

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.rickandmorty.MainActivity
import com.example.rickandmorty.R


class view_splash : AppCompatActivity() {

    private val PREFS_NAME = "MyPrefsFile"
    private val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val splash_icon:ImageView=findViewById(R.id.splash_icon)
        var splash_text:TextView=findViewById(R.id.splash_text)


        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val isFirstTimeLaunch = prefs.getBoolean(IS_FIRST_TIME_LAUNCH, true)

        if (isFirstTimeLaunch) {
            splash_text.setText("Welcome!")
            val editor = prefs.edit()
            editor.putBoolean(IS_FIRST_TIME_LAUNCH, false)
            editor.apply()
        } else {
            splash_text.setText("Hello!")
        }

        splash_icon.alpha = 0f
        splash_icon.animate().setDuration(1500).alpha(1f).withEndAction{
            val intent = Intent(this@view_splash, MainActivity::class.java)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            startActivity(intent)
            finish()
        }

    }
}