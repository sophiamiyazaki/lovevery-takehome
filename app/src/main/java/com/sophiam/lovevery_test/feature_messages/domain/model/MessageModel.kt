package com.sophiam.lovevery_test.feature_messages.domain.model

data class MessageModel(
    val user: String,
    val operation: String?,
    val subject: String,
    val message: String
)
