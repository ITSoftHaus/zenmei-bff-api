package br.inf.softhausit.zenite.zenmei.bff.service;

import br.inf.softhausit.zenite.zenmei.bff.client.ProdutoClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

/**
 * Service para integração com Produto API
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoClient produtoClient;

    @CircuitBreaker(name = "produtoService", fallbackMethod = "listarProdutosFallback")
    @Retry(name = "produtoService")
    public ResponseEntity<?> listarProdutos(UUID userId) {
        log.debug("Listando produtos para userId: {}", userId);
        return produtoClient.listarProdutos(userId);
    }

    @CircuitBreaker(name = "produtoService", fallbackMethod = "buscarProdutoFallback")
    @Retry(name = "produtoService")
    public ResponseEntity<?> buscarProduto(UUID id) {
        log.debug("Buscando produto: {}", id);
        return produtoClient.buscarProduto(id);
    }

    @CircuitBreaker(name = "produtoService", fallbackMethod = "criarProdutoFallback")
    @Retry(name = "produtoService")
    public ResponseEntity<?> criarProduto(Object produto, UUID userId) {
        log.debug("Criando produto");
        return produtoClient.criarProduto(produto, userId);
    }

    @CircuitBreaker(name = "produtoService", fallbackMethod = "atualizarProdutoFallback")
    @Retry(name = "produtoService")
    public ResponseEntity<?> atualizarProduto(UUID id, Object produto) {
        log.debug("Atualizando produto: {}", id);
        return produtoClient.atualizarProduto(id, produto);
    }

    @CircuitBreaker(name = "produtoService", fallbackMethod = "deletarProdutoFallback")
    @Retry(name = "produtoService")
    public ResponseEntity<Void> deletarProduto(UUID id) {
        log.debug("Deletando produto: {}", id);
        return produtoClient.deletarProduto(id);
    }

    // Fallback methods
    private ResponseEntity<?> listarProdutosFallback(UUID userId, Exception e) {
        log.error("Fallback: Erro ao listar produtos", e);
        return ResponseEntity.ok(Collections.emptyList());
    }

    private ResponseEntity<?> buscarProdutoFallback(UUID id, Exception e) {
        log.error("Fallback: Erro ao buscar produto {}", id, e);
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> criarProdutoFallback(Object produto, UUID userId, Exception e) {
        log.error("Fallback: Erro ao criar produto", e);
        return ResponseEntity.status(503).build();
    }

    private ResponseEntity<?> atualizarProdutoFallback(UUID id, Object produto, Exception e) {
        log.error("Fallback: Erro ao atualizar produto {}", id, e);
        return ResponseEntity.status(503).build();
    }

    private ResponseEntity<Void> deletarProdutoFallback(UUID id, Exception e) {
        log.error("Fallback: Erro ao deletar produto {}", id, e);
        return ResponseEntity.status(503).build();
    }
}
