package com.example.kotlinapiserverguide.common.exception.controller

import com.example.kotlinapiserverguide.common.exception.ResponseException
import com.example.kotlinapiserverguide.common.function.logger
import com.example.kotlinapiserverguide.common.http.constant.ResponseCode
import com.example.kotlinapiserverguide.common.http.domain.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionController {

    val log = logger()

    @ExceptionHandler(value = [Exception::class])
    fun unknownExceptionHandler(e: Exception): ApiResponse<Nothing> {
        e.printStackTrace()
        log.error("[unknownException Error] - errorMessage : ${e.message}")
        return ApiResponse(ResponseCode.UNKNOWN_ERROR)
    }

    @ExceptionHandler(value = [ResponseException::class])
    fun responseExceptionHandler(e: ResponseException): ApiResponse<Nothing> {
        e.printStackTrace()
        val responseCode = e.responseCode
        log.error("[responseExceptionHandler] - errorCase : $e")
        return ApiResponse(responseCode)
    }

    @ExceptionHandler(value = [AuthenticationException::class])
    fun authExceptionHandler(e: AuthenticationException): ApiResponse<Nothing> {
        return ApiResponse(ResponseCode.AUTH_ERROR)
    }

    @ExceptionHandler(value = [AccessDeniedException::class])
    fun accessDeniedExceptionHandler(e: AccessDeniedException): ApiResponse<Nothing> {
        return ApiResponse(ResponseCode.ACCESS_DENIED_ERROR)
    }
}