package com.example.act07_menu_llista_filtre

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import android.text.TextWatcher
import android.util.Log

class MainUserActivity : AppCompatActivity() {
    private lateinit var btnTestNav: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private lateinit var etSearch: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_user)
        setupRecyclerView()
        setupSearchFilter()
        initComponents()
        initListeners()
    }

    private fun setupSearchFilter() {
        etSearch = findViewById(R.id.etSearch)

        // Afegir TextWatcher per escoltar canvis en el text
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No necessitem fer res aquí
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Filtra en temps real mentre escriu
                val query = s.toString()
                adapter.filter(query)

                // Log per veure què està filtrant
                Log.d("Filter", "Filtrant per: $query")
            }

            override fun afterTextChanged(s: Editable?) {
                // No necessitem fer res aquí
            }
        })
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.rvLists)
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
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initComponents() {
        btnTestNav = findViewById(R.id.btnTestNav)
    }
}


