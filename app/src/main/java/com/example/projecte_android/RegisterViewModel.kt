package com.example.projecte_android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {
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

    fun setName(value: String){
        _name.value = value
    }
    fun setEmail(value: String){
        _email.value = value
    }
    fun setPassword(value: String){
        _password.value = value
    }
    fun setPasswordConfirm(value: String){
        _passwordConfirm.value = value
    }

    private fun isEmailValid(email: String): Boolean {
        // Expresió regular per validar emails
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
        return email.matches(emailRegex)
    }

    fun register(){
        val nom = _name.value.orEmpty()
        val email = _email.value.orEmpty()
        val contrasenya = _password.value.orEmpty()
        val confirmaContra = _passwordConfirm.value.orEmpty()
        when{
            nom.isEmpty() -> _registrationError.value = "El nom no pot estar buit"
            email.isEmpty() || !isEmailValid(email) -> _registrationError.value = "Correu invàlid"
            contrasenya.length < 6 -> _registrationError.value = "La contrasenya ha de tenir almenys 6 caracters"
            contrasenya != confirmaContra -> _registrationError.value = "Les contrasenyes no coincideixen"
            else -> {
                _registrationError.value = null
                _registrationSuccess.value = true
            }
        }
    }

}