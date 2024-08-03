package com.example.kotlinapiserverguide.restDocs.util

import com.example.kotlinapiserverguide.common.exception.ResponseException
import com.example.kotlinapiserverguide.common.http.constant.ResponseCode
import com.example.kotlinapiserverguide.restDocs.constant.DocsApi
import com.example.kotlinapiserverguide.restDocs.constant.DocsApiType
import java.io.File
import java.io.FileOutputStream

class RestDocsGenerator {

    private var apiDefaultPath = "{snippets}"

    private var pathParameterTemplate = "path-parameters.adoc"
    private var httpRequestTemplate = "http-request.adoc"
    private var requestFieldsTemplate = "request-fields.adoc"
    private var httpResponseTemplate = "http-response.adoc"
    private var responseFieldsTemplate = "response-fields.adoc"
    private val curlRequestTemplate = "curl-request.adoc"

    fun generateTemplate() {
        val docs: File = File("src/docs/asciidoc/apiDocs.adoc")

        val snippetsDir = File("build/generated-snippets")

        val snippetFiles = snippetsDir.listFiles() ?: emptyArray()

        if (!snippetsDir.exists() || !snippetsDir.isDirectory || snippetFiles.isEmpty()) throw ResponseException(
            ResponseCode.NOT_FOUND_ERROR
        )

        val snippetNames: List<String> = snippetFiles.map { snippet -> snippet.name }

        val stringBuilder = StringBuilder()
        stringBuilder

            // snippets 경로
            .append("ifndef::snippets[]")
            .append(System.lineSeparator())
            .append(":snippets: ./build/generated-snippets")
            .append(System.lineSeparator())
            .append("endif::[]")

            // 구분자
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

            // 구분자
            .append(System.lineSeparator())
            .append(System.lineSeparator())

        // 공통 header
        buildDocsCommonHeader(stringBuilder)

        // 공통 responseCode
        buildDocsCommonResponseCode(stringBuilder)

        // API 목록
        DocsApiType.entries
            .forEach { type ->
                stringBuilder
                    .append(buildHeader(type.description, 2))
                    .append(System.lineSeparator())

                DocsApi.entries.filter { it.docsApiType == type }
                    .filter { snippetNames.contains(it.directoryName) }
                    .forEach { api ->

                        stringBuilder
                            .append(buildHeader(api.title, 3))

                            .append(System.lineSeparator())
                            .append(".${api.description}")

                            .append(System.lineSeparator())
                            .append(System.lineSeparator())

                        buildCurlRequestTemplate(stringBuilder, api.directoryName)

                        stringBuilder
                            .append(buildHighlight("Request"))
                            .append(System.lineSeparator())
                            .append(System.lineSeparator())

                        buildPathParameterTemplate(stringBuilder, api.directoryName)

                        buildRequestFieldsTemplate(stringBuilder, api.directoryName)

                        buildRequestSampleTemplate(stringBuilder, api.directoryName)

                        stringBuilder
                            .append(buildHighlight("Response"))
                            .append(System.lineSeparator())
                            .append(System.lineSeparator())

                        buildResponseFieldsTemplate(stringBuilder, api.directoryName)

                        buildResponseSampleTemplate(stringBuilder, api.directoryName)
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

    private fun buildDocsCommonResponseCode(stringBuilder: StringBuilder): StringBuilder {
        stringBuilder
            .append("== 공통 ResponseCode")
            .append(System.lineSeparator())
            .append("[%autowidth]")
            .append(System.lineSeparator())
            .append("|===")
            .append(System.lineSeparator())
            .append("|Code|Description")
            .append(System.lineSeparator())
            .append(System.lineSeparator())

        ResponseCode.entries.forEach {
            stringBuilder
                .append("\t|${it.code}")
                .append(System.lineSeparator())
                .append("\t|${it.message}")
                .append(System.lineSeparator())
                .append(System.lineSeparator())
        }

        stringBuilder
            .append(System.lineSeparator())
            .append(System.lineSeparator())
            .append("|===")
            .append(System.lineSeparator())

            .append(System.lineSeparator())
        return stringBuilder
    }

    private fun buildHeader(text: String, depth: Int): String {
        return "=".repeat(depth) + " " + text
    }

    private fun buildHighlight(text: String): String {
        return "*$text*"
    }

    private fun buildTemplateTitle(text: String): String {
        return ".$text"
    }

    private fun buildTemplate(path: String, template: String): String {
        return "include::$path/$template[]"
    }

    private fun isTemplateExist(snippetDir: String, template: String): Boolean {
        return File(snippetDir + template).exists()

    }

    private fun buildPathParameterTemplate(stringBuilder: StringBuilder, directoryName: String): StringBuilder {
        val apiPath = "$apiDefaultPath/$directoryName"
        val snippetDir = "build/generated-snippets/${directoryName}/"

        if (isTemplateExist(snippetDir, pathParameterTemplate)) buildTemplate(
            stringBuilder,
            "path parameter",
            apiPath,
            pathParameterTemplate
        )

        return stringBuilder
    }

    private fun buildRequestFieldsTemplate(stringBuilder: StringBuilder, directoryName: String): StringBuilder {
        val apiPath = "$apiDefaultPath/$directoryName"
        val snippetDir = "build/generated-snippets/${directoryName}/"

        if (isTemplateExist(snippetDir, requestFieldsTemplate)) buildTemplate(
            stringBuilder,
            "request body",
            apiPath,
            requestFieldsTemplate
        )

        return stringBuilder
    }

    private fun buildRequestSampleTemplate(stringBuilder: StringBuilder, directoryName: String): StringBuilder {
        val apiPath = "$apiDefaultPath/$directoryName"
        val snippetDir = "build/generated-snippets/${directoryName}/"

        if (isTemplateExist(snippetDir, httpRequestTemplate)) buildSampleTemplate(
            stringBuilder,
            "sample",
            apiPath,
            httpRequestTemplate
        )

        return stringBuilder
    }

    private fun buildResponseFieldsTemplate(stringBuilder: StringBuilder, directoryName: String): StringBuilder {
        val apiPath = "$apiDefaultPath/$directoryName"
        val snippetDir = "build/generated-snippets/${directoryName}/"

        if (isTemplateExist(snippetDir, responseFieldsTemplate)) buildTemplate(
            stringBuilder,
            "response body",
            apiPath,
            responseFieldsTemplate
        )

        return stringBuilder
    }

    private fun buildResponseSampleTemplate(stringBuilder: StringBuilder, directoryName: String): StringBuilder {
        val apiPath = "$apiDefaultPath/$directoryName"
        val snippetDir = "build/generated-snippets/${directoryName}/"

        if (isTemplateExist(snippetDir, httpResponseTemplate)) buildTemplate(
            stringBuilder,
            "sample",
            apiPath,
            httpResponseTemplate
        )

        return stringBuilder
    }

    private fun buildCurlRequestTemplate(stringBuilder: StringBuilder, directoryName: String): StringBuilder {
        val apiPath = "$apiDefaultPath/$directoryName"
        val snippetDir = "build/generated-snippets/${directoryName}/"
        if (isTemplateExist(snippetDir, curlRequestTemplate)) buildTemplate(
            stringBuilder,
            "curl",
            apiPath,
            curlRequestTemplate
        )

        return stringBuilder
    }

    private fun buildTemplate(stringBuilder: StringBuilder, title: String, apiPath: String, template: String) {
        stringBuilder
            .append(buildTemplateTitle(title))
            .append(System.lineSeparator())
            .append(System.lineSeparator())
            .append(buildTemplate(apiPath, template))
            .append(System.lineSeparator())
            .append(System.lineSeparator())
    }

    private fun buildSampleTemplate(stringBuilder: StringBuilder, title: String, apiPath: String, template: String) {
        stringBuilder
            .append(buildTemplateTitle(title))
            .append(System.lineSeparator())
            .append(System.lineSeparator())
            .append("[%collapsible]")
            .append(System.lineSeparator())
            .append("====")
            .append(System.lineSeparator())
            .append(buildTemplate(apiPath, template))
            .append(System.lineSeparator())
            .append("====")
            .append(System.lineSeparator())
            .append(System.lineSeparator())
    }
}