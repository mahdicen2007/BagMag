package com.mahdicen.bagmag.ui.features.signIn

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahdicen.bagmag.model.repository.user.UserRepository
import com.mahdicen.bagmag.util.coroutineExceptionHandler
import kotlinx.coroutines.launch

class SignInViewModel(private val userRepository: UserRepository) : ViewModel() {

    val email = MutableLiveData("")
    val password = MutableLiveData("")

    fun signInUser(loggingEvent :(String) -> Unit) {

        viewModelScope.launch(context = coroutineExceptionHandler) {

            val result = userRepository
                .signIn(
                    email.value!!,
                    password.value!!
                )
            loggingEvent(result)

        }

    }

}