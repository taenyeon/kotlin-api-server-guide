package com.example.kotlinapiserverguide.serviceTest.user.coverage.userService

import com.example.kotlinapiserverguide.api.member.service.MemberService
import com.example.kotlinapiserverguide.api.user.domain.dto.User
import com.example.kotlinapiserverguide.api.user.service.UserService
import com.example.kotlinapiserverguide.common.exception.ResponseException
import com.example.kotlinapiserverguide.common.http.constant.ResponseCode
import com.example.kotlinapiserverguide.common.security.provider.JwtTokenProvider
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime

internal class GetTest : DescribeSpec({
    describe("유저 조회") {
        context("로그인 하지 않았다면") {

            it("FAIL") {

                val exception = shouldThrow<ResponseException> { userService.getUser() }
                exception.responseCode shouldBe ResponseCode.INVALID_TOKEN

            }
        }
        context("로그인 했다면") {

            val authenticationToken = UsernamePasswordAuthenticationToken(user, null, null)
            authenticationToken.details = user
            SecurityContextHolder.getContext().authentication = authenticationToken

            it("SUCCESS") {
                userService.getUser().id shouldBe user.id
            }
        }
    }
}) {
    companion object {
        val memberService: MemberService = mockk()

        val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()

        val redisTemplate: RedisTemplate<String, Any> = mockk()

        val jwtTokenProvider: JwtTokenProvider = JwtTokenProvider(redisTemplate)

        val userService: UserService = UserService(memberService, passwordEncoder, jwtTokenProvider)

        val user = User(
            id = 1,
            username = "test",
            password = "test",
            name = "test",
            phoneNumber = "01011111111",
            imageUrl = null,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }
}