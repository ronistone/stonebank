package br.com.ronistone.stonebank.application

import br.com.ronistone.stonebank.domain.Account
import br.com.ronistone.stonebank.domain.AccountStatus
import br.com.ronistone.stonebank.domain.jsonToObject
import br.com.ronistone.stonebank.entity.AccountEntity
import br.com.ronistone.stonebank.entity.CustomerEntity
import br.com.ronistone.stonebank.entity.TransactionEntity
import br.com.ronistone.stonebank.entity.toDTO
import br.com.ronistone.stonebank.service.AccountService
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
@ContextConfiguration(classes = [ Application::class ])
class TransactionControllerIT {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var accountService: AccountService

    @Test
    fun `transfer success`() {
        val payer = createAccount(getAccountEntity())
        val receiver = createAccount(getAccountEntity())

        doDeposit(payer, getAmount())

        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/api/transaction/customer/${payer.id.toString()}/transfer")
                        .content(getTransfer(receiver.id.toString()))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payer.id").value(payer.id.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.receiver.id").value(receiver.id.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(10000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("TRANSFER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("CREATED"))
    }

    fun doDeposit(account: Account, transactionEntity: TransactionEntity) =
            accountService.deposit(account.id!!, transactionEntity)

    private fun createAccount(accountEntity: AccountEntity): Account {
        val account = accountService.createAccount(getAccountEntity())
        accountService.updateStatus(account.id!!, AccountStatus.CREATED)

        return account.toDTO()
    }


    private fun getAccountEntity() =
            AccountEntity(
                    customer = CustomerEntity(
                            document = UUID.randomUUID().leastSignificantBits.toString(),
                            name = "Test Name"
                    )
            )

    private fun getAmount() =
            TransactionEntity(
                    amount = BigDecimal(10000)
            )

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