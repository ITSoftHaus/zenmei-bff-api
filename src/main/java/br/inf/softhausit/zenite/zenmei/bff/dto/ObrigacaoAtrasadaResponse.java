package br.inf.softhausit.zenite.zenmei.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Response DTO for overdue fiscal obligations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObrigacaoAtrasadaResponse {
    
    private UUID id;
    private UUID idMei;
    private String obrigacao;
    private String diaCompetencia;
    private String mesAnoCompetencia;
    private Integer diasAtraso;
}
