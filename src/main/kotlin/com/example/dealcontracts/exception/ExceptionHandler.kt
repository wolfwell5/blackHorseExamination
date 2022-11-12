package com.example.dealcontracts.exception

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

//    @ExceptionHandler(EntityUpdateException::class)
//    fun handleNoSuchElementException(exception: NoSuchElementException, request: WebRequest): ResponseEntity<Nothing> {
//        logger.warn("Deal not found! - ${(request as ServletWebRequest).request.requestURI} $exception")
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
//    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        logger.warn("HttpMessageNotReadable Exception! - ${(request as ServletWebRequest).request.requestURI} $ex")
        return super.handleHttpMessageNotReadable(ex, headers, status, request)
    }
}
