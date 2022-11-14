package com.example.dealcontracts.service

import com.example.dealcontracts.clients.ClientResponse
import com.example.dealcontracts.clients.biddingmanage.BiddingManageClient
import com.example.dealcontracts.clients.rabbitmq.MessageSender
import com.example.dealcontracts.clients.rabbitmq.MessagingConfig.Companion.FANOUT_EXCHANGE_NAME
import com.example.dealcontracts.constants.BiddingPickupStatus
import com.example.dealcontracts.constants.PaymentStatus
import com.example.dealcontracts.controller.dto.BiddingPickupDto
import com.example.dealcontracts.exception.BalancePaymentException
import com.example.dealcontracts.repository.OrderRepository
import com.example.dealcontracts.repository.entity.Order
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.amqp.rabbit.core.RabbitTemplate

class BiddingPickupServiceTest {


    private val orderRepository: OrderRepository = mockk()
    private val biddingManageClient: BiddingManageClient = mockk()
    private val rabbitTemplate: RabbitTemplate = mockk()
    private val biddingPickupService = BiddingPickupService(orderRepository, biddingManageClient, rabbitTemplate)

    private val clientResponse = ClientResponse("30", BiddingPickupStatus.ACCEPT_REQUEST.text)
    private val biddingPickupDto = BiddingPickupDto(userId = "did", biddingId = "123")
    private val currentOrder = Order("ING", "1", id = "123", payStatus = PaymentStatus.PAYMENT_SUCCEED)

    @Test
    fun `should accept pickup request with no exception when call biddingPickup`() {

        //given
        every { orderRepository.findUserLastOrder("did") } returns currentOrder
        every { biddingManageClient.pickupBidding(any()) } returns clientResponse

        //when
        biddingPickupService.pickupBidding(biddingPickupDto)

        //then
        verify(exactly = 1) { orderRepository.findUserLastOrder("did") }
        verify(exactly = 1) { biddingManageClient.pickupBidding(any()) }
    }

    @Test
    fun `should get will accept pickup request with exception when call pickupBidding `() {
        //given
        every { orderRepository.findUserLastOrder("did") } returns currentOrder
        every { biddingManageClient.pickupBidding(any()) }.throws(BalancePaymentException(BiddingPickupStatus.WILL_ACCEPT_REQUEST.text))
        every { MessageSender(rabbitTemplate).broadcast(any()) } returns Unit
        //then
        biddingPickupService.pickupBidding(biddingPickupDto)

        verify(exactly = 1) { biddingManageClient.pickupBidding(any()) }
        verify(exactly = 1) {
            MessageSender(rabbitTemplate).broadcast(any())
        }
    }
}
