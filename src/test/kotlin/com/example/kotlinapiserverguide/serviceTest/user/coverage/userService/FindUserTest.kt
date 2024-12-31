package com.example.kotlinapiserverguide.serviceTest.user.coverage.userService

import com.example.kotlinapiserverguide.api.member.service.MemberService
import com.example.kotlinapiserverguide.api.user.domain.dto.User
import com.example.kotlinapiserverguide.api.user.service.UserService
import com.example.kotlinapiserverguide.common.exception.ResponseException
import com.example.kotlinapiserverguide.common.function.encrypt
import com.example.kotlinapiserverguide.common.http.constant.ResponseCode
import com.example.kotlinapiserverguide.common.security.provider.JwtTokenProvider
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.LocalDateTime

internal class FindUserTest : DescribeSpec({

    describe("유저 조회 (로그인 상태)") {
        context("로그인 안했으면") {

            it("[ERROR] AUTH_ERROR") {
                shouldThrow<ResponseException> { userService.getUser() }
                    .responseCode shouldBe ResponseCode.INVALID_TOKEN
            }

        }
        context("로그인 했으면") {

            token.details = user
            SecurityContextHolder.getContext().authentication = token

            it("SUCCESS") {
                userService.getUser().id shouldBe id
            }

        }
    }


}) {
    companion object {
        private val memberService: MemberService = mockk();
        private val jwtTokenProvider: JwtTokenProvider = mockk()
        private val userService = UserService(memberService, BCryptPasswordEncoder(), jwtTokenProvider);

        private var id = 1L;

        private val user = User(
            1,
            "test".encrypt(),
            "test",
            "test".encrypt(),
            "01011111111".encrypt(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            null
        )

        private val token: UsernamePasswordAuthenticationToken =
            UsernamePasswordAuthenticationToken(user, null, null);
    }
}