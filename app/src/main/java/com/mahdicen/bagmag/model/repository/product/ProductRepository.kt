package com.mahdicen.bagmag.model.repository.product

import com.mahdicen.bagmag.model.data.Ads
import com.mahdicen.bagmag.model.data.Product

interface ProductRepository {

    suspend fun getAllProducts(isInternetConnected :Boolean) :List<Product>

    suspend fun getAllProductsByCategory(category :String) :List<Product>

    suspend fun getProductById(productId:String) :Product

    suspend fun getAllAds(isInternetConnected :Boolean) :List<Ads>



}