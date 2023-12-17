package com.hyoii.common.event

import com.hyoii.utils.logger
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload

class Consumer{

    @KafkaListener(topics = ["new-topic"])
    fun consume(@Payload message: String) =
        runCatching {
            logger().info("message: $message")
        }

}
