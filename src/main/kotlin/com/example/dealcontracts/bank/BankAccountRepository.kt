package com.example.dealcontracts.bank

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Repository
interface BankAccountRepository : CrudRepository<BankAccount, Long> {}
