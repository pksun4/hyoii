package com.hyoii.utils

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.util.LinkedMultiValueMap

object JsonUtil {
    private val mapper: ObjectMapper = jacksonObjectMapper().apply {
        // String -> LocalDate Mapping 하기 위한 설정
        registerModule(JavaTimeModule())
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        // 유연한 스키마 보장 (정의되지 않은 프로퍼티가 오더라도 실패하지 않도록)
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        // 필드명을 감싸는 문자가 없는 것 허용 {key: "value"}
        configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
    }

    fun <T> String.toObject(clazz: Class<T>): Either<JsonUtilError, T> =
        runCatching {
            mapper.readValue(this, clazz).right()
        }.getOrElse {
            logger().error("Object to json string convert error : {}", it.message)

            parseFailError
        }

    fun Any.toJson(): Either<JsonUtilError, String> =
        runCatching {
            mapper.writeValueAsString(this).right()
        }.getOrElse {
            logger().error("Object to json string convert error : {}", it.message)
            parseFailError
        }

    fun Any.toMultiValueMap() =
        runCatching {
            mapper.convertValue<Map<String, String>>(this).let {
                LinkedMultiValueMap<String, String>().apply {
                    setAll(it)
                }
            }.right()
        }.getOrElse {
            logger().error("Object to multiValueMap convert error : {}", it.message)
            parseFailError
        }

    private val parseFailError get() = JsonUtilError.ParseFail.left()
}

sealed interface JsonUtilError {
    data object ParseFail : JsonUtilError
}
