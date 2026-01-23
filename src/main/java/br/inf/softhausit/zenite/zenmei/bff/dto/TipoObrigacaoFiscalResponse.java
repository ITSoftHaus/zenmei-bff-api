package br.inf.softhausit.zenite.zenmei.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Response DTO for fiscal obligation types.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoObrigacaoFiscalResponse {
    
    private UUID id;
    private String obrigacao;
    private String mesCompetencia;
    private String diaCompetencia;
}
