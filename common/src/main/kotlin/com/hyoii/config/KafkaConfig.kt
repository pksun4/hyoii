package com.hyoii.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import java.net.InetAddress
import java.util.UUID

//@Configuration
class KafkaConfig(
    @Value("\${spring.kafka.host}")
    private val host: String
) {

    @Bean
    @Primary
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> =
        ConcurrentKafkaListenerContainerFactory<String, String>().also {
            it.consumerFactory = consumeFactory()
        }

    @Bean
    @Primary
    fun consumeFactory(): ConsumerFactory<String, String> = DefaultKafkaConsumerFactory(consumerProperties())

    @Bean
    fun consumerProperties(): Map<String, Any> {
        val hostName = InetAddress.getLocalHost().hostName + UUID.randomUUID().toString()
        return hashMapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to host,
            ConsumerConfig.GROUP_ID_CONFIG to hostName,
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to "true",
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "latest",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java
        )
    }

}
