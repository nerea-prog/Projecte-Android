package com.example.projecte_android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel de la pantalla de registre que segueix el patró MVVM.
 *
 * Aquesta classe s'encarrega de gestionar tota la lògica relacionada
 * amb el registre d'usuaris, mantenint la UI lliure de lògica de negoci.
 *
 * El ViewModel controla:
 * - Les dades introduïdes a la pantalla de registre
 * - Les validacions del formulari (nom, email i contrasenyes)
 * - L'estat del procés de registre (èxit o error)
 *
 * La comunicació amb la vista es fa mitjançant LiveData, permetent
 * que l'Activity observi els canvis i actualitzi la UI
 * respectant el cicle de vida.
 */
class RegisterViewModel : ViewModel() {
    /**
     * S'utilitza MutableLiveData per modificar les dades dins del ViewModel
     * i LiveData per exposar-les a la UI de manera segura.
     *
     * Això evita que la vista pugui modificar directament l'estat intern
     * del ViewModel i ajuda a mantenir una arquitectura més neta.
     */
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _passwordConfirm = MutableLiveData<String>()
    val passwordConfirm: LiveData<String> = _passwordConfirm

    private val _registrationError = MutableLiveData<String?>()
    val registrationError: LiveData<String?> = _registrationError

    private val _registrationSuccess = MutableLiveData<Boolean>()
    val registrationSuccess: LiveData<Boolean> = _registrationSuccess

    /**
     * Actualitza el nom d'usuari introduït per l'usuari.
     * @param value Text introduït per l'usuari
     */
    fun setName(value: String){
        _name.value = value
    }
    /**
     * Actualitza el correu del usuari introduït per l'usuari.
     * @param value Text introduït per l'usuari
     */
    fun setEmail(value: String){
        _email.value = value
    }
    /**
     * Actualitza la contrassenya del usuari introduït per l'usuari.
     * @param value Text introduït per l'usuari
     */
    fun setPassword(value: String){
        _password.value = value
    }
    /**
     * Actualitza la confirmació de la contrassenya del usuari introduït per l'usuari.
     * @param value Text introduït per l'usuari
     */
    fun setPasswordConfirm(value: String){
        _passwordConfirm.value = value
    }

    /**
     * Funció que valida el format del correu electrònic.
     *
     * Comprova que l'email segueixi una expressió regular correcta,
     * amb un nom d'usuari, símbols vàlids i domini.
     *
     * @param email El correu electrònic introduït per l'usuari.
     * @return Boolean indicant si l'email és vàlid (true) o no (false).
     */
    private fun isEmailValid(email: String): Boolean {
        // Expresió regular per validar emails
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
        return email.matches(emailRegex)
    }

    /**
     * Funció que valida les dades del registre.
     *
     * Comprova:
     * - Que el nom no estigui buit
     * - Que l'email sigui correcte i no estigui buit
     * - Que la contrasenya tingui almenys 6 caràcters
     * - Que la confirmació de la contrasenya coincideixi amb la contrasenya
     *
     * En funció del resultat, actualitza els LiveData corresponents
     * perquè la UI pugui mostrar errors o indicar registre exitós.
     */
    fun register(){
        val nom = _name.value.orEmpty()
        val email = _email.value.orEmpty()
        val contrasenya = _password.value.orEmpty()
        val confirmaContra = _passwordConfirm.value.orEmpty()
        when{
            nom.isEmpty() -> _registrationError.value = "El nom no pot estar buit"
            email.isEmpty() || !isEmailValid(email) -> _registrationError.value = "Correu invàlid"
            contrasenya.isEmpty() -> _registrationError.value = "La contrasenya no pot estar buida"
            contrasenya.length < 6 -> _registrationError.value = "La contrasenya ha de tenir almenys 6 caracters"
            contrasenya != confirmaContra -> _registrationError.value = "Les contrasenyes no coincideixen"
            else -> {
                _registrationError.value = null
                _registrationSuccess.value = true
            }
        }
    }
}