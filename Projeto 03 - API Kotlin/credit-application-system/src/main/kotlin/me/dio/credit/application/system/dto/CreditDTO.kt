package me.dio.credit.application.system.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotNull
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import java.math.BigDecimal
import java.time.LocalDate

@Schema(description = "An object used to insert data into a credit")
data class CreditDTO(
    @field:Schema(description = "The value of the credit.", type = "BigDecimal", example = "1000")
    @field:NotNull(message = "Value must not be null.")
    val creditValue: BigDecimal,
    @field:Schema(description = "The day for the payment of the first installment", type = "LocalDate")
    @field:Future(message = "Date should not be in the past.")
    val dayFirstInstallment: LocalDate,
    @field:Schema(description = "The number of installments the payment will be done in", type = "Int", maximum = "48")
    @field:Max(value = 48, message = "Number of Installments can't be higher than 48.")
    val numberOfInstallments: Int,
    @field:Schema(description = "The ID of the customer who owns the credit", type = "Long", example = "1")
    @field:NotNull(message = "Customer ID must not be null.")
    val customerId: Long
) {
    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        dayFirstInstallment = this.dayFirstInstallment,
        numberOfInstallments = this.numberOfInstallments,
        customer = Customer(id = this.customerId)
    )
}
