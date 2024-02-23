package me.dio.credit.application.system.controller

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
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import java.util.stream.Collectors

@RestController
@RequestMapping("/api/credits")
class CreditController(
    private val creditService: CreditService
) {
    @PostMapping
    fun saveCredit(@RequestBody creditDTO: CreditDTO): ResponseEntity<String> {
        val credit: Credit = this.creditService.save(creditDTO.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("Credit ${credit.creditCode} - Customer ${credit.customer?.firstName} saved!")
    }

    @GetMapping("/{customerId}")
    fun findAllByCustomerID(@PathVariable customerId: Long): ResponseEntity<List<CreditViewListDTO>> {
        val creditViewList = this.creditService.findAllByCustomer(customerId).stream()
            .map { credit: Credit -> CreditViewListDTO(credit) }.collect(Collectors.toList())
        return ResponseEntity.status(HttpStatus.OK).body(creditViewList)
    }

    @GetMapping("/{customerId}/{creditCode}")
    fun findByCreditCode(
        @PathVariable customerId: Long,
        @PathVariable creditCode: UUID
    ): ResponseEntity<CreditViewDTO> {
        val credit: Credit = this.creditService.findByCreditCode(creditCode, customerId)
        return ResponseEntity.status(HttpStatus.OK).body(CreditViewDTO(credit))
    }
}