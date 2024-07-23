package com.itau.insurance.domain.repository

import com.itau.insurance.domain.Product
import com.itau.insurance.infrastructure.repository.ProductRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface JpaProductRepository: JpaRepository<Product, UUID>, ProductRepository