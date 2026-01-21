package br.inf.softhausit.zenite.zenmei.bff.service;

import br.inf.softhausit.zenite.zenmei.bff.client.ChamadoClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

/**
 * Service para integração com Chamado API
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChamadoService {

    private final ChamadoClient chamadoClient;

    @CircuitBreaker(name = "chamadoService", fallbackMethod = "listarChamadosFallback")
    @Retry(name = "chamadoService")
    public ResponseEntity<?> listarChamados(UUID userId) {
        log.debug("Listando chamados para userId: {}", userId);
        return chamadoClient.listarChamados(userId);
    }

    @CircuitBreaker(name = "chamadoService", fallbackMethod = "buscarChamadoFallback")
    @Retry(name = "chamadoService")
    public ResponseEntity<?> buscarChamado(UUID id) {
        log.debug("Buscando chamado: {}", id);
        return chamadoClient.buscarChamado(id);
    }

    @CircuitBreaker(name = "chamadoService", fallbackMethod = "criarChamadoFallback")
    @Retry(name = "chamadoService")
    public ResponseEntity<?> criarChamado(Object chamado, UUID userId) {
        log.debug("Criando chamado");
        return chamadoClient.criarChamado(chamado, userId);
    }

    @CircuitBreaker(name = "chamadoService", fallbackMethod = "atualizarChamadoFallback")
    @Retry(name = "chamadoService")
    public ResponseEntity<?> atualizarChamado(UUID id, Object chamado) {
        log.debug("Atualizando chamado: {}", id);
        return chamadoClient.atualizarChamado(id, chamado);
    }

    @CircuitBreaker(name = "chamadoService", fallbackMethod = "deletarChamadoFallback")
    @Retry(name = "chamadoService")
    public ResponseEntity<Void> deletarChamado(UUID id) {
        log.debug("Deletando chamado: {}", id);
        return chamadoClient.deletarChamado(id);
    }

    // Fallback methods
    private ResponseEntity<?> listarChamadosFallback(UUID userId, Exception e) {
        log.error("Fallback: Erro ao listar chamados", e);
        return ResponseEntity.ok(Collections.emptyList());
    }

    private ResponseEntity<?> buscarChamadoFallback(UUID id, Exception e) {
        log.error("Fallback: Erro ao buscar chamado {}", id, e);
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> criarChamadoFallback(Object chamado, UUID userId, Exception e) {
        log.error("Fallback: Erro ao criar chamado", e);
        return ResponseEntity.status(503).build();
    }

    private ResponseEntity<?> atualizarChamadoFallback(UUID id, Object chamado, Exception e) {
        log.error("Fallback: Erro ao atualizar chamado {}", id, e);
        return ResponseEntity.status(503).build();
    }

    private ResponseEntity<Void> deletarChamadoFallback(UUID id, Exception e) {
        log.error("Fallback: Erro ao deletar chamado {}", id, e);
        return ResponseEntity.status(503).build();
    }
}
