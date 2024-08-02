package com.example.kotlinapiserverguide.common.exception

import com.example.kotlinapiserverguide.common.function.logger
import com.example.kotlinapiserverguide.common.http.constant.ResponseCode

class ResponseException() : RuntimeException() {
    val log = logger()

    var responseCode: ResponseCode = ResponseCode.UNKNOWN_ERROR
    var originalException: Exception? = null

    constructor(responseCode: ResponseCode) : this() {
        this.responseCode = responseCode
    }

    constructor(responseCode: ResponseCode, originalException: Exception) : this() {
        this.responseCode = responseCode
        this.originalException = originalException
    }

    override fun toString(): String {
        return "ResponseException - responseCode : ${responseCode.print()}, originalException : $originalException"
    }

}