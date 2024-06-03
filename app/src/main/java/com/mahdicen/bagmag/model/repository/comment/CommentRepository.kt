package com.mahdicen.bagmag.model.repository.comment

import com.mahdicen.bagmag.model.data.Comment

interface CommentRepository {

    suspend fun getAllComments(productId :String) :List<Comment>
    suspend fun addNewComment(productId: String , text :String , isSuccess :(String) -> Unit)

}