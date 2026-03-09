package com.example.projecte_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TasksTodayActivity : AppCompatActivity() {

    private lateinit var btnTestNav: Button
    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter

    private lateinit var tvNovaTasca: TextView

    private var allTasks: List<MyItem> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks_today)

        setupToolbar()
        setupRecyclerView()
        initComponents()
        initListeners()
        loadTasksFromApi()
    }

    private fun loadTasksFromApi() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.getApi().getAllTasks()
                }
                if (response.isSuccessful) {
                    val tasks = response.body() ?: emptyList()
                    allTasks = tasks
                    adapter.updateList(allTasks)
                } else if (response.code() == 404) {
                    // No hay tareas, mostrar lista vacía sin error
                    allTasks = emptyList()
                    adapter.updateList(allTasks)
                } else {
                    Toast.makeText(this@TasksTodayActivity,
                        "Error ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@TasksTodayActivity,
                    "Error de connexió", Toast.LENGTH_SHORT).show()
                Log.e("API", "Error: ${e.message}")
            }
        }
    }
    private fun setupToolbar() {
        toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "TaskBuddy"
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))

    }
    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.rvListToday)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter(allTasks) { item, position ->
            handleItemClick(item, position)
        }
        recyclerView.adapter = adapter
    }

    // Inflar el menú
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Gestionar clics del menú
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_inici -> {
                Toast.makeText(this, "Anant a Inici", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainUserActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_category_button -> {
                // Mostrar el PopupMenu
                showCategoryPopupMenu(toolbar)
                true
            }
            R.id.action_configuracio -> {
                Toast.makeText(this, "Obrint Configuració...", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ConfigurationActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_sobre -> {
                mostrarDialogSobre()
                true
            }
            R.id.action_delete_all -> {
                deleteAllTasks()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showCategoryPopupMenu(view: View) {
        val popup = PopupMenu(this, view)

        popup.menuInflater.inflate(R.menu.popup_categories, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.cat_totes -> {
                    applyCategoryFilter("Totes")
                    true
                }
                R.id.cat_personal -> {
                    applyCategoryFilter("Personal")
                    true
                }
                R.id.cat_classe -> {
                    applyCategoryFilter("Classe")
                    true
                }
                else -> false
            }
        }

        popup.show()
    }

    private fun applyCategoryFilter(category: String) {
        Toast.makeText(this, "Filtrat per: $category", Toast.LENGTH_SHORT).show()
        Log.d("Filter", "Categoria seleccionada: $category")

        val filteredList = if (category == "Totes") {
            allTasks
        } else {
            allTasks.filter { it.category == category } // Filtrar per categoria
        }
        adapter.updateList(filteredList)
    }

    private fun mostrarDialogSobre() {
        AlertDialog.Builder(this)
            .setTitle("Sobre l'aplicació")
            .setMessage("Aplicació de gestió de tasques\nVersió 1.0")
            .setPositiveButton("Tancar", null)
            .show()
    }

    private fun handleItemClick(item: MyItem, position: Int) {
        Toast.makeText(this, "Has seleccionat: ${item.title}", Toast.LENGTH_SHORT).show()
        Log.d("RecyclerView", "Item clicat: ${item.title} a la posició $position")
        showItemOptionsDialog(item, position)
    }

    private fun showItemOptionsDialog(item: MyItem, position: Int) {
        val options = arrayOf("Eliminar", "Editar", "Cancel·lar")

        AlertDialog.Builder(this)
            .setTitle(item.title)
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> deleteTask(item)
                    1 -> {
                        val intent = Intent(this, EditActivity::class.java)
                        intent.putExtra("taskId", item.id)
                        startActivity(intent)
                    }
                }
            }
            .show()
    }

    private fun deleteTask(item: MyItem) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar tasca")
            .setMessage("Segur que vols eliminar?")
            .setPositiveButton("Eliminar") { _, _ ->
                lifecycleScope.launch {
                    try {
                        val response = withContext(Dispatchers.IO) {
                            RetrofitClient.getApi().deleteTask(item.id)
                        }
                        if (response.isSuccessful){
                            Toast.makeText(this@TasksTodayActivity, "Tasca eliminada", Toast.LENGTH_SHORT).show()
                            loadTasksFromApi()
                        } else{
                            Toast.makeText(this@TasksTodayActivity,
                                "Error eliminant la tasca", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@TasksTodayActivity, "Error de connexió",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancel·lar", null)
            .show()
    }

    private fun deleteAllTasks() {
        AlertDialog.Builder(this)
            .setTitle("Eliminar totes les tasques")
            .setMessage("Segur que vols eliminar-les totes?")
            .setPositiveButton("Eliminar tot") { _, _ ->
                lifecycleScope.launch {
                    try {
                        val response = withContext(Dispatchers.IO) {
                            RetrofitClient.getApi().deleteAllTasks()
                        }
                        if (response.isSuccessful) {
                            Toast.makeText(this@TasksTodayActivity,
                                "Totes les tasques eliminades", Toast.LENGTH_SHORT).show()
                            loadTasksFromApi()
                        } else {
                            Toast.makeText(this@TasksTodayActivity,
                                "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@TasksTodayActivity,
                            "Error de connexió", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancel·lar", null)
            .show()
    }

    private fun showCreateDialog() {
        val input = android.widget.EditText(this)
        input.hint = "Títol de la tasca"

        AlertDialog.Builder(this)
            .setTitle("Nova tasca")
            .setView(input)
            .setPositiveButton("Crear") { _, _ ->
                val title = input.text.toString().trim()
                if (title.isEmpty()) {
                    Toast.makeText(this, "El títol no pot estar buit", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                val newTask = MyItem(title = title)
                lifecycleScope.launch {
                    try {
                        val response = withContext(Dispatchers.IO) {
                            RetrofitClient.getApi().createTasks(listOf(newTask))
                        }
                        if (response.isSuccessful) {
                            Toast.makeText(this@TasksTodayActivity,
                                "Tasca creada!", Toast.LENGTH_SHORT).show()
                            loadTasksFromApi()
                        } else {
                            Toast.makeText(this@TasksTodayActivity,
                                "Error creant: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@TasksTodayActivity,
                            "Error de connexió", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancel·lar", null)
            .show()
    }

    private fun showEditDialog(item: MyItem) {
        val input = android.widget.EditText(this)
        input.setText(item.title)

        AlertDialog.Builder(this)
            .setTitle("Editar tasca")
            .setView(input)
            .setPositiveButton("Guardar") { _, _ ->
                val newTitle = input.text.toString().trim()
                if (newTitle.isEmpty()) {
                    Toast.makeText(this, "El títol no pot estar buit", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                val updatedTask = item.copy(title = newTitle)
                lifecycleScope.launch {
                    try {
                        val response = withContext(Dispatchers.IO) {
                            RetrofitClient.getApi().updateTask(item.id, updatedTask)
                        }
                        if (response.isSuccessful) {
                            Toast.makeText(this@TasksTodayActivity,
                                "Tasca actualitzada!", Toast.LENGTH_SHORT).show()
                            loadTasksFromApi()
                        } else {
                            Toast.makeText(this@TasksTodayActivity,
                                "Error actualitzant: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@TasksTodayActivity,
                            "Error de connexió", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancel·lar", null)
            .show()
    }

    // Recarga la lista al volver
    override fun onResume() {
        super.onResume()
        loadTasksFromApi()
    }

    private fun initListeners() {
        btnTestNav.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
        }
        tvNovaTasca.setOnClickListener {
            val intent = Intent(this, NewTaskActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initComponents() {
        btnTestNav = findViewById(R.id.btnTestNav)
        tvNovaTasca = findViewById(R.id.tvNovaTasca)
    }
}