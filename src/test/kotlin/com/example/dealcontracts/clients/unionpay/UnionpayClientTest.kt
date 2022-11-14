package com.example.dealcontracts.clients.unionpay

import com.example.dealcontracts.DealContractsApplication
import com.example.dealcontracts.clients.ClientResponse
import com.example.dealcontracts.controller.dto.BalancePaymentDto
import com.example.dealcontracts.utils.readTextFile
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockClassRule
import io.mockk.MockKAnnotations
import org.assertj.core.api.Assertions.assertThat
import org.junit.ClassRule
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.cloud.contract.wiremock.WireMockSpring
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource

@ContextConfiguration(classes = [DealContractsApplication::class])
@TestPropertySource(properties = ["decorator.datasource.enabled=false"])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UnionpayClientTest {

    private val unionpayClient: UnionpayClient = UnionpayClient()

    private val successBalancePaymentDto = BalancePaymentDto(accountId = "did", paymentAmount = 200000)
    private val notEnoughbalancePaymentDto = BalancePaymentDto(accountId = "did", paymentAmount = 500000)
    private val serverErrorbalancePaymentDto = BalancePaymentDto(accountId = "error", paymentAmount = 100)

    companion object {
        @get:ClassRule
        val wiremock: WireMockClassRule = WireMockClassRule(WireMockSpring.options().dynamicPort())
    }

    @BeforeAll
    private fun setUP() {
        MockKAnnotations.init(this)
        wiremock.start()
        unionpayClient.unionpayUrl = wiremock.baseUrl().plus("/unionpay/pay")
        unionpayClient.restTemplate = TestRestTemplate().restTemplate
    }

    @Test
    fun `should renturn pay success message`() {

        wiremock.stubFor(
            post(urlMatching("/unionpay/pay"))
                .withRequestBody(matchingJsonPath("$.[?(@.paymentAmount == '200000')]"))
                .willReturn(
                    aResponse()
                        .withHeader("Content-type", "application/json")
                        .withBody(readTextFile("/fixtures/unionpay/unionpay_success_response.json"))
                )
        )


        val clientResponse = unionpayClient.balancePayToUnionpay(successBalancePaymentDto)

        assertThat(clientResponse).isNotNull

        assertThat(clientResponse.code).isEqualTo("10")
        assertThat(clientResponse.message).isEqualTo("支付成功")
    }

    @Test
    fun `should renturn account not enough message`() {

        wiremock.stubFor(
            post(urlMatching("/unionpay/pay"))
                .withRequestBody(matchingJsonPath("$.[?(@.paymentAmount == '500000')]"))
                .willReturn(
                    aResponse()
                        .withHeader("Content-type", "application/json")
                        .withBody(readTextFile("/fixtures/unionpay/unionpay_account_not_enough_response.json"))
                )
        )


        val clientResponse = unionpayClient.balancePayToUnionpay(notEnoughbalancePaymentDto)

        assertThat(clientResponse).isNotNull

        assertThat(clientResponse.code).isEqualTo("20")
        assertThat(clientResponse.message).isEqualTo("账户余额不足")
    }

    @Test
    fun `should renturn 500 error message`() {

        wiremock.stubFor(
            post(urlMatching("/unionpay/pay"))
                .withRequestBody(matchingJsonPath("$.[?(@.accountId == 'error')]"))
                .willReturn(
                    aResponse()
                        .withStatus(HttpStatus.BAD_GATEWAY.value())
                        .withBody(readTextFile("/fixtures/unionpay/unionpay_account_not_enough_response.json"))
                )
        )


        val clientResponse = unionpayClient.balancePayToUnionpay(serverErrorbalancePaymentDto)

        assertThat(clientResponse).isNotNull

        assertThat(clientResponse.code).isEqualTo("-1")
        assertThat(clientResponse.message).isEqualTo("error happen")

    }

}
