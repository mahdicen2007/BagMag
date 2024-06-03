package com.mahdicen.bagmag.ui.features.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.mahdicen.bagmag.model.repository.user.UserRepository

class ProfileViewModel(
    private val userRepository: UserRepository
) :ViewModel() {

    val email = mutableStateOf("")
    val address = mutableStateOf("")
    val postalCode = mutableStateOf("")
    val loginTime = mutableStateOf("")

    val showLocationDialog = mutableStateOf(false)

    fun loadUserData() {

        email.value = userRepository.getUserName()!!
        loginTime.value = userRepository.getUserLoginTime()
        address.value = userRepository.getUserLocation().first
        postalCode.value = userRepository.getUserLocation().second

    }


    fun signOut() {
        userRepository.signOut()
    }

    fun setUserLocation(address: String, postalCode: String) {
        userRepository.saveUserLocation(address, postalCode)
    }


}