package br.inf.softhausit.zenite.zenmei.bff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Feign Client para CNAE API
 * <p>
 * Cliente para comunicação com o microserviço de CNAEs
 * </p>
 */
@FeignClient(
    name = "cnae-service",
    url = "${microservices.cnae-api.url}",
    configuration = br.inf.softhausit.zenite.zenmei.bff.config.FeignConfig.class
)
public interface CnaeClient {

    @GetMapping("/api/v1/cnaes")
    ResponseEntity<?> listarMeiCnaes(@RequestHeader("X-User-Id") UUID userId);

    @GetMapping("/api/v1/cnaes/lc116")
    ResponseEntity<?> listarLc116();

    @GetMapping("/api/v1/cnaes/lc116/{codigoLc116}")
    ResponseEntity<?> buscarLc116PorCodigo(@PathVariable("codigoLc116") String codigoLc116);

    @GetMapping("/api/v1/cnaes/lc116/cnae/{codigoCnae}")
    ResponseEntity<?> listarLc116PorCnae(@PathVariable("codigoCnae") String codigoCnae);

    @GetMapping("/api/v1/cnaes/lista")
    ResponseEntity<?> listarCnaes();

    @GetMapping("/api/v1/cnaes/consulta/tipo/{codigoCnae}")
    ResponseEntity<?> buscarCnaePorCodigo(@PathVariable("codigoCnae") String codigoCnae);
}
