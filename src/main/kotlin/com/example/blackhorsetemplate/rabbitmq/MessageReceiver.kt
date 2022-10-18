package com.example.blackhorsetemplate.rabbitmq

import com.example.blackhorsetemplate.rabbitmq.MessagingConfig.Companion.BINDING_PATTERN_ERROR
import com.example.blackhorsetemplate.rabbitmq.MessagingConfig.Companion.FANOUT_QUEUE_NAME
import com.example.blackhorsetemplate.rabbitmq.MessagingConfig.Companion.TOPIC_QUEUE_NAME
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class MessageReceiver {
    @RabbitListener(queues = [FANOUT_QUEUE_NAME])
    private fun receiveMessageFromFanout(message: String) {
        println("Received broadcast message: $message")
    }

    @RabbitListener(queues = [TOPIC_QUEUE_NAME])
    private fun receiveMessageFromTopic(message: String) {
        println("Received topic ($BINDING_PATTERN_ERROR) message: $message")
    }
}
