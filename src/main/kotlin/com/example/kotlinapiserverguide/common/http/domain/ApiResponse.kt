package com.example.kotlinapiserverguide.common.http.domain

import com.example.kotlinapiserverguide.common.http.constant.ResponseCode

class ApiResponse<T> {
    var resultCode: String
    var resultMessage: String
    var body: T? = null

    constructor() {
        this.resultCode = ResponseCode.SUCCESS.code
        this.resultMessage = ResponseCode.SUCCESS.message
    }

    constructor(responseCode: ResponseCode) {
        this.resultCode = responseCode.code
        this.resultMessage = responseCode.message
    }

    constructor(body: T) {
        this.resultCode = ResponseCode.SUCCESS.code
        this.resultMessage = ResponseCode.SUCCESS.message
        this.body = body
    }

    constructor(responseCode: ResponseCode, body: T) {
        this.resultCode = responseCode.code
        this.resultMessage = responseCode.message
        this.body = body
    }
}