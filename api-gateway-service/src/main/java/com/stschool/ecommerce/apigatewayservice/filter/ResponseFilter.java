package com.stschool.ecommerce.apigatewayservice.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stschool.ecommerce.apigatewayservice.dto.ErrorResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class ResponseFilter implements GlobalFilter, Ordered {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpResponse originalResponse = exchange.getResponse();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {

            @Override
            public Mono<Void> writeWith(org.reactivestreams.Publisher<? extends DataBuffer> body) {

                if (body instanceof Mono) {

                    Mono<? extends DataBuffer> monoBody = (Mono<? extends DataBuffer>) body;

                    return monoBody.flatMap(dataBuffer -> {

                        byte[] content = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(content);

                        HttpStatus statusCode = HttpStatus.valueOf(getStatusCode().value());

                        if (statusCode.isError()) {

                            String responseBody = new String(content, StandardCharsets.UTF_8);

                            log.error("Downstream service error: {} - {}", statusCode, responseBody);

                            ErrorResponseDto errorResponse = buildErrorResponse(
                                    statusCode, responseBody, exchange);

                            try {
                                byte[] bytes = objectMapper.writeValueAsBytes(errorResponse);

                                getHeaders().setContentType(MediaType.APPLICATION_JSON);
                                getHeaders().setContentLength(bytes.length);

                                return super.writeWith(
                                        Mono.just(bufferFactory().wrap(bytes))
                                );
                            } catch (Exception e) {
                                return Mono.error(e);
                            }
                        }

                        getHeaders().setContentType(MediaType.APPLICATION_JSON);
                        getHeaders().setContentLength(content.length);

                        return super.writeWith(
                                Mono.just(
                                        bufferFactory().wrap(content)
                                )
                        );
                    });
                }

                return super.writeWith(body);
            }
        };

        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    private ErrorResponseDto buildErrorResponse(HttpStatus statusCode, String responseBody, ServerWebExchange exchange) {

        String message = extractMessage(responseBody);
        String error = statusCode.getReasonPhrase();

        return ErrorResponseDto.builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .status(statusCode.value())
                .error(error)
                .message(message)
                .path(exchange.getRequest().getPath().value())
                .build();
    }

    private String extractMessage(String responseBody) {

        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            if (jsonNode.has("message")) {
                return jsonNode.get("message").asText();
            }

            if (jsonNode.has("error")) {
                return jsonNode.get("error").asText();
            }

            return responseBody;
        } catch (Exception e) {
            return responseBody;
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
