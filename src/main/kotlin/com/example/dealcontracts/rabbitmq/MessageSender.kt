package com.example.dealcontracts.rabbitmq

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class MessageSender(private val rabbitTemplate: RabbitTemplate) {
    fun broadcast(message: String?) {
        rabbitTemplate.convertAndSend(MessagingConfig.FANOUT_EXCHANGE_NAME, "", message!!)
    }

    fun sendError(message: String?) {
        rabbitTemplate.convertAndSend(MessagingConfig.TOPIC_EXCHANGE_NAME, "this.is.an.error", message!!)
    }
}
