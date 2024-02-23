package me.dio.credit.application.system.controller

import me.dio.credit.application.system.dto.CustomerDTO
import me.dio.credit.application.system.dto.CustomerUpdateDTO
import me.dio.credit.application.system.dto.CustomerViewDTO
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.service.implement.CustomerService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/customers")
class CustomerController(
    private val customerService: CustomerService
) {
    @PostMapping
    fun saveCustomer(@RequestBody customerDTO: CustomerDTO): String {
        val savedCustomer = this.customerService.save(customerDTO.toEntity())
        return "Customer {${savedCustomer.email}} saved!"
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): CustomerViewDTO {
        val customer : Customer = this.customerService.findById(id)
        return CustomerViewDTO(customer)
    }

    @DeleteMapping("/{id}")
    fun deleteCustomer(@PathVariable id: Long) {
        this.customerService.delete(id)
    }

    @PatchMapping("/{id}")
    fun updateCustomer(@PathVariable id: Long, @RequestBody customerUpdateDTO: CustomerUpdateDTO) : CustomerViewDTO {
        val customer: Customer = this.customerService.findById(id)
        val customerToUpdate = customerUpdateDTO.toEntity(customer)
        val customerUpdated = this.customerService.save(customerToUpdate)
        return CustomerViewDTO(customerUpdated)
    }
}