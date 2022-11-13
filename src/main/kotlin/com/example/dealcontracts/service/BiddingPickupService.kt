package com.example.dealcontracts.service

import com.example.dealcontracts.clients.ClientResponse
import com.example.dealcontracts.clients.biddingmanage.BiddingManageClient
import com.example.dealcontracts.constants.BiddingPickupStatus
import com.example.dealcontracts.constants.PaymentStatus.PAYMENT_SUCCEED
import com.example.dealcontracts.controller.dto.BiddingPickupDto
import com.example.dealcontracts.repository.OrderRepository
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BiddingPickupService(
    var orderRepository: OrderRepository,
    var biddingManageClient: BiddingManageClient,
    var rabbitTemplate: RabbitTemplate
) {

    fun pickupBidding(biddingPickupDto: BiddingPickupDto): ClientResponse {

        val currentOrder = orderRepository.findUserLastOrder(biddingPickupDto.userId)

        if (currentOrder != null && currentOrder.payStatus == PAYMENT_SUCCEED) {

            try {
                biddingManageClient.pickupBidding(biddingPickupDto)
            } catch (e: Exception) {
                postMessage(biddingPickupDto)
                return ClientResponse(
                    BiddingPickupStatus.WILL_ACCEPT_REQUEST.biddingPickupResponseCode,
                    BiddingPickupStatus.WILL_ACCEPT_REQUEST.text
                )
            }
        }

        return ClientResponse(
            BiddingPickupStatus.ACCEPT_REQUEST.biddingPickupResponseCode,
            BiddingPickupStatus.ACCEPT_REQUEST.text
        )
    }

    fun postMessage(biddingPickupDto: BiddingPickupDto) {
        val FANOUT_EXCHANGE_NAME = "amqp.fanout.exchange"

        rabbitTemplate.convertAndSend(FANOUT_EXCHANGE_NAME, "", biddingPickupDto)
    }
}
