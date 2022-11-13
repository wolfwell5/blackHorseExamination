package com.example.dealcontracts.controller

import com.example.dealcontracts.clients.ClientResponse
import com.example.dealcontracts.controller.dto.BiddingPickupDto
import com.example.dealcontracts.service.BiddingPickupService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/deal-contracts")
class BiddingPickupController(var biddingPickupService: BiddingPickupService) {

    @PostMapping("/{did}/pick-up", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun biddingPickup(
        @PathVariable did: String, @RequestBody biddingPickupDto: BiddingPickupDto
    ): ResponseEntity<ClientResponse> {

        val clientResponse = biddingPickupService.pickupBidding(biddingPickupDto)

        return ResponseEntity.ok(clientResponse)
    }

}
