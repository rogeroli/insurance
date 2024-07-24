package com.itau.insurance.presentation.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.itau.insurance.domain.enums.Category
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class ProductDtoRequest(
    @JsonProperty("nome")
    @field:NotNull
    val name: String,

    @JsonProperty("categoria")
    @field:NotNull
    val category: Category,

    @JsonProperty("preco_base")
    @field:NotNull
    val priceBase: BigDecimal,

    @JsonProperty("preco_tarifado")
    val priceTariff: BigDecimal?=null
)