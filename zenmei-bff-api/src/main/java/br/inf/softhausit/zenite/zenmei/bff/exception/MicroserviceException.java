package br.inf.softhausit.zenite.zenmei.bff.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Exceção personalizada para erros de comunicação com microserviços
 */
@Getter
public class MicroserviceException extends RuntimeException {

    private final String serviceName;
    private final HttpStatus status;
    private final String details;

    public MicroserviceException(String serviceName, HttpStatus status, String details) {
        super(String.format("Error in %s: %s", serviceName, details));
        this.serviceName = serviceName;
        this.status = status;
        this.details = details;
    }

    public MicroserviceException(String serviceName, String message) {
        super(String.format("Error in %s: %s", serviceName, message));
        this.serviceName = serviceName;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.details = message;
    }
}
