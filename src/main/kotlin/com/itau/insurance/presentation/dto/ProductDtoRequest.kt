package com.itau.insurance.presentation.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.itau.insurance.domain.enums.Category
import java.math.BigDecimal

data class ProductDtoRequest(
    @JsonProperty("nome")
    val name: String,

    @JsonProperty("categoria")
    val category: Category,

    @JsonProperty("preco_base")
    val priceBase: BigDecimal,

    @JsonProperty("preco_tarifado")
    val priceTariff: BigDecimal?=null
)