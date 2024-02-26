package me.dio.credit.application.system.controller

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import me.dio.credit.application.system.dto.CustomerDTO
import me.dio.credit.application.system.dto.CustomerUpdateDTO
import me.dio.credit.application.system.dto.CustomerViewDTO
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.service.implement.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/customers")
class CustomerController(
    private val customerService: CustomerService
) {
    @Operation(
        summary = "Saves a customer",
        description = "Receives the data of a customer and saves it. The e-mail and CPF fields have to contain valid information, and no fields can be empty."
    )
    @PostMapping
    fun saveCustomer(@RequestBody @Valid customerDTO: CustomerDTO): ResponseEntity<CustomerViewDTO> {
        val savedCustomer = this.customerService.save(customerDTO.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(CustomerViewDTO(customer = savedCustomer))
    }

    @Operation(
        summary = "Finds a customer by Id.",
        description = "Receives a customer Id and searches for a customer, returning their data if the id exists."
    )
    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<CustomerViewDTO> {
        val customer: Customer = this.customerService.findById(id)
        return ResponseEntity.status(HttpStatus.OK).body(CustomerViewDTO(customer))
    }

    @Operation(
        summary = "Deletes a customer by Id",
        description = "Receives a customer Id and searches for a customer. If one is found, deletes it."
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomer(@PathVariable id: Long) {
        this.customerService.delete(id)
    }

    @Operation(
        summary = "Updates a customer",
        description = "Receives a customer Id and searches for a customer. If one is found, updates it with the customer data received. No fields can be empty."
    )
    @PatchMapping("/{id}")
    fun updateCustomer(
        @PathVariable id: Long,
        @RequestBody @Valid customerUpdateDTO: CustomerUpdateDTO
    ): ResponseEntity<CustomerViewDTO> {
        val customer: Customer = this.customerService.findById(id)
        val customerToUpdate = customerUpdateDTO.toEntity(customer)
        val customerUpdated = this.customerService.save(customerToUpdate)
        return ResponseEntity.status(HttpStatus.OK).body(CustomerViewDTO(customerUpdated))
    }
}