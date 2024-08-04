package com.example.kotlinapiserverguide.restDocs.docs.base

import com.example.kotlinapiserverguide.api.user.domain.dto.User
import com.example.kotlinapiserverguide.restDocs.util.RestDocsGenerator
import com.example.kotlinapiserverguide.restDocs.util.RestDocsUtils
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Profile
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.time.LocalDateTime

@ExtendWith(RestDocumentationExtension::class)
@WebMvcTest(useDefaultFilters = false)
@EnableMethodSecurity(proxyTargetClass = true)
@WithMockUser
@AutoConfigureMockMvc
@AutoConfigureRestDocs(
//    uriScheme = "http",
    uriHost = "localhost",
    uriPort = 8000
)
open class BaseDocs {

    @Autowired
    lateinit var mockMvc: MockMvc

    var restDocsUtils: RestDocsUtils = RestDocsUtils()

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp(
        webApplicationContext: WebApplicationContext,
        restDocumentationContextProvider: RestDocumentationContextProvider,
    ) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply<DefaultMockMvcBuilder>(documentationConfiguration(restDocumentationContextProvider))
            .build()

        setUser()
    }

    private fun setUser() {
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
        val authenticationToken = UsernamePasswordAuthenticationToken(user, null, null)
        authenticationToken.details = user
        SecurityContextHolder.getContext().authentication = authenticationToken
    }

    companion object {
        @JvmStatic
        @AfterAll
        fun end(): Unit {
            RestDocsGenerator().generateTemplate()
        }
    }


}