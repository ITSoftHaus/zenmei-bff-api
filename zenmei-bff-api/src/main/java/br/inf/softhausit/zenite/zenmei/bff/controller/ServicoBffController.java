package br.inf.softhausit.zenite.zenmei.bff.controller;

import br.inf.softhausit.zenite.zenmei.bff.service.ServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller BFF para operações de Serviços
 */
@Slf4j
@RestController
@RequestMapping("/api/bff/v1/services")
@RequiredArgsConstructor
@Tag(name = "Services", description = "APIs de gerenciamento de serviços")
public class ServicoBffController {

    private final ServicoService servicoService;

    @Operation(summary = "Listar todos os serviços")
    @GetMapping
    public ResponseEntity<?> listarServicos(@RequestHeader("X-User-Id") UUID userId) {
        log.info("BFF: Listando serviços para userId: {}", userId);
        return servicoService.listarServicos(userId);
    }

    @Operation(summary = "Buscar serviço por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarServico(@PathVariable UUID id) {
        log.info("BFF: Buscando serviço: {}", id);
        return servicoService.buscarServico(id);
    }

    @Operation(summary = "Criar novo serviço")
    @PostMapping
    public ResponseEntity<?> criarServico(
            @RequestBody Object servico,
            @RequestHeader("X-User-Id") UUID userId) {
        log.info("BFF: Criando novo serviço");
        return servicoService.criarServico(servico, userId);
    }

    @Operation(summary = "Atualizar serviço")
    @PutMapping
    public ResponseEntity<?> atualizarServico(@RequestBody Object servico) {
        log.info("BFF: Atualizando serviço");
        return servicoService.atualizarServico(servico);
    }

    @Operation(summary = "Deletar serviço")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarServico(@PathVariable UUID id) {
        log.info("BFF: Deletando serviço: {}", id);
        return servicoService.deletarServico(id);
    }
}
