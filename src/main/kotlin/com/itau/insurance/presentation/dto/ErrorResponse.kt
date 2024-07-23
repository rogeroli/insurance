package com.itau.insurance.presentation.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ErrorResponse(
    val message: String,
    val details: Map<String, Any>?=null
){
    companion object {
        fun create(message: String, details: Map<String, Any>?= null) = ErrorResponse(
            message = message,
            details = details
        )
    }
}