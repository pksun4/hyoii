package com.hyoii.mall.common.res

import com.hyoii.enums.MessageEnums
import org.springframework.http.ResponseEntity

object ResponseData {
    fun fail(messageEnums: MessageEnums) = ResponseEntity.badRequest().body(ResponseFail(messageEnums.code, messageEnums.message))
    fun <T> success(response: T): ResponseEntity<ResponseSuccess<T>> = ResponseEntity.ok(ResponseSuccess(MessageEnums.SUCCESS.code, MessageEnums.SUCCESS.message, response))
}

class ResponseSuccess<T>(val code: String, val message: String, val data: T)

class ResponseFail(val code: String, val message: String)

class ResponseException<T> (val code: String = MessageEnums.ERROR.code, val message: String = MessageEnums.ERROR.message, val error: T? = null)
