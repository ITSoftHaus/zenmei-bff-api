package br.inf.softhausit.zenite.zenmei.bff.service;

import br.inf.softhausit.zenite.zenmei.bff.client.ReceitaClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

/**
 * Service para integração com Receita API
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReceitaService {

    private final ReceitaClient receitaClient;

    @CircuitBreaker(name = "receitaService", fallbackMethod = "listarVendasFallback")
    @Retry(name = "receitaService")
    public ResponseEntity<?> listarVendas(UUID userId) {
        log.debug("Listando vendas para userId: {}", userId);
        return receitaClient.listarVendas(userId);
    }

    @CircuitBreaker(name = "receitaService", fallbackMethod = "buscarVendaFallback")
    @Retry(name = "receitaService")
    public ResponseEntity<?> buscarVenda(UUID id) {
        log.debug("Buscando venda: {}", id);
        return receitaClient.buscarVenda(id);
    }

    @CircuitBreaker(name = "receitaService", fallbackMethod = "criarVendaFallback")
    @Retry(name = "receitaService")
    public ResponseEntity<?> criarVenda(Object venda, UUID userId) {
        log.debug("Criando venda");
        return receitaClient.criarVenda(venda, userId);
    }

    @CircuitBreaker(name = "receitaService", fallbackMethod = "atualizarVendaFallback")
    @Retry(name = "receitaService")
    public ResponseEntity<?> atualizarVenda(UUID id, Object venda) {
        log.debug("Atualizando venda: {}", id);
        return receitaClient.atualizarVenda(id, venda);
    }

    @CircuitBreaker(name = "receitaService", fallbackMethod = "deletarVendaFallback")
    @Retry(name = "receitaService")
    public ResponseEntity<Void> deletarVenda(UUID id) {
        log.debug("Deletando venda: {}", id);
        return receitaClient.deletarVenda(id);
    }

    // Fallback methods
    private ResponseEntity<?> listarVendasFallback(UUID userId, Exception e) {
        log.error("Fallback: Erro ao listar vendas", e);
        return ResponseEntity.ok(Collections.emptyList());
    }

    private ResponseEntity<?> buscarVendaFallback(UUID id, Exception e) {
        log.error("Fallback: Erro ao buscar venda {}", id, e);
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> criarVendaFallback(Object venda, UUID userId, Exception e) {
        log.error("Fallback: Erro ao criar venda", e);
        return ResponseEntity.status(503).build();
    }

    private ResponseEntity<?> atualizarVendaFallback(UUID id, Object venda, Exception e) {
        log.error("Fallback: Erro ao atualizar venda {}", id, e);
        return ResponseEntity.status(503).build();
    }

    private ResponseEntity<Void> deletarVendaFallback(UUID id, Exception e) {
        log.error("Fallback: Erro ao deletar venda {}", id, e);
        return ResponseEntity.status(503).build();
    }
}
