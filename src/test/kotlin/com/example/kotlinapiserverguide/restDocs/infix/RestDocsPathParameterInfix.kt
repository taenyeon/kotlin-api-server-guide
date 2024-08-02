package com.example.kotlinapiserverguide.restDocs.infix

import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.restdocs.request.RequestDocumentation

private fun createField(name: String, description: String): ParameterDescriptor {
    return RequestDocumentation.parameterWithName(name)
        .description(description)
}

infix fun String.means(description: String): ParameterDescriptor {
    return createField(this, description)
}