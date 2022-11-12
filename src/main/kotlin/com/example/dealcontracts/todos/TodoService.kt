package com.example.dealcontracts.todos

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TodoService(val todoRepository: TodoRepository) : TodoServiceInterface {

    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate


    override fun save(todo: Todo): Todo {
        return todoRepository.save(todo)
    }

    override fun findAllAndGetFirst(): Todo {
        return todoRepository.findAll()[0]
    }

    fun postMessage(todoRequest: TodoRequest) {
        val FANOUT_EXCHANGE_NAME = "amqp.fanout.exchange"

        rabbitTemplate.convertAndSend(FANOUT_EXCHANGE_NAME, "", todoRequest)
    }

}
