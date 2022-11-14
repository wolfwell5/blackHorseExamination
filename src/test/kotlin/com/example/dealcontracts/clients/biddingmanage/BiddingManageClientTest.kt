package com.example.dealcontracts.clients.biddingmanage

import com.example.dealcontracts.DealContractsApplication
import com.example.dealcontracts.controller.dto.BiddingPickupDto
import com.example.dealcontracts.utils.readTextFile
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
class BiddingManageClientTest {

    private val biddingManageClient: BiddingManageClient = BiddingManageClient()

    private val successBiddingPickupDto = BiddingPickupDto(userId = "did", biddingId = "123")
    private val serverErrorBiddingPickupDto = BiddingPickupDto(userId = "error", biddingId = "789")

    companion object {
        @get:ClassRule
        val wiremock: WireMockClassRule = WireMockClassRule(WireMockSpring.options().dynamicPort())
    }

    @BeforeAll
    private fun setUP() {
        MockKAnnotations.init(this)
        wiremock.start()
        biddingManageClient.biddingManageUrl = wiremock.baseUrl().plus("/biddinginfo/pick-up")
        biddingManageClient.restTemplate = TestRestTemplate().restTemplate
    }

    @Test
    fun `should return request access message`() {

        wiremock.stubFor(
            post(urlMatching("/biddinginfo/pick-up"))
                .withRequestBody(matchingJsonPath("$.[?(@.biddingId == '123')]"))
                .willReturn(
                    aResponse()
                        .withHeader("Content-type", "application/json")
                        .withBody(readTextFile("/fixtures/biddingmanage/bidding_success_response.json"))
                )
        )


        val clientResponse = biddingManageClient.pickupBidding(successBiddingPickupDto)

        assertThat(clientResponse).isNotNull

        assertThat(clientResponse.code).isEqualTo("30")
        assertThat(clientResponse.message).isEqualTo("您的提货申请已成功受理")
    }


    @Test
    fun `should return will later access message`() {

        wiremock.stubFor(
            post(urlMatching("/biddinginfo/pick-up"))
                .withRequestBody(matchingJsonPath("$.[?(@.biddingId == '789')]"))
                .willReturn(
                    aResponse()
                        .withStatus(HttpStatus.BAD_GATEWAY.value())
                        .withBody(readTextFile("/fixtures/biddingmanage/bidding_success_response.json"))
                )
        )


        val clientResponse = biddingManageClient.pickupBidding(serverErrorBiddingPickupDto)

        assertThat(clientResponse).isNotNull

        assertThat(clientResponse.code).isEqualTo("-1")
        assertThat(clientResponse.message).isEqualTo("error happen")

    }

}
