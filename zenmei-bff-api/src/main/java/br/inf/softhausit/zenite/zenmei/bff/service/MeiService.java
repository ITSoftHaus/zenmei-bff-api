package br.inf.softhausit.zenite.zenmei.bff.service;

import br.inf.softhausit.zenite.zenmei.bff.client.MeiClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

/**
 * Service para integração com MEI API
 * <p>
 * Implementa Circuit Breaker e Retry patterns para resiliência
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MeiService {

    private final MeiClient meiClient;

    @CircuitBreaker(name = "meiService", fallbackMethod = "listarMeisFallback")
    @Retry(name = "meiService")
    public ResponseEntity<?> listarMeis(UUID userId) {
        log.debug("Listando MEIs para userId: {}", userId);
        return meiClient.listarMeis(userId);
    }

    @CircuitBreaker(name = "meiService", fallbackMethod = "buscarMeiFallback")
    @Retry(name = "meiService")
    public ResponseEntity<?> buscarMei(UUID id) {
        log.debug("Buscando MEI: {}", id);
        return meiClient.buscarMei(id);
    }

    @CircuitBreaker(name = "meiService", fallbackMethod = "criarMeiFallback")
    @Retry(name = "meiService")
    public ResponseEntity<?> criarMei(Object mei) {
        log.debug("Criando MEI");
        return meiClient.criarMei(mei);
    }

    @CircuitBreaker(name = "meiService", fallbackMethod = "atualizarMeiFallback")
    @Retry(name = "meiService")
    public ResponseEntity<?> atualizarMei(UUID id, Object mei) {
        log.debug("Atualizando MEI: {}", id);
        return meiClient.atualizarMei(id, mei);
    }

    @CircuitBreaker(name = "meiService", fallbackMethod = "deletarMeiFallback")
    @Retry(name = "meiService")
    public ResponseEntity<Void> deletarMei(UUID id) {
        log.debug("Deletando MEI: {}", id);
        return meiClient.deletarMei(id);
    }

    // Fallback methods
    private ResponseEntity<?> listarMeisFallback(UUID userId, Exception e) {
        log.error("Fallback: Erro ao listar MEIs", e);
        return ResponseEntity.ok(Collections.emptyList());
    }

    private ResponseEntity<?> buscarMeiFallback(UUID id, Exception e) {
        log.error("Fallback: Erro ao buscar MEI {}", id, e);
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> criarMeiFallback(Object mei, Exception e) {
        log.error("Fallback: Erro ao criar MEI", e);
        return ResponseEntity.status(503).build();
    }

    private ResponseEntity<?> atualizarMeiFallback(UUID id, Object mei, Exception e) {
        log.error("Fallback: Erro ao atualizar MEI {}", id, e);
        return ResponseEntity.status(503).build();
    }

    private ResponseEntity<Void> deletarMeiFallback(UUID id, Exception e) {
        log.error("Fallback: Erro ao deletar MEI {}", id, e);
        return ResponseEntity.status(503).build();
    }
}
