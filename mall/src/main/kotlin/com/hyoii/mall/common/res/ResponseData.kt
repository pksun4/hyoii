package com.hyoii.mall.common.res

import com.hyoii.core.enums.MessageEnums
import org.springframework.http.ResponseEntity

object ResponseData {
    fun fail(messageEnums: MessageEnums) = ResponseEntity.badRequest().body(ResponseFail(messageEnums.code, messageEnums.message))
    fun <T> success(response: T): ResponseEntity<ResponseSuccess<T>> = ResponseEntity.ok(ResponseSuccess(response))
}

class ResponseSuccess<T>(val data: T)

class ResponseFail(val errorCode: String, val errorMessage: String)
