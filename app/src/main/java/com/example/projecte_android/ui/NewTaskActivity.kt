package com.example.projecte_android.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.projecte_android.R
import com.example.projecte_android.data.MyItem
import com.example.projecte_android.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewTaskActivity : AppCompatActivity() {
    private lateinit var btnTestNav: Button
    private lateinit var btnDesar: Button
    private lateinit var btnCancelar: Button
    private lateinit var toolbar: Toolbar
    private lateinit var etTitle: EditText
    private lateinit var etCategoria: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)
        setupToolbar()
        initComponents()
        initListeners()
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


    /**
     * Crea una tasca a la API
     * Valida que els camps no estiguin buits
     * Si tot es correcte, tanca l'activity i torna enrere
     */
    private fun createTask() {
        val title = etTitle.text.toString().trim()
        val category = etCategoria.text.toString().trim()

        if (title.isEmpty()) {
            etTitle.error = "El títol no pot estar buit"
            return
        }
        if (category.isEmpty()) {
            etCategoria.error = "La categoria no pot estar buida"
            return
        }

        val newTask = MyItem(
            title = title,
            category = category
        )

        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.getApi().createTasks(listOf(newTask))
                }
                if (response.isSuccessful) {
                    Toast.makeText(this@NewTaskActivity,
                        "Tasca creada!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@NewTaskActivity,
                        "Error ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }  catch (e: Exception) {
            Toast.makeText(this@NewTaskActivity,
                "Error de connexió", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun initComponents() {
        btnTestNav = findViewById(R.id.btnTestNav)
        btnDesar = findViewById(R.id.btnDesar)
        btnCancelar = findViewById(R.id.btnCancelar)
        etTitle = findViewById(R.id.etTitle)
        etCategoria = findViewById(R.id.etCategoria)
    }

    private fun initListeners() {
        btnTestNav.setOnClickListener {
            val intent = Intent(this, ConfigurationActivity::class.java)
            startActivity(intent)
        }

        btnCancelar.setOnClickListener {
            finish() // Cierra la activity y vuelve atrás
        }

        btnDesar.setOnClickListener {
            createTask()
        }
    }
}