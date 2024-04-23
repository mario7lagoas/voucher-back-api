package com.rematec.voucher.voucherbackapi.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class VoucherPromocaoResponse {
    private String transacao;
    private String descricao;
    private BigDecimal valorDesconto;

}
