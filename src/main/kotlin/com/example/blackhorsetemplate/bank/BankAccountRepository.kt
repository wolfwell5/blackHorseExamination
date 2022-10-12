package com.example.blackhorsetemplate.bank

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Repository
interface BankAccountRepository : CrudRepository<BankAccount, Long> {}
