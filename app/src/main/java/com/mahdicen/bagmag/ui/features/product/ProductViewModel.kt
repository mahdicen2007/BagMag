package com.mahdicen.bagmag.ui.features.product

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahdicen.bagmag.model.data.Comment
import com.mahdicen.bagmag.model.data.Product
import com.mahdicen.bagmag.model.repository.cart.CartRepository
import com.mahdicen.bagmag.model.repository.comment.CommentRepository
import com.mahdicen.bagmag.model.repository.product.ProductRepository
import com.mahdicen.bagmag.util.EmptyProduct
import com.mahdicen.bagmag.util.coroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productRepository: ProductRepository,
    private val commentRepository: CommentRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    val thisProduct = mutableStateOf(EmptyProduct)
    val comments = mutableStateOf(listOf<Comment>())
    val isAddingProduct = mutableStateOf(false)
    val badgeNumber = mutableStateOf(0)

    fun loadData(productId: String, isInternetConnected: Boolean) {
        loadProductFromCache(productId = productId)

        if (isInternetConnected) {
            loadAllComments(productId)
            loadBadgeNumber()
        }

    }

    private fun loadProductFromCache(productId: String) {

        viewModelScope.launch(coroutineExceptionHandler) {
            thisProduct.value =
                productRepository.getProductById(productId = productId)
        }

    }

    private fun loadBadgeNumber() {

        viewModelScope.launch(coroutineExceptionHandler) {
            badgeNumber.value = cartRepository.getCartSize()
        }
    }

    private fun loadAllComments(productId: String) {

        viewModelScope.launch(coroutineExceptionHandler) {
            comments.value =
                commentRepository.getAllComments(productId = productId)
        }

    }

    fun addNewComment(
        productId: String,
        text: String,
        isSuccess: (String) -> Unit
    ) {

        viewModelScope.launch(coroutineExceptionHandler) {

            commentRepository.addNewComment(productId, text, isSuccess)
            delay(500)
            comments.value = commentRepository.getAllComments(productId)

        }

    }

    fun addProductToCart(productId: String, addingToCartResult: (String) -> Unit) {

        viewModelScope.launch(coroutineExceptionHandler) {

            isAddingProduct.value = true

            val result = cartRepository.addToCart(productId)
            delay(1000)

            isAddingProduct.value = false

            if (result) {
                addingToCartResult.invoke("Product Added To Cart")
            } else {
                addingToCartResult.invoke("Product Not Added To Cart!")

            }

        }

    }

}