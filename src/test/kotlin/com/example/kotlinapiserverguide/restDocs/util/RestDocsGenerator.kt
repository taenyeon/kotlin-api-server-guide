package com.example.kotlinapiserverguide.restDocs.util

import com.example.kotlinapiserverguide.restDocs.constant.DocsApi
import com.example.kotlinapiserverguide.restDocs.constant.DocsApiType
import java.io.File
import java.io.FileOutputStream

class RestDocsGenerator {

    var apiDefaultPath = "{snippets}"

    var pathParameterTemplate = "/path-parameters.adoc[]"
    var httpRequestTemplate = "/http-request.adoc[]"
    var httpResponseTemplate = "/response-fields.adoc[]"
    var responseFieldsTemplate = "/http-response.adoc[]"

    fun generateTemplate() {
        val docs = File("src/docs/asciidoc/apiDocs.adoc")

        val stringBuilder = StringBuilder()
        stringBuilder

            // snippets 경로
            .append("ifndef::snippets[]")
            .append(System.lineSeparator())
            .append(":snippets: ./build/generated-snippets")
            .append(System.lineSeparator())
            .append("endif::[]")
            .append(System.lineSeparator())

            .append(System.lineSeparator())

            // Ascii Docs 서식
            .append(":doctype: book")
            .append(System.lineSeparator())
            .append(":icons: font")
            .append(System.lineSeparator())
            .append(":source-highlighter: highlight.js")
            .append(System.lineSeparator())
            .append(":table-caption!:")
            .append(System.lineSeparator())
            .append("= \uD83D\uDE80 API")
            .append(System.lineSeparator())
            .append(":toc: left")
            .append(System.lineSeparator())
            .append(":toc-title: \uD83D\uDE80 API")
            .append(System.lineSeparator())
            .append(":toclevels: 4")
            .append(System.lineSeparator())
            .append(":sectlinks:")
            .append(System.lineSeparator())

            .append(System.lineSeparator())

        // 공통 header
        buildDocsCommonHeader(stringBuilder)

        DocsApiType.entries.forEach { type ->
            stringBuilder
                .append(buildHeader(type.description, 2))
                .append(System.lineSeparator())

            DocsApi.entries.filter { it.docsApiType == type }
                .forEach { api ->
                    val apiPath = apiDefaultPath + api.directoryName
                    stringBuilder
                        .append(buildHeader(api.description, 3))
                        .append(System.lineSeparator())
                        .append(buildHighlight("Request"))
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())
                        .append(buildTemplateTitle("path parameter"))
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())
                        .append(buildTemplate(apiPath, pathParameterTemplate))
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())
                        .append(buildTemplateTitle("sample"))
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())
                        .append(buildTemplate(apiPath, httpRequestTemplate))
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())
                        .append(buildHighlight("Response"))
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())
                        .append(buildTemplateTitle("response body"))
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())
                        .append(buildTemplate(apiPath, responseFieldsTemplate))
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())
                        .append(buildTemplateTitle("sample"))
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())
                        .append(buildTemplate(apiPath, httpResponseTemplate))
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())

                }
        }

        try {
            val fileOutputStream = FileOutputStream(docs)
            fileOutputStream.write(stringBuilder.toString().toByteArray())
            fileOutputStream.close()
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

    }

    private fun buildDocsCommonHeader(stringBuilder: StringBuilder): StringBuilder {
        stringBuilder
            .append("== 공통 headers")
            .append(System.lineSeparator())
            .append("[%autowidth]")
            .append(System.lineSeparator())
            .append("|===")
            .append(System.lineSeparator())
            .append("|Name|Description|optional")
            .append(System.lineSeparator())
            .append(System.lineSeparator())
            .append("\t|access_token")
            .append(System.lineSeparator())
            .append("\t|JWT accessToken")
            .append(System.lineSeparator())
            .append("\t|")
            .append(System.lineSeparator())
            .append(System.lineSeparator())
            .append("\t|refresh_token")
            .append(System.lineSeparator())
            .append("\t|JWT refreshToken (accessToken이 만료된 경우, 재발급 용도로 사용)")
            .append(System.lineSeparator())
            .append("\t|")
            .append(System.lineSeparator())
            .append(System.lineSeparator())
            .append("|===")
            .append(System.lineSeparator())

            .append(System.lineSeparator())

        return stringBuilder
    }

    private fun buildHeader(text: String, depth: Int): String {
        val s = "=".repeat(depth) + " " + text
        println(s)
        return s
    }

    private fun buildHighlight(text: String): String {
        return "*$text*"
    }

    private fun buildTemplateTitle(text: String): String {
        return ".$text"
    }

    private fun buildTemplate(path: String, template: String): String {
        return "include::$path$template"
    }
}