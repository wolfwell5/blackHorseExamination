package com.example.dealcontracts.repository

import com.example.dealcontracts.constants.PaymentStatus
import com.example.dealcontracts.repository.entity.Order
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest


@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderRepositoryTest {

    @Autowired
    lateinit var orderRepository: OrderRepository

    @Test
    fun `should return order according to accountId`() {
        val lastOrder = Order("ING", "1", id = "123", payStatus = PaymentStatus.PAYMENT_SUCCEED)
        orderRepository.save(lastOrder)

        val order = orderRepository.findUserLastOrder(lastOrder.accountId)

        assertEquals(lastOrder, order)
    }

}
