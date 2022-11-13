package com.example.dealcontracts.service

import com.example.dealcontracts.clients.unionpay.UnionpayClient
import com.example.dealcontracts.constants.PaymentStatus
import com.example.dealcontracts.controller.dto.BalancePaymentDto
import com.example.dealcontracts.exception.AccountNotEnoughException
import com.example.dealcontracts.exception.BalancePaymentException
import com.example.dealcontracts.repository.OrderRepository
import com.example.dealcontracts.repository.entity.Order
import com.example.dealcontracts.clients.ClientResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpServerErrorException

class BalancePaymentServiceTest {

    private val orderRepository: OrderRepository = mockk()
    private val unionpayClient: UnionpayClient = mockk()
    private val balancePaymentService = BalancePaymentService(orderRepository, unionpayClient)

    private val lastOrder = Order("ING", "1", id = "123", payStatus = PaymentStatus.PAYMENT_SUCCEED)
    private val newOrder = Order("ING", "1", id = "123", payStatus = PaymentStatus.PAYING)
    private val balancePaymentDto = BalancePaymentDto(accountId = "did", paymentAmount = 500000)
    private val clientResponse = ClientResponse("10", PaymentStatus.PAYMENT_SUCCEED.text)

    @BeforeEach
    fun setUp() {
        every { orderRepository.findUserLastOrder("did") } returns lastOrder
        every { orderRepository.save(any()) } returns newOrder
    }

    @Test
    fun `should pay succeed with no exception when call balancePayment`() {
        //given
        every { unionpayClient.balancePayToUnionpay(any()) } returns clientResponse

        //when
        balancePaymentService.balancePayment(balancePaymentDto)

        //then
        verify(exactly = 1) { orderRepository.findUserLastOrder("did") }
        verify(exactly = 2) { orderRepository.save(any()) }
    }

    @Test
    fun `should pay failed with not enough money exception and dont retry when call balancePayment `() {
        //given
        every { unionpayClient.balancePayToUnionpay(any()) }.throws(AccountNotEnoughException(PaymentStatus.ACCOUNT_NOT_ENOUGH.text))

        //then
        assertThrows<AccountNotEnoughException> { balancePaymentService.balancePayment(balancePaymentDto) }
        verify(exactly = 1) { unionpayClient.balancePayToUnionpay(any()) }
    }

    @Test
    fun `should try 3 times and failed with BalancePaymentException when call balancePayment `() {
        //given
        every { unionpayClient.balancePayToUnionpay(any()) }.throws(HttpServerErrorException(HttpStatus.BAD_GATEWAY))

        //then
        assertThrows<BalancePaymentException> { balancePaymentService.balancePayment(balancePaymentDto) }
        verify(exactly = 4) { unionpayClient.balancePayToUnionpay(any()) }
    }
}
