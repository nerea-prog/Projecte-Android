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
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat

class MainUserActivity : AppCompatActivity() {
    private lateinit var btnTestNav: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private lateinit var etSearch: TextInputEditText

    private lateinit var toolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_user)
        setupRecyclerView()
        setupSearchFilter()
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


