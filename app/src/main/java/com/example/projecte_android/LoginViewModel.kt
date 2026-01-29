package com.example.projecte_android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel del Login que implementa el patró MVVM.
 *
 * Aquesta classe s'encarrega de tota la lògica de la pantalla
 * de login, separant-la completament de la UI (LoginActivity).
 *
 * El ViewModel gestiona:
 * - Les dades introduïdes per l'usuari (usuari i contrasenya)
 * - Les validacions del formulari de login
 * - L'estat del login (èxit o error)
 *
 * Per comunicar-se amb la UI utilitza LiveData, que permet observar
 * els canvis de dades de manera segura respecte al cicle de vida.
 */
class LoginViewModel : ViewModel() {

    /**
     * LiveData s'utilitza per exposar dades observables a la UI.
     *
     * Dins del ViewModel es fa servir MutableLiveData per poder modificar
     * els valors, mentre que cap a l'exterior s'exposen com a LiveData
     * de només lectura. Això garanteix que la UI no pugui modificar
     * directament les dades.
     */
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginError = MutableLiveData<String>()
    val loginError: LiveData<String?> = _loginError

    private val _loginSucces = MutableLiveData<Boolean>()
    val loginSucces: MutableLiveData<Boolean> = _loginSucces

    /**
     * Actualitza el nom d'usuari introduït per l'usuari.
     * @param value es un text
     */

    fun setUsername(value: String){
        _username.value = value
    }

    /**
     * Actualitza la contrasenya introduïda per l'usuari.
     * @param value es un text
     */
    fun setPassword(value: String){
        _password.value = value
    }

    /**
     * Funció que valida les dades del login.
     *
     * Comprova:
     * - Que el nom d'usuari no estigui buit
     * - Que la contrasenya no estigui buida
     * - Que la contrasenya tingui almenys 6 caràcters
     *
     * En funció del resultat, actualitza els LiveData corresponents
     * perquè la UI reaccioni automàticament.
     */
    fun login(){
        val user = _username.value.orEmpty()
        val password = _password.value.orEmpty()

        when{
            user.isEmpty() -> _loginError.value = "El usuari no pot estar buit"
            password.isEmpty() -> _loginError.value = "La contraseña no pot estar buida"
            password.length < 6 -> _loginError.value = "La contraseña ha de tenir almenys 6 caracters"
            else -> {
                _loginError.value = null
                _loginSucces.value = true
            }
        }
    }
}