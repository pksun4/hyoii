package com.hyoii.mall.common.client

import com.fasterxml.jackson.annotation.JsonProperty

class WebClientHeader(
    @JsonProperty("Authorization")
    val accessToken: String? = null
)
