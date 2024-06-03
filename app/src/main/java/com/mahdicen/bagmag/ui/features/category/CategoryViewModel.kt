package com.mahdicen.bagmag.ui.features.category

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahdicen.bagmag.model.data.Product
import com.mahdicen.bagmag.model.repository.product.ProductRepository
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val productRepository :ProductRepository
) :ViewModel() {

    val dataProductsCategory = mutableStateOf<List<Product>>( listOf() )

    fun loadDataByCategory(category :String) {

        viewModelScope.launch {

            val dataFromLocal = productRepository.getAllProductsByCategory(category)
            dataProductsCategory.value = dataFromLocal

        }

    }



}