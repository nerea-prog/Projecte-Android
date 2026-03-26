package com.example.projecte_android

import android.graphics.Color
import android.os.Bundle
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

data class Estadistiques(
    var minuts: Float = 0f,
    var accions: Map<String, Float> = mapOf("afegir" to 0f, "eliminar" to 0f, "navegar" to 0f),
    var tasques: Map<String, Float> = mapOf("fet" to 0f, "pendent" to 0f, "eliminar" to 0f)
)
class GraficsActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private val db = Firebase.firestore
    private val documentId = "usuari_test"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grafics)

        setupToolbar()
        setupStats()
        setupBarChart()
        setupPieChart()
        setupBtnReset()
        escoltarCanvis(documentId)
    }

    fun guardarItemFirestore(titulo: String, categoria: String = "General") {
        // Creamos una referencia a un nuevo documento (ID automático)
        val nuevoDoc = db.collection("tasks").document()

        val item = MyItem(
            id = nuevoDoc.id.toLong(), // usamos el ID generado
            title = titulo,
            category = categoria
        )

        nuevoDoc.set(item)
            .addOnSuccessListener {
                println("Tasca guardada a Firestore: $titulo")
            }
            .addOnFailureListener { e ->
                println("Error guardant la tasca: $e")
            }
    }

    fun obtenerItemsFirestore() {
        db.collection("tasks")
            .get()
            .addOnSuccessListener { result ->
                val llistaItems = result.toObjects(MyItem::class.java)
                actualitzarGrafics(llistaItems)
            }
            .addOnFailureListener { e ->
                println("Error en obtenir les tasques: $e")
            }
    }

    private fun actualitzarGrafics(items: List<MyItem>) {
        // Contadors de accions
        val afegir = items.size.toFloat()
        val eliminar = contadorEliminadas.toFloat()
        val navegar = items.size.toFloat()

        // BarChart
        val barEntries = listOf(
            BarEntry(0f, afegir),
            BarEntry(1f, eliminar),
            BarEntry(2f, navegar)
        )
        val barDataSet = BarDataSet(barEntries, "Accions")
        barDataSet.color = Color.parseColor("#D2691E")
        barDataSet.valueTextSize = 12f
        barChart.data = BarData(barDataSet)
        barChart.description.text = "Accions realitzades"
        barChart.invalidate()

        // PieChart
        val fet = items.count { it.completed }.toFloat()
        val pendent = items.count { !it.completed }.toFloat()
        val eliminades = contadorEliminadas.toFloat()

        val pieEntries = listOf(
            PieEntry(fet, "Fetes"),
            PieEntry(pendent, "Pendents"),
            PieEntry(eliminades, "Eliminades")
        )
        val pieDataSet = PieDataSet(pieEntries, "Tasques")
        pieDataSet.colors = listOf(
            Color.parseColor("#D2691E"),
            Color.parseColor("#FF8C00"),
            Color.parseColor("#A0522D")
        )
        pieDataSet.valueTextSize = 12f
        pieChart.data = PieData(pieDataSet)
        pieChart.description.text = "Tasques per categoria"
        pieChart.invalidate()
    }

    private fun setupToolbar() {
        toolbar = findViewById(R.id.graficsToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "TaskBuddy"
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
    }

    fun setupStats() {
        val txtMinuts = findViewById<TextView>(R.id.txtMinutsUs)
        val txtCO2 = findViewById<TextView>(R.id.txtCO2)

        val minuts = 42f
        val co2 = minuts * 0.035f

        txtMinuts.text = "$minuts min"
        txtCO2.text = String.format("%.2f g", co2)
    }

    fun setupBarChart() {
        val barChart = findViewById<BarChart>(R.id.barChart)
        val entries = listOf(
            BarEntry(0f, 15f),
            BarEntry(1f, 8f),
            BarEntry(2f, 23f)
        )

        val dataSet = BarDataSet(entries, "Accions")
        dataSet.color = Color.parseColor("#D2691E")
        dataSet.valueTextSize = 12f

        val data = BarData(dataSet)
        barChart.data = data
        barChart.description.text = "Accions realitzades"
        barChart.invalidate()
    }

    fun setupPieChart() {
        val pieChart = findViewById<PieChart>(R.id.pieChart)
        val entries = listOf(
            PieEntry(12f, "Fetes"),
            PieEntry(7f, "Pendents"),
            PieEntry(5f, "Eliminades")
        )

        val dataSet = PieDataSet(entries, "Tasques")
        dataSet.colors = listOf(Color.parseColor("#D2691E"), Color.parseColor("#FF8C00"), Color.parseColor("#A0522D"))
        dataSet.valueTextSize = 12f

        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.description.text = "Tasques per categoria"
        pieChart.invalidate()
    }

    fun setupBtnReset() {
        val btnReset = findViewById<Button>(R.id.btnResetStats)
        btnReset.setOnClickListener {
            // aquí aniria el reset del DataStore
        }
    }
}