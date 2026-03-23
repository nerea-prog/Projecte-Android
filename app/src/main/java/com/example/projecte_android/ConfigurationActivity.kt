package com.example.projecte_android

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat

class ConfigurationActivity : AppCompatActivity() {

    private lateinit var btnTestNav: Button

    private lateinit var toolbar: Toolbar
    private lateinit var tvGrafics: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)
        setupToolbar()
        initComponents()
        initListeners()
    }
    private fun setupToolbar() {
        toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "TaskBuddy"
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.action_inici -> {
                Toast.makeText(this, "Anant al Inici", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainUserActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_configuracio -> {
                Toast.makeText(this, "Anant a configuració", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ConfigurationActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_sobre -> {
                mostrarDialogSobre()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun mostrarDialogSobre() {
        AlertDialog.Builder(this)
            .setTitle("Sobre l'aplicació")
            .setMessage("Aplicació que gestiona les tasques")
            .setPositiveButton("Tancar", null)
            .show()
    }
    private fun initListeners() {
        btnTestNav.setOnClickListener {
            val intent = Intent(this, TutorialActivity::class.java)
            startActivity(intent)
        }
        tvGrafics.setOnClickListener {
            val intent = Intent(this, GraficsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initComponents() {
        btnTestNav = findViewById(R.id.btnTestNav)
        tvGrafics = findViewById(R.id.tvGrafic)
    }
}