package com.example.projecte_android.ui

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Button
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
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
    private lateinit var recognizer: SpeechRecognizer
    private lateinit var recognizerIntent: Intent
    private lateinit var btnVeu: ImageButton

    companion object {
        // codi identificador per a saber de qué permís ve la resposta
        private const val REQUEST_MIC = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_user)
        setupToolbar()
        setupRecyclerView()
        initComponents()
        initListeners()
        checkMicPermission()
        setupVoiceRecognizer()

    }

    /**
     * Inicialitza el reconeixedor de veu i configura els resultats.
     *
     * Crea una instància de SpeechRecognizer i defineix un Intent amb la configuració
     * del reconeixement de veu (idioma i model).
     *
     * També assigna un listener per gestionar els resultats de la veu:
     * - Quan es detecta una frase, es transforma a text i es passa al gestor de comandes.
     * - Es poden capturar altres estats del reconeixement (errors, inici, fi, etc.).
     */
    private fun setupVoiceRecognizer() {
        recognizer = SpeechRecognizer.createSpeechRecognizer(this)
        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ca-ES")
        }
        recognizer.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                val spokenText = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.get(0)
                    ?.lowercase()
                handleVoiceCommand(spokenText)
            }

            /**
             * Es crida quan hi ha un error al reconeixement de veu.
             * @param error codi d'error
             */
            override fun onError(error: Int) {
                Log.e("VOICE_ERROR", "Error code: $error")
                Toast.makeText(this@MainUserActivity, "Error de veu: $error", Toast.LENGTH_SHORT).show()
            }
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    /**
     * Es crida quan l'activitat torna a primer pla.
     *
     * Actualitza la visibilitat del botó de veu segons la preferència guardada
     * a SharedPreferences.
     *
     * Si la veu està activada, el botó es mostra i si no, s'oculta.
     */
    override fun onResume() {
        super.onResume()
        // es llegeixen les preferencies guardades
        val prefs = getSharedPreferences("taskbuddy_prefs", MODE_PRIVATE)
        // mostrem u ocultem el botó segons si la veu está activada a les preferències
        btnVeu.visibility = if (prefs.getBoolean("veu_activada", false)) View.VISIBLE else View.GONE
    }

    /**
     * Comprova si l'aplicació té permís per utilitzar el micròfon.
     *
     * Si el permís no està concedit, es mostra un diàleg al usuari per demanar-lo.
     */
    private fun checkMicPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_MIC
            )
        }
    }
    /**
     * Comprova si l'aplicació té permís per utilitzar el micròfon.
     *
     * Si el permís no està concedit, es mostra un diàleg al usuari per demanar-lo.
     *
     * @param requestCode codi identificador per a saber de qué permís ve la resposta
     * @param permissions array amb els noms dels permisos
     * @param grantResults array amb els resultats de la sol·licitud de permís
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_MIC && grantResults.isNotEmpty()
            && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            setupVoiceRecognizer()
            Toast.makeText(this, "Cal permís de micròfon per usar la veu", Toast.LENGTH_SHORT).show()
            btnVeu.visibility = View.GONE
        }
    }

    /**
     * Interpreta les comandes de veu reconegudes.
     *
     * Segons el text detectat, navega a diferents pantalles de l'aplicació:
     * - "configuració": obre la pantalla de configuració.
     * - "tasques": obre la pantalla de tasques.
     * - "enrere": torna a la pantalla anterior.
     *
     * @param command text reconegut per el reconeixement de veu
     */
    private fun handleVoiceCommand(command: String?) {
        when {
            command?.contains("configuració") == true -> {
                startActivity(Intent(this, ConfigurationActivity::class.java))
            }
            command?.contains("tasques") == true -> {
                startActivity(Intent(this, TasksTodayActivity::class.java))
            }
            command?.contains("enrere") == true -> {
                onBackPressedDispatcher.onBackPressed()
            }
        }
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
        btnVeu.setOnClickListener {
            recognizer.startListening(recognizerIntent)
        }
    }

    private fun initComponents() {
        btnTestNav = findViewById(R.id.btnTestNav)
        btnVeu = findViewById(R.id.btnVeu)
    }

    /**
     * Es crida quan l'activitat es tanca.
     * Es destroien els recursos del reconeixement de veu.
     */
    override fun onDestroy() {
        super.onDestroy()
        recognizer.destroy()
    }
}


