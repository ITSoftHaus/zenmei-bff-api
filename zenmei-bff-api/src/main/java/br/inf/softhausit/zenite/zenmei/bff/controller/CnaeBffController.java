package br.inf.softhausit.zenite.zenmei.bff.controller;

import br.inf.softhausit.zenite.zenmei.bff.service.CnaeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller BFF para operações de CNAEs
 */
@Slf4j
@RestController
@RequestMapping("/api/bff/v1/cnaes")
@RequiredArgsConstructor
@Tag(name = "CNAEs", description = "APIs de gerenciamento de CNAEs e LC116")
public class CnaeBffController {

    private final CnaeService cnaeService;

    @Operation(summary = "Listar CNAEs MEI do usuário")
    @GetMapping
    public ResponseEntity<?> listarMeiCnaes(@RequestHeader("X-User-Id") UUID userId) {
        log.info("BFF: Listando CNAEs MEI para userId: {}", userId);
        return cnaeService.listarMeiCnaes(userId);
    }

    @Operation(summary = "Listar todos os códigos LC116")
    @GetMapping("/lc116")
    public ResponseEntity<?> listarLc116() {
        log.info("BFF: Listando LC116");
        return cnaeService.listarLc116();
    }

    @Operation(summary = "Buscar LC116 por código")
    @GetMapping("/lc116/{codigoLc116}")
    public ResponseEntity<?> buscarLc116PorCodigo(@PathVariable String codigoLc116) {
        log.info("BFF: Buscando LC116: {}", codigoLc116);
        return cnaeService.buscarLc116PorCodigo(codigoLc116);
    }

    @Operation(summary = "Listar LC116 por CNAE")
    @GetMapping("/lc116/cnae/{codigoCnae}")
    public ResponseEntity<?> listarLc116PorCnae(@PathVariable String codigoCnae) {
        log.info("BFF: Listando LC116 por CNAE: {}", codigoCnae);
        return cnaeService.listarLc116PorCnae(codigoCnae);
    }

    @Operation(summary = "Listar todos os CNAEs")
    @GetMapping("/lista")
    public ResponseEntity<?> listarCnaes() {
        log.info("BFF: Listando todos os CNAEs");
        return cnaeService.listarCnaes();
    }

    @Operation(summary = "Buscar CNAE por código")
    @GetMapping("/consulta/tipo/{codigoCnae}")
    public ResponseEntity<?> buscarCnaePorCodigo(@PathVariable String codigoCnae) {
        log.info("BFF: Buscando CNAE: {}", codigoCnae);
        return cnaeService.buscarCnaePorCodigo(codigoCnae);
    }
}
