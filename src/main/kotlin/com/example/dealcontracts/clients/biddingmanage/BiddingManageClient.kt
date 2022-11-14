package com.example.dealcontracts.clients.biddingmanage

import com.example.dealcontracts.controller.dto.BiddingPickupDto
import com.example.dealcontracts.clients.ClientResponse
import com.fasterxml.jackson.databind.JsonNode
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.*

@Service
class BiddingManageClient {

    companion object : KLogging()

    @Value("\${bidding.pickup.api.url}")
    lateinit var biddingManageUrl: String

    @Autowired
    lateinit var restTemplate: RestTemplate

    fun pickupBidding(biddingPickupDto: BiddingPickupDto): ClientResponse {

        return try {
            restTemplate.postForObject(biddingManageUrl, HttpEntity(biddingPickupDto), ClientResponse::class)
//            return ClientResponse("1", "mock data", "1")
        } catch (exception: Exception) {
            when (exception) {
                is RestClientResponseException, is RestClientException, is HttpClientErrorException -> {
                    return ClientResponse("-1", "error happen")
                }
                else -> throw exception
            }
        }
    }

}
