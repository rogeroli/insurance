package com.itau.insurance.unit.application.mapper

import com.itau.insurance.application.mapper.ProductMapper
import com.itau.insurance.domain.Product
import com.itau.insurance.domain.enums.Category
import com.itau.insurance.presentation.dto.ProductDtoRequest
import com.itau.insurance.presentation.dto.ProductDtoResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.RoundingMode

class ProductMapperTest {

    private val mapper = ProductMapper()

    @Test
    fun `should map ProductDtoRequest to Product`() {
        val dto = ProductDtoRequest(
            name = "Product1",
            category = Category.VIDA,
            priceBase = BigDecimal("100.1234")
        )
        val expectedPriceTariff = BigDecimal("103.12")

        val product = mapper.toDomain(dto, expectedPriceTariff)

        assertEquals(dto.name, product.name)
        assertEquals(dto.category, product.category)
        assertEquals(dto.priceBase.setScale(2, RoundingMode.DOWN), product.priceBase)
        assertEquals(expectedPriceTariff.setScale(2, RoundingMode.DOWN), product.priceTariff)
    }

    @Test
    fun `should map Product to ProductDtoResponse`() {
        val product = Product(
            id = 1L,
            name = "Product1",
            category = Category.VIDA,
            priceBase = BigDecimal("100.1234"),
            priceTariff = BigDecimal("103.1234")
        )

        val expectedDtoResponse = ProductDtoResponse(
            id = product.id!!,
            name = product.name,
            category = product.category,
            priceBase = product.priceBase.setScale(2, RoundingMode.DOWN),
            priceTariff = product.priceTariff.setScale(2, RoundingMode.DOWN)
        )

        val dtoResponse = mapper.toDto(product)

        assertEquals(expectedDtoResponse.id, dtoResponse.id)
        assertEquals(expectedDtoResponse.name, dtoResponse.name)
        assertEquals(expectedDtoResponse.category, dtoResponse.category)
        assertEquals(expectedDtoResponse.priceBase, dtoResponse.priceBase)
        assertEquals(expectedDtoResponse.priceTariff, dtoResponse.priceTariff)
    }
}
