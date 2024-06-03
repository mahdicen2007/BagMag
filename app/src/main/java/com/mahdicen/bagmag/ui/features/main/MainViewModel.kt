package com.mahdicen.bagmag.ui.features.main

import android.util.Log
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahdicen.bagmag.model.data.Ads
import com.mahdicen.bagmag.model.data.CheckOut
import com.mahdicen.bagmag.model.data.Product
import com.mahdicen.bagmag.model.repository.cart.CartRepository
import com.mahdicen.bagmag.model.repository.product.ProductRepository
import com.mahdicen.bagmag.util.PAYMENT_SUCCESS
import com.mahdicen.bagmag.util.coroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(
    private val productRepository: ProductRepository ,
    private val cartRepository: CartRepository,
    isInternetConnected :Boolean
) :ViewModel() {

    val dataProduct = mutableStateOf<List<Product>>( listOf() )
    val dataAds = mutableStateOf<List<Ads>>( listOf() )
    val showProgressBar = mutableStateOf(false)
    val badgeNumber = mutableStateOf(0)
    
    val showPaymentResultDialog = mutableStateOf(false)
    val checkOutData = mutableStateOf(CheckOut(null , null))

    // as soon as defining this viewModel in view it will be handle here =>
    init {
        refreshAllDataFromServer(isInternetConnected)
    }

    private fun refreshAllDataFromServer(isInternetConnected: Boolean) {

        viewModelScope.launch(context = coroutineExceptionHandler) {

            if (isInternetConnected) {
                showProgressBar.value = true
            }

            val newDataProducts = async { productRepository.getAllProducts(isInternetConnected) }
            val newDataAds = async { productRepository.getAllAds(isInternetConnected) }

            updateData(newDataProducts.await() , newDataAds.await())



            showProgressBar.value = false

        }

    }

    fun loadBadgeNumber() {

        viewModelScope.launch(coroutineExceptionHandler) {
            badgeNumber.value = cartRepository.getCartSize()
        }
    }

    private fun updateData(products :List<Product> , ads :List<Ads>) {
        dataProduct.value = products
        dataAds.value = ads
    }

    fun getPaymentStatus() :Int {
        return cartRepository.getPurchaseStatus()
    }
    
    fun setPaymentStatus(status :Int) {
        cartRepository.setPurchaseStatus(status)
    }
    
    fun getCheckOutData() {
        viewModelScope.launch(coroutineExceptionHandler) {
            
            val result = cartRepository.checkOut(cartRepository.getOrderId())
            if (result.success!!) {
                checkOutData.value = result
                showPaymentResultDialog.value = true
                setPaymentStatus(PAYMENT_SUCCESS)
            } 
            
        }
    }

}