package com.example.blackhorsetemplate.bank

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class BankAccountService(var bankAccountRepository: BankAccountRepository) {

    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate

    fun addBankAccount(bankAccount: BankAccount): BankAccount {
        return bankAccountRepository.save(bankAccount);
    }

    fun getBankAccount(id: Long): BankAccount? {
        return bankAccountRepository.findByIdOrNull(id)
    }

    fun postMessage(bankAccount: BankAccount) {
        val FANOUT_EXCHANGE_NAME = "amqp.fanout.exchange"

        rabbitTemplate.convertAndSend(FANOUT_EXCHANGE_NAME, "", bankAccount)
    }
}
