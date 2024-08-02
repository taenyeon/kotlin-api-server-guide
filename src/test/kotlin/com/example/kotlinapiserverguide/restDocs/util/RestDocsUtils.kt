package com.example.kotlinapiserverguide.restDocs.util

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor
import org.springframework.restdocs.operation.preprocess.Preprocessors.*


class RestDocsUtils {

    fun <T> ENUM_FORMAT(enumValues: Collection<T>): String {
        return enumValues.joinToString(" / ")
    }


    fun getDocumentRequest(): OperationRequestPreprocessor {
        return preprocessRequest(
            modifyUris() // (1)
                .scheme("https")
                .host("docs.api.com")
                .removePort(),
            modifyHeaders()
                .add("access_token", "access_token_value")
                .add("refresh_token", "refresh_token_value"),
            prettyPrint()
        ) // (2)
    }

    fun getDocumentResponse(): OperationResponsePreprocessor {
        return preprocessResponse(prettyPrint()) // (3)
    }

}