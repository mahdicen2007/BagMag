package com.mahdicen.bagmag.model.repository.user

interface UserRepository {

    // Online =>
    suspend fun signUp(name: String, userName: String, password: String) :String
    suspend fun signIn(username: String, password: String) :String

    // Offline =>
    fun signOut()
    fun loadToken()

    fun saveToken(newToken :String)
    fun getToken() :String?

    fun saveUserName(userName: String)
    fun getUserName() :String?

    fun saveUserLocation(address :String , postalCode :String)
    fun getUserLocation() :Pair<String , String>

    fun saveUserLoginTime()
    fun getUserLoginTime() :String

}