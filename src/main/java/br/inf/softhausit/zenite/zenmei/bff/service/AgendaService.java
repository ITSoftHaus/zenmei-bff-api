package br.inf.softhausit.zenite.zenmei.bff.service;

import java.util.Collections;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.inf.softhausit.zenite.zenmei.bff.client.AgendaClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service para integração com Agenda API
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AgendaService {

    private final AgendaClient agendaClient;

    @CircuitBreaker(name = "agendaService", fallbackMethod = "listarAgendaFallback")
    @Retry(name = "agendaService")
    public ResponseEntity<?> listarAgenda(UUID userId) {
        log.debug("Listando Agenda para userId: {}", userId);
        return agendaClient.listarAgenda(userId);
    }

    @CircuitBreaker(name = "agendaService", fallbackMethod = "buscarAgendaFallback")
    @Retry(name = "agendaService")
    public ResponseEntity<?> buscarAgenda(UUID id) {
        log.debug("Buscando Agenda: {}", id);
        return agendaClient.buscarAgenda(id);
    }

    @CircuitBreaker(name = "agendaService", fallbackMethod = "criarAgendaFallback")
    @Retry(name = "agendaService")
    public ResponseEntity<?> criarAgenda(Object Agenda, UUID userId) {
        log.debug("Criando Agenda");
        return agendaClient.criarAgenda(Agenda, userId);
    }

    @CircuitBreaker(name = "agendaService", fallbackMethod = "atualizarAgendaFallback")
    @Retry(name = "agendaService")
    public ResponseEntity<?> atualizarAgenda(UUID id, Object Agenda) {
        log.debug("Atualizando Agenda: {}", id);
        return agendaClient.atualizarAgenda(id, Agenda);
    }

    @CircuitBreaker(name = "agendaService", fallbackMethod = "deletarAgendaFallback")
    @Retry(name = "agendaService")
    public ResponseEntity<Void> deletarAgenda(UUID id) {
        log.debug("Deletando Agenda: {}", id);
        return agendaClient.deletarAgenda(id);
    }

    // Fallback methods
    private ResponseEntity<?> listarAgendaFallback(UUID userId, Exception e) {
        log.error("Fallback: Erro ao listar Agenda", e);
        return ResponseEntity.ok(Collections.emptyList());
    }

    private ResponseEntity<?> buscarAgendaFallback(UUID id, Exception e) {
        log.error("Fallback: Erro ao buscar Agenda {}", id, e);
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> criarAgendaFallback(Object Agenda, UUID userId, Exception e) {
        log.error("Fallback: Erro ao criar Agenda", e);
        return ResponseEntity.status(503).build();
    }

    private ResponseEntity<?> atualizarAgendaFallback(UUID id, Object Agenda, Exception e) {
        log.error("Fallback: Erro ao atualizar Agenda {}", id, e);
        return ResponseEntity.status(503).build();
    }

    private ResponseEntity<Void> deletarAgendaFallback(UUID id, Exception e) {
        log.error("Fallback: Erro ao deletar Agenda {}", id, e);
        return ResponseEntity.status(503).build();
    }
}
