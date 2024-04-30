package com.rematec.voucher.voucherbackapi.models.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromocaoResponse {
    private String guid;
    private String descricao;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inicio;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fim;
    private String tipoDesconto;
    private String promocaoStatus;
    private BigDecimal valorMinimoParaDisparo;
    private Integer diasValidadeVoucher;
    private BigDecimal discontoValor;
    private BigDecimal discontoPercentual;
    private String autorAlteracao;
    private BigDecimal valorMaximoDesconto;

    private List<LojaResponse> lojas;

}
