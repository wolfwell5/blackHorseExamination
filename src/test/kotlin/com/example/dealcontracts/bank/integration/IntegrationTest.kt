package com.example.dealcontracts.bank.integration

import com.example.dealcontracts.DealContractsApplication
import com.example.dealcontracts.bank.BankAccount
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
    fun whenGetCalled_thenShouldBadReqeust() {
        val result = restTemplate.getForEntity("/api/bankAccount?id=2", BankAccount::class.java);

        assertNotNull(result)
        assertEquals(HttpStatus.BAD_REQUEST, result?.statusCode)
    }

    @Test
    fun whePostCalled_thenShouldReturnBankObject() {
        val result = restTemplate.postForEntity(
            "/api/bankAccount",
            BankAccount("ING", "123ING456", "JOHN SMITH"),
            BankAccount::class.java
        )

        assertNotNull(result)
        assertEquals(HttpStatus.OK, result?.statusCode)
        assertEquals("ING", result.getBody()?.bankCode)
    }
}
