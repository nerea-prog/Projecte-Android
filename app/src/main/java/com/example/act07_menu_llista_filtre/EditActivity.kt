package com.example.act07_menu_llista_filtre

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EditActivity : AppCompatActivity() {
    private lateinit var btnTestNav: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        initComponents()
        initListeners()
    }
    private fun initListeners() {
        btnTestNav.setOnClickListener {
            val intent = Intent(this, ConfigurationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initComponents() {
        btnTestNav = findViewById(R.id.btnTestNav)
    }
}