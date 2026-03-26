package com.example.projecte_android

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class GraficsActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var barChart: BarChart
    private lateinit var pieChart: PieChart

    // Instància de Firestore
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grafics)

        setupToolbar()
        setupStats()
        setupBtnReset()

        barChart = findViewById(R.id.barChart)
        pieChart = findViewById(R.id.pieChart)

        // Escoltem canvis en temps real
        escoltarCanvis()
    }

    // FIRESTORE: Escoltar canvis en temps real
    private fun escoltarCanvis() {
        val statsRef = db.collection("stats").document("taskStats")

        statsRef.addSnapshotListener { snapshot, e ->
            // Si hi ha error, sortim
            if (e != null) {
                Log.e("Firestore", "Error escoltant canvis", e)
                return@addSnapshotListener
            }

            // Si el document existeix, llegim les dades i actualitzem els gràfics
            if (snapshot != null && snapshot.exists()) {
                val afegir  = (snapshot.getLong("afegir")   ?: 0L).toFloat()
                val eliminar = (snapshot.getLong("eliminar") ?: 0L).toFloat()

                actualitzarBarChart(afegir, eliminar)
                actualitzarPieChart(afegir, eliminar)
            }
        }
    }

    private fun actualitzarBarChart(afegir: Float, eliminar: Float) {
        val entries = listOf(
            BarEntry(0f, afegir),
            BarEntry(1f, eliminar)
        )

        val dataSet = BarDataSet(entries, "Accions")
        dataSet.colors = listOf(
            Color.parseColor("#D2691E"),  // Afegir → marró
            Color.parseColor("#FF8C00")   // Eliminar → taronja
        )
        dataSet.valueTextSize = 12f

        barChart.data = BarData(dataSet)
        barChart.description.text = "Tasques afegides / eliminades"
        barChart.invalidate() // Refresca el gràfic
    }

    private fun actualitzarPieChart(afegir: Float, eliminar: Float) {
        // Evitem mostrar el gràfic si tot és 0 (quedaria buit i donaria error)
        if (afegir == 0f && eliminar == 0f) {
            pieChart.clear()
            pieChart.setNoDataText("Encara no hi ha dades")
            return
        }

        val entries = listOf(
            PieEntry(afegir,   "Afegides"),
            PieEntry(eliminar, "Eliminades")
        )

        val dataSet = PieDataSet(entries, "Tasques")
        dataSet.colors = listOf(
            Color.parseColor("#D2691E"),
            Color.parseColor("#A0522D")
        )
        dataSet.valueTextSize = 12f

        pieChart.data = PieData(dataSet)
        pieChart.description.text = "Proporció afegir / eliminar"
        pieChart.invalidate() // Refresca el gràfic
    }

    private fun setupBtnReset() {
        val btnReset = findViewById<Button>(R.id.btnResetStats)
        btnReset.setOnClickListener {
            // Reseteja els comptadors a Firestore
            val statsRef = db.collection("stats").document("taskStats")
            val dadesBuides = hashMapOf("afegir" to 0L, "eliminar" to 0L)
            statsRef.set(dadesBuides)
                .addOnSuccessListener {
                    Log.d("Firestore", "Estadístiques reiniciades")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error reiniciant estadístiques", e)
                }
        }
    }

    private fun setupToolbar() {
        toolbar = findViewById(R.id.graficsToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "TaskBuddy"
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
    }

    private fun setupStats() {
        val txtMinuts = findViewById<TextView>(R.id.txtMinutsUs)
        val txtCO2    = findViewById<TextView>(R.id.txtCO2)
        val minuts    = 42f
        txtMinuts.text = "$minuts min"
        txtCO2.text    = String.format("%.2f g", minuts * 0.035f)
    }
}