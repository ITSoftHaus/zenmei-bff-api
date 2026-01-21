package br.inf.softhausit.zenite.zenmei.bff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Feign Client para Client API
 * <p>
 * Cliente para comunicação com o microserviço de clientes
 * </p>
 */
@FeignClient(
    name = "client-service",
    url = "${microservices.client-api.url}",
    configuration = br.inf.softhausit.zenite.zenmei.bff.config.FeignConfig.class
)
public interface ClientClient {

    @GetMapping("/api/v1/clients")
    ResponseEntity<?> listarClientes(@RequestHeader("X-User-Id") UUID userId);

    @GetMapping("/api/v1/clients/{id}")
    ResponseEntity<?> buscarCliente(@PathVariable("id") UUID id);

    @PostMapping("/api/v1/clients")
    ResponseEntity<?> criarCliente(@RequestBody Object cliente, @RequestHeader("X-User-Id") UUID userId);

    @PutMapping("/api/v1/clients/{id}")
    ResponseEntity<?> atualizarCliente(@PathVariable("id") UUID id, @RequestBody Object cliente);

    @DeleteMapping("/api/v1/clients/{id}")
    ResponseEntity<Void> deletarCliente(@PathVariable("id") UUID id);
}
