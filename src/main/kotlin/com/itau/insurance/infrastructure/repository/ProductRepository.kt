package com.itau.insurance.infrastructure.repository

import com.itau.insurance.domain.Product

interface ProductRepository {
    fun save(product: Product): Product
    fun findById(id: Long): Product?
}
