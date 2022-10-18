package com.example.blackhorsetemplate.bank

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import java.io.Serializable

@Entity
data class BankAccount(
    var bankCode: String,
    var accountNumber: String,
    var accountHolderName: String,
    @Id @GeneratedValue var id: Long? = null,
): Serializable
