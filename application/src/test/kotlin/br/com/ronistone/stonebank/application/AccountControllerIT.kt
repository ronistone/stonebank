package br.com.ronistone.stonebank.application

import br.com.ronistone.stonebank.domain.AccountDTO
import br.com.ronistone.stonebank.service.commons.jsonToObject
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
    fun `getAccount success`() {
        val account = createAccount(getCreateAccountBody())

        this.mockMvc.perform(
            get("/api/account")
                .param("document", account.document)
        )
            .andExpect(status().is2xxSuccessful)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(account.id.toString()))
            .andExpect(jsonPath("$.amount").value(0))
    }

    @Test
    fun `getAccount without document return 400`() {

        this.mockMvc.perform(
            get("/api/account")
        )
            .andExpect(status().is4xxClientError)
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

    @Test
    fun `getExtract success`() {
        val account = createAccount(getCreateAccountBody())

        this.mockMvc.perform(
            get("/api/account/${account.id.toString()}/extract")
        ).andExpect(status().is2xxSuccessful)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    fun `transfer success`() {
        val payer = createAccount(getCreateAccountBody())
        val receiver = createAccount(getCreateAccountBody())

        doDeposit(payer, getAmount())

        this.mockMvc.perform(
            put("/api/account/${payer.id.toString()}/transfer")
                .content(getTransfer(receiver.id.toString()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(payer.id.toString()))
            .andExpect(jsonPath("$.amount").value(0))
            .andExpect(jsonPath("$.document").value(payer.document!!))
    }

    fun doDeposit(account: AccountDTO, payload: String) = this.mockMvc.perform(
        put("/api/account/${account.id.toString()}/deposit")
            .content(payload)
            .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(status().is2xxSuccessful)

    private fun createAccount(payload: String): AccountDTO {
        return this.mockMvc.perform(
            post("/api/account")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful)
            .andReturn().response.contentAsString.jsonToObject(AccountDTO::class.java)
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
