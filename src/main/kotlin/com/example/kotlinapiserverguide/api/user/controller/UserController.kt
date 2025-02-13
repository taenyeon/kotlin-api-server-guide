package com.example.kotlinapiserverguide.api.user.controller

import com.example.kotlinapiserverguide.api.member.domain.dto.MemberDto
import com.example.kotlinapiserverguide.api.user.domain.dto.JoinRequest
import com.example.kotlinapiserverguide.api.user.domain.dto.JoinResponse
import com.example.kotlinapiserverguide.api.user.domain.dto.JwtToken
import com.example.kotlinapiserverguide.api.user.domain.dto.LoginRequest
import com.example.kotlinapiserverguide.api.user.service.UserService
import com.example.kotlinapiserverguide.common.exception.ResponseException
import com.example.kotlinapiserverguide.common.function.logger
import com.example.kotlinapiserverguide.common.function.user
import com.example.kotlinapiserverguide.common.http.constant.ResponseCode
import com.example.kotlinapiserverguide.common.http.domain.ApiResponse
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
) {
    val log = logger()

    @GetMapping("")
    fun info(): ApiResponse<MemberDto> {
        return ApiResponse(userService.getUser())
    }

    @PostMapping("/join")
    fun join(@RequestBody joinRequest: JoinRequest): ApiResponse<JoinResponse> {
        return ApiResponse(userService.join(joinRequest))
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ApiResponse<JwtToken> {
        return ApiResponse(userService.login(loginRequest))
    }

    @DeleteMapping("/logout")
    fun logout(): ApiResponse<Nothing?> {
        val user = user()
        log.info("logoutUser - id : ${user.id}")
        userService.logout(user.id!!)
        return ApiResponse(null)
    }

    @PostMapping("/accessToken")
    fun reIssueAccessToken(@RequestBody jwtToken: JwtToken): ApiResponse<JwtToken> {
        val refreshToken = jwtToken.refreshToken
        log.info("refreshToken : $refreshToken")

        if (refreshToken.isBlank()) throw ResponseException(ResponseCode.INVALID_REQUEST_PARAM)
        val resultJwtToken = JwtToken(
            userService.reIssueAccessToken(refreshToken),
            refreshToken
        )
        return ApiResponse(resultJwtToken)
    }


}