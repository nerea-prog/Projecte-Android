package com.example.projecte_android.ui

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
import com.example.projecte_android.R
import com.example.projecte_android.adapter.MyAdapter
import com.example.projecte_android.data.MyItem
import com.example.projecte_android.network.RetrofitClient
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
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

    private val db = Firebase.firestore // Instància de Firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks_today)

        setupToolbar()
        setupRecyclerView()
        initComponents()
        initListeners()
        loadTasksFromApi()
    }

    /**
     * Actualitza un comptador d'estadistiques en firestore
     *
     * @param camp Camp a incrementar
     * @return res
     */
    private fun incrementarEstadistica(camp: String) {
        val statsRef = db.collection("stats").document("taskStats")

        statsRef.get()
            .addOnSuccessListener { document ->
                val afegir = document.getLong("afegir") ?: 0L
                val eliminar = document.getLong("eliminar") ?: 0L

                val nousDades = when (camp) {
                    "afegir"   -> hashMapOf("afegir" to afegir + 1, "eliminar" to eliminar)
                    "eliminar" -> hashMapOf("afegir" to afegir, "eliminar" to eliminar + 1)
                    else       -> return@addOnSuccessListener
                }

                statsRef.set(nousDades)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Estadística actualitzada: $camp")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Error actualitzant estadística", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error llegint estadística", e)
            }
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
                        if (response.isSuccessful) {
                            Toast.makeText(this@TasksTodayActivity,
                                "Tasca eliminada", Toast.LENGTH_SHORT).show()
                            loadTasksFromApi()
                            incrementarEstadistica("eliminar") // Incrementa el comptador d'eliminar a Firestore
                        } else {
                            Toast.makeText(this@TasksTodayActivity,
                                "Error eliminant la tasca", Toast.LENGTH_SHORT).show()
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
                            incrementarEstadistica("eliminar") // Incrementa el comptador d'eliminar a Firestore
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_inici -> {
                startActivity(Intent(this, MainUserActivity::class.java))
                true
            }
            R.id.action_category_button -> {
                showCategoryPopupMenu(toolbar)
                true
            }
            R.id.action_configuracio -> {
                startActivity(Intent(this, ConfigurationActivity::class.java))
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
                R.id.cat_totes    -> { applyCategoryFilter("Totes"); true }
                R.id.cat_personal -> { applyCategoryFilter("Personal"); true }
                R.id.cat_classe   -> { applyCategoryFilter("Classe"); true }
                else -> false
            }
        }
        popup.show()
    }

    private fun applyCategoryFilter(category: String) {
        val filteredList = if (category == "Totes") allTasks
        else allTasks.filter { it.category == category }
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
        showItemOptionsDialog(item, position)
    }

    private fun showItemOptionsDialog(item: MyItem, position: Int) {
        val options = arrayOf("Eliminar", "Editar", "Cancel·lar")
        AlertDialog.Builder(this)
            .setTitle(item.title)
            .setItems(options) { _, which ->
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

    override fun onResume() {
        super.onResume()
        loadTasksFromApi()
    }

    private fun initListeners() {
        btnTestNav.setOnClickListener {
            startActivity(Intent(this, ConfigurationActivity::class.java))
        }
        tvNovaTasca.setOnClickListener {
            startActivity(Intent(this, NewTaskActivity::class.java))
            incrementarEstadistica("afegir") // Incrementa el comptador d'afegir a Firestore
        }
    }

    private fun initComponents() {
        btnTestNav = findViewById(R.id.btnTestNav)
        tvNovaTasca = findViewById(R.id.tvNovaTasca)
    }
}