package com.hyoii.mall.exception

class WebClientException(
    override val message: String?
) : RuntimeException(message)
