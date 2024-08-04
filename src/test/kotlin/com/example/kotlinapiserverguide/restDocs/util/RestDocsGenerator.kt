package com.example.kotlinapiserverguide.restDocs.util

import com.example.kotlinapiserverguide.common.http.constant.ResponseCode
import com.example.kotlinapiserverguide.restDocs.constant.DocsApi
import com.example.kotlinapiserverguide.restDocs.constant.DocsApiType
import java.io.File
import java.io.FileOutputStream

class RestDocsGenerator {

    private val defaultUri: String = "localhost:8080"

    private var snippetDefaultPathParameter: String = "{snippets}"
    private val snippetPath: String = "build/generated-snippets"

    private var pathParameterTemplate: String = "path-parameters.adoc"
    private var httpRequestTemplate: String = "http-request.adoc"
    private var requestFieldsTemplate: String = "request-fields.adoc"
    private var httpResponseTemplate: String = "http-response.adoc"
    private var responseFieldsTemplate: String = "response-fields.adoc"
    private val curlRequestTemplate: String = "curl-request.adoc"
    private val URITemplate:String = "httpie-request.adoc"

    fun generateTemplate() {
        val docs: File = File("src/docs/asciidoc/apiDocs.adoc")

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
            .append(":source-highlighter: rouge")
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
                    .filter { File("$snippetPath${it.path}").exists() }
                    .forEach { api ->

                        stringBuilder
                            .append(buildHeader(api.title, 3))

                            .append(System.lineSeparator())
                            .append(".👀 ${api.description}")

                            .append(System.lineSeparator())
                            .append(System.lineSeparator())

                        stringBuilder
                            .append(System.lineSeparator())
                            .append(System.lineSeparator())
                        buildURITemplate(stringBuilder, api.path)

                        stringBuilder
                            .append("{nbsp} +")
                            .append(System.lineSeparator())
                            .append(buildHighlight("Request"))
                            .append(System.lineSeparator())
                            .append(System.lineSeparator())
                        buildCurlRequestTemplate(stringBuilder, api.path)

                        buildPathParameterTemplate(stringBuilder, api.path)

                        buildRequestFieldsTemplate(stringBuilder, api.path)

                        buildRequestSampleTemplate(stringBuilder, api.path)

                        stringBuilder
                            .append("{nbsp} +")
                            .append(System.lineSeparator())
                            .append(buildHighlight("Response"))
                            .append(System.lineSeparator())
                            .append(System.lineSeparator())

                        buildResponseFieldsTemplate(stringBuilder, api.path)

                        buildResponseSampleTemplate(stringBuilder, api.path)
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
            .append("\t|*access_token*")
            .append(System.lineSeparator())
            .append("\t|JWT accessToken")
            .append(System.lineSeparator())
            .append("\t|")
            .append(System.lineSeparator())
            .append(System.lineSeparator())
            .append("\t|*refresh_token*")
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
                .append("\t|*${it.code}*")
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

    private fun buildURITemplate(stringBuilder: StringBuilder, directoryName: String): StringBuilder{
        val apiPath = "$snippetDefaultPathParameter$directoryName"
        val snippetDir = "$snippetPath${directoryName}/"
        if (isTemplateExist(snippetDir, URITemplate)) {
            stringBuilder.append(File(snippetDir + URITemplate).readText())
            stringBuilder.append(" $defaultUri$directoryName HTTP/1.1")
            stringBuilder.append(System.lineSeparator())
            stringBuilder.append(System.lineSeparator())
            stringBuilder.append("----")
            stringBuilder.append(System.lineSeparator())
            stringBuilder.append(System.lineSeparator())
        }
        return stringBuilder
    }

    private fun buildPathParameterTemplate(stringBuilder: StringBuilder, directoryName: String): StringBuilder {
        val snippet = "$snippetDefaultPathParameter$directoryName"
        val snippetDir = "$snippetPath${directoryName}/"

        if (isTemplateExist(snippetDir, pathParameterTemplate)) buildTemplate(
            stringBuilder,
            "path parameter",
            snippet,
            pathParameterTemplate
        )

        return stringBuilder
    }

    private fun buildRequestFieldsTemplate(stringBuilder: StringBuilder, directoryName: String): StringBuilder {
        val apiPath = "$snippetDefaultPathParameter$directoryName"
        val snippetDir = "$snippetPath${directoryName}/"

        if (isTemplateExist(snippetDir, requestFieldsTemplate)) buildTemplate(
            stringBuilder,
            "request body",
            apiPath,
            requestFieldsTemplate
        )

        return stringBuilder
    }

    private fun buildRequestSampleTemplate(stringBuilder: StringBuilder, directoryName: String): StringBuilder {
        val apiPath = "$snippetDefaultPathParameter$directoryName"
        val snippetDir = "$snippetPath${directoryName}/"

        if (isTemplateExist(snippetDir, httpRequestTemplate)) buildSampleTemplate(
            stringBuilder,
            "sample",
            apiPath,
            httpRequestTemplate
        )

        return stringBuilder
    }

    private fun buildResponseFieldsTemplate(stringBuilder: StringBuilder, directoryName: String): StringBuilder {
        val apiPath = "$snippetDefaultPathParameter$directoryName"
        val snippetDir = "$snippetPath${directoryName}/"

        if (isTemplateExist(snippetDir, responseFieldsTemplate)) buildTemplate(
            stringBuilder,
            "response body",
            apiPath,
            responseFieldsTemplate
        )

        return stringBuilder
    }

    private fun buildResponseSampleTemplate(stringBuilder: StringBuilder, directoryName: String): StringBuilder {
        val apiPath = "$snippetDefaultPathParameter$directoryName"
        val snippetDir = "$snippetPath${directoryName}/"

        if (isTemplateExist(snippetDir, httpResponseTemplate)) buildSampleTemplate(
            stringBuilder,
            "sample",
            apiPath,
            httpResponseTemplate
        )

        return stringBuilder
    }

    private fun buildCurlRequestTemplate(stringBuilder: StringBuilder, directoryName: String): StringBuilder {
        val apiPath = "$snippetDefaultPathParameter$directoryName"
        val snippetDir = "$snippetPath${directoryName}/"
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