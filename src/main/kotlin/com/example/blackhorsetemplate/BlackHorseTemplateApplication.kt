package com.example.blackhorsetemplate

import com.example.blackhorsetemplate.rabbitmq.MessageSender
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@SpringBootApplication
class BlackHorseTemplateApplication

fun main(args: Array<String>) {
    runApplication<BlackHorseTemplateApplication>(*args)
}

// 不能生效，不像下面几个 class 可以执行并打印出日志
@Bean
fun runner(messageSender: MessageSender): ApplicationRunner? {
    return ApplicationRunner { args: ApplicationArguments? ->
        println("开始发送消息---")
        messageSender.broadcast("a broadcast message")
        messageSender.sendError("an error message")
        messageSender.broadcast("another broadcast message")
        messageSender.broadcast("that's it")
    }
}

@Component //此类一定要交给spring管理
class ConsumerRunner : ApplicationRunner {
    @Throws(Exception::class)
    override fun run(args: ApplicationArguments?) {
        //代码
        println("需要在springBoot项目启动时执行的代码---")
    }
}

@Component //此类一定要交给spring管理
@Order(value = 1) //首先执行
class ConsumerRunnerA : ApplicationRunner {
    @Throws(Exception::class)
    override fun run(args: ApplicationArguments?) {
        //代码
        println("需要在springBoot项目启动时执行的代码1---")
    }
}

@Component //此类一定要交给spring管理
@Order(value = 2) //其次执行
class ConsumerRunnerB : ApplicationRunner {
    @Throws(Exception::class)
    override fun run(args: ApplicationArguments?) {
        //代码
        println("需要在springBoot项目启动时执行的代码2---")
    }
}

