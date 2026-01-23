package br.inf.softhausit.zenite.zenmei.bff.controller;

import br.inf.softhausit.zenite.zenmei.bff.service.AgendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller BFF para operações de Agenda/Compromissos
 */
@Slf4j
@RestController
@RequestMapping("/api/bff/v1/agenda")
@RequiredArgsConstructor
@Tag(name = "Agenda", description = "APIs de gerenciamento de compromissos")
public class AgendaBffController {

    private final AgendaService agendaService;

    @Operation(summary = "Listar todos os compromissos")
    @GetMapping
    public ResponseEntity<?> listarCompromissos(@RequestHeader("X-User-Id") UUID userId) {
        log.info("BFF: Listando compromissos para userId: {}", userId);
        return agendaService.listarCompromissos(userId);
    }

    @Operation(summary = "Buscar compromisso por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarCompromisso(@PathVariable UUID id) {
        log.info("BFF: Buscando compromisso: {}", id);
        return agendaService.buscarCompromisso(id);
    }

    @Operation(summary = "Criar novo compromisso")
    @PostMapping
    public ResponseEntity<?> criarCompromisso(
            @RequestBody Object compromisso,
            @RequestHeader("X-User-Id") UUID userId) {
        log.info("BFF: Criando novo compromisso");
        return agendaService.criarCompromisso(compromisso, userId);
    }

    @Operation(summary = "Atualizar compromisso")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCompromisso(
            @PathVariable UUID id,
            @RequestBody Object compromisso) {
        log.info("BFF: Atualizando compromisso: {}", id);
        return agendaService.atualizarCompromisso(id, compromisso);
    }

    @Operation(summary = "Deletar compromisso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCompromisso(@PathVariable UUID id) {
        log.info("BFF: Deletando compromisso: {}", id);
        return agendaService.deletarCompromisso(id);
    }
}
