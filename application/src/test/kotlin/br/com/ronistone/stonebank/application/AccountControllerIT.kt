package br.com.ronistone.stonebank.application

import br.com.ronistone.stonebank.domain.Account
import br.com.ronistone.stonebank.domain.AccountStatus
import br.com.ronistone.stonebank.domain.jsonToObject
import br.com.ronistone.stonebank.service.AccountService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID


@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = [ Application::class ])
class AccountControllerIT {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var accountService: AccountService


    @Test
    fun `createNewAccount success`() {
        createAccount(getCreateAccountBody())
    }

    @Test
    fun `createNewAccount with document already used return 400`() {
        val payload = getCreateAccountBody()
        createAccount(payload)

        this.mockMvc.perform(
            post("/api/account")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError)
    }


    @Test
    fun `deposit success`() {
        val account = createAccount(getCreateAccountBody())


        doDeposit(account, getAmount())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(account.id.toString()))
            .andExpect(jsonPath("$.amount").value(10000))
            .andExpect(jsonPath("$.document").value(account.document!!))
    }


    fun doDeposit(account: Account, payload: String) = this.mockMvc.perform(
        put("/api/account/${account.id.toString()}/deposit")
            .content(payload)
            .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(status().is2xxSuccessful)

    private fun createAccount(payload: String): Account {
        val account = this.mockMvc.perform(
            post("/api/account")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful)
            .andReturn().response.contentAsString.jsonToObject(Account::class.java)

        accountService.updateStatus(account.id!!, AccountStatus.CREATED)

        return account
    }


    private fun getCreateAccountBody() =
        """
            {
                "name": "Teste Name",
                "document": "${UUID.randomUUID().leastSignificantBits}"
            }
        """.trimIndent()

    private fun getAmount() =
        """
            {
                "amount": 10000
            }
        """.trimIndent()

    private fun getTransfer(receiver: String) =
        """
            {
            	"amount": 10000,
            	"receiver": {
            		"id": "$receiver"
            	}
            }
        """.trimIndent()
}
