package br.com.ronistone.stonebank.query.controller

import br.com.ronistone.stonebank.query.QueryApplication
import br.com.ronistone.stonebank.query.model.AccountDocument
import br.com.ronistone.stonebank.query.model.CustomerDocument
import br.com.ronistone.stonebank.query.service.AccountService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.util.UUID


@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = [ QueryApplication::class ])
class AccountQueryControllerIT {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var accountService: AccountService

    @Test
    fun `getAccount success`() {
        val account = createAccount(getAccountDocument())

        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/account")
                        .param("document", account.customer?.document)
        )
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(account.id.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(0))
    }

    @Test
    fun `getAccount without document return 400`() {

        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/account")
        )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @Test
    fun `getExtract success`() {
        val account = createAccount(getAccountDocument())

        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/transaction/extract/account/${account.id.toString()}")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
    }

    private fun createAccount(account: AccountDocument) =
        accountService.createOrUpdate(account)

    private fun getAccountDocument() =
            AccountDocument(
                    id = UUID.randomUUID(),
                    customer = CustomerDocument(
                            document = UUID.randomUUID().leastSignificantBits.toString(),
                            name = "Test Name"
                    ),
                    amount = BigDecimal.ZERO
            )

}