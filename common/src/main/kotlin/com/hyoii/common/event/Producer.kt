package com.hyoii.common.event

import com.hyoii.utils.logger
import org.springframework.kafka.core.KafkaTemplate

class Producer(
    private val kafkaTemplate: KafkaTemplate<String, String>
) {

    private val topicName: String = "sample-topic"
    fun sendMessage(message: String) =
        runCatching {
            this.kafkaTemplate.send(topicName, message)
        }.getOrElse {
            logger().error("Kafka producer sendMessage() : " + it.message)
        }
}
