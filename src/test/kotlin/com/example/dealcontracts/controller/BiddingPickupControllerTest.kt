package com.example.dealcontracts.controller

import com.example.dealcontracts.clients.ClientResponse
import com.example.dealcontracts.constants.BiddingPickupStatus
import com.example.dealcontracts.constants.PaymentStatus
import com.example.dealcontracts.controller.dto.BalancePaymentDto
import com.example.dealcontracts.controller.dto.BiddingPickupDto
import com.example.dealcontracts.service.BiddingPickupService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


@WebMvcTest(BiddingPickupController::class)
class BiddingPickupControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var biddingPickupService: BiddingPickupService

    private val mapper = jacksonObjectMapper()
    private val biddingPickupDto = BiddingPickupDto(userId = "did", biddingId = "123")
    private val clientResponse = ClientResponse("30", BiddingPickupStatus.ACCEPT_REQUEST.text)

    @Test
    fun `should returns status 200 and code 30 message ACCEPT_REQUEST when request to bidding pickup`() {
        every { biddingPickupService.pickupBidding(biddingPickupDto) } returns clientResponse

        mockMvc.perform(
            MockMvcRequestBuilders.post("/deal-contracts/did/pick-up")
                .content(mapper.writeValueAsString(biddingPickupDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.code")
                    .value(BiddingPickupStatus.ACCEPT_REQUEST.biddingPickupResponseCode)
            )
    }
}
