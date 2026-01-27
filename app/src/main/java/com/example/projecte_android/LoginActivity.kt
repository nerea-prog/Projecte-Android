package com.example.projecte_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var btnTestNav: Button
    private lateinit var etUserName: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition {
            false
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initComponents()
        initListeners()
        initObservers()
        initInputs()
    }

    private fun initComponents() {
        btnTestNav = findViewById(R.id.btnTestNav)
        etUserName = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
    }
    private fun initListeners() {
        btnTestNav.setOnClickListener {
            val intent = Intent(this, MainUserActivity::class.java)
            startActivity(intent)
        }
        btnLogin.setOnClickListener {
            viewModel.login()
        }
    }

    private fun initObservers(){
        viewModel.loginSucces.observe(this) {success ->
            if (success){
                Toast.makeText(this, "Login exitòs", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainUserActivity::class.java)
                startActivity(intent)
            }
        }
        viewModel.loginError.observe(this){ error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun initInputs() {
        etUserName.addTextChangedListener { viewModel.setUsername(it.toString()) }
        etPassword.addTextChangedListener { viewModel.setPassword(it.toString()) }
    }
}