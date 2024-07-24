package com.itau.insurance.application

import com.itau.insurance.application.mapper.ProductMapper
import com.itau.insurance.application.common.Logger
import com.itau.insurance.domain.enums.Category
import com.itau.insurance.domain.exception.ProductNotFoundException
import com.itau.insurance.infrastructure.repository.ProductRepository
import com.itau.insurance.presentation.dto.ProductDtoRequest
import com.itau.insurance.presentation.dto.ProductDtoResponse
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ProductService(
    private val repository: ProductRepository,
    private val mapper: ProductMapper
) {

    companion object {
        private val PERCENTAGE_DIVISOR = BigDecimal.valueOf(100)
        private val logger = Logger.getLogger(this::class.java)
    }

    fun create(productDtoRequest: ProductDtoRequest): ProductDtoResponse {
        val priceTariff =
            productDtoRequest.priceTariff ?:
            calculatePriceTariff(productDtoRequest.category, productDtoRequest.priceBase)

        val product = mapper.toDomain(productDtoRequest, priceTariff)
        val productPersisted = repository.save(product)

        return mapper.toDto(productPersisted).let { response ->
                logger.info("[CREATE_PRODUCT] Product with id ${response.id} created successfully")
            response
        }
    }

    fun update(id: Long, dtoRequest: ProductDtoRequest): ProductDtoResponse {

        val productPersisted = repository.findById(id)?: throw ProductNotFoundException(id = id)

        val productToUpdate = productPersisted.copy(
            name = dtoRequest.name,
            category = dtoRequest.category,
            priceBase = dtoRequest.priceBase,
            priceTariff = dtoRequest.priceTariff ?: calculatePriceTariff(dtoRequest.category, dtoRequest.priceBase)
        )

        val productUpdated = repository.save(productToUpdate)

        return mapper.toDto(productUpdated).let { response ->
            logger.info("[UPDATE_PRODUCT] Product with id $id was updated successfully")
            response
        }
    }


    private fun calculatePriceTariff(category: Category, priceBase: BigDecimal): BigDecimal {
        val iof = BigDecimal.valueOf(category.iof).divide(PERCENTAGE_DIVISOR)
        val pis = BigDecimal.valueOf(category.pis).divide(PERCENTAGE_DIVISOR)
        val cofins = BigDecimal.valueOf(category.cofins).divide(PERCENTAGE_DIVISOR)

        return (priceBase + (priceBase.multiply(iof)) + (priceBase.multiply(pis)) + (priceBase.multiply(cofins))).let {
            result -> logger.info("[CALCULATE_PRICE_TARIFF] Price tariff calculated with success. Value: $result")
            result
        }
    }
}