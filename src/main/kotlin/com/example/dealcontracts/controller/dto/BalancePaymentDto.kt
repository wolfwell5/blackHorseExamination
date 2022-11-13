package com.example.dealcontracts.controller.dto

import java.io.Serializable

data class BalancePaymentDto(
    var accountId: String,
    var paymentAmount: Int? = 0,
) : Serializable

