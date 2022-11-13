package com.example.dealcontracts.bank.integration

import com.example.dealcontracts.DealContractsApplication
import com.example.dealcontracts.controller.dto.BalancePaymentDto
import com.example.dealcontracts.clients.ClientResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

@SpringBootTest(
    classes = [DealContractsApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class KotlinTestingDemoApplicationIntegrationTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun `should get code 10 in response when balance pay`() {
        val result = restTemplate.postForEntity(
            "/deal-contracts/did/balance-payment",
            BalancePaymentDto(accountId = "did", paymentAmount = 500000),
            ClientResponse::class.java
        )

        assertNotNull(result)
        assertEquals(HttpStatus.OK, result?.statusCode)
        assertEquals("10", result.body?.code)
        assertEquals("支付成功", result.body?.message)
    }
}
