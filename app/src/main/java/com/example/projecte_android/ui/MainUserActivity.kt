package com.example.projecte_android.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.projecte_android.R
import com.example.projecte_android.adapter.MyAdapter
import com.example.projecte_android.data.DataSource

class MainUserActivity : AppCompatActivity() {
    private lateinit var btnTestNav: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_user)
        setupToolbar()
        setupRecyclerView()
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
            R.id.action_category_button -> {
                showCategoryPopupMenu(toolbar)
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
            DataSource.items
        } else {
            DataSource.items.filter { it.category == category }
        }

        adapter.updateList(filteredList)
    }

    private fun mostrarDialogSobre() {
        AlertDialog.Builder(this)
            .setTitle("Sobre l'aplicació")
            .setMessage("Aplicació que gestiona les tasques")
            .setPositiveButton("Tancar", null)
            .show()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.rvLists)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = MyAdapter(DataSource.items) { item, position ->
            Toast.makeText(this, "Has seleccionat: ${item.title}", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = adapter
    }

    private fun initListeners() {
        btnTestNav.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initComponents() {
        btnTestNav = findViewById(R.id.btnTestNav)
    }
}


