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
public class UsuariosPaginadaResponse extends PaginacaoResponse{
    private List<UsuarioResponse> usuarios;

}
