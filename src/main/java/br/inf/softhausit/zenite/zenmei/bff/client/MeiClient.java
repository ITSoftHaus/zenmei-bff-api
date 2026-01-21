package br.inf.softhausit.zenite.zenmei.bff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Feign Client para User API
 * <p>
 * Cliente para comunicação com o microserviço de usuários
 * </p>
 */
@FeignClient(
    name = "user-service",
    url = "${microservices.user-api.url}",
    configuration = br.inf.softhausit.zenite.zenmei.bff.config.FeignConfig.class
)
public interface UserClient {

    @GetMapping("/api/v1/users")
    ResponseEntity<?> listarUsuarios(@RequestHeader("X-User-Id") UUID userId);

    @GetMapping("/api/v1/users/{id}")
    ResponseEntity<?> buscarUsuario(@PathVariable("id") UUID id);

    @PostMapping("/api/v1/users")
    ResponseEntity<?> criarUsuario(@RequestBody Object usuario);

    @PutMapping("/api/v1/users/{id}")
    ResponseEntity<?> atualizarUsuario(@PathVariable("id") UUID id, @RequestBody Object usuario);

    @DeleteMapping("/api/v1/users/{id}")
    ResponseEntity<Void> deletarUsuario(@PathVariable("id") UUID id);
}
