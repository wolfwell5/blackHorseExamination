package com.example.dealcontracts.exception

import com.example.dealcontracts.constants.PaymentStatus
import com.example.dealcontracts.clients.ClientResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(Exception::class)
    fun internalServerErrorHandler(exception: Exception): ResponseEntity<Any> {
        logger.error(exception.message, exception)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        logger.warn("HttpMessageNotReadable Exception! - ${(request as ServletWebRequest).request.requestURI} $ex")
        return super.handleHttpMessageNotReadable(ex, headers, status, request)
    }

    @ExceptionHandler(BalancePaymentException::class)
    fun handleUnionpayException(exception: BalancePaymentException): ResponseEntity<ClientResponse> {
        logger.error("payment exeception!")
        return ResponseEntity(
            ClientResponse(
                code = PaymentStatus.PAYMENT_FAILED.paymentResponseCode,
                message = PaymentStatus.PAYMENT_EXCEPTION.text
            ), HttpStatus.BAD_GATEWAY
        )
    }

    @ExceptionHandler(AccountNotEnoughException::class)
    fun handleAccountNotEnoughExceptionException(exception: AccountNotEnoughException): ResponseEntity<ClientResponse> {
        logger.error("payment exeception!")
        return ResponseEntity(
            ClientResponse(
                code = PaymentStatus.ACCOUNT_NOT_ENOUGH.paymentResponseCode,
                message = PaymentStatus.ACCOUNT_NOT_ENOUGH.text
            ), HttpStatus.BAD_GATEWAY
        )
    }
}
