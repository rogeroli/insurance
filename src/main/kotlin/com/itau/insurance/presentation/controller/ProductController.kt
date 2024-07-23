package com.itau.insurance.presentation.controller

import com.itau.insurance.application.ProductService
import com.itau.insurance.presentation.dto.ProductDtoRequest
import com.itau.insurance.presentation.dto.ProductDtoResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/product")
class ProductController(
    @Autowired val service: ProductService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody dtoRequest: ProductDtoRequest): ProductDtoResponse {
        return service.create(dtoRequest)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody dtoRequest: ProductDtoRequest): ProductDtoResponse {
        return service.update(id, dtoRequest)
    }

}