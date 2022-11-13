package com.example.dealcontracts.controller.dto

import java.io.Serializable

data class BiddingPickupDto(
    var userId: String,
    var biddingId: String,
) : Serializable
