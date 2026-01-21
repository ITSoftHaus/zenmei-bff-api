package br.inf.softhausit.zenite.zenmei.bff.service;

import br.inf.softhausit.zenite.zenmei.bff.client.NotaFiscalClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

/**
 * Service para integração com Nota Fiscal API
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotaFiscalService {

    private final NotaFiscalClient notaFiscalClient;

    @CircuitBreaker(name = "notaService", fallbackMethod = "listarNotasFallback")
    @Retry(name = "notaService")
    public ResponseEntity<?> listarNotas(UUID userId) {
        log.debug("Listando notas fiscais para userId: {}", userId);
        return notaFiscalClient.listarNotas(userId);
    }

    @CircuitBreaker(name = "notaService", fallbackMethod = "buscarNotaFallback")
    @Retry(name = "notaService")
    public ResponseEntity<?> buscarNota(UUID id) {
        log.debug("Buscando nota fiscal: {}", id);
        return notaFiscalClient.buscarNota(id);
    }

    @CircuitBreaker(name = "notaService", fallbackMethod = "criarNotaFallback")
    @Retry(name = "notaService")
    public ResponseEntity<?> criarNota(Object nota, UUID userId) {
        log.debug("Criando nota fiscal");
        return notaFiscalClient.criarNota(nota, userId);
    }

    @CircuitBreaker(name = "notaService", fallbackMethod = "atualizarNotaFallback")
    @Retry(name = "notaService")
    public ResponseEntity<?> atualizarNota(UUID id, Object nota) {
        log.debug("Atualizando nota fiscal: {}", id);
        return notaFiscalClient.atualizarNota(id, nota);
    }

    @CircuitBreaker(name = "notaService", fallbackMethod = "deletarNotaFallback")
    @Retry(name = "notaService")
    public ResponseEntity<Void> deletarNota(UUID id) {
        log.debug("Deletando nota fiscal: {}", id);
        return notaFiscalClient.deletarNota(id);
    }

    @CircuitBreaker(name = "notaService", fallbackMethod = "emitirNotaFallback")
    @Retry(name = "notaService")
    public ResponseEntity<?> emitirNota(UUID id) {
        log.debug("Emitindo nota fiscal: {}", id);
        return notaFiscalClient.emitirNota(id);
    }

    // Fallback methods
    private ResponseEntity<?> listarNotasFallback(UUID userId, Exception e) {
        log.error("Fallback: Erro ao listar notas fiscais", e);
        return ResponseEntity.ok(Collections.emptyList());
    }

    private ResponseEntity<?> buscarNotaFallback(UUID id, Exception e) {
        log.error("Fallback: Erro ao buscar nota fiscal {}", id, e);
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> criarNotaFallback(Object nota, UUID userId, Exception e) {
        log.error("Fallback: Erro ao criar nota fiscal", e);
        return ResponseEntity.status(503).build();
    }

    private ResponseEntity<?> atualizarNotaFallback(UUID id, Object nota, Exception e) {
        log.error("Fallback: Erro ao atualizar nota fiscal {}", id, e);
        return ResponseEntity.status(503).build();
    }

    private ResponseEntity<Void> deletarNotaFallback(UUID id, Exception e) {
        log.error("Fallback: Erro ao deletar nota fiscal {}", id, e);
        return ResponseEntity.status(503).build();
    }

    private ResponseEntity<?> emitirNotaFallback(UUID id, Exception e) {
        log.error("Fallback: Erro ao emitir nota fiscal {}", id, e);
        return ResponseEntity.status(503).build();
    }
}
