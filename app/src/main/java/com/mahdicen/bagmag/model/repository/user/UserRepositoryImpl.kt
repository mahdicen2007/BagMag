package com.mahdicen.bagmag.model.repository.user

import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonObject
import com.mahdicen.bagmag.model.net.ApiService
import com.mahdicen.bagmag.model.repository.TokenInMemory

class UserRepositoryImpl(
    private val apiService :ApiService ,
    private val sharedPref :SharedPreferences
) : UserRepository {

    override suspend fun signUp(name: String, userName: String, password: String) :String {

        val jsonObject = JsonObject().apply {
            addProperty("name" , name)
            addProperty("email" , userName)
            addProperty("password" , password)
        }

        val result = apiService.signUp(jsonObject)



        return if (result.success) {

            TokenInMemory.refreshToken(userName , result.token)
            saveToken(result.token)
            saveUserName(userName)
            saveUserLoginTime()

            "Success"
        } else {
            result.message
        }

    }
    override suspend fun signIn(username: String, password: String) :String {

        val jsonObject = JsonObject().apply {
            addProperty("email" , username)
            addProperty("password" , password)
        }

        val result = apiService.signIn(jsonObject)
        return if (result.success) {

            TokenInMemory.refreshToken(username , result.token)
            saveToken(result.token)
            saveUserName(username)
            saveUserLoginTime()

            "Success"
        } else {
            result.message
        }

    }

    override fun signOut() {
        TokenInMemory.refreshToken(null , null)
        sharedPref.edit().clear().apply()
    }

    override fun loadToken() {
        TokenInMemory.refreshToken(getUserName() , getToken())
    }

    override fun saveToken(newToken: String) {
        sharedPref.edit().putString("token" , newToken).apply()
    }

    override fun getToken(): String? {
        return sharedPref.getString("token" , null)
    }

    override fun saveUserName(userName: String) {
        sharedPref.edit().putString("username" , userName).apply()
    }

    override fun getUserName(): String? {
        return sharedPref.getString("username" , null)
    }

    override fun saveUserLocation(address: String, postalCode: String) {
        sharedPref.edit().putString("address" , address).apply()
        sharedPref.edit().putString("postalCode" , postalCode).apply()
    }

    override fun getUserLocation(): Pair<String, String> {
        val address = sharedPref.getString("address" , "Click to add")!!
        val postalCode = sharedPref.getString("postalCode" , "Click to add")!!
        return Pair(address , postalCode)
    }

    override fun saveUserLoginTime() {

        val now = System.currentTimeMillis()
        sharedPref.edit().putString("loginTime" , now.toString()).apply()

    }

    override fun getUserLoginTime() :String {
        return sharedPref.getString("loginTime" , "N/A")!!
    }


}