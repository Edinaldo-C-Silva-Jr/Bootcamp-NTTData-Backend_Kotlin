package me.dio.credit.application.system.controller

import me.dio.credit.application.system.dto.CreditDTO
import me.dio.credit.application.system.dto.CreditViewDTO
import me.dio.credit.application.system.dto.CreditViewListDTO
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.service.implement.CreditService
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
    fun saveCredit(@RequestBody creditDTO: CreditDTO): String {
        val credit: Credit = this.creditService.save(creditDTO.toEntity())
        return "Credit ${credit.creditCode} - Customer ${credit.customer?.firstName} saved!"
    }

    @GetMapping("/{customerId}")
    fun findAllByCustomerID(@PathVariable customerId: Long): List<CreditViewListDTO> {
        return this.creditService.findAllByCustomer(customerId).stream().
        map{ credit: Credit -> CreditViewListDTO(credit)}.collect(Collectors.toList())
    }

    @GetMapping("/{customerId}/{creditCode}")
    fun findByCreditCode(@PathVariable customerId: Long, @PathVariable creditCode: UUID): CreditViewDTO {
        val credit: Credit = this.creditService.findByCreditCode(creditCode, customerId)
        return CreditViewDTO(credit)
    }
}