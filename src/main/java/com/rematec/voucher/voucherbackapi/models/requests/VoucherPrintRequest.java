package com.rematec.voucher.voucherbackapi.models.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class VoucherPrintRequest {
    private String guid;
    private String codigo;
    private String clienteCpf;
    private String filialCnpj;
    private String promocaoGuid;
    private String descricao;
    private String pdv;
    private String cupom;
    private String pdvResgate;
    private String filialCnpjResgate;
    private String cupomResgate;
    private BigDecimal valorPagamento;
    private String tipoDesconto;
    private String voucherStatus;
    private String promocaoStatus;
    private BigDecimal valorDesconto;
    private String fimResgate;
    private String dataResgate;
    private String inicio;
    private String fim;
    private String dataAtualizacao;
    private String dataCadastro;
}
