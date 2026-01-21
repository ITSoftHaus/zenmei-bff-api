package br.inf.softhausit.zenite.zenmei.bff.service;

import br.inf.softhausit.zenite.zenmei.bff.client.AgendaClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

/**
 * Service para integração com Agenda API
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AgendaService {

    private final AgendaClient agendaClient;

    @CircuitBreaker(name = "agendaService", fallbackMethod = "listarCompromissosFallback")
    @Retry(name = "agendaService")
    public ResponseEntity<?> listarCompromissos(UUID userId) {
        log.debug("Listando compromissos para userId: {}", userId);
        return agendaClient.listarCompromissos(userId);
    }

    @CircuitBreaker(name = "agendaService", fallbackMethod = "buscarCompromissoFallback")
    @Retry(name = "agendaService")
    public ResponseEntity<?> buscarCompromisso(UUID id) {
        log.debug("Buscando compromisso: {}", id);
        return agendaClient.buscarCompromisso(id);
    }

    @CircuitBreaker(name = "agendaService", fallbackMethod = "criarCompromissoFallback")
    @Retry(name = "agendaService")
    public ResponseEntity<?> criarCompromisso(Object compromisso, UUID userId) {
        log.debug("Criando compromisso");
        return agendaClient.criarCompromisso(compromisso, userId);
    }

    @CircuitBreaker(name = "agendaService", fallbackMethod = "atualizarCompromissoFallback")
    @Retry(name = "agendaService")
    public ResponseEntity<?> atualizarCompromisso(UUID id, Object compromisso) {
        log.debug("Atualizando compromisso: {}", id);
        return agendaClient.atualizarCompromisso(id, compromisso);
    }

    @CircuitBreaker(name = "agendaService", fallbackMethod = "deletarCompromissoFallback")
    @Retry(name = "agendaService")
    public ResponseEntity<Void> deletarCompromisso(UUID id) {
        log.debug("Deletando compromisso: {}", id);
        return agendaClient.deletarCompromisso(id);
    }

    // Fallback methods
    private ResponseEntity<?> listarCompromissosFallback(UUID userId, Exception e) {
        log.error("Fallback: Erro ao listar compromissos", e);
        return ResponseEntity.ok(Collections.emptyList());
    }

    private ResponseEntity<?> buscarCompromissoFallback(UUID id, Exception e) {
        log.error("Fallback: Erro ao buscar compromisso {}", id, e);
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> criarCompromissoFallback(Object compromisso, UUID userId, Exception e) {
        log.error("Fallback: Erro ao criar compromisso", e);
        return ResponseEntity.status(503).build();
    }

    private ResponseEntity<?> atualizarCompromissoFallback(UUID id, Object compromisso, Exception e) {
        log.error("Fallback: Erro ao atualizar compromisso {}", id, e);
        return ResponseEntity.status(503).build();
    }

    private ResponseEntity<Void> deletarCompromissoFallback(UUID id, Exception e) {
        log.error("Fallback: Erro ao deletar compromisso {}", id, e);
        return ResponseEntity.status(503).build();
    }
}
