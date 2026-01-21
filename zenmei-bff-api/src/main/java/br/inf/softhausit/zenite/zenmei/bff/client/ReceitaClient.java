package br.inf.softhausit.zenite.zenmei.bff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Feign Client para Receita API
 * <p>
 * Cliente para comunicação com o microserviço de receitas/vendas
 * </p>
 */
@FeignClient(
    name = "receita-service",
    url = "${microservices.receita-api.url}",
    configuration = br.inf.softhausit.zenite.zenmei.bff.config.FeignConfig.class
)
public interface ReceitaClient {

    @GetMapping("/api/v1/vendas")
    ResponseEntity<?> listarVendas(@RequestHeader("X-User-Id") UUID userId);

    @GetMapping("/api/v1/vendas/{id}")
    ResponseEntity<?> buscarVenda(@PathVariable("id") UUID id);

    @PostMapping("/api/v1/vendas")
    ResponseEntity<?> criarVenda(@RequestBody Object venda, @RequestHeader("X-User-Id") UUID userId);

    @PutMapping("/api/v1/vendas/{id}")
    ResponseEntity<?> atualizarVenda(@PathVariable("id") UUID id, @RequestBody Object venda);

    @DeleteMapping("/api/v1/vendas/{id}")
    ResponseEntity<Void> deletarVenda(@PathVariable("id") UUID id);
}
