package com.example.blackhorsetemplate.rabbitmq

import com.example.blackhorsetemplate.bank.BankAccount
import com.example.blackhorsetemplate.bank.BankAccountService
import com.example.blackhorsetemplate.todos.TodoRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

//@Disabled
@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
//@ExtendWith(OutputCaptureExtension::class)
class RabbitMQIntegrationTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var bankAccountService: BankAccountService

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
        bankAccountService.postMessage(BankAccount("nothing", "1", "2"))
    }


    @Test
    fun `should create a todo item`() {
        val json = ObjectMapper().writeValueAsString(TodoRequest("Write a blog on Testcontainers"))
        this.mockMvc.perform(
            MockMvcRequestBuilders.post("/todos").contentType(MediaType.APPLICATION_JSON).content(json)
        ).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isCreated)
    }

    @Test
    fun `should get the just created todo item`() {

        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/todos").contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("Write a blog on Testcontainers"))
    }

    @Test
    fun `should call the endpoint then rabbitMQ send out message`() {

        val json = ObjectMapper().writeValueAsString(TodoRequest("Write a blog on Testcontainers"))
        this.mockMvc.perform(
            MockMvcRequestBuilders.post("/todos/rabbit").contentType(MediaType.APPLICATION_JSON).content(json)
        ).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk)
    }
}

class KRabbitMQContainer(val image: String) : RabbitMQContainer(image)
