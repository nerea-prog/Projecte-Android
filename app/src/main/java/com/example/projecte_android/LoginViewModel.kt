package com.example.projecte_android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LoginViewModel {
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginError = MutableLiveData<String>()
    val loginError: LiveData<String?> = _loginError

    private val _loginSucces = MutableLiveData<Boolean>()
    val loginSucces: MutableLiveData<Boolean> = _loginSucces

    fun setUsername(value: String){
        _username.value = value
    }

    fun setPassword(value: String){
        _password.value = value
    }

    fun login(){
        val user = _username.value.orEmpty()
        val contraseña = _password.value.orEmpty()

        when{
            user.isEmpty() -> _loginError.value = "El usuari no pot estar buit"
            contraseña.isEmpty() -> _loginError.value = "La contraseña no pot estar buida"
            contraseña.length < 6 -> _loginError.value = "La contraseña ha de tenir almenys 6 caracters"
            else -> {
                _loginError.value = null
                _loginSucces.value = true
            }
        }
    }
}