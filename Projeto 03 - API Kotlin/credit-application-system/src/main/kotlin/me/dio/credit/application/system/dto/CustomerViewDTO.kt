package me.dio.credit.application.system.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import me.dio.credit.application.system.entity.Customer
import java.math.BigDecimal

@Schema(description = "An object used to return customer data")
data class CustomerViewDTO(
    @field:Schema(description = "The first name of the customer", type = "String", example = "João")
    val firstName: String,
    @field:Schema(description = "The last name of the customer", type = "String", example = "Silva")
    val lastName: String,
    @field:Schema(description = "The CPF of the customer (brazilian ID document)", type = "String", example = "12345678909")
    val cpf: String,
    @field:Schema(description = "The income of the customer", type = "BigDecimal", example = "2000")
    val income: BigDecimal,
    @field:Schema(description = "The email of the customer", type = "String", example = "joao@example.com")
    val email: String,
    @field:Schema(description = "The CEP of the customer (brazilian zip code)", type = "String", example = "12345-000")
    val zipCode: String,
    @field:Schema(description = "The street name of the customer's address", type = "String")
    val street: String,
    @field:Schema(description = "The unique identifier of the customer", type = "Long", example = "1")
    val id: Long?
) {
    constructor(customer: Customer) : this(
        firstName = customer.firstName,
        lastName = customer.lastName,
        cpf = customer.cpf,
        income = customer.income,
        email = customer.email,
        zipCode = customer.address.zipCode,
        street = customer.address.street,
        id = customer.id
    )

}
