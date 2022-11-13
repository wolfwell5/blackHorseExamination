package com.example.dealcontracts.controller

import com.example.dealcontracts.constants.PaymentStatus
import com.example.dealcontracts.controller.dto.BalancePaymentDto
import com.example.dealcontracts.clients.ClientResponse
import com.example.dealcontracts.service.BalancePaymentService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/deal-contracts")
class BalancePaymentController(var balancePaymentService: BalancePaymentService) {
    @PostMapping("/{did}/balance-payment", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun balancePayment(
        @PathVariable did: String, @RequestBody balancePaymentDto: BalancePaymentDto
    ): ResponseEntity<ClientResponse> {

        balancePaymentService.balancePayment(
            BalancePaymentDto(accountId = did, paymentAmount = balancePaymentDto.paymentAmount)
        )

        return ResponseEntity.ok(
            ClientResponse(
                PaymentStatus.PAYMENT_SUCCEED.paymentResponseCode, PaymentStatus.PAYMENT_SUCCEED.text
            )
        )
    }
}
