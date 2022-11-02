package com.group.libraryapp.common.exception

import com.group.libraryapp.util.logger
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
//class CommonExceptionHandler : ResponseEntityExceptionHandler() {
class CommonExceptionHandler : ResponseEntityExceptionHandler() {
    val log = logger<CommonExceptionHandler>()

//    override fun handleMethodArgumentNotValid(
//        ex: MethodArgumentNotValidException,
//        headers: HttpHeaders,
//        status: HttpStatus,
//        request: WebRequest
//    ): ResponseEntity<Any> {
//        log.error("Exception:" + ex);
//        return super.handleMethodArgumentNotValid(ex, headers, status, request)
//    }
//
//    override fun handleHttpMessageNotReadable(
//        ex: HttpMessageNotReadableException,
//        headers: HttpHeaders,
//        status: HttpStatus,
//        request: WebRequest
//    ): ResponseEntity<Any> {
//        log.error("Exception:" + ex);
//        return super.handleHttpMessageNotReadable(ex, headers, status, request)
//    }

    @ExceptionHandler(value = [Exception::class])
    fun handleAnyException(e: Exception, request: WebRequest): ResponseEntity<String>{
        log.error("Exception:" + e);
        return ResponseEntity.internalServerError().body("기타 Exception 발생")
    }


}