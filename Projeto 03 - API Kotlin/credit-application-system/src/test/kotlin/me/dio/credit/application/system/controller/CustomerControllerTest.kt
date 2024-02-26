package me.dio.credit.application.system.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.dio.credit.application.system.dto.CustomerDTO
import me.dio.credit.application.system.dto.CustomerUpdateDTO
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.repository.CustomerRepository
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

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CustomerControllerTest {
    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var mockMVC: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL: String = "/api/customers"
    }

    @BeforeEach
    fun setup() = customerRepository.deleteAll()

    @AfterEach
    fun tearDown() = customerRepository.deleteAll()

    @Test
    fun `should create a customer and return status 201`() {
        //given
        val customerDTO: CustomerDTO = buildCustomerDTO()
        val valueAsString: String = objectMapper.writeValueAsString(customerDTO)
        //then
        mockMVC.perform(
            MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON).content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("João"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Silva"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("12345678909"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("joao@teste.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.income").value("1000.0"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("12345-000"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Rua 1"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should return status 409 when saving a customer with duplicated CPF`() {
        //given
        customerRepository.save(buildCustomerDTO().toEntity())
        val customerDTO: CustomerDTO = buildCustomerDTO()
        val valueAsString: String = objectMapper.writeValueAsString(customerDTO)
        //then
        mockMVC.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Conflict: Consult the documentation."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(409))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.dao.DataIntegrityViolationException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should return status 400 when saving a customer with empty first name`() {
        //given
        val customerDTO: CustomerDTO = buildCustomerDTO(firstName = "")
        val valueAsString: String = objectMapper.writeValueAsString(customerDTO)
        //when
        //then
        mockMVC.perform(
            MockMvcRequestBuilders.post(URL)
                .content(valueAsString)
                .contentType(MediaType.APPLICATION_JSON)
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
    fun `should find customer by id and return status 200`() {
        //given
        val customer: Customer = customerRepository.save(buildCustomerDTO().toEntity())
        //then
        mockMVC.perform(
            MockMvcRequestBuilders.get("$URL/${customer.id}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("João"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Silva"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("12345678909"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("joao@teste.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.income").value("1000.0"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("12345-000"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Rua 1"))
            .andDo(MockMvcResultHandlers.print())

    }

    @Test
    fun `should return status 400 when customer id is invalid`() {
        //given
        val invalidID: Long = 10
        //then
        mockMVC.perform(
            MockMvcRequestBuilders.get("$URL/${invalidID}")
                .accept(MediaType.APPLICATION_JSON)
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
    fun `should delete customer by id` () {
        //given
        val customer: Customer = customerRepository.save(buildCustomerDTO().toEntity())
        //then
        mockMVC.perform(
            MockMvcRequestBuilders.delete("$URL/${customer.id}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isNoContent)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should return status 400 when trying to delete invalid customer`() {
        //given
        val invalidID: Long = 10
        //then
        mockMVC.perform(
            MockMvcRequestBuilders.delete("$URL/${invalidID}")
                .accept(MediaType.APPLICATION_JSON)
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
    fun `should update customer and return status 200` () {
        //given
        val customer: Customer = customerRepository.save(buildCustomerDTO().toEntity())
        val customerUpdateDTO: CustomerUpdateDTO = buildCustomerUpdateDTO()
        val valueAsString: String = objectMapper.writeValueAsString(customerUpdateDTO)
        //then
        mockMVC.perform(
            MockMvcRequestBuilders.patch("$URL/${customer.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("João"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Silva Souza"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("12345678909"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("joao@teste.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.income").value("2000.0"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("12345-678"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Rua 2"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should return status 400 when updating a customer with invalid id` () {
        //given
        val invalidId: Long = 10
        val customerUpdateDTO: CustomerUpdateDTO = buildCustomerUpdateDTO()
        val valueAsString: String = objectMapper.writeValueAsString(customerUpdateDTO)
        //then
        mockMVC.perform(
            MockMvcRequestBuilders.patch("$URL/${invalidId}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
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
        income: BigDecimal = BigDecimal.valueOf(1000.0)
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

    private fun buildCustomerUpdateDTO(
        firstName: String = "João",
        lastName: String = "Silva Souza",
        income: BigDecimal = BigDecimal.valueOf(2000.0),
        zipCode: String = "12345-678",
        street: String = "Rua 2"
    ): CustomerUpdateDTO = CustomerUpdateDTO(
        firstName = firstName,
        lastName = lastName,
        income = income,
        zipCode = zipCode,
        street = street
    )
}