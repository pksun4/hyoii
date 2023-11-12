package com.hyoii.mall.common.res

import com.hyoii.core.enums.MessageEnums

/**
 * 강의 기준
 */
data class BaseResponse<T>(
    val resultCode: String = MessageEnums.SUCCESS.code,
    val message: String = MessageEnums.SUCCESS.message,
    val data: T? = null
)
