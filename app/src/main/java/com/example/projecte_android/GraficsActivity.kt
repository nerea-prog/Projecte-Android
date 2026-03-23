package com.example.projecte_android

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class GraficsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grafics)
        dataset()

    }

    fun dataset(){
        // Crear dataset per al grafic
        val lineChart = findViewById<LineChart>(R.id.lineChart)
        val dataSet = LineDataSet(Dades.entries, "Vendes mensuals")
        dataSet.color = Color.BLUE
        dataSet.setCircleColor(Color.RED)
        dataSet.valueTextSize = 12f
        dataSet.lineWidth = 2f

        // Assignar les dades al grafic
        val data = LineData(dataSet);
        lineChart.data = data;
        lineChart.description.text = "Informe de vendes"
        lineChart.invalidate()
    }
}