package me.dio.credit.application.system.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import me.dio.credit.application.system.entity.Address
import me.dio.credit.application.system.entity.Customer
import org.hibernate.validator.constraints.br.CPF
import java.math.BigDecimal

data class CustomerDTO(
    @field:NotEmpty(message = "Name should not be empty.")
    val firstName: String,
    @field:NotEmpty(message = "Name should not be empty.")
    val lastName: String,
    @field:NotEmpty(message = "CPF should not be empty.") @field:CPF(message = "Invalid CPF.")
    val cpf: String,
    @field:NotNull(message = "Income should not be null.")
    val income: BigDecimal,
    @field:NotEmpty(message = "E-mail should not be empty") @field:Email(message = "Invalid e-mail")
    val email: String,
    @field:NotEmpty(message = "password should not be empty")
    val password: String,
    @field:NotEmpty(message = "CEP should not be empty")
    val zipCode: String,
    @field:NotEmpty(message = "Street should not be empty")
    val street: String
) {
    fun toEntity(): Customer {
        return Customer(
            firstName = this.firstName,
            lastName = this.lastName,
            cpf = this.cpf,
            income = this.income,
            email = this.email,
            password = this.password,
            address = Address(zipCode = this.zipCode, street = this.street)
        )
    }
}