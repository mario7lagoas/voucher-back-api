package com.rematec.voucher.voucherbackapi.models.requests;

import com.rematec.voucher.voucherbackapi.models.response.LojaResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PromocaoPrintRequest {
    private String guid;
    private String descricao;
    private String inicio;
    private String fim;
    private String tipoDesconto;
    private String promocaoStatus;
    private Double valorMinimoParaDisparo;
    private Integer diasValidadeVoucher;
    private Double discontoValor;
    private Integer discontoPercentual;
    private String autorAlteracao;
    private List<LojaResponse> lojas;

}
