package com.rematec.voucher.voucherbackapi.models.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class VouchersPaginadaResponse extends PaginacaoResponse{

    private List<VoucherFiltroResponse> transacoes;

}
