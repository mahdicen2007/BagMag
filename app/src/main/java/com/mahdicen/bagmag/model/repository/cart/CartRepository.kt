package com.mahdicen.bagmag.model.repository.cart

import com.mahdicen.bagmag.model.data.CheckOut
import com.mahdicen.bagmag.model.data.SubmitOrder
import com.mahdicen.bagmag.model.data.UserCartInfo

interface CartRepository {

    suspend fun addToCart(productId: String): Boolean
    suspend fun removeFromCart(productId: String): Boolean

    suspend fun getCartSize(): Int
    suspend fun getUserCartInfo(): UserCartInfo

    suspend fun submitOrder(address: String, postalCode: String): SubmitOrder
    suspend fun checkOut(orderId: String): CheckOut

    fun setOrderId(orderId: String)
    fun getOrderId(): String
    fun setPurchaseStatus(status: Int)
    fun getPurchaseStatus(): Int


}