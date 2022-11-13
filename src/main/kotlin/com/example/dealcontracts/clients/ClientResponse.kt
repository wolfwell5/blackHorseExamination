package com.example.dealcontracts.clients

import java.io.Serializable
import javax.persistence.*

data class ClientResponse(
    var code: String,
    var message: String? = "",
    var data: String? = null,
) : Serializable
