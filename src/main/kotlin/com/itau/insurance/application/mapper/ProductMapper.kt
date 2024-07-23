package com.itau.insurance.application.mapper

import com.itau.insurance.domain.Product
import com.itau.insurance.presentation.dto.ProductDtoRequest
import com.itau.insurance.presentation.dto.ProductDtoResponse
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.RoundingMode

@Component
class ProductMapper {

    fun toDomain(dto: ProductDtoRequest, priceCalculated: BigDecimal): Product {
        return Product(
            name = dto.name,
            category = dto.category,
            priceBase = dto.priceBase.setScale(2, RoundingMode.DOWN),
            priceTariff = priceCalculated.setScale(2, RoundingMode.DOWN)
        )
    }

    fun toDto(product: Product): ProductDtoResponse {
        return ProductDtoResponse(
            id = product.id!!,
            name = product.name,
            category = product.category,
            priceBase = product.priceBase.setScale(2, RoundingMode.DOWN),
            priceTariff = product.priceTariff.setScale(2, RoundingMode.DOWN)
        )
    }
}