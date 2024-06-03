package com.mahdicen.bagmag.ui.features.signUp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahdicen.bagmag.model.repository.user.UserRepository
import com.mahdicen.bagmag.util.coroutineExceptionHandler
import kotlinx.coroutines.launch

class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {

    val name = MutableLiveData("")
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val confirmPass = MutableLiveData("")

    fun signUpUser(loggingEvent :(String) -> Unit) {

        viewModelScope.launch(context = coroutineExceptionHandler) {

            val result = userRepository
                .signUp(
                    name.value!!,
                    email.value!!,
                    password.value!!
                )

            loggingEvent(result)

        }

    }

}