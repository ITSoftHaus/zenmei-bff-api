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
 * Controller BFF para operações de Agenda/Agenda
 */
@Slf4j
@RestController
@RequestMapping("/api/bff/v1/agenda")
@RequiredArgsConstructor
@Tag(name = "Agenda", description = "APIs de gerenciamento de Agenda")
public class AgendaBffController {

    private final AgendaService agendaService;

    @Operation(summary = "Listar todos os Agenda")
    @GetMapping
    public ResponseEntity<?> listarAgenda(@RequestHeader("X-User-Id") UUID userId) {
        log.info("BFF: Listando Agenda para userId: {}", userId);
        return agendaService.listarAgenda(userId);
    }

    @Operation(summary = "Buscar Agenda por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarAgenda(@PathVariable UUID id) {
        log.info("BFF: Buscando Agenda: {}", id);
        return agendaService.buscarAgenda(id);
    }

    @Operation(summary = "Criar novo Agenda")
    @PostMapping
    public ResponseEntity<?> criarAgenda(
            @RequestBody Object agenda,
            @RequestHeader("X-User-Id") UUID userId) {
        log.info("BFF: Criando nova Agenda");
        return agendaService.criarAgenda(agenda, userId);
    }

    @Operation(summary = "Atualizar Agenda")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarAgenda(
            @PathVariable UUID id,
            @RequestBody Object agenda) {
        log.info("BFF: Atualizando Agenda: {}", id);
        return agendaService.atualizarAgenda(id, agenda);
    }

    @Operation(summary = "Deletar Agenda")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAgenda(@PathVariable UUID id) {
        log.info("BFF: Deletando Agenda: {}", id);
        return agendaService.deletarAgenda(id);
    }
}
