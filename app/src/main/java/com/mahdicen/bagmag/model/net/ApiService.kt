package com.mahdicen.bagmag.model.net

import com.google.gson.JsonObject
import com.mahdicen.bagmag.model.data.AddNewCommentResponse
import com.mahdicen.bagmag.model.data.AdsResponse
import com.mahdicen.bagmag.model.data.CartResponse
import com.mahdicen.bagmag.model.data.CheckOut
import com.mahdicen.bagmag.model.data.CommentResponse
import com.mahdicen.bagmag.model.data.LogInResponse
import com.mahdicen.bagmag.model.data.ProductResponse
import com.mahdicen.bagmag.model.data.SubmitOrder
import com.mahdicen.bagmag.model.data.UserCartInfo
import com.mahdicen.bagmag.model.repository.TokenInMemory
import com.mahdicen.bagmag.util.BaseUrl
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HEAD
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @POST("signUp")
    suspend fun signUp(@Body jsonObject :JsonObject) :LogInResponse

    @POST("signIn")
    suspend fun signIn(@Body jsonObject: JsonObject) :LogInResponse

    @GET("refreshToken")
    fun refreshToken() : Call<LogInResponse>

    @GET("getProducts")
    suspend fun getAllProducts() :ProductResponse

    @GET("getSliderPics")
    suspend fun getAllAds() :AdsResponse

    @POST("getComments")
    suspend fun getAllComments(@Body jsonObject: JsonObject) :CommentResponse

    @POST("addNewComment")
    suspend fun addNewComment(@Body jsonObject: JsonObject) :AddNewCommentResponse

    @POST("addToCart")
    suspend fun addProductToCart(@Body jsonObject: JsonObject) :CartResponse

    @POST("removeFromCart")
    suspend fun removeProductFromCart(@Body jsonObject: JsonObject) :CartResponse

    @GET("getUserCart")
    suspend fun getUserCart() :UserCartInfo

    @POST("submitOrder")
    suspend fun submitOrder(@Body jsonObject: JsonObject) :SubmitOrder

    @POST("checkout")
    suspend fun checkOut(@Body jsonObject: JsonObject) :CheckOut

}

fun createApiService() :ApiService {

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor {

            val oldRequest = it.request()
            val newRequest = oldRequest.newBuilder()

            if (TokenInMemory.token != null) {
                newRequest.addHeader("Authorization", TokenInMemory.token!!)
            }

            newRequest.addHeader("Accept" , "application/json")
            newRequest.method(oldRequest.method , oldRequest.body)

            return@addInterceptor it.proceed(newRequest.build())


        }.build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    return retrofit.create(ApiService::class.java)

}