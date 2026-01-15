package com.example.act07_menu_llista_filtre

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TasksTodayActivity : AppCompatActivity() {
    private lateinit var btnTestNav: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks_today)

        initComponents()
        initListeners()
    }
    private fun initListeners() {
        btnTestNav.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java) // Pon el que quieras
            startActivity(intent)
        }
    }

    private fun initComponents() {
        btnTestNav = findViewById(R.id.btnTestNav)
    }
}