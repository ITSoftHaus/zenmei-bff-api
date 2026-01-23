package br.inf.softhausit.zenite.zenmei.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Response DTO for MEI with overdue obligations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeiObrigacoesAtrasadasResponse {
    
    private UUID idMei;
    private String nomeMei;
    private Integer quantidadeAtrasadas;
}
