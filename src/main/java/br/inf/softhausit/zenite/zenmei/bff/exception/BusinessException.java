package br.inf.softhausit.zenite.zenmei.bff.exception;

/**
 * Business exception for domain-specific errors.
 * This exception should not trigger circuit breaker or retry logic.
 */
public class BusinessException extends RuntimeException {
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
