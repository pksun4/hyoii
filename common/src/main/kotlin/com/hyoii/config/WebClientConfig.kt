package com.hyoii.config

import com.hyoii.utils.logger
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.LoggingCodecSupport
import org.springframework.web.reactive.function.client.*
import org.springframework.web.util.DefaultUriBuilderFactory
import reactor.core.publisher.Mono
import reactor.netty.Connection
import reactor.netty.ConnectionObserver
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.time.Duration
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds

@Configuration
class WebClientConfig : ConnectionObserver {

    @Bean
    fun loadWebClientConfiguration() = WebClientProperties()

    @Bean
    fun webClient(@Qualifier("loadWebClientConfiguration") webClientProperties: WebClientProperties): WebClient? {
        val exchangeStrategies = ExchangeStrategies.builder().codecs { configurer ->
            configurer.defaultCodecs().maxInMemorySize(1024 * webClientProperties.maxInMemorySize)
        }.build()

        exchangeStrategies.messageWriters().filterIsInstance<LoggingCodecSupport>()
            .forEach { it.isEnableLoggingRequestDetails = true }

        val httpClient = ConnectionProvider.builder("aod-http-client").apply {
            maxConnections(webClientProperties.maxConnections)
            pendingAcquireTimeout(Duration.ofSeconds(webClientProperties.pendingAcquireTimeout))
            pendingAcquireMaxCount(webClientProperties.pendingAcquireMaxCount)
            maxIdleTime(Duration.ofSeconds(webClientProperties.maxIdleTime.seconds.inWholeSeconds))
            maxLifeTime(Duration.ofSeconds(webClientProperties.maxLifeTime.seconds.inWholeSeconds))
        }.build().let {
            HttpClient.create(it).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, webClientProperties.connectChannelOptionTimeout)
                .doOnConnected { conn ->
                    conn.apply {
                        addHandlerLast(ReadTimeoutHandler(webClientProperties.connectReadTimeout, TimeUnit.MILLISECONDS))
                        addHandlerLast(WriteTimeoutHandler(webClientProperties.connectWriteTimeout, TimeUnit.MILLISECONDS))
                    }
                }
                .responseTimeout(Duration.ofSeconds(webClientProperties.responseTimeout.seconds.inWholeSeconds))
                .observe(this)
        }

        return WebClient.builder().clientConnector(ReactorClientHttpConnector(httpClient)).exchangeStrategies(exchangeStrategies)
            .filter(
                ExchangeFilterFunction.ofRequestProcessor { clientRequest: ClientRequest ->
                    Mono.just(clientRequest)
                }
            ).filter(
                ExchangeFilterFunction.ofResponseProcessor { clientResponse: ClientResponse ->
                    Mono.just(clientResponse)
                }
            ).uriBuilderFactory(
                DefaultUriBuilderFactory().apply {
                    encodingMode = DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY
                }
            ).defaultRequest {
                it.accept(MediaType.APPLICATION_JSON)
            }
            .build()
    }

    override fun onStateChange(connection: Connection, newState: ConnectionObserver.State) {
        logger().info("WebClient State Change: connection=$connection, newState=$newState")
    }

}

data class WebClientProperties(
    val maxInMemorySize: Int = 10,
    val maxConnections: Int = 10,
    val pendingAcquireTimeout: Long = 30L,
    val pendingAcquireMaxCount: Int = 30,
    val maxIdleTime: Long = 30L,
    val maxLifeTime: Long = 30L,
    val responseTimeout: Long = 30L,
    val connectReadTimeout: Long = 30L,
    val connectWriteTimeout: Long = 30L,
    val connectChannelOptionTimeout: Int = 30
)
