package me.dio.credit.application.system.dto

import io.swagger.v3.oas.annotations.media.Schema
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.enummerations.Status
import java.math.BigDecimal
import java.util.UUID

@Schema(description = "An object used to recover credit data of a single credit")
data class CreditViewDTO(
    @field:Schema(description = "The ID of the credit", type = "UUID")
    val creditCode: UUID,
    @field:Schema(description = "The value of the credit.", type = "BigDecimal", example = "1000")
    val creditValue: BigDecimal,
    @field:Schema(description = "The number of installments the payment will be done in", type = "Int", maximum = "48")
    val numberOfInstallments: Int,
    @field:Schema(description = "The current stats of the credit", type = "Status")
    val status: Status,
    @field:Schema(description = "The email of the customer who owns the credit", type = "String?", example = "joao@example.com")
    val emailCustomer: String?,
    @field:Schema(description = "The income of the customer who owns the credit", type = "BigDecimal", example = "2000")
    val incomeCustomer: BigDecimal?
) {
    constructor(credit: Credit) : this(
        creditCode = credit.creditCode,
        creditValue = credit.creditValue,
        numberOfInstallments = credit.numberOfInstallments,
        status = credit.status,
        emailCustomer = credit.customer?.email,
        incomeCustomer = credit.customer?.income
    )
}
