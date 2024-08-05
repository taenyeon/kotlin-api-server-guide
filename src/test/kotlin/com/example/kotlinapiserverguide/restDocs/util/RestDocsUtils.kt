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


class RestDocsUtils {

    fun <T> ENUM_FORMAT(enumValues: Collection<T>): String {
        return enumValues.joinToString(" / ")
    }


    companion object {

        fun buildDocument(documentPath: String, vararg snippets: Snippet): RestDocumentationResultHandler {
            return document(
                documentPath,
                DEFAULT_DOCUMENT_REQUEST,
                DEFAULT_DOCUMENT_RESPONSE,
                *snippets
            )
        }

        fun buildResponseFields(vararg fields: DocsField): ResponseFieldsSnippet {
            val docsFields =
                if (fields.isEmpty())
                    arrayOf("body" type NULL means "결과")
                else
                    "body" type OBJECT means "결과" fields arrayOf(*fields)

            val depth = docsFields.maxOfOrNull { it.depth } ?: 1

            return responseFields(
                attributes(*(1..depth).map { DocsAttributeKeys.DEPTH.set(it) }.toTypedArray()),
                "resultCode" type STRING means "결과 코드",
                "resultMessage" type STRING means "결과 메세지",
                *docsFields
            )
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