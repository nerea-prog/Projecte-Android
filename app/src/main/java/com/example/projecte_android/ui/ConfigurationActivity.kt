package com.example.projecte_android.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.projecte_android.R

class ConfigurationActivity : AppCompatActivity() {

    private lateinit var btnTestNav: Button
    private lateinit var toolbar: Toolbar
    private lateinit var tvGrafics: TextView

    private lateinit var switchVeu: SwitchCompat
    // Constants per a SharedPreferences
    private val PREFS_NAME = "taskbuddy_prefs"
    private val KEY_VEU = "veu_activada"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)
        setupToolbar()
        initComponents()
        initListeners()
        setupVeuSwitch()
    }

    /**
     * Configura el switch que activa o desactiva la veu a l'aplicació.
     *
     * Llegeix la preferència guardada a SharedPreferences per saber si la veu
     * està activada i posa el switch en el mateix estat.
     *
     * Quan l'usuari canvia el switch, es guarda el nou valor a les preferències i
     * es mostra un missatge indicant si la veu s'ha activat o desactivat.
     *
     * Això permet que la configuració de veu es mantingui encara que es tanqui
     * l'aplicació.
     */
    private fun setupVeuSwitch() {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        switchVeu.isChecked = prefs.getBoolean(KEY_VEU, false)
        switchVeu.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean(KEY_VEU, isChecked).apply()
            val msg = if (isChecked) "Veu activada" else "Veu desactivada"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
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
        switchVeu = findViewById(R.id.switchVeu)
    }
}