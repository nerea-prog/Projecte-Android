package com.example.projecte_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditActivity : AppCompatActivity() {
    private lateinit var btnTestNav: Button
    private lateinit var btnDesar: Button
    private lateinit var btnCancelar: Button
    private lateinit var toolbar: Toolbar
    private lateinit var etTitle: EditText
    private lateinit var etCategory: EditText

    private var taskId: Long = -1L
    private var currentTask: MyItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setupToolbar()
        initComponents()
        initListeners()
        // Recibir el ID de la tarea
        taskId = intent.getLongExtra("taskId", -1L)
        if (taskId == -1L) {
            Toast.makeText(this, "Error: tasca no trobada", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        loadTask()
    }

    private fun loadTask() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.getApi().getTaskById(taskId)
                }
                if (response.isSuccessful) {
                    val json = response.body()?.string()
                    val gson = com.google.gson.Gson()
                    currentTask = gson.fromJson(json, MyItem::class.java)
                    currentTask?.let {
                        etTitle.setText(it.title)
                        etCategory.setText(it.category)
                    }
                } else {
                    Toast.makeText(this@EditActivity,
                        "Error carregant la tasca", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                Toast.makeText(this@EditActivity,
                    "Error de connexió", Toast.LENGTH_SHORT).show()
                Log.e("EditActivity", "Error: ${e.message}")
                finish()
            }
        }
    }

    private fun updateTask() {
        val title = etTitle.text.toString().trim()
        val category = etCategory.text.toString().trim()

        if (title.isEmpty()) {
            etTitle.error = "El títol no pot estar buit"
            return
        }
        if (category.isEmpty()) {
            etCategory.error = "La categoria no pot estar buida"
            return
        }

        val updatedTask = currentTask!!.copy(title = title, category = category)

        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.getApi().updateTask(taskId, updatedTask)
                }
                if (response.isSuccessful) {
                    Toast.makeText(this@EditActivity,
                        "Tasca actualitzada!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@EditActivity,
                        "Error actualitzant: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@EditActivity,
                    "Error de connexió", Toast.LENGTH_SHORT).show()
                Log.e("EditActivity", "Error: ${e.message}")
            }
        }
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