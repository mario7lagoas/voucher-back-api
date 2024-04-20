package com.rematec.voucher.voucherbackapi.models.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class PromocaoResponse {
    private String guid;
    private String descricao;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inicio;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fim;
    private String tipoDesconto;
    private String promocaoStatus;
    private Double valorMinimoParaDisparo;
    private Integer diasValidadeVoucher;
    private Double discontoValor;
    private Integer discontoPercentual;
    private String autorAlteracao;

    private List<LojaResponse> lojas;

}
