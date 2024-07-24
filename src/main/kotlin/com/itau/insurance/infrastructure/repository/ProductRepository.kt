package com.itau.insurance.infrastructure.repository

import com.itau.insurance.domain.Product
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository {
    fun save(product: Product): Product
    fun findById(id: Long): Product?
    fun deleteAll()
    fun flush()
}
