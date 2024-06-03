package com.mahdicen.bagmag.model.repository.product

import android.util.Log
import com.mahdicen.bagmag.model.data.Ads
import com.mahdicen.bagmag.model.data.Product
import com.mahdicen.bagmag.model.db.ProductDao
import com.mahdicen.bagmag.model.net.ApiService

class ProductRepositoryImpl(
    private val apiService: ApiService ,
    private val productDao : ProductDao
) :ProductRepository {

    override suspend fun getAllProducts(isInternetConnected :Boolean): List<Product> {

        if (isInternetConnected) {
            // net
            val dataFromServer = apiService.getAllProducts()
            if (dataFromServer.success) {
                productDao.insertOrUpdate(dataFromServer.products) // Caching data into room
                return dataFromServer.products
            }
        } else {
            // local
            return productDao.getAll()
        }

        return listOf()

    }

    override suspend fun getAllProductsByCategory(category: String): List<Product> {

        return productDao.getAllByCategory(category)

    }

    override suspend fun getProductById(productId: String) :Product {
        return productDao.getProductById(productId = productId)
    }

    override suspend fun getAllAds(isInternetConnected: Boolean): List<Ads> {

        if (isInternetConnected) {
            // net
            val dataFromServer = apiService.getAllAds()
            if (dataFromServer.success)
                return dataFromServer.ads
        }

        return listOf()

    }

}