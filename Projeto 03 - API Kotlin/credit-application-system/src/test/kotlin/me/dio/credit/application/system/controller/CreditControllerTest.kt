package me.dio.credit.application.system.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.dio.credit.application.system.dto.CreditDTO
import me.dio.credit.application.system.dto.CustomerDTO
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.enummerations.Status
import me.dio.credit.application.system.repository.CreditRepository
import me.dio.credit.application.system.repository.CustomerRepository
import me.dio.credit.application.system.service.CustomerServiceTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CreditControllerTest {
    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var creditRepository: CreditRepository

    @Autowired
    private lateinit var mockMVC: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL: String = "/api/credits"
    }

    @BeforeEach
    fun setup() {
        customerRepository.deleteAll()
        creditRepository.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        customerRepository.deleteAll()
        creditRepository.deleteAll()
    }

    @Test
    fun `should create credit and return status 201` () {
        //given
        val customer: Customer = buildCustomerDTO().toEntity()
        customerRepository.save(customer)
        val creditDTO: CreditDTO = buildCreditDTO(customerId = customer.id!!)
        val valueAsString: String = objectMapper.writeValueAsString(creditDTO)
        //then
        mockMVC.perform(
            MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON).content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value("1000"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallments").value("12"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("IN_PROGRESS"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.emailCustomer").value("joao@teste.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.incomeCustomer").value("2000.0"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should return status 400 when creating a credit with more than 48 installments` () {
        //given
        val customer: Customer = buildCustomerDTO().toEntity()
        customerRepository.save(customer)
        val creditDTO: CreditDTO = buildCreditDTO(numberOfInstallments = 49, customerId = customer.id!!)
        val valueAsString: String = objectMapper.writeValueAsString(creditDTO)
        //then
        mockMVC.perform(
            MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON).content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request: Consult the documentation."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.web.bind.MethodArgumentNotValidException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should return status 400 when creating a credit with less than 1 installment` () {
        //given
        val customer: Customer = buildCustomerDTO().toEntity()
        customerRepository.save(customer)
        val creditDTO: CreditDTO = buildCreditDTO(numberOfInstallments = 0, customerId = customer.id!!)
        val valueAsString: String = objectMapper.writeValueAsString(creditDTO)
        //then
        mockMVC.perform(
            MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON).content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request: Consult the documentation."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.web.bind.MethodArgumentNotValidException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should return status 400 when creating a credit with date in the past` () {
        //given
        val customer: Customer = buildCustomerDTO().toEntity()
        customerRepository.save(customer)
        val creditDTO: CreditDTO = buildCreditDTO(dayFirstInstallment = LocalDate.now().minusDays(1), customerId = customer.id!!)
        val valueAsString: String = objectMapper.writeValueAsString(creditDTO)
        //then
        mockMVC.perform(
            MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON).content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request: Consult the documentation."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.web.bind.MethodArgumentNotValidException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find customer by id and return all their credits`() {
        //given
        val customer: Customer = buildCustomerDTO().toEntity()
        customerRepository.save(customer)
        val credits: List<Credit> = listOf(
            buildCreditDTO(creditValue = BigDecimal(200), customerId = customer.id!!).toEntity(),
            buildCreditDTO(creditValue = BigDecimal(300), customerId = customer.id!!).toEntity(),
            buildCreditDTO(creditValue = BigDecimal(500), customerId = customer.id!!).toEntity()
        )
        for (credit in credits) {
            creditRepository.save(credit)
        }
        //then
        mockMVC.perform(
            MockMvcRequestBuilders.get("$URL/from/${customer.id}").contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find credit by the credit code and its owner id`() {
        //given
        val customer: Customer = buildCustomerDTO().toEntity()
        customerRepository.save(customer)
        val credit: Credit = buildCreditDTO(customerId = customer.id!!).toEntity()
        creditRepository.save(credit)
        //then
        mockMVC.perform(
            MockMvcRequestBuilders.get("$URL/${credit.creditCode}?customerId=${customer.id}").contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value("1000.0"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallments").value("12"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("IN_PROGRESS"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.emailCustomer").value("joao@teste.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.incomeCustomer").value("2000.0"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should return status 400 when finding credit with an invalid credit code`() {
        //given
        val customer: Customer = buildCustomerDTO().toEntity()
        customerRepository.save(customer)
        val credit: Credit = buildCreditDTO(customerId = customer.id!!).toEntity()
        creditRepository.save(credit)
        //then
        mockMVC.perform(
            MockMvcRequestBuilders.get("$URL/${UUID.randomUUID()}?customerId=${customer.id}").contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request: Consult the documentation."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class me.dio.credit.application.system.exception.BusinessException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should return status 400 when searching credit with an id different than its owner`() {
        //given
        val customer: Customer = buildCustomerDTO().toEntity()
        customerRepository.save(customer)
        val credit: Credit = buildCreditDTO(customerId = customer.id!!).toEntity()
        creditRepository.save(credit)
        //then
        mockMVC.perform(
            MockMvcRequestBuilders.get("$URL/${credit.creditCode}?customerId=10").contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request: Consult the documentation."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class me.dio.credit.application.system.exception.BusinessException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    private fun buildCustomerDTO(
        firstName: String = "João",
        lastName: String = "Silva",
        cpf: String = "12345678909",
        email: String = "joao@teste.com",
        password: String = "123",
        zipCode: String = "12345-000",
        street: String = "Rua 1",
        income: BigDecimal = BigDecimal.valueOf(2000.0)
    ) = CustomerDTO(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        zipCode = zipCode,
        street = street,
        income = income
    )

    fun buildCreditDTO(
        creditValue: BigDecimal = BigDecimal(1000),
        dayFirstInstallment: LocalDate = LocalDate.of(2024, 12, 1),
        numberOfInstallments: Int = 12,
        status: Status = Status.IN_PROGRESS,
        customerId: Long
    ) = CreditDTO(
        creditValue = creditValue,
        dayFirstInstallment = dayFirstInstallment,
        numberOfInstallments = numberOfInstallments,
        customerId = customerId
    )
}