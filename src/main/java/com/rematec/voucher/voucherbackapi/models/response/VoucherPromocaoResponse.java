package com.rematec.voucher.voucherbackapi.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VoucherPromocaoResponse {
    private String guid;
    private String descricao;
    private Double valorDesconto;

}
