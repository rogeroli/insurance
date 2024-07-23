package com.itau.insurance.application.mapper

import com.itau.insurance.domain.Product
import com.itau.insurance.presentation.dto.ProductDtoRequest
import com.itau.insurance.presentation.dto.ProductDtoResponse
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class ProductMapper {

    fun toDomain(dto: ProductDtoRequest, priceCalculated: BigDecimal): Product {
        return Product(
            name = dto.name,
            category = dto.category,
            priceBase = priceCalculated
        )
    }

    fun toDto(product: Product, priceTariff: BigDecimal): ProductDtoResponse {
        return ProductDtoResponse(
            id = product.id!!,
            name = product.name,
            category = product.category,
            priceBase = product.priceBase,
            priceTariff = priceTariff
        )
    }
}