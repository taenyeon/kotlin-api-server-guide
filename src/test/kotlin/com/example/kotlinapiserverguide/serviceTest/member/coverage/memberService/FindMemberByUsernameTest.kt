package com.example.kotlinapiserverguide.serviceTest.member.coverage.memberService

import com.example.kotlinapiserverguide.api.member.domain.entity.Member
import com.example.kotlinapiserverguide.api.member.repository.MemberRepository
import com.example.kotlinapiserverguide.api.member.service.MemberService
import com.example.kotlinapiserverguide.common.exception.ResponseException
import com.example.kotlinapiserverguide.common.function.encrypt
import com.example.kotlinapiserverguide.common.http.constant.ResponseCode
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

internal class FindMemberByUsernameTest : DescribeSpec({

    describe("회원 조회 (username)") {

        context("기존 회원 정보가 없으면") {

            every { memberRepository.findByUsername(username.encrypt()) } returns null

            it("[ERROR] NOT_FOUND") {

                shouldThrow<ResponseException> { memberService.findMember(username) }
                    .responseCode shouldBe ResponseCode.NOT_FOUND_ERROR
            }
        }

        context("회원 정보가 있으면") {

            every { memberRepository.findByUsername(username.encrypt()) } returns member

            it("SUCCESS") {

                memberService.findMember(username).id shouldBe id
            }
        }
    }
}) {
    companion object {

        private val memberRepository: MemberRepository = mockk()

        private val memberService: MemberService = MemberService(memberRepository)

        private var id = 1L

        private var username = "test"

        private var member = Member(
            1,
            "test",
            "test",
            "test",
            "01011111111",
            null
        )
    }
}