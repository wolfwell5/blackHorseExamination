package com.example.dealcontracts.repository.entity

import com.example.dealcontracts.constants.PaymentStatus
import com.example.dealcontracts.constants.PaymentStatus.PAYING
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "orders")
data class Order(
    @Column(name = "user_id")
    var userId: String,
    @Column(name = "account_id")
    var accountId: String,
    @Column(name = "order_name")
    var orderName: String? = "",
    @Column(name = "pay_status")
    @Enumerated(EnumType.STRING)
    var payStatus: PaymentStatus? = PAYING,

    @Id
    @Column
    var id: String,
) : Serializable
