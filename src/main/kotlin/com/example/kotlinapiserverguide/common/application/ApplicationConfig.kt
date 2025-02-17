package com.example.kotlinapiserverguide.common.application

import com.example.kotlinapiserverguide.common.filter.LoggingFilter
import com.example.kotlinapiserverguide.common.paging.QueryStringArgumentResolver
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Configuration
class ApplicationConfig(private val queryString: QueryStringArgumentResolver) : WebMvcConfigurer {


    @Bean
    fun filterRegistrationBean(): FilterRegistrationBean<LoggingFilter> {
        val filterRegistrationBean = FilterRegistrationBean(LoggingFilter())
        filterRegistrationBean.order = Int.MIN_VALUE
        return filterRegistrationBean
    }

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.add(queryString)
    }

    @Bean
    fun objectMapper(): ObjectMapper {

        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")

        val javaTimeModule = JavaTimeModule()

        javaTimeModule.addSerializer(LocalDateTime::class.java, CustomLocalDateTimeSerializer(formatter))
        javaTimeModule.addDeserializer(LocalDateTime::class.java, CustomLocalDateTimeDeserializer(formatter))

        val objectMapper = ObjectMapper()

        objectMapper.registerModules(javaTimeModule)
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return objectMapper
    }

    class CustomLocalDateTimeSerializer(private val formatter: DateTimeFormatter) : JsonSerializer<LocalDateTime>() {
        override fun serialize(value: LocalDateTime?, gen: JsonGenerator?, serializers: SerializerProvider?) {
            gen?.writeString(value?.format(formatter))
        }
    }

    class CustomLocalDateTimeDeserializer(private val formatter: DateTimeFormatter) :
        JsonDeserializer<LocalDateTime>() {
        override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): LocalDateTime {
            val dateString = p?.text
            return LocalDateTime.parse(dateString!!, formatter)
        }
    }

}