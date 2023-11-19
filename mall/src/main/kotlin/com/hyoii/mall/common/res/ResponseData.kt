package com.hyoii.mall.common.res

import com.hyoii.enums.MessageEnums
import org.springframework.http.ResponseEntity

object ResponseData {
    fun fail(messageEnums: MessageEnums) = ResponseEntity.badRequest().body(ResponseFail(messageEnums.code, messageEnums.message))
    fun <T> success(response: T): ResponseEntity<ResponseSuccess<T>> = ResponseEntity.ok(ResponseSuccess(response, MessageEnums.SUCCESS.code, MessageEnums.SUCCESS.message))
}

class ResponseSuccess<T>(val data: T, val code: String, val message: String)

class ResponseFail(val errorCode: String, val errorMessage: String)

class ResponseException<T> (val errorCode: String = MessageEnums.ERROR.code, val errorMessage: String = MessageEnums.ERROR.message, val error: T? = null)
