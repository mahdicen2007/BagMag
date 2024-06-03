package com.mahdicen.bagmag.model.net

import com.mahdicen.bagmag.model.data.LogInResponse
import com.mahdicen.bagmag.model.repository.TokenInMemory
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthChecker :Authenticator , KoinComponent {
    private val apiService :ApiService by inject()

    override fun authenticate(route: Route?, response: Response): Request? {

        if (
            TokenInMemory.token != null &&
            !response.request.url.pathSegments.last().equals("refreshToken" , false)
            ) {

            val result = refreshToken()
            return if (result) {
                // old request has been refreshed in time =>
                response.request // the previous request that has been returned as a problem
            } else {
                null
            }

        } else {
            return null
        }
    }

    private fun refreshToken() :Boolean {
        
        val request : retrofit2.Response<LogInResponse> =
            apiService.refreshToken().execute()

        if (request.body() != null) {
            if (request.body()!!.success) {
                return true
            }
        }

        return false
    }
}