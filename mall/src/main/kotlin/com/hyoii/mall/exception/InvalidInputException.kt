package com.hyoii.mall.exception

class InvalidInputException(
    val fieldName: String = "",
    override val message: String = "Invalid Input"
) : RuntimeException(message)
