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

import br.inf.softhausit.zenite.zenmei.bff.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller BFF para operações de Clientes
 */
@Slf4j
@RestController
@RequestMapping("/api/bff/v1/cliente")
@RequiredArgsConstructor
@Tag(name = "Clients", description = "APIs de gerenciamento de clientes")
public class ClientBffController {

    private final ClientService clientService;

    @Operation(summary = "Listar todos os clientes")
    @GetMapping
    public ResponseEntity<?> listarClientes(@RequestHeader("X-User-Id") UUID userId) {
        log.info("BFF: Listando clientes para userId: {}", userId);
        return clientService.listarClientes(userId);
    }

    @Operation(summary = "Buscar cliente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarCliente(@PathVariable UUID id) {
        log.info("BFF: Buscando cliente: {}", id);
        return clientService.buscarCliente(id);
    }

    @Operation(summary = "Criar novo cliente")
    @PostMapping
    public ResponseEntity<?> criarCliente(
            @RequestBody Object cliente,
            @RequestHeader("X-User-Id") UUID userId) {
        log.info("BFF: Criando novo cliente");
        return clientService.criarCliente(cliente, userId);
    }

    @Operation(summary = "Atualizar cliente")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCliente(
            @PathVariable UUID id,
            @RequestBody Object cliente) {
        log.info("BFF: Atualizando cliente: {}", id);
        return clientService.atualizarCliente(id, cliente);
    }

    @Operation(summary = "Deletar cliente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable UUID id) {
        log.info("BFF: Deletando cliente: {}", id);
        return clientService.deletarCliente(id);
    }
}
