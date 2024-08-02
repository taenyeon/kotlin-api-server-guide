package com.example.kotlinapiserverguide.common.http.constant

import com.example.kotlinapiserverguide.common.http.domain.ApiResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity


enum class ResponseCode(
    val code: String,
    val message: String
) {

    // SUCCESS
    SUCCESS("SUCCESS", "success."),

    // ERROR

    // USER
    INVALID_TOKEN("INVALID_TOKEN_ERROR", "invalid token error occurred."),
    WRONG_PASSWORD_ERROR("WRONG_PASSWORD_ERROR", "wrong password error occurred."),
    ALREADY_LOGOUT_ERROR("ALREADY_LOGOUT_ERROR", "already logout error occurred."),

    // AUTH
    AUTH_ERROR("AUTH_ERROR", "auth error occurred."),
    ACCESS_DENIED_ERROR("ACCESS_DENIED_ERROR", "access denied error occurred."),

    // MEMBER
    EXIST_MEMBER("EXIST_MEMBER", "already member error occurred."),

    // DEFAULT
    UNKNOWN_ERROR("UNKNOWN_ERROR", "unknown error occurred."),
    MANUAL_ERROR("MANUAL_ERROR", "manual error occurred."),
    NOT_FOUND_ERROR("NOT_FOUNT_ERROR", "not found error occurred."),
    INVALID_REQUEST_PARAM("INVALID_REQUEST_PARAM", "invalid request parameter error occurred."),

    // ENCRYPT
    ENCRYPT_ERROR("ENCRYPT_ERROR", "encrypt error occurred"),
    DECRYPT_ERROR("DECRYPT_ERROR", "decrypt error occurred"),
    ;

    fun print(): String {
        return "ResponseCode - code : $code, message: $message"
    }
}