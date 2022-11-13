package com.example.dealcontracts.config

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.web.client.RestTemplate
import java.nio.charset.StandardCharsets

@Configuration
class RestTemplateConfig {

//    @Bean
//    fun restTemplate(restTemplateBuilder: RestTemplateBuilder): RestTemplate {
//        return restTemplateBuilder
//            .requestFactory(requestFactorySupplier)
//            .additionalMessageConverters(StringHttpMessageConverter(StandardCharsets.UTF_8))
//            .build()
//    }
//
//    val requestFactorySupplier: () -> ClientHttpRequestFactory = {
//        val requestFactory = HttpComponentsClientHttpRequestFactory()
//        requestFactory
//    }

    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate? {
        return builder.build()
    }
}
