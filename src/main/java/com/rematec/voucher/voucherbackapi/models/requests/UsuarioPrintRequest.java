package com.rematec.voucher.voucherbackapi.models.requests;

import com.rematec.voucher.voucherbackapi.models.response.PerfilResumidoResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
public class UsuarioPrintRequest {
    private String guid;
    private String userName;
    private String email;
    private Boolean status;
    private String dataCadastro;
    private String dataAtualizacao;
    private Set<PerfilResumidoResponse> perfis ;

}
