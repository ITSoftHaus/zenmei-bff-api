package br.inf.softhausit.zenite.zenmei.bff.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign Client para User API
 * <p>
 * Cliente para comunicação com o microserviço de MEI
 * </p>
 */
@FeignClient(
    name = "mei-service",
    url = "${microservices.mei-api.url}",
    configuration = br.inf.softhausit.zenite.zenmei.bff.config.FeignConfig.class
)
public interface MeiClient {

    @GetMapping("/api/v1/meis")
    ResponseEntity<?> listarMeis(@RequestParam("userId") UUID userId);

    @GetMapping("/api/v1/meis/{id}")
    ResponseEntity<?> buscarMei(@PathVariable("id") UUID id);

    @PostMapping("/api/v1/meis")
    ResponseEntity<?> criarMei(@RequestBody Object mei);

    @PutMapping("/api/v1/meis/{id}")
    ResponseEntity<?> atualizarMei(@PathVariable("id") UUID id, @RequestBody Object mei);
    
    @DeleteMapping("/api/v1/meis/{id}")
    ResponseEntity<Void> deletarMei(@PathVariable("id") UUID id);
}
