package com.example.kotlinapiserverguide.restDocs.docs

import com.example.kotlinapiserverguide.api.user.controller.UserController
import com.example.kotlinapiserverguide.api.user.domain.dto.JoinRequest
import com.example.kotlinapiserverguide.api.user.domain.dto.JoinResponse
import com.example.kotlinapiserverguide.api.user.domain.dto.JwtToken
import com.example.kotlinapiserverguide.api.user.domain.dto.LoginRequest
import com.example.kotlinapiserverguide.api.user.service.UserService
import com.example.kotlinapiserverguide.restDocs.constant.*
import com.example.kotlinapiserverguide.restDocs.docs.base.BaseDocs
import com.example.kotlinapiserverguide.restDocs.infix.type
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest

import org.mockito.BDDMockito.*
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.snippet.Attributes.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers

@WebMvcTest(controllers = [UserController::class], useDefaultFilters = false)
@Import(UserController::class)
class UserControllerDocs : BaseDocs() {

    @MockBean
    private lateinit var userService: UserService

    @Test
    fun join() {

        // init
        val joinRequest = JoinRequest()
        joinRequest.name = "test"
        joinRequest.username = "test"
        joinRequest.phoneNumber = "01011111111"
        joinRequest.password = "test"

        given(userService.join(joinRequest))
            .willReturn(JoinResponse(1))

        val result = super.mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/user/join")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .content(super.objectMapper.writeValueAsString(joinRequest))
        )

        result
            .andDo(MockMvcResultHandlers.print())
            .andDo(
            document(
                "user-join",
                super.restDocsUtils.getDocumentRequest(),
                super.restDocsUtils.getDocumentResponse(),
                requestBody(),
                requestFields(
                    attributes(
                        key(DocsAttributeKeys.DEPTH.code + 1).value("")
                    ),
                    "name" type STRING means "회원 이름",
                    "username" type STRING means "로그인 ID",
                    "password" type STRING means "로그인 PWD",
                    "phoneNumber" type STRING means "회원 전화번호",
                ),
                responseFields(
                    attributes(
                        key(DocsAttributeKeys.DEPTH.code + 1).value(""),
                        key(DocsAttributeKeys.DEPTH.code + 2).value(""),
                    ),
                    "resultCode" type STRING means "결과 코드",
                    "resultMessage" type STRING means "결과 메세지",
                    *"body" type OBJECT means "결과" fields
                            arrayOf(
                                "body.id" type NUMBER means "회원 SEQ"
                            ),
                )
            )
        )
    }

    @Test
    fun login() {
        val loginRequest = LoginRequest()
        loginRequest.username = "test"
        loginRequest.password = "test"

        given(userService.login(loginRequest))
            .willReturn(
                JwtToken(
                    "access_token_value",
                    "refresh_token_value"
                )
            )

        val result = super.mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .content(super.objectMapper.writeValueAsString(loginRequest))
        )
        result
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document(
                    "user-login",
                    super.restDocsUtils.getDocumentRequest(),
                    super.restDocsUtils.getDocumentResponse(),
                    requestFields(
                        attributes(
                            key(DocsAttributeKeys.DEPTH.code + 1).value(""),
                        ),
                        "username" type STRING means "로그인 ID",
                        "password" type STRING means "로그인 PWD",
                    ),
                    responseFields(
                        attributes(
                            key(DocsAttributeKeys.DEPTH.code + 1).value(""),
                            key(DocsAttributeKeys.DEPTH.code + 2).value("")
                        ),
                        "resultCode" type STRING means "결과 코드",
                        "resultMessage" type STRING means "결과 메세지",
                        *"body" type OBJECT means "결과" fields
                                arrayOf(
                                    "body.accessToken" type STRING means "JWT accessToken",
                                    "body.refreshToken" type STRING means "JWT refreshToken",
                                ),
                    )
                )
            )
    }


}