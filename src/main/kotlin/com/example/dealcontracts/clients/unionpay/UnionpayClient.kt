package com.example.dealcontracts.clients.unionpay

import com.example.dealcontracts.clients.ClientResponse
import com.example.dealcontracts.controller.dto.BalancePaymentDto
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.*
import org.springframework.web.client.HttpServerErrorException.BadGateway
import org.springframework.web.client.HttpServerErrorException.InternalServerError

@Service
class UnionpayClient {

    companion object : KLogging()

    @Value("\${unionpay.api.url}")
    lateinit var unionpayUrl: String

    @Autowired
    lateinit var restTemplate: RestTemplate

    fun balancePayToUnionpay(balancePaymentDto: BalancePaymentDto): ClientResponse {

        return try {
            restTemplate.postForObject(unionpayUrl, HttpEntity(balancePaymentDto), ClientResponse::class)
//            return ClientResponse("1", "mock data", "1")

        } catch (exception: Exception) {
            when (exception) {
                is RestClientResponseException, is RestClientException, is HttpClientErrorException, is BadGateway -> {
                    return ClientResponse("-1", "error happen")
                }
                else -> throw exception
            }
        }
    }

}
