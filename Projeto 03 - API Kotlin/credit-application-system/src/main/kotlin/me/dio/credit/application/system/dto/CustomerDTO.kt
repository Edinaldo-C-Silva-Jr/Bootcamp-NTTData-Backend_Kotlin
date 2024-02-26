package me.dio.credit.application.system.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import me.dio.credit.application.system.entity.Address
import me.dio.credit.application.system.entity.Customer
import org.hibernate.validator.constraints.br.CPF
import java.math.BigDecimal

@Schema(description = "An object used to insert data into a customer")
data class CustomerDTO(
    @field:Schema(description = "The first name of the customer", type = "String", example = "João")
    @field:NotEmpty(message = "Name should not be empty.")
    val firstName: String,
    @field:Schema(description = "The last name of the customer", type = "String", example = "Silva")
    @field:NotEmpty(message = "Name should not be empty.")
    val lastName: String,
    @field:Schema(description = "The CPF of the customer (brazilian ID document)", type = "String", example = "12345678909")
    @field:NotEmpty(message = "CPF should not be empty.") @field:CPF(message = "Invalid CPF.")
    val cpf: String,
    @field:Schema(description = "The income of the customer", type = "BigDecimal", example = "2000")
    @field:NotNull(message = "Income should not be null.")
    val income: BigDecimal,
    @field:Schema(description = "The email of the customer", type = "String", example = "joao@example.com")
    @field:NotEmpty(message = "E-mail should not be empty") @field:Email(message = "Invalid e-mail")
    val email: String,
    @field:Schema(description = "The access password of the customer", type = "String")
    @field:NotEmpty(message = "password should not be empty")
    val password: String,
    @field:Schema(description = "The CEP of the customer (brazilian zip code)", type = "String", example = "12345-000")
    @field:NotEmpty(message = "CEP should not be empty")
    val zipCode: String,
    @field:Schema(description = "The street name of the customer's address", type = "String")
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