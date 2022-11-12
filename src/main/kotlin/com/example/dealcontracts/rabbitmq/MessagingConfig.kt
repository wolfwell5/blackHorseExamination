package com.example.dealcontracts.rabbitmq

import org.springframework.amqp.core.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class MessagingConfig {
    @Bean
    fun topicBindings(): Declarables {
        val topicQueue = Queue(TOPIC_QUEUE_NAME, NON_DURABLE)
        val topicExchange = TopicExchange(
            TOPIC_EXCHANGE_NAME, NON_DURABLE,
            DO_NOT_AUTO_DELETE
        )
        return Declarables(
            topicQueue, topicExchange, BindingBuilder
                .bind(topicQueue)
                .to(topicExchange)
                .with(BINDING_PATTERN_ERROR)
        )
    }

    @Bean
    fun fanoutBindings(): Declarables {
        val fanoutQueue = Queue(FANOUT_QUEUE_NAME, NON_DURABLE)
        val fanoutExchange = FanoutExchange(
            FANOUT_EXCHANGE_NAME, NON_DURABLE,
            DO_NOT_AUTO_DELETE
        )
        return Declarables(
            fanoutQueue, fanoutExchange, BindingBuilder
                .bind(fanoutQueue)
                .to(fanoutExchange)
        )
    }

    companion object {
        const val FANOUT_QUEUE_NAME = "amqp.fanout.queue"
        const val FANOUT_EXCHANGE_NAME = "amqp.fanout.exchange"
        const val TOPIC_QUEUE_NAME = "amqp.topic.queue"
        const val TOPIC_EXCHANGE_NAME = "amqp.topic.exchange"
        const val BINDING_PATTERN_ERROR = "#.error"
        private const val NON_DURABLE = false
        private const val DO_NOT_AUTO_DELETE = false
    }
}
