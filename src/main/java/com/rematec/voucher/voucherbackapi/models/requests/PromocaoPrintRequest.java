package com.rematec.voucher.voucherbackapi.models.requests;

import com.rematec.voucher.voucherbackapi.models.response.LojaResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
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
    private BigDecimal valorMinimoParaDisparo;
    private BigDecimal valorMaximoDesconto;
    private Integer diasValidadeVoucher;
    private BigDecimal descontoValor;
    private Integer descontoPercentual;
    private String autorAlteracao;
    private List<LojaResponse> lojas;

}
