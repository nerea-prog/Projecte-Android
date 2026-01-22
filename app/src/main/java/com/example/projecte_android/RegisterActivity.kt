package com.example.projecte_android

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener

class RegisterActivity : AppCompatActivity() {
    private lateinit var btnTestNav: Button
    private lateinit var toolbar: Toolbar
    private val viewModel: RegisterViewModel by viewModels()

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPasswordConfirm: EditText
    private lateinit var btnRegister: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setupToolbar()
        initComponents()
        initListeners()
        initObservers()
        initInputs()
    }


    private fun initComponents() {
        btnTestNav = findViewById(R.id.btnTestNav)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etGmail)
        etPassword = findViewById(R.id.etPassword)
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm)
        btnRegister = findViewById(R.id.btnRegistre)
    }
    private fun initListeners() {
        btnTestNav.setOnClickListener {
            val intent = Intent(this, RegistroCorrectoActivity::class.java)
            startActivity(intent)
        }
        btnRegister.setOnClickListener {
            viewModel.register()
        }
    }
    private fun initObservers() {
        viewModel.registrationSuccess.observe(this) {success ->
            if (success){
                Toast.makeText(this, "Registre exitòs", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        viewModel.registrationError.observe(this) {error ->
            error?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }
    }

    private fun initInputs() {
        etName.addTextChangedListener { viewModel.setName(it.toString()) }
        etEmail.addTextChangedListener { viewModel.setEmail(it.toString()) }
        etPassword.addTextChangedListener { viewModel.setPassword(it.toString()) }
        etPasswordConfirm.addTextChangedListener { viewModel.setPasswordConfirm(it.toString()) }
    }

    private fun setupToolbar() {
        toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "TaskBuddy"
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))    }

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
}