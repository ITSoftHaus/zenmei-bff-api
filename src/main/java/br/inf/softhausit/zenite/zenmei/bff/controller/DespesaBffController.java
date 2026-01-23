package br.inf.softhausit.zenite.zenmei.bff.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.inf.softhausit.zenite.zenmei.bff.service.DespesaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller BFF para operações de Despesas
 */
@Slf4j
@RestController
@RequestMapping("/api/bff/v1/despesas")
@RequiredArgsConstructor
@Tag(name = "Despesas", description = "APIs de gerenciamento de despesas")
public class DespesaBffController {

    private final DespesaService despesaService;

    @Operation(summary = "Listar todas as despesas")
    @GetMapping
    public ResponseEntity<?> listarDespesas(@RequestHeader("X-User-Id") UUID userId) {
        log.info("BFF: Listando despesas para userId: {}", userId);
        return despesaService.listarDespesas(userId);
    }

    @Operation(summary = "Buscar despesa por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarDespesa(@PathVariable UUID id) {
        log.info("BFF: Buscando despesa: {}", id);
        return despesaService.buscarDespesa(id);
    }

    @Operation(summary = "Criar nova despesa")
    @PostMapping
    public ResponseEntity<?> criarDespesa(
            @RequestBody Object despesa,
            @RequestHeader("X-User-Id") UUID userId) {
        log.info("BFF: Criando nova despesa");
        return despesaService.criarDespesa(despesa, userId);
    }

    @Operation(summary = "Atualizar despesa")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarDespesa(
            @PathVariable UUID id,
            @RequestBody Object despesa) {
        log.info("BFF: Atualizando despesa: {}", id);
        return despesaService.atualizarDespesa(id, despesa);
    }

    @Operation(summary = "Deletar despesa")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDespesa(@PathVariable UUID id) {
        log.info("BFF: Deletando despesa: {}", id);
        return despesaService.deletarDespesa(id);
    }
}
