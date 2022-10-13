package com.example.blackhorsetemplate.rabbitmq

import com.example.blackhorsetemplate.rabbitmqintegration.MessagingConfig
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.springframework.amqp.rabbit.core.RabbitTemplate

class MessageSenderTest {
    private var subject: MessageSender? = null
    private var rabbitTemplateMock: RabbitTemplate? = null

    @BeforeEach
    fun setUp() {
        rabbitTemplateMock = Mockito.mock(RabbitTemplate::class.java)
        subject = MessageSender(rabbitTemplateMock!!)
    }

    @Test
    fun testBroadcast() {
        Assertions.assertThatCode { subject!!.broadcast("Test") }.doesNotThrowAnyException()
        Mockito.verify(rabbitTemplateMock)
            ?.convertAndSend(
                ArgumentMatchers.eq(MessagingConfig.FANOUT_EXCHANGE_NAME),
                ArgumentMatchers.eq(""),
                ArgumentMatchers.eq("Test")
            )
    }

    @Test
    fun testSendError() {
        Assertions.assertThatCode { subject!!.sendError("Test Error") }.doesNotThrowAnyException()
        Mockito.verify(rabbitTemplateMock)
            ?.convertAndSend(
                ArgumentMatchers.eq(MessagingConfig.TOPIC_EXCHANGE_NAME), ArgumentMatchers.endsWith("error"),
                ArgumentMatchers.eq("Test Error")
            )
    }
}
