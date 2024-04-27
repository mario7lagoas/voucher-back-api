package com.rematec.voucher.voucherbackapi.models.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LojasPaginadaResponse extends PaginacaoResponse{
    private List<LojaResponse> lojas;

}
