package com.example.kotlinapiserverguide.restDocs.docs

import com.example.kotlinapiserverguide.api.member.controller.MemberController
import com.example.kotlinapiserverguide.api.member.domain.dto.MemberDto
import com.example.kotlinapiserverguide.api.member.service.MemberService
import com.example.kotlinapiserverguide.restDocs.constant.*
import com.example.kotlinapiserverguide.restDocs.constant.parser.ResponseFields
import com.example.kotlinapiserverguide.restDocs.docs.base.BaseDocs
import com.example.kotlinapiserverguide.restDocs.infix.means
import com.example.kotlinapiserverguide.restDocs.infix.type
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest

import org.mockito.BDDMockito.*
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.ResultActions

@WebMvcTest(controllers = [MemberController::class], useDefaultFilters = false)
@Import(MemberController::class)
class MemberControllerDocs : BaseDocs() {


    @MockBean
    private lateinit var memberService: MemberService

    @Test
    fun findMember() {
        val api = DocsApi.MEMBER_FIND

        val username: String = "test"
        given(memberService.findMemberDto(username))
            .willReturn(
                MemberDto(
                    id = 1L,
                    username = "test",
                    name = "test",
                    phoneNumber = "01011111111",
                    createdAt = "2024-01-01 00:00:00",
                    updatedAt = "2024-01-01 00:00:00",
                    imageUrl = "https://~~.com"
                )
            )

        val result: ResultActions = mockMvc.perform(
            RestDocumentationRequestBuilders.get(api.path, username)
        )

        result.andDo(
            document(
                api.documentPath,
                restDocsUtils.getDocumentRequest(),
                restDocsUtils.getDocumentResponse(),
                pathParameters(
                    "username" means "로그인 ID"
                ),
                ResponseFields.DEPTH_2.build(
                    "body.id" type NUMBER means "회원 SEQ",
                    "body.username" type STRING means "로그인 ID",
                    "body.name" type STRING means "회원 이름",
                    "body.phoneNumber" type STRING means "회원 전화번호" isOptional true,
                    "body.createdAt" type STRING means "회원 생성일자" formattedAs DocsFormatter.DATETIME,
                    "body.updatedAt" type STRING means "회원 수정일자" formattedAs DocsFormatter.DATETIME,
                    "body.imageUrl" type STRING means "회원 프로필 URL" isOptional true
                )
            )

        )

    }
}