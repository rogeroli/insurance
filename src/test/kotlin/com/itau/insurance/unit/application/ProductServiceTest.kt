package com.itau.insurance.unit.application

import com.itau.insurance.application.ProductService
import com.itau.insurance.application.exceptions.DataBaseGenericException
import com.itau.insurance.application.mapper.ProductMapper
import com.itau.insurance.domain.Product
import com.itau.insurance.domain.enums.Category
import com.itau.insurance.domain.exception.ProductNotFoundException
import com.itau.insurance.infrastructure.repository.ProductRepository
import com.itau.insurance.presentation.dto.ProductDtoRequest
import com.itau.insurance.presentation.dto.ProductDtoResponse
import io.mockk.every
import io.mockk.mockk
import jakarta.persistence.PersistenceException
import org.hibernate.HibernateException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

@SpringBootTest
class ProductServiceTest {
    private lateinit var repository: ProductRepository
    private lateinit var mapper: ProductMapper
    private lateinit var productService: ProductService

    @BeforeTest
    fun setup() {
        repository = mockk()
        mapper = mockk()
        productService = ProductService(repository, mapper)
    }

    @Test
    fun `should create VIDA product successfully, when client not send price tariff`() {
        val dtoRequest = ProductDtoRequest(
            name = "Product1",
            category = Category.VIDA,
            priceBase = BigDecimal.valueOf(100.0)
        )

        val product = Product(
            id = 1L,
            name = dtoRequest.name,
            category = dtoRequest.category,
            priceBase = dtoRequest.priceBase,
            priceTariff = BigDecimal.valueOf(103.20)
        )

        val productDtoResponse = ProductDtoResponse(
            id = product.id!!,
            name = product.name,
            category = product.category,
            priceBase = product.priceBase,
            priceTariff = product.priceTariff
        )
        every { mapper.toDomain(dtoRequest, any()) } returns product
        every { repository.save(product) } returns product
        every { mapper.toDto(product) } returns productDtoResponse

        val response = productService.create(dtoRequest)

        assertEquals(productDtoResponse, response)
    }

    @Test
    fun `should create AUTO product successfully, when client not send price tariff`() {
        val dtoRequest = ProductDtoRequest(
            name = "Product1",
            category = Category.AUTO,
            priceBase = BigDecimal.valueOf(50.0)
        )

        val product = Product(
            id = 1L,
            name = dtoRequest.name,
            category = dtoRequest.category,
            priceBase = dtoRequest.priceBase,
            priceTariff = BigDecimal.valueOf(55.25)
        )

        val productDtoResponse = ProductDtoResponse(
            id = product.id!!,
            name = product.name,
            category = product.category,
            priceBase = product.priceBase,
            priceTariff = product.priceTariff
        )
        every { mapper.toDomain(dtoRequest, any()) } returns product
        every { repository.save(product) } returns product
        every { mapper.toDto(product) } returns productDtoResponse

        val response = productService.create(dtoRequest)

        assertEquals(productDtoResponse, response)
    }

    @Test
    fun `should create product successfully, when client send price tariff`() {
        val dtoRequest = ProductDtoRequest(
            name = "Product1",
            category = Category.AUTO,
            priceBase = BigDecimal.valueOf(50.0),
            priceTariff = BigDecimal.valueOf(105.22),
        )

        val product = Product(
            id = 1L,
            name = dtoRequest.name,
            category = dtoRequest.category,
            priceBase = dtoRequest.priceBase,
            priceTariff = BigDecimal.valueOf(105.22)
        )

        val productDtoResponse = ProductDtoResponse(
            id = product.id!!,
            name = product.name,
            category = product.category,
            priceBase = product.priceBase,
            priceTariff = product.priceTariff
        )
        every { mapper.toDomain(dtoRequest, any()) } returns product
        every { repository.save(product) } returns product
        every { mapper.toDto(product) } returns productDtoResponse

        val response = productService.create(dtoRequest)

        assertEquals(productDtoResponse, response)
    }

    @Test
    fun `should update product successfully, when there is a product with id`() {

        val productAlreadyExist = Product(
            id = 1L,
            name = "Product1",
            category = Category.AUTO,
            priceBase = BigDecimal.valueOf(50.0),
            priceTariff = BigDecimal.valueOf(105.22),
        )

        val dtoRequest = ProductDtoRequest(
            name = "Product1_updated",
            category = Category.VIAGEM,
            priceBase = BigDecimal.valueOf(10.0),
            priceTariff = BigDecimal.valueOf(20.22),
        )

        val product = Product(
            id = 1L,
            name = dtoRequest.name,
            category = dtoRequest.category,
            priceBase = dtoRequest.priceBase,
            priceTariff = dtoRequest.priceTariff!!
        )

        val productDtoResponse = ProductDtoResponse(
            id = product.id!!,
            name = product.name,
            category = product.category,
            priceBase = product.priceBase,
            priceTariff = product.priceTariff
        )

        every { mapper.toDomain(dtoRequest, any()) } returns product
        every { repository.findById(1L) } returns productAlreadyExist
        every { repository.save(product) } returns product
        every { mapper.toDto(product) } returns productDtoResponse

        val response = productService.update(1L, dtoRequest)

        assertEquals(productDtoResponse, response)
    }

