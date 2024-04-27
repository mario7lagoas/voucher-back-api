package com.rematec.voucher.voucherbackapi.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherPromocaoResponse {
    private String status;
    private String transacao;
    private String descricao;
    private BigDecimal valorDesconto;

}
