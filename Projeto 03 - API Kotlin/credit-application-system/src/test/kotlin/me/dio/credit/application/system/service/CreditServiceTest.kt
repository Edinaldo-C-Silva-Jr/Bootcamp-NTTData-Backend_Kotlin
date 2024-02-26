package me.dio.credit.application.system.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.enummerations.Status
import me.dio.credit.application.system.exception.BusinessException
import me.dio.credit.application.system.repository.CreditRepository
import me.dio.credit.application.system.service.implement.CreditService
import me.dio.credit.application.system.service.implement.CustomerService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CreditServiceTest {
    @MockK
    lateinit var creditRepository: CreditRepository

    @MockK
    lateinit var customerService: CustomerService

    @InjectMockKs
    lateinit var creditService: CreditService

    @Test
    fun `should create credit`() {
        //given
        val fakeCredit: Credit = buildCredit()
        val customerId: Long = 1
        every { customerService.findById(customerId) } returns fakeCredit.customer!!
        every { creditRepository.save(any()) } returns fakeCredit
        //when
        val actual: Credit = creditService.save(fakeCredit)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCredit)
        verify(exactly = 1) { customerService.findById(customerId) }
        verify(exactly = 1) { creditRepository.save(fakeCredit) }
    }

    @Test
    fun `should return list of credits owned by a customer`() {
        //given
        val customerId: Long = 1
        val listOfFakeCredits: List<Credit> = listOf(buildCredit(), buildCredit(), buildCredit())
        every { creditRepository.findAllByCustomer(customerId) } returns listOfFakeCredits
        //when
        val actual: List<Credit> = creditService.findAllByCustomer(customerId)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isNotEmpty
        Assertions.assertThat(actual).isSameAs(listOfFakeCredits)

        verify(exactly = 1) { creditRepository.findAllByCustomer(customerId) }
    }

    @Test
    fun `should return credit for a valid customer and credit code`() {
        //given
        val customerId: Long = 1
        val fakeCreditCode: UUID = UUID.randomUUID()
        val fakeCredit: Credit = buildCredit(customer = Customer(id = customerId))
        every { creditRepository.findByCreditCode(fakeCreditCode) } returns fakeCredit
        //when
        val actual: Credit = creditService.findByCreditCode(fakeCreditCode, customerId)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCredit)

        verify(exactly = 1) { creditRepository.findByCreditCode(fakeCreditCode) }
    }

    @Test
    fun `should throw BusinessException when credit code is invalid`() {
        //given
        val customerId: Long = 1
        val invalidCreditCode: UUID = UUID.randomUUID()
        every { creditRepository.findByCreditCode(invalidCreditCode) } returns null
        //then
        Assertions.assertThatThrownBy { creditService.findByCreditCode(invalidCreditCode, customerId) }
            .isInstanceOf(BusinessException::class.java)
            .hasMessage("CreditCode $invalidCreditCode not found!")
        verify(exactly = 1) { creditRepository.findByCreditCode(invalidCreditCode) }
    }

    @Test
    fun `should throw BusinessException when customer ID is different than credit owner`() {
        //given
        val customerId: Long = 1
        val fakeCreditCode: UUID = UUID.randomUUID()
        val fakeCredit: Credit = buildCredit(customer = Customer(id = 2))
        every { creditRepository.findByCreditCode(fakeCreditCode) } returns fakeCredit
        //then
        Assertions.assertThatThrownBy { creditService.findByCreditCode(fakeCreditCode, customerId) }
            .isInstanceOf(BusinessException::class.java)
            .hasMessage("Contact admin")
        verify { creditRepository.findByCreditCode(fakeCreditCode) }
    }

    companion object {
        fun buildCredit(
            creditValue: BigDecimal = BigDecimal(1000),
            dayFirstInstallment: LocalDate = LocalDate.of(2024, 12, 1),
            numberOfInstallments: Int = 12,
            status: Status = Status.IN_PROGRESS,
            customer: Customer = CustomerServiceTest.buildCustomer(),
        ) = Credit(
            creditValue = creditValue,
            dayFirstInstallment = dayFirstInstallment,
            numberOfInstallments = numberOfInstallments,
            status = status,
            customer = customer,
        )
    }
}