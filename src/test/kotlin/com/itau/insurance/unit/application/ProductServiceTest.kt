package com.itau.insurance.application

import com.itau.insurance.application.mapper.ProductMapper
import com.itau.insurance.domain.Product
import com.itau.insurance.domain.enums.Category
import com.itau.insurance.domain.exception.ProductNotFoundException
import com.itau.insurance.infrastructure.repository.ProductRepository
import com.itau.insurance.presentation.dto.ProductDtoRequest
import com.itau.insurance.presentation.dto.ProductDtoResponse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import kotlin.test.assertEquals

@SpringBootTest
class ProductServiceTest {
    private val repository: ProductRepository = mock(ProductRepository::class.java)
    private val mapper: ProductMapper = mock(ProductMapper::class.java)
    private val productService = ProductService(repository, mapper)

    @Test
    fun `should create product successfully`() {
        val dtoRequest = ProductDtoRequest(
            name = "Product1",
            category = Category.AUTO,
            priceBase = BigDecimal.valueOf(100.0),
            priceTariff = null
        )

        val product = Product(
            id = 1L,
            name = dtoRequest.name,
            category = dtoRequest.category,
            priceBase = dtoRequest.priceBase,
            priceTariff = BigDecimal.valueOf(105.0) // Assuming this is the calculated tariff
        )

        val productDtoResponse = ProductDtoResponse(
            id = product.id!!,
            name = product.name,
            category = product.category,
            priceBase = product.priceBase,
            priceTariff = product.priceTariff
        )

        `when`(mapper.toDomain(dtoRequest, any())).thenReturn(product)
        `when`(repository.save(product)).thenReturn(product)
        `when`(mapper.toDto(product)).thenReturn(productDtoResponse)

        val response = productService.create(dtoRequest)

        assertEquals(productDtoResponse, response)
        verify(repository).save(product)
        verify(mapper).toDomain(dtoRequest, any())
        verify(mapper).toDto(product)
    }

    @Test
    fun `should update product successfully`() {
        val id = 1L
        val dtoRequest = ProductDtoRequest(
            name = "Updated Product",
            category = Category.VIDA,
            priceBase = BigDecimal.valueOf(150.0),
            priceTariff = null
        )

        val existingProduct = Product(
            id = id,
            name = "Old Product",
            category = Category.AUTO,
            priceBase = BigDecimal.valueOf(100.0),
            priceTariff = BigDecimal.valueOf(105.0)
        )

        val updatedProduct = existingProduct.copy(
            name = dtoRequest.name,
            category = dtoRequest.category,
            priceBase = dtoRequest.priceBase,
            priceTariff = BigDecimal.valueOf(160.0)
        )

        val productDtoResponse = ProductDtoResponse(
            id = updatedProduct.id!!,
            name = updatedProduct.name,
            category = updatedProduct.category,
            priceBase = updatedProduct.priceBase,
            priceTariff = updatedProduct.priceTariff
        )

        `when`(repository.findById(id)).thenReturn(existingProduct)
        `when`(repository.save(updatedProduct)).thenReturn(updatedProduct)
        `when`(mapper.toDto(updatedProduct)).thenReturn(productDtoResponse)

        val response = productService.update(id, dtoRequest)

        assertEquals(productDtoResponse, response)
        verify(repository).findById(id)
        verify(repository).save(updatedProduct)
        verify(mapper).toDto(updatedProduct)
    }

    @Test
    fun `should throw ProductNotFoundException when product does not exist`() {
        val id = 1L
        val dtoRequest = ProductDtoRequest(
            name = "Product",
            category = Category.AUTO,
            priceBase = BigDecimal.valueOf(100.0),
            priceTariff = null
        )

        `when`(repository.findById(id)).thenReturn(null)

        assertThrows<ProductNotFoundException> {
            productService.update(id, dtoRequest)
        }

        verify(repository).findById(id)
    }
}