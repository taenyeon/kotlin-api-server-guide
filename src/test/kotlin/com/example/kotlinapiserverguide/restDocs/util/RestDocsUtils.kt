package com.example.kotlinapiserverguide.restDocs.util

import com.example.kotlinapiserverguide.restDocs.constant.DocsAttributeKeys
import com.example.kotlinapiserverguide.restDocs.constant.DocsField
import com.example.kotlinapiserverguide.restDocs.constant.STRING
import com.example.kotlinapiserverguide.restDocs.infix.type
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.snippet.Attributes.*


class RestDocsUtils {

    fun <T> ENUM_FORMAT(enumValues: Collection<T>): String {
        return enumValues.joinToString(" / ")
    }


    fun getDocumentRequest(): OperationRequestPreprocessor {
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

    fun getDocumentResponse(): OperationResponsePreprocessor {
        return preprocessResponse(prettyPrint())
    }
}