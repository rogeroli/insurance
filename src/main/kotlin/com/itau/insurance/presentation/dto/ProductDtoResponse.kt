package com.itau.insurance.presentation.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.itau.insurance.domain.enums.Category
import java.math.BigDecimal

class ProductDtoResponse (
    val id: Long,

    @JsonProperty("nome")
    val name: String,

    @JsonProperty("categoria")
    val category: Category,

    @JsonProperty("preco_base")
    val priceBase: BigDecimal?,

    @JsonProperty("preco_tarifado")
    val priceTariff: BigDecimal?
)