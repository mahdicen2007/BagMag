package com.mahdicen.bagmag.ui.features.cart

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahdicen.bagmag.model.data.Product
import com.mahdicen.bagmag.model.repository.cart.CartRepository
import com.mahdicen.bagmag.model.repository.user.UserRepository
import com.mahdicen.bagmag.util.coroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository :CartRepository ,
    private val userRepository: UserRepository
) :ViewModel() {

    val productList = mutableStateOf(listOf<Product>())
    val totalPrice = mutableStateOf(0)
    val isChangingNumber = mutableStateOf(Pair("" , false))

    fun loadCartData() {

        viewModelScope.launch(coroutineExceptionHandler) {

            val data = cartRepository.getUserCartInfo()
            productList.value = data.productList
            totalPrice.value = data.totalPrice

        }


    }

    fun purchaseAll(address: String , postalCode: String , IsSuccess :(Boolean , String) -> Unit) {

        viewModelScope.launch(coroutineExceptionHandler) {

            val result = cartRepository.submitOrder(address , postalCode)
            IsSuccess.invoke(result.success , result.paymentLink)

        }


    }

    fun setPaymentStatus(status :Int) {
        cartRepository.setPurchaseStatus(status)
    }

    fun getUserLocation() :Pair<String , String> {
        return userRepository.getUserLocation()
    }

    fun setUserLocation(address :String , postalCode :String) {
        userRepository.saveUserLocation(address , postalCode)
    }

    fun addItem(productID :String) {
        viewModelScope.launch(coroutineExceptionHandler) {

            isChangingNumber.value = isChangingNumber.value.copy(productID , true)

            val isSuccess = cartRepository.addToCart(productID)
            if (isSuccess)
                loadCartData()

            delay(250)
            isChangingNumber.value = isChangingNumber.value.copy(productID , false)

        }
    }

    fun removeItem(productID :String) {
        viewModelScope.launch(coroutineExceptionHandler) {

            isChangingNumber.value = isChangingNumber.value.copy(productID , true)

            val isSuccess = cartRepository.removeFromCart(productID)
            if (isSuccess)
                loadCartData()

            delay(250)
            isChangingNumber.value = isChangingNumber.value.copy(productID , false)

        }
    }

}