    @Test
    fun `should throw ProductNotFoundException, when there is not a product with id`() {

        val dtoRequest = ProductDtoRequest(
            name = "Product1_updated",
            category = Category.VIAGEM,
            priceBase = BigDecimal.valueOf(10.0),
            priceTariff = BigDecimal.valueOf(20.22),
        )
        every { repository.findById(2L) } throws ProductNotFoundException(2L)

        assertThrows<ProductNotFoundException> {
            productService.update(2L, dtoRequest)
        }
    }

    @Test
    fun `should update product with calculated price tariff when price tariff is not provided`() {
        val productAlreadyExist = Product(
            id = 1L,
            name = "Product1",
            category = Category.AUTO,
            priceBase = BigDecimal.valueOf(50.0),
            priceTariff = BigDecimal.valueOf(55.25)
        )

        val dtoRequest = ProductDtoRequest(
            name = "Product1_updated",
            category = Category.VIDA,
            priceBase = BigDecimal.valueOf(100.0),
        )

        val calculatedPriceTariff = BigDecimal.valueOf(103.20)

        val product = Product(
            id = 1L,
            name = dtoRequest.name,
            category = dtoRequest.category,
            priceBase = dtoRequest.priceBase,
            priceTariff = calculatedPriceTariff
        )

        val productDtoResponse = ProductDtoResponse(
            id = product.id!!,
            name = product.name,
            category = product.category,
            priceBase = product.priceBase,
            priceTariff = product.priceTariff
        )

        every { mapper.toDomain(dtoRequest, any()) } returns product
        every { repository.findById(1L) } returns productAlreadyExist
        every { repository.save(any()) } returns product
        every { mapper.toDto(product) } returns productDtoResponse

        val response = productService.update(1L, dtoRequest)

        assertEquals(productDtoResponse, response)
    }

    @Test
    fun `should throw exception when repository save fails during creation`() {
        val dtoRequest = ProductDtoRequest(
            name = "ProductFail",
            category = Category.AUTO,
            priceBase = BigDecimal.valueOf(50.0)
        )

        val product = Product(
            id = 1L,
            name = dtoRequest.name,
            category = dtoRequest.category,
            priceBase = dtoRequest.priceBase,
            priceTariff = BigDecimal.valueOf(55.25)
        )

        every { mapper.toDomain(dtoRequest, any()) } returns product
        every { repository.save(product) } throws RuntimeException("Database error")

        assertThrows<RuntimeException> {
            productService.create(dtoRequest)
        }
    }

    @Test
    fun `should throw DataBaseGenericException when repository save fails during creation`() {
        val dtoRequest = ProductDtoRequest(
            name = "ProductFail",
            category = Category.AUTO,
            priceBase = BigDecimal.valueOf(50.0)
        )

        val product = Product(
            id = 1L,
            name = dtoRequest.name,
            category = dtoRequest.category,
            priceBase = dtoRequest.priceBase,
            priceTariff = BigDecimal.valueOf(55.25)
        )

        every { mapper.toDomain(dtoRequest, any()) } returns product
        every { repository.save(product) } throws HibernateException("Database error")

        val exception = assertThrows<DataBaseGenericException> {
            productService.create(dtoRequest)
        }

        assertEquals("Error to persist product", exception.message)
    }

    @Test
    fun `should throw DataBaseGenericException when repository save fails during update`() {
        val productAlreadyExist = Product(
            id = 1L,
            name = "Product1",
            category = Category.AUTO,
            priceBase = BigDecimal.valueOf(50.0),
            priceTariff = BigDecimal.valueOf(55.25)
        )

        val dtoRequest = ProductDtoRequest(
            name = "Product1_updated",
            category = Category.VIDA,
            priceBase = BigDecimal.valueOf(100.0)
        )

        every { repository.findById(1L) } returns productAlreadyExist
        every { mapper.toDomain(dtoRequest, any()) } returns productAlreadyExist.copy(
            name = dtoRequest.name,
            category = dtoRequest.category,
            priceBase = dtoRequest.priceBase,
            priceTariff = BigDecimal.valueOf(103.20)
        )
        every { repository.save(any()) } throws PersistenceException("Database error")

        val exception = assertThrows<DataBaseGenericException> {
            productService.update(1L, dtoRequest)
        }

        assertEquals("Error to persist product", exception.message)
    }

    @Test
    fun `should throw DataBaseGenericException when repository findById fails`() {
        val dtoRequest = ProductDtoRequest(
            name = "Product1_updated",
            category = Category.VIAGEM,
            priceBase = BigDecimal.valueOf(10.0),
            priceTariff = BigDecimal.valueOf(20.22)
        )

        every { repository.findById(2L) } throws HibernateException("Database error")

        val exception = assertThrows<DataBaseGenericException> {
            productService.update(2L, dtoRequest)
        }

        assertEquals("Error to find product", exception.message)
    }



}



