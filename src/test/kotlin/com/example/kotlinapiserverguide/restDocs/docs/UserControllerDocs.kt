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
import com.example.kotlinapiserverguide.restDocs.util.RestDocsUtils.Companion.buildDocument
import com.example.kotlinapiserverguide.restDocs.util.RestDocsUtils.Companion.buildRequestFields
import com.example.kotlinapiserverguide.restDocs.util.RestDocsUtils.Companion.buildResponseFields
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest

import org.mockito.BDDMockito.*
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers

@WebMvcTest(controllers = [UserController::class], useDefaultFilters = false)
@Import(UserController::class)
class UserControllerDocs : BaseDocs() {

    @MockBean
    private lateinit var userService: UserService

    @Test
    fun join() {
        val api = DocsApi.USER_JOIN

        // init
        val joinRequest = JoinRequest()
        joinRequest.name = "test"
        joinRequest.username = "test"
        joinRequest.phoneNumber = "01011111111"
        joinRequest.password = "test"

        given(userService.join(joinRequest))
            .willReturn(JoinResponse(1))

        val result = super.mockMvc.perform(
            RestDocumentationRequestBuilders.post(api.path)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .content(super.objectMapper.writeValueAsString(joinRequest))
        )

        result
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                buildDocument(
                    api.documentPath,
                    buildRequestFields(
                        "name" type STRING means "회원 이름",
                        "username" type STRING means "로그인 ID",
                        "password" type STRING means "로그인 PWD",
                        "phoneNumber" type STRING means "회원 전화번호",
                    ),
                    buildResponseFields(
                        "id" type NUMBER means "회원 SEQ"
                    ),
                )

            )
    }

    @Test
    fun login() {
        val api = DocsApi.USER_LOGIN

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
            RestDocumentationRequestBuilders.post(api.path)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .content(super.objectMapper.writeValueAsString(loginRequest))
        )
        result
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                buildDocument(
                    api.documentPath,
                    buildRequestFields(
                        "username" type STRING means "로그인 ID",
                        "password" type STRING means "로그인 PWD",
                    ),
                    buildResponseFields(
                        "accessToken" type STRING means "JWT accessToken",
                        "refreshToken" type STRING means "JWT refreshToken",
                    )
                )
            )
    }

    @Test
    fun logout() {

        val api = DocsApi.USER_LOGOUT

        val result = super.mockMvc.perform(RestDocumentationRequestBuilders.delete(api.path))

        result
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                buildDocument(
                    api.documentPath,
                    buildResponseFields()
                )
            )
    }


}