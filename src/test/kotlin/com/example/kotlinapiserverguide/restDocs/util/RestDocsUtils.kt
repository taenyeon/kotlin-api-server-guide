package com.example.kotlinapiserverguide.restDocs.util

import com.example.kotlinapiserverguide.common.function.logger
import com.example.kotlinapiserverguide.restDocs.constant.*
import com.example.kotlinapiserverguide.restDocs.infix.type
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.restdocs.snippet.Attributes.attributes
import org.springframework.restdocs.snippet.Snippet
import java.lang.System.Logger


class RestDocsUtils {

    fun <T> ENUM_FORMAT(enumValues: Collection<T>): String {
        return enumValues.joinToString(" / ")
    }


    companion object {
        private val log = logger()
        fun buildDocument(documentPath: String, vararg snippets: Snippet): RestDocumentationResultHandler {
            log.info("buildDocument: $documentPath")
            return document(
                documentPath,
                DEFAULT_DOCUMENT_REQUEST,
                DEFAULT_DOCUMENT_RESPONSE,
                *snippets
            )
        }

        fun buildResponseFields(vararg fields: DocsField): ResponseFieldsSnippet {
            val docsFields: Array<DocsField> =
                if (fields.isEmpty())
                    arrayOf("body" type NULL means "결과")
                else
                    "body" type OBJECT means "결과" fields arrayOf(*fields)

            docsFields.forEach { println("field: ${it.attributes}") }

            val depth: Int = docsFields.maxOfOrNull { it.depth } ?: 1

            val attributes: MutableMap<String, Any> =
                attributes(*(1..depth).map { DocsAttributeKeys.DEPTH.set(it) }.toTypedArray())

            attributes[DocsAttributeKeys.FORMAT.code] = "&nbsp;"

            val responseFields = responseFields(
                attributes,
                "resultCode" type STRING means "결과 코드" isOptional true,
                "resultMessage" type STRING means "결과 메세지" isOptional true,
                *docsFields
            )
            return responseFields
        }

        fun buildRequestFields(vararg field: DocsField): RequestFieldsSnippet {
            val depth = field.maxOfOrNull { it.depth } ?: 1

            return requestFields(
                attributes(*(1..depth).map { DocsAttributeKeys.DEPTH.set(it) }.toTypedArray()),
                *field
            )
        }

        val DEFAULT_DOCUMENT_REQUEST: OperationRequestPreprocessor
            get() {
                return preprocessRequest(
                    modifyUris()
                        .host("localhost")
                        .port(8080),
                    modifyHeaders()
                        .add("access_token", "access_token_value")
                        .add("refresh_token", "refresh_token_value"),
                    prettyPrint()
                )
            }

        val DEFAULT_DOCUMENT_RESPONSE: OperationResponsePreprocessor
            get() {
                return preprocessResponse(prettyPrint())
            }
    }
}