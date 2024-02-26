package me.dio.credit.application.system.dto

import io.swagger.v3.oas.annotations.media.Schema
import me.dio.credit.application.system.entity.Credit
import java.math.BigDecimal
import java.util.UUID

@Schema(description = "An object used to return credit data in a list")
data class CreditViewListDTO(
    @field:Schema(description = "The ID of the credit", type = "UUID")
    val creditCode: UUID,
    @field:Schema(description = "The value of the credit.", type = "BigDecimal", example = "1000")
    val creditValue: BigDecimal,
    @field:Schema(description = "The number of installments the payment will be done in", type = "Int", maximum = "48")
    val numberOfInstallments: Int
) {
    constructor(credit: Credit) : this(
        creditCode = credit.creditCode,
        creditValue = credit.creditValue,
        numberOfInstallments = credit.numberOfInstallments
    )
}
