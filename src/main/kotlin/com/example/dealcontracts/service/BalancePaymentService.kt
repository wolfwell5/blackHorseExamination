package com.example.dealcontracts.service

import com.example.dealcontracts.clients.unionpay.UnionpayClient
import com.example.dealcontracts.constants.PaymentStatus
import com.example.dealcontracts.constants.PaymentStatus.PAYMENT_SUCCEED
import com.example.dealcontracts.controller.dto.BalancePaymentDto
import com.example.dealcontracts.exception.BalancePaymentException
import com.example.dealcontracts.repository.OrderRepository
import com.example.dealcontracts.repository.entity.Order
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpServerErrorException
import java.util.*

@Service
class BalancePaymentService(
    var orderRepository: OrderRepository,
    var unionpayClient: UnionpayClient
) {

    val maxRetryTimes = 3

    fun balancePayment(balancePaymentDto: BalancePaymentDto) {

        val lastPayOrder = orderRepository.findUserLastOrder(balancePaymentDto.accountId)

        if (lastPayOrder == null || lastPayOrder.payStatus == PAYMENT_SUCCEED) {
            val newOrder = Order(
                "userId-".plus(balancePaymentDto.accountId),
                balancePaymentDto.accountId,
                payStatus = PaymentStatus.PAYING,
                id = UUID.randomUUID().toString()
            )
            orderRepository.save(newOrder)

            var count = 0
            while (true) {
                try {
                    unionpayClient.balancePayToUnionpay(balancePaymentDto)
                    break
                } catch (e: HttpServerErrorException) {
                    if (count++ == maxRetryTimes) throw BalancePaymentException(PaymentStatus.PAYMENT_EXCEPTION.text)
                }
            }

            newOrder.payStatus = PAYMENT_SUCCEED
            orderRepository.save(newOrder)
        }

    }
}
