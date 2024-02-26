package me.dio.credit.application.system.controller

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import me.dio.credit.application.system.dto.CreditDTO
import me.dio.credit.application.system.dto.CreditViewDTO
import me.dio.credit.application.system.dto.CreditViewListDTO
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.service.implement.CreditService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import java.util.stream.Collectors

@RestController
@RequestMapping("/api/credits")
class CreditController(
    private val creditService: CreditService
) {
    @Operation(
        summary = "Saves a Credit",
        description = "Receives the data of a credit and saves it. The date should be a future date, and the number of installments can't be higher than 48."
    )
    @PostMapping
    fun saveCredit(@RequestBody @Valid creditDTO: CreditDTO): ResponseEntity<String> {
        val credit: Credit = this.creditService.save(creditDTO.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("Credit of ${credit.creditValue} for Customer ${credit.customer?.firstName} saved!")
    }

    @Operation(
        summary = "Recovers all credits from a customer",
        description = "Receives the a customer Id and searches for all credits of that customer, returning them in a list. Returns an empty list if the customer has no credits."
    )
    @GetMapping("/from/{customerId}")
    fun findAllByCustomerID(@PathVariable customerId: Long): ResponseEntity<List<CreditViewListDTO>> {
        val creditViewList = this.creditService.findAllByCustomer(customerId).stream()
            .map { credit: Credit -> CreditViewListDTO(credit) }.collect(Collectors.toList())
        return ResponseEntity.status(HttpStatus.OK).body(creditViewList)
    }

    @Operation(
        summary = "Recovers a credit by the credit code",
        description = "Receives a Credit Code and searches for a credit, returning a credit if found."
    )
    @GetMapping("/{creditCode}")
    fun findByCreditCode(
        @RequestParam(value = "customerId") customerId: Long,
        @PathVariable creditCode: UUID
    ): ResponseEntity<CreditViewDTO> {
        val credit: Credit = this.creditService.findByCreditCode(creditCode, customerId)
        return ResponseEntity.status(HttpStatus.OK).body(CreditViewDTO(credit))
    }
}