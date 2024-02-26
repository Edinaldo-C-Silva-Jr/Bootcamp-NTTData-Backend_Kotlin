package me.dio.credit.application.system.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import me.dio.credit.application.system.entity.Customer
import java.math.BigDecimal

@Schema(description = "An object used to insert data into a customer for an update")
data class CustomerUpdateDTO(
    @field:Schema(description = "The first name of the customer", type = "String", example = "João")
    @field:NotEmpty(message = "Name should not be empty.")
    val firstName: String,
    @field:Schema(description = "The last name of the customer", type = "String", example = "Silva")
    @field:NotEmpty(message = "Name should not be empty.")
    val lastName: String,
    @field:Schema(description = "The income of the customer", type = "BigDecimal", example = "2000")
    @field:NotNull(message = "Income should not be null.")
    val income: BigDecimal,
    @field:Schema(description = "The CEP of the customer (brazilian zip code)", type = "String", example = "12345-000")
    @field:NotEmpty(message = "CEP should not be empty")
    val zipCode: String,
    @field:Schema(description = "The street name of the customer's address", type = "String")
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
