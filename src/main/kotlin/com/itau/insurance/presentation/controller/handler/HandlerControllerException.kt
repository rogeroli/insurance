package com.itau.insurance.presentation.controller.handler

import com.itau.insurance.domain.exception.ProductNotFoundException
import com.itau.insurance.presentation.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class HandlerControllerException: ResponseEntityExceptionHandler() {

    @ExceptionHandler(ProductNotFoundException::class)
    fun productNotFound(exception: ProductNotFoundException): ResponseEntity<ErrorResponse> {

        val response = ErrorResponse.create(
            message = exception.message!!,
            details = mapOf("id" to exception.id)
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
    }
}