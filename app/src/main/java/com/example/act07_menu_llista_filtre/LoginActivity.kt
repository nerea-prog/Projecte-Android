package com.example.act07_menu_llista_filtre

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class LoginActivity : AppCompatActivity() {
    private lateinit var btnTestNav: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition {
            false
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initComponents()
        initListeners()
    }
    private fun initListeners() {
        btnTestNav.setOnClickListener {
            val intent = Intent(this, MainUserActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initComponents() {
        btnTestNav = findViewById(R.id.btnTestNav)
    }
}