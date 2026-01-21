package br.inf.softhausit.zenite.zenmei.bff.service;

import br.inf.softhausit.zenite.zenmei.bff.client.DespesaClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

/**
 * Service para integração com Despesa API
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DespesaService {

    private final DespesaClient despesaClient;

    @CircuitBreaker(name = "despesaService", fallbackMethod = "listarDespesasFallback")
    @Retry(name = "despesaService")
    public ResponseEntity<?> listarDespesas(UUID userId) {
        log.debug("Listando despesas para userId: {}", userId);
        return despesaClient.listarDespesas(userId);
    }

    @CircuitBreaker(name = "despesaService", fallbackMethod = "buscarDespesaFallback")
    @Retry(name = "despesaService")
    public ResponseEntity<?> buscarDespesa(UUID id) {
        log.debug("Buscando despesa: {}", id);
        return despesaClient.buscarDespesa(id);
    }

    @CircuitBreaker(name = "despesaService", fallbackMethod = "criarDespesaFallback")
    @Retry(name = "despesaService")
    public ResponseEntity<?> criarDespesa(Object despesa, UUID userId) {
        log.debug("Criando despesa");
        return despesaClient.criarDespesa(despesa, userId);
    }

    @CircuitBreaker(name = "despesaService", fallbackMethod = "atualizarDespesaFallback")
    @Retry(name = "despesaService")
    public ResponseEntity<?> atualizarDespesa(UUID id, Object despesa) {
        log.debug("Atualizando despesa: {}", id);
        return despesaClient.atualizarDespesa(id, despesa);
    }

    @CircuitBreaker(name = "despesaService", fallbackMethod = "deletarDespesaFallback")
    @Retry(name = "despesaService")
    public ResponseEntity<Void> deletarDespesa(UUID id) {
        log.debug("Deletando despesa: {}", id);
        return despesaClient.deletarDespesa(id);
    }

    // Fallback methods
    private ResponseEntity<?> listarDespesasFallback(UUID userId, Exception e) {
        log.error("Fallback: Erro ao listar despesas", e);
        return ResponseEntity.ok(Collections.emptyList());
    }

    private ResponseEntity<?> buscarDespesaFallback(UUID id, Exception e) {
        log.error("Fallback: Erro ao buscar despesa {}", id, e);
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> criarDespesaFallback(Object despesa, UUID userId, Exception e) {
        log.error("Fallback: Erro ao criar despesa", e);
        return ResponseEntity.status(503).build();
    }

    private ResponseEntity<?> atualizarDespesaFallback(UUID id, Object despesa, Exception e) {
        log.error("Fallback: Erro ao atualizar despesa {}", id, e);
        return ResponseEntity.status(503).build();
    }

    private ResponseEntity<Void> deletarDespesaFallback(UUID id, Exception e) {
        log.error("Fallback: Erro ao deletar despesa {}", id, e);
        return ResponseEntity.status(503).build();
    }
}
