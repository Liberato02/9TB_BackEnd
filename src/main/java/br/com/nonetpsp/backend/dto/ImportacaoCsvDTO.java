
package br.com.nonetpsp.backend.dto;

import java.time.LocalDate;
import java.math.BigDecimal;

public record ImportacaoCsvDTO(
        String apresentante,
        String numeroProtocolo,
        LocalDate dataProtocolo,
        BigDecimal valorCustas,
        BigDecimal valorTitulo) {
}
