package br.inf.softhausit.zenite.zenmei.bff.controller;

import br.inf.softhausit.zenite.zenmei.bff.service.NotaFiscalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller BFF para operações de Notas Fiscais
 */
@Slf4j
@RestController
@RequestMapping("/api/bff/v1/nota")
@RequiredArgsConstructor
@Tag(name = "Notas Fiscais", description = "APIs de gerenciamento de notas fiscais")
public class NotaFiscalBffController {

    private final NotaFiscalService notaFiscalService;

    @Operation(summary = "Listar todas as notas fiscais")
    @GetMapping
    public ResponseEntity<?> listarNotas(@RequestHeader("X-User-Id") UUID userId) {
        log.info("BFF: Listando notas fiscais para userId: {}", userId);
        return notaFiscalService.listarNotas(userId);
    }

    @Operation(summary = "Buscar nota fiscal por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarNota(@PathVariable UUID id) {
        log.info("BFF: Buscando nota fiscal: {}", id);
        return notaFiscalService.buscarNota(id);
    }

    @Operation(summary = "Criar nova nota fiscal")
    @PostMapping
    public ResponseEntity<?> criarNota(
            @RequestBody Object nota,
            @RequestHeader("X-User-Id") UUID userId) {
        log.info("BFF: Criando nova nota fiscal");
        return notaFiscalService.criarNota(nota, userId);
    }

    @Operation(summary = "Atualizar nota fiscal")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarNota(
            @PathVariable UUID id,
            @RequestBody Object nota) {
        log.info("BFF: Atualizando nota fiscal: {}", id);
        return notaFiscalService.atualizarNota(id, nota);
    }

    @Operation(summary = "Deletar nota fiscal")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarNota(@PathVariable UUID id) {
        log.info("BFF: Deletando nota fiscal: {}", id);
        return notaFiscalService.deletarNota(id);
    }

    @Operation(summary = "Emitir nota fiscal", description = "Emite uma nota fiscal no sistema externo")
    @PostMapping("/{id}/emitir")
    public ResponseEntity<?> emitirNota(@PathVariable UUID id) {
        log.info("BFF: Emitindo nota fiscal: {}", id);
        return notaFiscalService.emitirNota(id);
    }
}
