package br.inf.softhausit.zenite.zenmei.bff.service;

import br.inf.softhausit.zenite.zenmei.bff.client.CnaeClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

/**
 * Service para integração com CNAE API
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CnaeService {

    private final CnaeClient cnaeClient;

    @CircuitBreaker(name = "cnaeService", fallbackMethod = "listarMeiCnaesFallback")
    @Retry(name = "cnaeService")
    public ResponseEntity<?> listarMeiCnaes(UUID userId) {
        log.debug("Listando CNAEs MEI para userId: {}", userId);
        return cnaeClient.listarMeiCnaes(userId);
    }

    @CircuitBreaker(name = "cnaeService", fallbackMethod = "listarLc116Fallback")
    @Retry(name = "cnaeService")
    public ResponseEntity<?> listarLc116() {
        log.debug("Listando LC116");
        return cnaeClient.listarLc116();
    }

    @CircuitBreaker(name = "cnaeService", fallbackMethod = "buscarLc116PorCodigoFallback")
    @Retry(name = "cnaeService")
    public ResponseEntity<?> buscarLc116PorCodigo(String codigoLc116) {
        log.debug("Buscando LC116: {}", codigoLc116);
        return cnaeClient.buscarLc116PorCodigo(codigoLc116);
    }

    @CircuitBreaker(name = "cnaeService", fallbackMethod = "listarLc116PorCnaeFallback")
    @Retry(name = "cnaeService")
    public ResponseEntity<?> listarLc116PorCnae(String codigoCnae) {
        log.debug("Listando LC116 por CNAE: {}", codigoCnae);
        return cnaeClient.listarLc116PorCnae(codigoCnae);
    }

    @CircuitBreaker(name = "cnaeService", fallbackMethod = "listarCnaesFallback")
    @Retry(name = "cnaeService")
    public ResponseEntity<?> listarCnaes() {
        log.debug("Listando todos os CNAEs");
        return cnaeClient.listarCnaes();
    }

    @CircuitBreaker(name = "cnaeService", fallbackMethod = "buscarCnaePorCodigoFallback")
    @Retry(name = "cnaeService")
    public ResponseEntity<?> buscarCnaePorCodigo(String codigoCnae) {
        log.debug("Buscando CNAE: {}", codigoCnae);
        return cnaeClient.buscarCnaePorCodigo(codigoCnae);
    }

    // Fallback methods
    private ResponseEntity<?> listarMeiCnaesFallback(UUID userId, Exception e) {
        log.error("Fallback: Erro ao listar CNAEs MEI", e);
        return ResponseEntity.ok(Collections.emptyList());
    }

    private ResponseEntity<?> listarLc116Fallback(Exception e) {
        log.error("Fallback: Erro ao listar LC116", e);
        return ResponseEntity.ok(Collections.emptyList());
    }

    private ResponseEntity<?> buscarLc116PorCodigoFallback(String codigoLc116, Exception e) {
        log.error("Fallback: Erro ao buscar LC116 {}", codigoLc116, e);
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> listarLc116PorCnaeFallback(String codigoCnae, Exception e) {
        log.error("Fallback: Erro ao listar LC116 por CNAE {}", codigoCnae, e);
        return ResponseEntity.ok(Collections.emptyList());
    }

    private ResponseEntity<?> listarCnaesFallback(Exception e) {
        log.error("Fallback: Erro ao listar CNAEs", e);
        return ResponseEntity.ok(Collections.emptyList());
    }

    private ResponseEntity<?> buscarCnaePorCodigoFallback(String codigoCnae, Exception e) {
        log.error("Fallback: Erro ao buscar CNAE {}", codigoCnae, e);
        return ResponseEntity.notFound().build();
    }
}
