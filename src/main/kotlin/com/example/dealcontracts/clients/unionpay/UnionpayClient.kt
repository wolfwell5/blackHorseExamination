package com.example.dealcontracts.clients.unionpay

import com.example.dealcontracts.clients.ClientResponse
import com.example.dealcontracts.controller.dto.BalancePaymentDto
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.*

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
            //            可手动更改三方返回来协助本地开发
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
