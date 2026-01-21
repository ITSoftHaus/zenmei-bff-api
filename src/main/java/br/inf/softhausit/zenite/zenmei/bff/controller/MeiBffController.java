package br.inf.softhausit.zenite.zenmei.bff.controller;

import br.inf.softhausit.zenite.zenmei.bff.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller BFF para operações de Usuários
 * <p>
 * Agrega e expõe endpoints do microserviço de usuários
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/api/bff/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "APIs de gerenciamento de usuários")
public class UserBffController {

    private final UserService userService;

    @Operation(summary = "Listar todos os usuários", description = "Retorna lista de usuários do sistema")
    @GetMapping
    public ResponseEntity<?> listarUsuarios(
            @Parameter(description = "ID do usuário autenticado", required = true)
            @RequestHeader("X-User-Id") UUID userId) {
        log.info("BFF: Listando usuários para userId: {}", userId);
        return userService.listarUsuarios(userId);
    }

    @Operation(summary = "Buscar usuário por ID", description = "Retorna detalhes de um usuário específico")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarUsuario(
            @Parameter(description = "ID do usuário", required = true)
            @PathVariable UUID id) {
        log.info("BFF: Buscando usuário: {}", id);
        return userService.buscarUsuario(id);
    }

    @Operation(summary = "Criar novo usuário", description = "Cria um novo usuário no sistema")
    @PostMapping
    public ResponseEntity<?> criarUsuario(@RequestBody Object usuario) {
        log.info("BFF: Criando novo usuário");
        return userService.criarUsuario(usuario);
    }

    @Operation(summary = "Atualizar usuário", description = "Atualiza dados de um usuário existente")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarUsuario(
            @Parameter(description = "ID do usuário", required = true)
            @PathVariable UUID id,
            @RequestBody Object usuario) {
        log.info("BFF: Atualizando usuário: {}", id);
        return userService.atualizarUsuario(id, usuario);
    }

    @Operation(summary = "Deletar usuário", description = "Remove um usuário do sistema")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(
            @Parameter(description = "ID do usuário", required = true)
            @PathVariable UUID id) {
        log.info("BFF: Deletando usuário: {}", id);
        return userService.deletarUsuario(id);
    }
}
