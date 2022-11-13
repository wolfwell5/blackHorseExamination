package com.example.dealcontracts.controller

import com.example.dealcontracts.constants.PaymentStatus
import com.example.dealcontracts.controller.dto.BalancePaymentDto
import com.example.dealcontracts.clients.ClientResponse
import com.example.dealcontracts.service.BalancePaymentService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*


@WebMvcTest(BalancePaymentController::class)
class BalancePaymentControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var balancePaymentService: BalancePaymentService

    private val mapper = jacksonObjectMapper()
    private val balancePaymentDto = BalancePaymentDto(accountId = "did", paymentAmount = 500000)


    @Test
    fun `should returns status 200 and code 10 message PAYMENT_SUCCEED when request to balance pay`() {
        every { balancePaymentService.balancePayment(balancePaymentDto) } returns Unit

        mockMvc.perform(
            post("/deal-contracts/did/balance-payment").content(mapper.writeValueAsString(balancePaymentDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(PaymentStatus.PAYMENT_SUCCEED.paymentResponseCode));
    }
}
