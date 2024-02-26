package me.dio.credit.application.system.dto

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotNull
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDTO(
    @field:NotNull(message = "Value must not be null.")
    val creditValue: BigDecimal,
    @field:Future(message = "Date should not be in the past.")
    val dayFirstInstallment: LocalDate,
    @field:Max(value = 48, message = "Number of Installments can't be higher than 48.")
    val numberOfInstallments: Int,
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
