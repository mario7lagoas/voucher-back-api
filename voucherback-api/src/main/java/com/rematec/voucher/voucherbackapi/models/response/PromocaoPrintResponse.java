package com.rematec.voucher.voucherbackapi.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PromocaoPrintResponse {
    private String descricao;
    private String inicio;
    private String fim;
    private String tipoDesconto;
    private String promocaoStatus;
    private Double valorMinimoParaDisparo;
    private Integer diasValidadeVoucher;
    private Double discontoValor;
    private Integer discontoPercentual;
    private List<LojaResponse> lojas;

}
