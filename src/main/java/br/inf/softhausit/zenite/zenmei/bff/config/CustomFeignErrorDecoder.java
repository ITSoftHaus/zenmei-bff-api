package br.inf.softhausit.zenite.zenmei.bff.config;

import br.inf.softhausit.zenite.zenmei.bff.exception.MicroserviceException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Decodificador de erro personalizado para Feign
 * <p>
 * Converte erros HTTP dos microserviços em exceções personalizadas
 * para melhor tratamento e propagação de erros no BFF
 * </p>
 */
@Slf4j
public class CustomFeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus status = HttpStatus.valueOf(response.status());
        String serviceName = extractServiceName(methodKey);
        String errorMessage = extractErrorMessage(response);

        log.error("Error calling {}: Status={}, Message={}",
                  serviceName, status, errorMessage);

        // Tratar erros específicos
        return switch (status) {
            case NOT_FOUND -> new MicroserviceException(
                serviceName,
                status,
                "Recurso não encontrado: " + errorMessage
            );
            case BAD_REQUEST -> new MicroserviceException(
                serviceName,
                status,
                "Requisição inválida: " + errorMessage
            );
            case UNAUTHORIZED -> new MicroserviceException(
                serviceName,
                status,
                "Não autorizado: " + errorMessage
            );
            case FORBIDDEN -> new MicroserviceException(
                serviceName,
                status,
                "Acesso negado: " + errorMessage
            );
            case INTERNAL_SERVER_ERROR -> new MicroserviceException(
                serviceName,
                status,
                "Erro interno no serviço: " + errorMessage
            );
            case SERVICE_UNAVAILABLE -> new MicroserviceException(
                serviceName,
                status,
                "Serviço temporariamente indisponível: " + errorMessage
            );
            default -> new MicroserviceException(
                serviceName,
                status,
                "Erro ao chamar serviço: " + errorMessage
            );
        };
    }

    /**
     * Extrai o nome do serviço a partir da chave do método Feign
     */
    private String extractServiceName(String methodKey) {
        if (methodKey != null && methodKey.contains("#")) {
            return methodKey.substring(0, methodKey.indexOf("#"));
        }
        return "UnknownService";
    }

    /**
     * Extrai a mensagem de erro do corpo da resposta
     */
    private String extractErrorMessage(Response response) {
        try {
            if (response.body() != null) {
                byte[] bodyData = response.body().asInputStream().readAllBytes();
                return new String(bodyData, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.warn("Não foi possível ler o corpo da resposta de erro", e);
        }
        return response.reason();
    }
}
