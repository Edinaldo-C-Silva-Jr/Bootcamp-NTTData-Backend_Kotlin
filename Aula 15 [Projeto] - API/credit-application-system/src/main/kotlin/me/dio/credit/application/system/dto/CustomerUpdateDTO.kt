package me.dio.credit.application.system.dto

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import me.dio.credit.application.system.entity.Customer
import java.math.BigDecimal

data class CustomerUpdateDTO(
    @field:NotEmpty(message = "Name should not be empty.")
    val firstName: String,
    @field:NotEmpty(message = "Name should not be empty.")
    val lastName: String,
    @field:NotNull(message = "Income should not be null.")
    val income: BigDecimal,
    @field:NotEmpty(message = "CEP should not be empty")
    val zipCode: String,
    @field:NotEmpty(message = "Street should not be empty")
    val street: String
) {
    fun toEntity(customer: Customer): Customer {
        customer.firstName = this.firstName
        customer.lastName = this.lastName
        customer.income = this.income
        customer.address.zipCode = this.zipCode
        customer.address.street = this.street
        return customer
    }
}
