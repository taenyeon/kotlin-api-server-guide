package com.example.kotlinapiserverguide.coverage.member.memberService

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

internal class AddMemberTest : DescribeSpec({

    describe("회원 생성") {

        // common given
        every { memberRepository.save(member) } returns member

        context("이미 기존 회원 정보가 있으면") {

            every { memberRepository.findByUsername(username.encrypt()) } returns member

            it("[ERROR] EXIST_MEMBER") {

                shouldThrow<ResponseException> { memberService.addMember(member) }
                    .responseCode shouldBe ResponseCode.EXIST_MEMBER

            }
        }

        context("기존 회원 정보가 없으면") {

            every { memberRepository.findByUsername(username.encrypt()) } returns null

            it("SUCCESS") {

                memberService.addMember(member) shouldBe id
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