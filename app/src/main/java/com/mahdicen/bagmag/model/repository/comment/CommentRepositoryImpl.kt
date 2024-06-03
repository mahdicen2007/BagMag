package com.mahdicen.bagmag.model.repository.comment

import android.util.Log
import com.google.gson.JsonObject
import com.mahdicen.bagmag.model.data.Comment
import com.mahdicen.bagmag.model.net.ApiService
import com.mahdicen.bagmag.model.repository.TokenInMemory

class CommentRepositoryImpl(
    private val apiService: ApiService
) :CommentRepository {

    override suspend fun getAllComments(productId: String): List<Comment> {

        val jsonObject = JsonObject().apply {
            addProperty("productId" , productId)
        }

        val data = apiService.getAllComments(jsonObject)
        if (data.success) {
            return data.comments
        }

        return listOf()

    }

    override suspend fun addNewComment(
        productId: String,
        text: String,
        isSuccess: (String) -> Unit
    ) {

        val jsonObject = JsonObject().apply {
            addProperty("productId" , productId)
            addProperty("text" , text)
        }

        val result = apiService.addNewComment(jsonObject)
        if (result.success) {
            isSuccess.invoke(result.message)
        } else {
            isSuccess.invoke("Comment not added!")
        }

    }

}