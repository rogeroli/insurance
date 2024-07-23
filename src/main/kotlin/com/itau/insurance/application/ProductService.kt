package com.itau.insurance.application

import com.itau.insurance.application.mapper.ProductMapper
import com.itau.insurance.domain.enums.Category
import com.itau.insurance.infrastructure.repository.ProductRepository
import com.itau.insurance.presentation.dto.ProductDtoRequest
import com.itau.insurance.presentation.dto.ProductDtoResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ProductService(
    private val repository: ProductRepository,
    private val mapper: ProductMapper
) {

    companion object {
        private val PERCENTAGE_DIVISOR = BigDecimal.valueOf(100)
        private val logger: Logger = LoggerFactory.getLogger(ProductService::class.java)
    }

    fun create(productDtoRequest: ProductDtoRequest): ProductDtoResponse {
        val priceTariff =
            productDtoRequest.priceTariff ?:
            calculatePriceTariff(productDtoRequest.category, productDtoRequest.priceBase)

        val product = mapper.toDomain(productDtoRequest, priceTariff)
        val productSaved = repository.save(product)
        return mapper.toDto(productSaved, BigDecimal.ONE)
    }

    fun update(id: Long, productDtoRequest: ProductDtoRequest): ProductDtoResponse {
        val product = repository.findById(id)

        if (product !== null) {
            val updatedProduct = product.copy(
                name = productDtoRequest.name,
                category = productDtoRequest.category,
                priceBase = productDtoRequest.priceBase ?: BigDecimal.TEN
            )
            val productUpdated = repository.save(updatedProduct)

            return mapper.toDto(productUpdated, BigDecimal.ONE)
        }
        else{
            throw IllegalArgumentException("Product ID cannot be null for update")
        }
    }

    private fun calculatePriceTariff(category: Category, priceBase: BigDecimal): BigDecimal {

        val iof = BigDecimal.valueOf(category.iof).divide(PERCENTAGE_DIVISOR)
        val pis = BigDecimal.valueOf(category.pis).divide(PERCENTAGE_DIVISOR)
        val cofins = BigDecimal.valueOf(category.cofins).divide(PERCENTAGE_DIVISOR)

        return priceBase + (priceBase.multiply(iof)) + (priceBase.multiply(pis)) + (priceBase.multiply(cofins))
    }
}