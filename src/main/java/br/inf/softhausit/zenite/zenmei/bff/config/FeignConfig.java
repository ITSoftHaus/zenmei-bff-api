package br.inf.softhausit.zenite.zenmei.bff.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Configuração global do Feign Client
 * <p>
 * Define configurações comuns para todos os clientes Feign, incluindo:
 * - Interceptadores para propagação de headers
 * - Decodificadores de erro personalizados
 * - Configuração de logging
 * </p>
 */
@Configuration
public class FeignConfig {

    /**
     * Interceptador para propagar headers importantes entre requisições
     * Inclui headers de autenticação e identificação do usuário
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();

                // Propagar Authorization header (JWT Token)
                String authorization = request.getHeader("Authorization");
                if (authorization != null && !authorization.isEmpty()) {
                    requestTemplate.header("Authorization", authorization);
                }

                // Propagar X-User-Id header
                String userId = request.getHeader("X-User-Id");
                if (userId != null && !userId.isEmpty()) {
                    requestTemplate.header("X-User-Id", userId);
                }

                // Propagar X-Request-Id para rastreamento distribuído
                String requestId = request.getHeader("X-Request-Id");
                if (requestId != null && !requestId.isEmpty()) {
                    requestTemplate.header("X-Request-Id", requestId);
                } else {
                    // Gerar um novo request ID se não existir
                    requestTemplate.header("X-Request-Id", java.util.UUID.randomUUID().toString());
                }

                // Propagar Content-Type
                String contentType = request.getHeader("Content-Type");
                if (contentType != null && !contentType.isEmpty()) {
                    requestTemplate.header("Content-Type", contentType);
                }
            }
        };
    }

    /**
     * Decodificador de erro personalizado para tratamento de erros HTTP
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomFeignErrorDecoder();
    }

    /**
     * Configuração de logging do Feign
     * BASIC: Log apenas método, URL, código de resposta e tempo de execução
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }
}
