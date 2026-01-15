package com.example.act07_menu_llista_filtre

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TasksTodayActivity : AppCompatActivity() {
    private lateinit var btnTestNav: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks_today)
        setupRecyclerView()
        initComponents()
        initListeners()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.rvListToday)
        recyclerView.layoutManager = LinearLayoutManager(this)


        adapter = MyAdapter(DataSource.items){ item, position ->
            handleItemClick(item, position)
        }

        recyclerView.adapter = adapter
    }

    private fun handleItemClick(item: MyItem, position: Int) {
        Toast.makeText(this, "Has seleccionat: ${item.title}", Toast.LENGTH_SHORT).show()
        showItemOptionsDialog(item, position)
    }

    private fun showItemOptionsDialog(item: MyItem, position: Int) {
        val options = arrayOf("Eliminar", "Cancel·lar")

        AlertDialog.Builder(this)
            .setTitle(item.title)
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> {
                        // Eliminar item
                        adapter.removeItem(position)
                        Toast.makeText(this, "S'ha eliminat: ${item.title}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .show()
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