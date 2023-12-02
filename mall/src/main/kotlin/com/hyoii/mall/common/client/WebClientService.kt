package com.hyoii.mall.common.client

import arrow.core.left
import arrow.core.right
import com.hyoii.enums.MessageEnums
import com.hyoii.mall.exception.WebClientException
import com.hyoii.utils.JsonUtil.toMultiValueMap
import com.hyoii.utils.logger
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriBuilder
import reactor.core.publisher.Mono
import kotlin.reflect.KClass

/**
 * WebClient : 비동기/논블로킹을 지원하는 HTTP Request Client (API)
 */
@Service
class WebClientService(
     private val webClient: WebClient
) {
     // 기본적이 코덱 단 설정
     private val clientForCamelCase =
          ExchangeStrategies.builder().codecs {
               it.defaultCodecs().jackson2JsonEncoder(Jackson2JsonEncoder(webClientMapperCamel, MediaType.APPLICATION_JSON))
               it.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(webClientMapperCamel, MediaType.APPLICATION_JSON))
          }.build().let {
               webClient.mutate().exchangeStrategies(it).build()
          }

     private val clientForSnake =
          ExchangeStrategies.builder()
               .codecs {
                    it.defaultCodecs().jackson2JsonEncoder(Jackson2JsonEncoder(webClientMapperSnake, MediaType.APPLICATION_JSON))
                    it.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(webClientMapperSnake, MediaType.APPLICATION_JSON))
               }
               .build().let {
               webClient.mutate().exchangeStrategies(it).build()
          }

     suspend fun <T, R : Any> executePost(
          header: WebClientHeader,
          hostUri: HostUri,
          queryParams: Any? = null,
          requestBodyValue: T? = null,
          requestBodyFormData: Any? = null,
          responseKClass: KClass<R>
     ) =
          runCatching {
               clientForCamelCase.post()
                    .uri {it.builderFrom(hostUri, queryParams) }
                    .headers {
                         it.clientDefaultHeader(header)
                    }
                    .apply {
                         require(requestBodyValue != null || requestBodyFormData != null)
                         if (requestBodyValue != null ) {
                              body(BodyInserters.fromValue(requestBodyValue))
                         } else {
                              body(BodyInserters.fromFormData(requestBodyFormData.toCamelCaseMultiValue()))
                         }
                    }
                    .exchangeToMono { response ->
                         when (response.statusCode()) {
                              HttpStatus.OK -> response.bodyToMono(responseKClass.java)
                              else -> response.bodyToMono(String::class.java).flatMap {
                                   return@flatMap Mono.error(WebClientException(it))
                              }
                         }
                    }.awaitSingle().right()
          }.getOrElse {
               logger().error("[ERROR] executePost() msg : ${it.message}")
               WebClientError.ConnectionError.left()
          }

     fun HttpHeaders.clientDefaultHeader(header: WebClientHeader) {
          header.toMultiValueMap().getOrNull()?.let {
               addAll(it)
          }
          header.accessToken?.let {
               set(HttpHeaders.AUTHORIZATION, it)
          }
          this.accept = listOf(MediaType.APPLICATION_JSON)
     }

     fun UriBuilder.builderFrom(hostUri: HostUri, queryParams: Any?) =
          scheme("http")
               .host(hostUri.host)
               .port(hostUri.port)
               .path(hostUri.uri)
               .queryParams(queryParams.toCamelCaseMultiValue())
               .build()
}

class HostUri(
     val host: String,
     val port: String,
     val uri: String
)

sealed class WebClientError(
     val messageEnums: MessageEnums
) {
     data object ConnectionError : WebClientError(MessageEnums.WEB_CLIENT_ERROR)
     data class Unknown(val className: String, val message: String?) : WebClientError(MessageEnums.WEB_CLIENT_ERROR)
}
