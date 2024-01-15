package com.sophiam.lovevery_test.feature_messages.data.remote.dto

import com.sophiam.lovevery_test.feature_messages.domain.model.MessageModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MessageApi {
    @POST("https://abraxvasbh.execute-api.us-east-2.amazonaws.com/proto/messages")
    fun postMessageApi(
        @Body body: MessageModel?
    ): Call<MessageApiResponse>

    @GET("https://abraxvasbh.execute-api.us-east-2.amazonaws.com/proto/messages")
    fun getAllMessagesApi(): Call<MessageApiResponse>

    @GET("https://abraxvasbh.execute-api.us-east-2.amazonaws.com/proto/messages/{user}")
    fun getMessageApiByUser(
        @Path("user") user: String
    ): Call<MessageApiResponse>
}