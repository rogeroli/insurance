package com.itau.insurance.domain.enums

enum class Category(val iof: Double, val pis: Double, val cofins: Double ){
    VIDA(1.0,2.2, 0.0),
    AUTO(5.5, 4.0, 1.0),
    VIAGEM(2.0, 4.0, 1.0),
    RESIDENCIAL(4.0,0.0, 3.0),
    PATRIMONIAL(5.0, 3.0, 0.0),
    DEFAULT(0.0, 0.0, 0.0)
}
