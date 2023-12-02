package com.hyoii.mall.common.client

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoii.enums.MessageEnums
import com.hyoii.mall.exception.WebClientException
import com.hyoii.utils.logger
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

val webClientMapperCamel = jacksonObjectMapper().apply {
    propertyNamingStrategy = PropertyNamingStrategies.LOWER_CAMEL_CASE
    setSerializationInclusion(JsonInclude.Include.NON_NULL) // NULL 제외
}

val webClientMapperSnake = jacksonObjectMapper().apply {
    propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
    setSerializationInclusion(JsonInclude.Include.NON_NULL) // NULL 제외
}

fun Any?.toCamelCaseMultiValue(
    defaultMap: MultiValueMap<String, String> = LinkedMultiValueMap()
) =
    runCatching {
        this?.let {
            webClientMapperCamel.convertValue<Map<String, String>>(this).let {
                LinkedMultiValueMap<String, String>().apply {
                    setAll(it)
                }
            }
        } ?: defaultMap
    }.getOrElse {
        logger().error(it.message)
        throw WebClientException(MessageEnums.WEB_CLIENT_ERROR.message)
    }

fun Any?.toSnakeCaseMultiValueMap(
    defaultMap: MultiValueMap<String, String> = LinkedMultiValueMap()
) =
    runCatching {
        this?.let {
            webClientMapperSnake.convertValue<Map<String, String>>(this).let {
                LinkedMultiValueMap<String, String>().apply {
                    setAll(it)
                }
            }
        } ?: defaultMap
    }.getOrElse {
        logger().error(it.message)
        throw WebClientException(MessageEnums.WEB_CLIENT_ERROR.message)
    }
