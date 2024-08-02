package com.example.kotlinapiserverguide.restDocs.docs.base

import com.example.kotlinapiserverguide.restDocs.util.RestDocsGenerator
import com.example.kotlinapiserverguide.restDocs.util.RestDocsUtils
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@ExtendWith(RestDocumentationExtension::class)
@WebMvcTest(useDefaultFilters = false)
@EnableMethodSecurity(proxyTargetClass = true)
@WithMockUser
@AutoConfigureMockMvc
@AutoConfigureRestDocs(
    uriScheme = "http",
    uriHost = "docs.api.com"
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
    }

    companion object {
        @JvmStatic
        @AfterAll
        fun end(): Unit {
            RestDocsGenerator().generateTemplate()
        }
    }


}