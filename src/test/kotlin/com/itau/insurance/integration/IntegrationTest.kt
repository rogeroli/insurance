package com.itau.insurance.integration

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.itau.insurance.domain.Product
import com.itau.insurance.domain.enums.Category
import com.itau.insurance.infrastructure.repository.ProductRepository
import jakarta.transaction.Transactional
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class IntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var productRepository: ProductRepository

    private val objectMapper = ObjectMapper().apply {
        setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }

    @BeforeEach
    fun setup() {
        productRepository.deleteAll()
        productRepository.flush()
    }

    @Test
    fun `should create product successfully when client not send preco_tarifado`() {
        val dtoRequestWithAlias = DtoAlias(
            nome = "Product1",
            categoria = Category.VIDA,
            preco_base = BigDecimal.valueOf(100)
        )
        val requestJson = objectMapper.writeValueAsString(dtoRequestWithAlias)

        val dtoResponseWithAlias = DtoAlias(
            nome = dtoRequestWithAlias.nome,
            categoria = dtoRequestWithAlias.categoria,
            preco_base = dtoRequestWithAlias.preco_base,
            preco_tarifado = BigDecimal.valueOf(103.20)
        )

        mockMvc.perform(
            post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(status().isCreated)
            .andExpect(content().json(objectMapper.writeValueAsString(dtoResponseWithAlias)))

    }

    @Test
    fun `should create product successfully when client send preco_tarifado`() {
        val dtoRequestWithAlias = DtoAlias(
            nome = "Product1",
            categoria = Category.VIDA,
            preco_base = BigDecimal.valueOf(100),
            preco_tarifado = BigDecimal.valueOf(50)
        )
        val requestJson = objectMapper.writeValueAsString(dtoRequestWithAlias)

        val dtoResponseWithAlias = DtoAlias(
            nome = dtoRequestWithAlias.nome,
            categoria = dtoRequestWithAlias.categoria,
            preco_base = dtoRequestWithAlias.preco_base,
            preco_tarifado = BigDecimal.valueOf(50)
        )

        mockMvc.perform(
            post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(status().isCreated)
            .andExpect(content().json(objectMapper.writeValueAsString(dtoResponseWithAlias)))
    }


    @Test
    fun `should update product successfully when client not send preco_tarifado`() {
        val product = Product(
            name = "Product1",
            category = Category.VIDA,
            priceBase = BigDecimal.valueOf(100),
            priceTariff = BigDecimal.valueOf(103.20)
        )
        val savedProduct = productRepository.save(product)

        val dtoRequestWithAlias = DtoAlias(
            nome = "Product1",
            categoria = Category.VIDA,
            preco_base = BigDecimal.valueOf(100),
            preco_tarifado = BigDecimal.valueOf(105)
        )
        val requestJson = objectMapper.writeValueAsString(dtoRequestWithAlias)

        val dtoResponseWithAlias = DtoAlias(
            id = savedProduct.id!!,
            nome = dtoRequestWithAlias.nome,
            categoria = dtoRequestWithAlias.categoria,
            preco_base = dtoRequestWithAlias.preco_base,
            preco_tarifado = dtoRequestWithAlias.preco_tarifado
        )

        mockMvc.perform(
            put("/product/${savedProduct.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(dtoResponseWithAlias)))
    }

    @Test
    fun `should update product successfully when client send preco_tarifado`() {
        val product = Product(
            name = "Product1",
            category = Category.VIDA,
            priceBase = BigDecimal.valueOf(100),
            priceTariff = BigDecimal.valueOf(103.20)
        )
        val savedProduct = productRepository.save(product)

        val dtoRequestWithAlias = DtoAlias(
            nome = "Product1",
            categoria = Category.VIDA,
            preco_base = BigDecimal.valueOf(100)
        )
        val requestJson = objectMapper.writeValueAsString(dtoRequestWithAlias)

        val dtoResponseWithAlias = DtoAlias(
            id = savedProduct.id!!,
            nome = dtoRequestWithAlias.nome,
            categoria = dtoRequestWithAlias.categoria,
            preco_base = dtoRequestWithAlias.preco_base,
            preco_tarifado = BigDecimal.valueOf(103.20)
        )

        mockMvc.perform(
            put("/product/${savedProduct.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(dtoResponseWithAlias)))
    }

    @Test
    fun `should return 404 when updating non-existent product`() {
        val nonExistentProductId = 999L

        val dtoRequestWithAlias = DtoAlias(
            nome = "Product1",
            categoria = Category.VIDA,
            preco_base = BigDecimal.valueOf(100)
        )
        val requestJson = objectMapper.writeValueAsString(dtoRequestWithAlias)

        mockMvc.perform(
            put("/product/$nonExistentProductId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(status().isNotFound)
            .andExpect(content().json("""{"message":"Product not found"}"""))
    }

    @Test
    fun `should return 400 when CATEGORY value is invalid`() {
        val dtoRequestWithAlias = """
            {
              "nome": "",
              "categoria": "INCORRECT_CATEGORY",
              "preco_base": 100
            }
            """.trimIndent()

        mockMvc.perform(
            post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dtoRequestWithAlias)
        ).andExpect(status().isBadRequest)
            .andExpect(content().json("""{"message":"Error in Request"}"""))
    }

    @Test
    fun `should return 400 when NAME value is null`() {
        val dtoRequestWithAlias = """
            {
              "categoria": "AUTO",
              "preco_base": 100
            }
            """.trimIndent()

        mockMvc.perform(
            post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dtoRequestWithAlias)
        ).andExpect(status().isBadRequest)
            .andExpect(content().json("""{"message":"Error in Request"}"""))
    }

    @Test
    fun `should return 400 when priceBase value is null`() {
        val dtoRequestWithAlias = """
            {
              "nome": "product1",
              "categoria": "AUTO"
            }
            """.trimIndent()

        mockMvc.perform(
            post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dtoRequestWithAlias)
        ).andExpect(status().isBadRequest)
            .andExpect(content().json("""{"message":"Error in Request"}"""))
    }

}

data class DtoAlias (
    val id: Long?=null,
    val nome: String,
    val categoria: Category,
    val preco_base: BigDecimal,
    val preco_tarifado: BigDecimal?=null
)
