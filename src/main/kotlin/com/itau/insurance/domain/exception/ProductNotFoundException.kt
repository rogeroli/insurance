package com.itau.insurance.domain.exception

import com.itau.insurance.application.common.Logger

class ProductNotFoundException(val id: Long, message: String = "Product not found") : RuntimeException(message) {
    companion object {
        private val logger = Logger.getLogger(this::class.java)
    }

    init {
        logger.info("[PRODUCT_NOT_FOUND] Product with ID $id not found")
    }
}
