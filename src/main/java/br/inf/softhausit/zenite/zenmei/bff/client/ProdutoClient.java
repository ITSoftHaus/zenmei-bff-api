package br.inf.softhausit.zenite.zenmei.bff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Feign Client para Produto API
 * <p>
 * Cliente para comunicação com o microserviço de produtos
 * </p>
 */
@FeignClient(
    name = "produto-service",
    url = "${microservices.produto-api.url}",
    configuration = br.inf.softhausit.zenite.zenmei.bff.config.FeignConfig.class
)
public interface ProdutoClient {

    @GetMapping("/api/v1/produtos")
    ResponseEntity<?> listarProdutos(@RequestHeader("X-User-Id") UUID userId);

    @GetMapping("/api/v1/produtos/{id}")
    ResponseEntity<?> buscarProduto(@PathVariable("id") UUID id);

    @PostMapping("/api/v1/produtos")
    ResponseEntity<?> criarProduto(@RequestBody Object produto, @RequestHeader("X-User-Id") UUID userId);

    @PutMapping("/api/v1/produtos/{id}")
    ResponseEntity<?> atualizarProduto(@PathVariable("id") UUID id, @RequestBody Object produto);

    @DeleteMapping("/api/v1/produtos/{id}")
    ResponseEntity<Void> deletarProduto(@PathVariable("id") UUID id);
}
