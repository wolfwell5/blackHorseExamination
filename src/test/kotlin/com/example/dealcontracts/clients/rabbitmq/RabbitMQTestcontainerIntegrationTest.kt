package com.example.dealcontracts.clients.rabbitmq

import com.example.dealcontracts.controller.dto.BiddingPickupDto
import com.example.dealcontracts.service.BalancePaymentService
import com.example.dealcontracts.service.BiddingPickupService
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class RabbitMQIntegrationTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var balancePaymentService: BalancePaymentService

    @Autowired
    lateinit var biddingPickupServicee: BiddingPickupService

    companion object {
        @Container
        private val rabbitMQContainer =
            KRabbitMQContainer(image = "rabbitmq:3.7-management").withExposedPorts(5672).withVhost("/")
                .withUser("admin", "admin").withPermission("/", "admin", ".*", ".*", ".*")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
//            rabbitMQ
            registry.add("spring.rabbitmq.host") { rabbitMQContainer.host }
            registry.add("spring.rabbitmq.port") { rabbitMQContainer.amqpPort }
        }
    }


    @Test
    fun containersStarted() {
        assertTrue(rabbitMQContainer.isRunning)
    }

    @Test
    fun `should rabbitMQ send out message`() {
        biddingPickupServicee.postMessage(BiddingPickupDto("nothing", "123"))
    }
}

class KRabbitMQContainer(val image: String) : RabbitMQContainer(image)
