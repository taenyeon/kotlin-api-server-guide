package com.example.kotlinapiserverguide.coverage.member.memberService

import com.example.kotlinapiserverguide.api.member.repository.MemberRepository
import com.example.kotlinapiserverguide.api.member.service.MemberService
import com.example.kotlinapiserverguide.common.exception.ResponseException
import com.example.kotlinapiserverguide.common.http.constant.ResponseCode
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

internal class DeleteMemberTest : DescribeSpec({

    describe("회원 삭제") {

        context("이미 회원 정보가 없으면") {

            every { memberRepository.deleteById(id) } throws ResponseException(ResponseCode.NOT_FOUND_ERROR)

            it("[ERROR] NOT_FOUND") {

                shouldThrow<ResponseException> { memberService.deleteMember(id) }
                    .responseCode shouldBe ResponseCode.NOT_FOUND_ERROR

            }
        }

        context("회원 정보가 있으면") {

            every { memberRepository.deleteById(id) } just Runs

            it("SUCCESS") {
                verify { memberService.deleteMember(id) }
            }
        }

    }
}) {
    companion object {

        private val memberRepository: MemberRepository = mockk()

        private val memberService: MemberService = MemberService(memberRepository)

        private var id = 1L

    }
}