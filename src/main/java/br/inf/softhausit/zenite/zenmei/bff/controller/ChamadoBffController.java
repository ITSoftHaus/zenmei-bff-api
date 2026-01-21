package br.inf.softhausit.zenite.zenmei.bff.controller;

import br.inf.softhausit.zenite.zenmei.bff.service.ChamadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller BFF para operações de Chamados
 */
@Slf4j
@RestController
@RequestMapping("/api/bff/v1/chamados")
@RequiredArgsConstructor
@Tag(name = "Chamados", description = "APIs de gerenciamento de chamados")
public class ChamadoBffController {

    private final ChamadoService chamadoService;

    @Operation(summary = "Listar todos os chamados")
    @GetMapping
    public ResponseEntity<?> listarChamados(@RequestHeader("X-User-Id") UUID userId) {
        log.info("BFF: Listando chamados para userId: {}", userId);
        return chamadoService.listarChamados(userId);
    }

    @Operation(summary = "Buscar chamado por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarChamado(@PathVariable UUID id) {
        log.info("BFF: Buscando chamado: {}", id);
        return chamadoService.buscarChamado(id);
    }

    @Operation(summary = "Criar novo chamado")
    @PostMapping
    public ResponseEntity<?> criarChamado(
            @RequestBody Object chamado,
            @RequestHeader("X-User-Id") UUID userId) {
        log.info("BFF: Criando novo chamado");
        return chamadoService.criarChamado(chamado, userId);
    }

    @Operation(summary = "Atualizar chamado")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarChamado(
            @PathVariable UUID id,
            @RequestBody Object chamado) {
        log.info("BFF: Atualizando chamado: {}", id);
        return chamadoService.atualizarChamado(id, chamado);
    }

    @Operation(summary = "Deletar chamado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarChamado(@PathVariable UUID id) {
        log.info("BFF: Deletando chamado: {}", id);
        return chamadoService.deletarChamado(id);
    }
}
