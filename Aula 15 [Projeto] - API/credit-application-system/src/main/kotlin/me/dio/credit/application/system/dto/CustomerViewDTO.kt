package me.dio.credit.application.system.dto

import jakarta.validation.constraints.Email
import me.dio.credit.application.system.entity.Customer
import java.math.BigDecimal

data class CustomerViewDTO(
    val firstName: String,
    val lastName: String,
    val cpf: String,
    val income: BigDecimal,
    val email: String,
    val zipcode: String,
    val street: String
) {
    constructor(customer: Customer): this (
        firstName = customer.firstName,
        lastName = customer.lastName,
        cpf = customer.cpf,
        income = customer.income,
        email = customer.email,
        zipcode = customer.address.zipCode,
        street = customer.address.street
    )

}
