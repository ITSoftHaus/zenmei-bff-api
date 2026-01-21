package br.inf.softhausit.zenite.zenmei.bff.service;

import br.inf.softhausit.zenite.zenmei.bff.client.ClientClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

/**
 * Service para integração com Client API
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientClient clientClient;

    @CircuitBreaker(name = "clientService", fallbackMethod = "listarClientesFallback")
    @Retry(name = "clientService")
    public ResponseEntity<?> listarClientes(UUID userId) {
        log.debug("Listando clientes para userId: {}", userId);
        return clientClient.listarClientes(userId);
    }

    @CircuitBreaker(name = "clientService", fallbackMethod = "buscarClienteFallback")
    @Retry(name = "clientService")
    public ResponseEntity<?> buscarCliente(UUID id) {
        log.debug("Buscando cliente: {}", id);
        return clientClient.buscarCliente(id);
    }

    @CircuitBreaker(name = "clientService", fallbackMethod = "criarClienteFallback")
    @Retry(name = "clientService")
    public ResponseEntity<?> criarCliente(Object cliente, UUID userId) {
        log.debug("Criando cliente");
        return clientClient.criarCliente(cliente, userId);
    }

    @CircuitBreaker(name = "clientService", fallbackMethod = "atualizarClienteFallback")
    @Retry(name = "clientService")
    public ResponseEntity<?> atualizarCliente(UUID id, Object cliente) {
        log.debug("Atualizando cliente: {}", id);
        return clientClient.atualizarCliente(id, cliente);
    }

    @CircuitBreaker(name = "clientService", fallbackMethod = "deletarClienteFallback")
    @Retry(name = "clientService")
    public ResponseEntity<Void> deletarCliente(UUID id) {
        log.debug("Deletando cliente: {}", id);
        return clientClient.deletarCliente(id);
    }

    // Fallback methods
    private ResponseEntity<?> listarClientesFallback(UUID userId, Exception e) {
        log.error("Fallback: Erro ao listar clientes", e);
        return ResponseEntity.ok(Collections.emptyList());
    }

    private ResponseEntity<?> buscarClienteFallback(UUID id, Exception e) {
        log.error("Fallback: Erro ao buscar cliente {}", id, e);
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> criarClienteFallback(Object cliente, UUID userId, Exception e) {
        log.error("Fallback: Erro ao criar cliente", e);
        return ResponseEntity.status(503).build();
    }

    private ResponseEntity<?> atualizarClienteFallback(UUID id, Object cliente, Exception e) {
        log.error("Fallback: Erro ao atualizar cliente {}", id, e);
        return ResponseEntity.status(503).build();
    }

    private ResponseEntity<Void> deletarClienteFallback(UUID id, Exception e) {
        log.error("Fallback: Erro ao deletar cliente {}", id, e);
        return ResponseEntity.status(503).build();
    }
}
